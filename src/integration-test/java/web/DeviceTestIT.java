package web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import web.domain.entity.Device;
import web.domain.entity.DeviceGroup;
import web.domain.response.ErrorCode;

import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static web.TestingUtils.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@AutoConfigureMockMvc
public class DeviceTestIT {

    private static final String URI = "/api/devices";
    private static final String GROUP_URI = URI + "/%d/group";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test get devices returns error with NOT_FOUND status initially, when database is empty
     */
    @Test
    public void testGetDevicesInitiallyReturnsErrorAndNotFound() throws Exception {
        log.info("Test GET /api/devices returns error with NOT_FOUND status initially, when database is empty");

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test get devices returns error when request parameter is invalid type
     */
    @Test
    public void testGetDevicesReturnsErrorWhenRequestParameterInvalid() throws Exception {
        log.info("Test GET /api/devices returns error when request parameter is invalid type");

        // When
        String id = "string-instead-of-integer";
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).param("id", id).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test get device by id returns parameter validation error when id is invalid type
     */
    @Test
    public void testGetDeviceReturnsErrorWhenPathParameterInvalid() throws Exception {
        log.info("Test GET /api/devices/{id} returns parameter validation error when id is invalid type");

        // When
        String idUri = URI + "/string-instead-of-integer";
        MockHttpServletResponse response = mockMvc
                .perform(get(idUri).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test get devices without parameters returns all devices
     */
    @Transactional
    @Test
    public void testGetDevicesWithoutParametersReturnsAllDevices() throws Exception {
        log.info("Test GET /api/devices without parameters returns all devices");

        // Given
        Device deviceOne = getTestDevice("device-one");
        addDevice(deviceOne, mockMvc);
        Device deviceTwo = getTestDevice("device-two");
        addDevice(deviceTwo, mockMvc);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertContentType(response);
        assertHref(response, URI);

        JsonNode items = parseToItems(response);
        assertThat(items.size(), is(2));

        for(JsonNode item : items) {
            Map<String, String> data = dataToMap(item.get("data"));

            assertNotNull(data.get("id"));
            assertThat(data.get("name"), anyOf(is(deviceOne.getName()), is(deviceTwo.getName())));
            assertNotNull(data.get("authenticationKey"));
        }
    }

    /**
     * Test get device by id returns only one device
     */
    @Transactional
    @Test
    public void testGetDeviceByIdReturnsOnlyOneDevice() throws Exception {
        log.info("Test GET /api/devices/{id} returns only one device");

        // Given
        Device extraDevice = getTestDevice("device-two");
        addDevice(extraDevice, mockMvc);

        Device expected = getTestDevice("device-one");
        Integer id = addDevice(expected, mockMvc);
        String idUri = URI + String.format("/%d", id);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(idUri).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertContentType(response);
        assertHref(response, idUri);

        JsonNode items = parseToItems(response);
        assertThat(items.size(), is(1));

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("id"), id);
    }

    /**
     * Test add device returns inserted device with autogenerated id and authentication key, and status is OK
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsInsertedDeviceAndOk() throws Exception {
        log.info("Test POST /api/devices returns inserted device with autogenerated id and authentication key, and status is CREATED");

        // Given
        Device expected = getTestDevice();

        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(getBasicAuth())
                        .content(mapper.writeValueAsString(expected))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.CREATED.value());
        assertContentType(response);
        assertHref(response, URI);

        JsonNode items = parseToItems(response);
        assertThat(items.size(), is(1));

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertDevice(data, expected);
    }

    /**
     * Test add device returns parameter conflict error when inserting device with duplicate name
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsErrorWhenNameConflicts() throws Exception {
        log.info("Test POST /api/devices returns parameter conflict error when inserting device with duplicate name");

        // Given
        Device device = getTestDevice();

        // When
        addDevice(device, mockMvc);
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(getBasicAuth())
                        .content(mapper.writeValueAsString(device))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.CONFLICT.value());
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_CONFLICT);
    }

    /**
     * Test add device returns parameter validation error when request body is missing
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsErrorRequestBodyMissing() throws Exception {
        log.info("Test POST /api/devices returns parameter validation error when request body is missing");

        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(getBasicAuth())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test add device returns parameter conflict error when inserting device invalid attribute
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsErrorWhenRequestBodyContainsInvalidParameter() throws Exception {
        log.info("Test POST /api/devices returns parameter conflict error when inserting device invalid attribute");

        // Given
        Device device = getTestDevice();
        String body = mapper.writeValueAsString(device)
                .replace("deviceTypeId\":null", "deviceTypeId\":\"string-value\"");

        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(getBasicAuth())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test update device modifies given device
     */
    @Test
    public void testUpdateDevice() throws Exception {
        log.info("Test PUT /api/devices modifies given device");

        // Given
        Device initialDevice = getTestDevice();
        Device updateDevice = getTestDevice("updated-name");

        // When
        addDevice(initialDevice, mockMvc);
        mockMvc
                .perform(put(URI)
                        .param("name", initialDevice.getName())
                        .content(mapper.writeValueAsString(updateDevice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(getBasicAuth()));

        MockHttpServletResponse response = mockMvc
                .perform(get(URI)
                        .param("name", updateDevice.getName())
                        .with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertHref(response, URI);
        assertContentType(response);

        JsonNode items = parseToItems(response);
        assertThat(items.size(), is(1));

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("name"), updateDevice.getName());

        // Update operations do not work as @Transactional
        clearDatabase(mockMvc);
    }

    /**
     * Test update device returns error when device not found
     */
    @Transactional
    @Test
    public void testUpdateDeviceReturnsErrorWhenDeviceNotFound() throws Exception {
        log.info("Test PUT /api/devices returns error when device not found");

        // Given
        Device initialDevice = getTestDevice();

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(URI)
                .param("name", initialDevice.getName())
                .content(mapper.writeValueAsString(initialDevice))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test update device returns error when device name is duplicate
     */
    @Transactional
    @Test
    public void testUpdateDeviceReturnsErrorWhenDeviceNameDuplicate() throws Exception {
        log.info("Test PUT /api/devices returns error when device name is duplicate");

        // Given
        Device initialDevice = getTestDevice();
        Device updatedDevice = getTestDevice("update-device");

        // When
        addDevice(initialDevice, mockMvc);
        addDevice(updatedDevice, mockMvc);

        MockHttpServletResponse response = mockMvc
            .perform(put(URI)
                .param("name", initialDevice.getName())
                .content(mapper.writeValueAsString(updatedDevice))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.CONFLICT.value());
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_CONFLICT);
    }

    /**
     * Test update device by ID modifies given device
     */
    @Test
    public void testUpdateDeviceById() throws Exception {
        log.info("Test PUT /api/devices/{id} modifies given device");

        // Given
        Device initialDevice = getTestDevice();
        Device updateDevice = getTestDevice("updated-name");

        // When
        Integer id = addDevice(initialDevice, mockMvc);
        String idUri = URI + "/" + id;

        mockMvc
            .perform(put(idUri)
                .content(mapper.writeValueAsString(updateDevice))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()));

        MockHttpServletResponse response = mockMvc
            .perform(get(idUri)
                .param("name", updateDevice.getName())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertContentType(response);
        assertHref(response, idUri);

        JsonNode items = parseToItems(response);
        assertThat(items.size(), is(1));

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("name"), updateDevice.getName());

        // Update operations do not work as @Transactional
        clearDatabase(mockMvc);
    }

    /**
     * Test update device by ID returns error when device not found
     */
    @Test
    public void testUpdateDeviceByIdReturnsErrorWhenDeviceNotFound() throws Exception {
        log.info("Test PUT /api/devices/{id} returns error when device not found");

        // Given
        Integer randomId = 123456;
        String idUri = URI + String.format("/%d", randomId);
        Device device = getTestDevice();

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(idUri)
                .content(mapper.writeValueAsString(device))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
        assertHref(response, idUri);
    }

    /**
     * Test update device by ID returns error when ID is invalid format
     */
    @Test
    public void testUpdateDeviceByIdReturnsErrorIdInvalidFormat() throws Exception {
        log.info("Test PUT /api/devices/{id} returns error when ID is invalid format");

        // Given
        String idUri = URI + "/string-instead-of-integer";
        Device device = getTestDevice();

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(idUri)
                .content(mapper.writeValueAsString(device))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();



        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
        assertHref(response, idUri);
    }

    /**
     * Test delete device removes device
     */
    @Test
    @Transactional
    public void testDeleteDevice() throws Exception {
        log.info("Test DELETE /api/devices removes device");

        // Given
        Device device = getTestDevice();

        // When
        addDevice(device, mockMvc);

        MockHttpServletResponse response = mockMvc
            .perform(delete(URI)
                .param("name", device.getName())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
        assertContentType(response);
        assertEquals(getResponse.getStatus(), HttpStatus.NOT_FOUND.value());
    }

    /**
     * Test delete device returns error when device not found
     */
    @Test
    public void testDeleteDeviceReturnsErrorWhenDevicesNotFound() throws Exception {
        log.info("Test DELETE /api/devices returns error when device not found");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(URI)
                .param("name", "test-name")
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test delete device returns error request parameter invalid type
     */
    @Test
    public void testDeleteDeviceReturnsErrorWhenRequestParameterInvalidType() throws Exception {
        log.info("Test DELETE /api/devices returns error when device not found");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(URI)
                .param("id", "string-instead-of-integer")
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test delete device by ID removes device
     */
    @Test
    @Transactional
    public void testDeleteDeviceById() throws Exception {
        log.info("Test DELETE /api/devices/{id} removes device");

        // Given
        Device device = getTestDevice();

        // When
        Integer id = addDevice(device, mockMvc);
        String idUri = URI + "/" + id;

        MockHttpServletResponse response = mockMvc
            .perform(delete(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NO_CONTENT.value());
        assertContentType(response);
        assertEquals(getResponse.getStatus(), HttpStatus.NOT_FOUND.value());
    }

    /**
     * Test delete device by ID returns error when device not found
     */
    @Test
    public void testDeleteDeviceByIdReturnsErrorWhenDevicesNotFound() throws Exception {
        log.info("Test DELETE /api/devices/{id} returns error when device not found");

        // Given
        String idUri = URI + "/123456";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test delete device by ID returns error when ID invalid type
     */
    @Test
    public void testDeleteDeviceByIdReturnsErrorWhenIdInvalidType() throws Exception {
        log.info("Test DELETE /api/devices/{id} returns error when ID is invalid type");

        // Given
        String idUri = URI + "/string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test get device's group returns group
     */
    @Transactional
    @Test
    public void testGetDevicesGroup() throws Exception {
        log.info("Test GET /api/devices/{id}/group returns device's group");

        // Given
        DeviceGroup deviceGroup = getTestGroup();
        Device device = getTestDevice();

        // When
        Integer deviceGroupId = addGroup(deviceGroup, mockMvc);
        device.setDeviceGroupId(deviceGroupId);
        Integer deviceId = addDevice(device, mockMvc);
        String groupUri = String.format(GROUP_URI, deviceId);

        MockHttpServletResponse response = mockMvc
            .perform(get(groupUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertHref(response, groupUri);
        assertContentType(response);

        JsonNode items = parseToItems(response);
        assertThat(items.size(), is(1));

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("name"), deviceGroup.getName());
        assertData(data.get("id"), deviceGroupId);
    }

    /**
     * Test get device's group returns error when device is not found
     */
    @Transactional
    @Test
    public void testGetDevicesGroupReturnsErrorWhenDeviceNotFound() throws Exception {
        log.info("Test GET /api/devices/{id}/group returns error when device is not found");

        // Given
        String idUri = String.format(GROUP_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(idUri).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test get device's group returns error when device is not in group
     */
    @Transactional
    @Test
    public void testGetDevicesGroupReturnsErrorWhenDeviceNotInGroup() throws Exception {
        log.info("Test GET /api/devices/{id}/group returns error when device is not in group");

        // Given
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        String idUri = String.format(GROUP_URI, deviceId);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(idUri).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.CONFLICT.value());
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_CONFLICT);
    }

    /**
     * Test add group for device creates new group and updates devices
     */
    @Transactional
    @Test
    public void testAddGroupForDevice() throws Exception {
        log.info("Test POST /api/devices/{id}/group creates new group and updates device's deviceGroupId");
        // Given
        Device device = getTestDevice();
        DeviceGroup deviceGroup = getTestGroup();
        Integer deviceId = addDevice(device, mockMvc);
        String idUri = String.format(GROUP_URI, deviceId);

        // When
        MockHttpServletResponse postResponse = mockMvc
                .perform(post(idUri)
                    .content(mapper.writeValueAsString(deviceGroup))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(getBasicAuth()))
                .andReturn().getResponse();

        MockHttpServletResponse getGroupResponse = mockMvc
                .perform(get(idUri).with(getBasicAuth()))
                .andReturn().getResponse();

        MockHttpServletResponse getDeviceResponse = mockMvc
                .perform(get(URI + "/" + deviceId).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(postResponse.getStatus(), HttpStatus.CREATED.value());
        assertEquals(getGroupResponse.getStatus(), HttpStatus.OK.value());
        assertEquals(getDeviceResponse.getStatus(), HttpStatus.OK.value());

        JsonNode createdGroup = parseToItems(getGroupResponse);
        JsonNode updatedDevice = parseToItems(getDeviceResponse);

        Map<String, String> groupData = dataToMap(createdGroup.get(0).get("data"));
        Map<String, String> deviceData = dataToMap(updatedDevice.get(0).get("data"));

        assertData(groupData.get("name"), deviceGroup.getName());
        assertData(deviceData.get("deviceGroupId"), groupData.get("id"));

    }

    /**
     * Test add group for device returns error when device is not found
     */
    @Transactional
    @Test
    public void testAddGroupForDeviceReturnsErrorWhenDeviceNotFound() throws Exception {
        log.info("Test POST /devices/{id}/group returns error when device is not found");

        // Given
        DeviceGroup deviceGroup = getTestGroup();
        String idUri = String.format(GROUP_URI, 123);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(idUri)
                    .content(mapper.writeValueAsString(deviceGroup))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test add group for device returns error when request body is missing
     */
    @Transactional
    @Test
    public void testAddGroupForDeviceReturnsErrorWhenRequestBodyMissing() throws Exception {
        log.info("Test POST /devices/{id}/group returns error when request body is missing");

        // Given
        String idUri = String.format(GROUP_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(idUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test add group for device returns error when group name is not unique
     */
    @Transactional
    @Test
    public void testAddGroupForDeviceReturnsErrorWhenGroupNameDuplicate() throws Exception {
        log.info("Test POST /device/{id}/group returns error when group name is not unique");

        // Given
        DeviceGroup deviceGroup = getTestGroup();
        addGroup(deviceGroup, mockMvc);
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        String idUri = String.format(GROUP_URI, deviceId);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(idUri)
                        .content(mapper.writeValueAsString(deviceGroup))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.CONFLICT.value());
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_CONFLICT);
    }

    /**
     *
     */
    @Transactional
    //@Test
    public void testAddGroupForDeviceReturnsErrorWhenDeviceAlreadyInGroup() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }


    /**
     *
     */
    @Transactional
    //@Test
    public void testUpdateDevicesGroup() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }

    /**
     *
     */
    @Transactional
    //@Test
    public void testUpdateDevicesGroupReturnsErrorWhenDeviceNotFound() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }

    /**
     *
     */
    @Transactional
    //@Test
    public void testUpdateDevicesGroupReturnsErrorWhenDeviceNotInGroup() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }

    /**
     *
     */
    @Transactional
    //@Test
    public void testUpdateDevicesGroupReturnsErrorWhenRequestBodyMissing() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }

    /**
     *
     */
    @Transactional
    //@Test
    public void testUpdateDevicesGroupReturnsErrorWhenGroupNameDuplicate() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }


    /**
     *
     */
    @Transactional
    //@Test
    public void testDeleteDevicesGroup() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }

    /**
     *
     */
    @Transactional
    //@Test
    public void testDeleteDevicesGroupReturnsErrorWhenDeviceNotFound() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }

    /**
     *
     */
    @Transactional
    //@Test
    public void testDeleteDevicesGroupReturnsErrorWhenDeviceNotInGroup() throws Exception {
        log.info("");
        // Given

        // When

        // Then
    }
}
