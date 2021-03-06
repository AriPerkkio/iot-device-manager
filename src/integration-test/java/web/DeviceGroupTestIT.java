package web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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
import web.domain.entity.Location;
import web.domain.response.ErrorCode;

import java.util.Date;
import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static web.TestingUtils.*;
import static web.mapper.MapperUtils.formatTime;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class DeviceGroupTestIT {

    private static final String URI = "/api/device-groups";
    private static final String ID_URI = URI + "/%d";
    private static final String LOCATIONS_URI = ID_URI + "/locations";
    private static final String DEVICES_URI = ID_URI + "/devices";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void logEmptyRowBeforeEachTest() {
        log.info("");
    }

    /**
     * Test get device group's locations returns group's all location updates
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsLocationsWithoutFiltersReturnsGroupsAllLocations() throws Exception {
        log.info("Test GET /api/device-groups/{id}/locations returns group's all location updates");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);

        Device device = getTestDevice();
        device.setDeviceGroupId(deviceGroupId);

        Integer deviceId = addDevice(device, mockMvc);
        Location locationOne = getTestLocation(deviceId);
        Location locationTwo = getTestLocation(deviceId);
        addLocation(locationOne, mockMvc);
        addLocation(locationTwo, mockMvc);

        String idUri = String.format(LOCATIONS_URI, deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertContentType(response);
        assertHref(response, idUri);

        JsonNode items = parseToItems(response);
        assertEquals(items.size(), 2);

        for(JsonNode item: items) {
            Map<String, String> data = dataToMap(item.get("data"));

            assertData(data.get("deviceId"), deviceId);
            assertData(data.get("longitude"), locationOne.getLongitude().doubleValue());
            assertData(data.get("latitude"), locationOne.getLatitude().doubleValue());
            assertData(data.get("time"), anyOf(is(formatTime(locationOne.getTime())), is(formatTime(locationTwo.getTime()))));
        }
    }

    /**
     * Test get device group's locations with time filter returns correct location updates
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsLocationsWithTimeFiltersReturnsCorrectLocationUpdates() throws Exception {
        log.info("Test GET /api/device-groups/{id}/locations with time filters returns correct location updates only");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        Device device = getTestDevice();
        device.setDeviceGroupId(deviceGroupId);
        Integer deviceId = addDevice(device, mockMvc);

        Location locationInTimeWindow = getTestLocation(deviceId);
        Location locationNotInTimeWindow = getTestLocation(deviceId);
        Date searchStartTime = stringToDate("01-01-2000 00:00:05");
        Date searchEndTime = stringToDate("01-01-2000 00:00:15");
        locationInTimeWindow.setTime(stringToDate("01-01-2000 00:00:10"));
        locationNotInTimeWindow.setTime(stringToDate("01-01-2000 00:00:20"));

        addLocation(locationInTimeWindow, mockMvc);
        addLocation(locationNotInTimeWindow, mockMvc);

        String idUri = String.format(LOCATIONS_URI, deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri)
                .param("startTime", formatTime(searchStartTime))
                .param("endTime", formatTime(searchEndTime))
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertContentType(response);
        assertHref(response, idUri);

        JsonNode items = parseToItems(response);
        assertEquals(items.size(), 1);

        Map<String, String> data = dataToMap(items.get(0).get("data"));

        assertData(data.get("deviceId"), deviceId);
        assertData(data.get("longitude"), locationInTimeWindow.getLongitude().doubleValue());
        assertData(data.get("latitude"), locationInTimeWindow.getLatitude().doubleValue());
        assertData(data.get("time"), formatTime(locationInTimeWindow.getTime()));
    }

    /**
     * Test get device group's locations returns error when device group not found
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsLocationsReturnsErrorWhenGroupNotFound() throws Exception {
        log.info("Test GET /api/device-groups/{id}/locations returns error when group not found");

        // Given
        String idUri = String.format(LOCATIONS_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test get device group's locations returns error when group has no devices
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsLocationsReturnsErrorWhenGroupHasNoDevices() throws Exception {
        log.info("Test GET /api/device-groups/{id}/locations returns error when group has no devices");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        String idUri = String.format(LOCATIONS_URI, deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test get device group's locations returns error when group's devices have no location updates
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsLocationsReturnsErrorWhenGroupsDevicesHaveNoLocations() throws Exception {
        log.info("Test GET /api/device-groups/{id}/locations returns error when group's devices have no location updates");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        Device device = getTestDevice();
        device.setDeviceGroupId(deviceGroupId);
        addDevice(device, mockMvc);
        String idUri = String.format(LOCATIONS_URI, deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test get device group's locations returns error when request parameter is invalid
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsLocationsReturnsErrorWhenRequestParameterInvalid() throws Exception {
        log.info("Test GET /api/device-groups/{id}/locations returns error when request parameter is invalid type");

        // Given
        String idUri = String.format(LOCATIONS_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri)
                .param("exactTime", "not-valid-timestamp")
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test add location to group's devices returns not implemented error
     */
    @Transactional
    @Test
    public void testAddLocationToGroupsDevicesReturnsNotImplementedError() throws Exception {
        log.info("Test POST /api/device-groups/{id}/locations returns not implemented error");

        // Given
        String idUri = String.format(LOCATIONS_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(idUri)
                .content(mapper.writeValueAsString(getTestMeasurement(1)))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_IMPLEMENTED);
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.INTERNAL_ERROR);
    }

    /**
     * Test update group's locations returns not implemented error
     */
    @Transactional
    @Test
    public void testUpdateGroupsLocationsReturnsNotImplementedError() throws Exception {
        log.info("Test PUT /api/device-groups/{id}/locations returns not implemented error");

        // Given
        String idUri = String.format(LOCATIONS_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(idUri)
                .param("name", "test-name")
                .content(mapper.writeValueAsString(getTestMeasurement(1)))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_IMPLEMENTED);
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.INTERNAL_ERROR);
    }

    /**
     * Test delete device group's locations without filters removes all group's locations
     */
    @Transactional
    @Test
    public void testDeleteDeviceGroupsLocationsWithoutFiltersRemovesAllGroupsLocations() throws Exception {
        log.info("Test DELETE /api/device-groups/{id}/locations without filters removes all group's locations");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);

        Device device = getTestDevice();
        device.setDeviceGroupId(deviceGroupId);

        Integer deviceId = addDevice(device, mockMvc);
        Location locationOne = getTestLocation(deviceId);
        Location locationTwo = getTestLocation(deviceId);
        addLocation(locationOne, mockMvc);
        addLocation(locationTwo, mockMvc);

        String idUri = String.format(LOCATIONS_URI, deviceGroupId);

        // When
        MockHttpServletResponse deleteResponse = mockMvc
            .perform(delete(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(deleteResponse, HttpStatus.NO_CONTENT);

        assertStatus(getResponse, HttpStatus.NOT_FOUND);
        assertError(getResponse, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test delete device group's locations with time filters removes correct locations
     */
    @Transactional
    @Test
    public void testDeleteDeviceGroupsLocationsWithTimeFilterRemovesCorrectLocations() throws Exception {
        log.info("Test DELETE /api/device-groups/{id}/locations with time filters removes correct locations");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        Device device = getTestDevice();
        device.setDeviceGroupId(deviceGroupId);
        Integer deviceId = addDevice(device, mockMvc);

        Location locationInTimeWindow = getTestLocation(deviceId);
        Location locationNotInTimeWindow = getTestLocation(deviceId);
        Date searchStartTime = stringToDate("01-01-2000 00:00:05");
        Date searchEndTime = stringToDate("01-01-2000 00:00:15");
        locationInTimeWindow.setTime(stringToDate("01-01-2000 00:00:10"));
        locationNotInTimeWindow.setTime(stringToDate("01-01-2000 00:00:20"));

        addLocation(locationInTimeWindow, mockMvc);
        addLocation(locationNotInTimeWindow, mockMvc);

        String idUri = String.format(LOCATIONS_URI, deviceGroupId);

        // When
        MockHttpServletResponse deleteResponse = mockMvc
            .perform(delete(idUri)
                .param("startTime", formatTime(searchStartTime))
                .param("endTime", formatTime(searchEndTime))
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(deleteResponse, HttpStatus.NO_CONTENT);

        assertStatus(getResponse, HttpStatus.OK);

        JsonNode items = parseToItems(getResponse);
        assertEquals(items.size(), 1);

        Map<String, String> data = dataToMap(items.get(0).get("data"));

        assertData(data.get("deviceId"), deviceId);
        assertData(data.get("longitude"), locationNotInTimeWindow.getLongitude().doubleValue());
        assertData(data.get("latitude"), locationNotInTimeWindow.getLatitude().doubleValue());
        assertData(data.get("time"), formatTime(locationNotInTimeWindow.getTime()));
    }

    /**
     * Test delete device group's locations returns error when group not found
     */
    @Transactional
    @Test
    public void testDeleteDeviceGroupsLocationsReturnsErrorWhenGroupNotFound() throws Exception {
        log.info("Test DELETE /api/device-groups/{id}/locations returns error when group not found");

        // Given
        String idUri = String.format(LOCATIONS_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test delete device group's locations returns error when group has no devices
     */
    @Transactional
    @Test
    public void testDeleteDeviceGroupsLocationsReturnsErrorWhenGroupHasNoDevices() throws Exception {
        log.info("Test DELETE /api/device-groups/{id}/locations returns error when group has no devices");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        String idUri = String.format(LOCATIONS_URI, deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test get group's devices without request parameters returns all group's devices
     */
    @Transactional
    @Test
    public void testGetGroupsDevicesWithoutParametersReturnsAllGroupsDevices() throws Exception {
        log.info("Test GET /api/device-groups/{id}/devices without request parameters returns all group's devices");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        Device deviceOne = getTestDevice();
        Device deviceTwo = getTestDevice("second-device");
        deviceOne.setDeviceGroupId(deviceGroupId);
        deviceTwo.setDeviceGroupId(deviceGroupId);
        addDevice(deviceOne, mockMvc);
        addDevice(deviceTwo, mockMvc);
        String idUri = String.format(DEVICES_URI, deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertHref(response, idUri);
        assertContentType(response);

        JsonNode items = parseToItems(response);
        assertEquals(items.size(), 2);

        for(JsonNode item : items) {
            Map<String, String> data = dataToMap(item.get("data"));
            assertData(data.get("deviceGroupId"), deviceGroupId);
            assertData(data.get("name"), anyOf(is(deviceOne.getName()), is(deviceTwo.getName())));
        }
    }

    /**
     * Test get group's devices returns error when group is not found
     */
    @Transactional
    @Test
    public void testGetGroupsDevicesReturnsErrorWhenGroupNotFound() throws Exception {
        log.info("Test GET /api/device-groups/{id}/devices returns error when group is not found");

        // Given
        String idUri = String.format(DEVICES_URI , 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test get group's devices returns error when group has no devices
     */
    @Transactional
    @Test
    public void testGetGroupsDevicesReturnsErrorWhenGroupHasNoDevices() throws Exception {
        log.info("Test GET /api/device-groups/{id}/devices returns error when group has no devices");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        String idUri = String.format(DEVICES_URI , deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test add device to group inserts device with correct deviceGroupId
     */
    @Transactional
    @Test
    public void testAddDeviceToGroupInsertsDeviceWithCorrectDeviceGroupId() throws Exception {
        log.info("Test POST /api/device-groups/{id}/devices inserts device with correct deviceGroupId");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        Device device = getTestDevice();
        String idUri = String.format(DEVICES_URI, deviceGroupId);

        // When
        MockHttpServletResponse postResponse = mockMvc
            .perform(post(idUri)
                .content(mapper.writeValueAsString(device))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(postResponse, HttpStatus.CREATED);
        assertHref(postResponse, idUri);
        assertContentType(postResponse);

        assertStatus(getResponse, HttpStatus.OK);
        JsonNode items = parseToItems(getResponse);
        assertEquals(items.size(), 1);

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("deviceGroupId"), deviceGroupId);
        assertData(data.get("name"), device.getName());
    }

    /**
     * Test add device to group returns error when device name is not unique
     */
    @Transactional
    @Test
    public void testAddDeviceToGroupReturnsErrorWhenDeviceNameDuplicate() throws Exception {
        log.info("Test POST /api/device-groups/{id}/devices returns error when device name is not unique");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        Device device = getTestDevice();
        addDevice(device, mockMvc);
        String idUri = String.format(DEVICES_URI, deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(idUri)
                .content(mapper.writeValueAsString(device))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.CONFLICT);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_CONFLICT);
    }

    /**
     * Test add device to group returns error when group not found
     */
    @Transactional
    @Test
    public void testAddDeviceToGroupReturnsErrorWhenGroupNotFound() throws Exception {
        log.info("Test POST /api/device-groups/{id}/devices returns error when group not found");

        // Given
        String idUri = String.format(DEVICES_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(idUri)
                .content(mapper.writeValueAsString(getTestDevice()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test add device to group returns error when request body is missing
     */
    @Transactional
    @Test
    public void testAddDeviceToGroupReturnsErrorWhenRequestBodyMissing() throws Exception {
        log.info("Test POST /api/device-groups/{id}/devices returns error when request body is missing");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        String idUri = String.format(DEVICES_URI, deviceGroupId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(idUri)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test add device to group returns error when request body is invalid
     */
    @Transactional
    @Test
    public void testAddDeviceToGroupReturnsErrorWhenRequestBodyInvalid() throws Exception {
        log.info("Test POST /api/device-groups/{id}/devices returns error when request body is invalid");

        // Given
        Integer deviceGroupId = addGroup(getTestGroup(), mockMvc);
        String idUri = String.format(DEVICES_URI, deviceGroupId);
        String invalidBody = mapper.writeValueAsString(getTestDevice())
            .replace("deviceGroupId\":null", "deviceGroupId\":\"string-instead-of-integer\"");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(idUri)
                .content(invalidBody)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test update group's devices returns not implemented error
     */
    @Transactional
    @Test
    public void testUpdateGroupsDevicesReturnsNotImplementedError() throws Exception {
        log.info("Test PUT /api/device-groups/{id}/devices returns not implemented error");

        // Given
        String idUri = String.format(DEVICES_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(idUri)
                .param("name", "test-name")
                .content(mapper.writeValueAsString(getTestDevice()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_IMPLEMENTED);
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.INTERNAL_ERROR);
    }

    /**
     * Test delete group's devices returns not implemented error
     */
    @Transactional
    @Test
    public void testDeleteGroupsDevicesReturnsNotImplementedError() throws Exception {
        log.info("Test DELETE /api/device-groups/{id}/devices returns not implemented error");

        // Given
        String idUri = String.format(DEVICES_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_IMPLEMENTED);
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.INTERNAL_ERROR);
    }
}
