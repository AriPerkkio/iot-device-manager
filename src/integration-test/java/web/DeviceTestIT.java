package web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import web.domain.entity.Device;
import web.domain.response.ErrorCode;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@AutoConfigureMockMvc
public class DeviceTestIT {

    private static final String URI = "/api/devices";
    private static final String APPLICATION_VND_COLLECTION_JSON = "application/vnd.collection+json;charset=utf-8";
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${iotdevicemanager.username}")
    String user;

    @Value("${iotdevicemanager.password}")
    String password;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test get devices returns error with NOT_FOUND status initially, when database is empty
     */
    @Test
    public void testGetDevicesInitiallyReturnsErrorAndNotFound() throws Exception {
        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).with(httpBasic(user,password)))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode collection = jsonNode.get("collection");
        JsonNode error = collection.get("error");

        assertThat(collection.get("href").toString(), containsString("api/devices"));
        assertThat(error.get("code").asText(), is(ErrorCode.NO_ITEMS_FOUND.getCode()));
    }

    /**
     * Test get devices returns error when request parameter is invalid type
     */
    @Test
    public void testGetDevicesReturnsErrorWhenRequestParameterInvalid() throws Exception {
        // When
        String id = "string-instead-of-integer";
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).param("id", id).with(httpBasic(user,password)))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode collection = jsonNode.get("collection");
        JsonNode error = collection.get("error");

        assertThat(collection.get("href").toString(), containsString("api/devices"));
        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test get device by id returns parameter validation error when id is invalid type
     */
    @Test
    public void testGetDeviceReturnsErrorWhenPathParameterInvalid() throws Exception {
        // When
        String idUri = "/string-instead-of-integer";
        MockHttpServletResponse response = mockMvc
                .perform(get(URI + idUri).with(httpBasic(user,password)))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode collection = jsonNode.get("collection");
        JsonNode error = collection.get("error");

        assertThat(collection.get("href").toString(), containsString("api/devices" + idUri));
        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test get devices without parameters returns all devices
     */
    @Transactional
    @Test
    public void testGetDevicesWithoutParametersReturnsAllDevices() throws Exception {
        // Given
        Device deviceOne = getTestDevice();
        deviceOne.setName("device-one");
        addDevice(deviceOne);
        Device deviceTwo = getTestDevice();
        deviceTwo.setName("device-two");
        addDevice(deviceTwo);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).with(httpBasic(user,password)))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode items = jsonNode.get("collection").get("items");
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
        // Given
        Device expected = getTestDevice();
        expected.setName("device-one");
        Device extraDevice = getTestDevice();
        extraDevice.setName("device-two");
        addDevice(extraDevice);
        Integer id = addDevice(expected);
        String idUri = String.format("/%d", id);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(URI + idUri).with(httpBasic(user,password)))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode items = jsonNode.get("collection").get("items");
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
        // Given
        Device expected = getTestDevice();

        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(httpBasic(user,password))
                        .content(mapper.writeValueAsString(expected))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode items = jsonNode.get("collection").get("items");

        assertThat(items.size(), is(1));
        Map<String, String> data = dataToMap(items.get(0).get("data"));

        assertNotNull(data.get("id"));
        assertData(data.get("name"), expected.getName());
        assertData(data.get("deviceTypeId"), expected.getDeviceTypeId());
        assertData(data.get("deviceGroupId"), expected.getDeviceGroupId());
        assertData(data.get("configurationId"), expected.getConfigurationId());
        assertNotNull(data.get("authenticationKey"));
    }

    /**
     * Test add device returns parameter conflict error when inserting device with duplicate name
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsErrorWhenNameConflicts() throws Exception {
        // Given
        Device device = getTestDevice();

        // When
        addDevice(device);
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(httpBasic(user,password))
                        .content(mapper.writeValueAsString(device))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.CONFLICT.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode error = jsonNode.get("collection").get("error");

        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_CONFLICT.getCode()));
    }

    /**
     * Test add device returns parameter validation error when request body is missing
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsErrorRequestBodyMissing() throws Exception {
        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(httpBasic(user,password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode error = jsonNode.get("collection").get("error");

        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test add device returns parameter conflict error when inserting device invalid attribute
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsErrorWhenRequestBodyContainsInvalidParameter() throws Exception {
        // Given
        Device device = getTestDevice();
        String body = mapper.writeValueAsString(device)
                .replace("deviceTypeId\":null", "deviceTypeId\":\"string-value\"");

        // When
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(httpBasic(user,password))
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode error = jsonNode.get("collection").get("error");

        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test update device modifies given device
     */
    @Test
    public void testUpdateDevice() throws Exception {
        // Given
        Device initialDevice = getTestDevice();
        Device updateDevice = getTestDevice();
        updateDevice.setName("updated-name");

        // When
        addDevice(initialDevice);
        mockMvc
                .perform(put(URI)
                        .param("name", initialDevice.getName())
                        .content(mapper.writeValueAsString(updateDevice))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(httpBasic(user, password)));

        MockHttpServletResponse response = mockMvc
                .perform(get(URI)
                        .param("name", updateDevice.getName())
                        .with(httpBasic(user,password)))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode items = jsonNode.get("collection").get("items");
        assertThat(items.size(), is(1));

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("name"), updateDevice.getName());

        // Update operations do not work as @Transactional
        clearDatabase();
    }

    /**
     * Test update device by ID returns error when device not found
     */
    @Test
    public void testUpdateDeviceReturnsErrorWhenDeviceNotFound() throws Exception {
        // Given
        Integer randomId = 123456;
        String idUri = String.format("/%d", randomId);
        Device device = getTestDevice();

        // When
        MockHttpServletResponse response = mockMvc
                .perform(put(URI + idUri)
                    .content(mapper.writeValueAsString(device))
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(httpBasic(user, password)))
                .andReturn().getResponse();

        // Then
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
        assertEquals(response.getContentType(), APPLICATION_VND_COLLECTION_JSON);

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode error = jsonNode.get("collection").get("error");

        assertThat(error.get("code").asText(), is(ErrorCode.NO_ITEMS_FOUND.getCode()));
    }

    // Add device to database, returns generated ID. Should be used as helper method - not to test adding device itself.
    private Integer addDevice(Device device) throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(post(URI)
                        .with(httpBasic(user,password))
                        .content(mapper.writeValueAsString(device))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode items = jsonNode.get("collection").get("items");
        Map<String, String> data = dataToMap(items.get(0).get("data"));

        return Integer.parseInt(data.get("id"));
    }

    private void removeDevice(String id) throws Exception {
        mockMvc.perform(delete(URI).param("id", id).with(httpBasic(user, password)));
    }

    private void assertData(String jsonValue, Object value) {
        assertThat(jsonValue, is(String.format("%s", value)));
    }

    // Helper method to delete all devices. Update operations do not work as @Transactional.
    private void clearDatabase() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).with(httpBasic(user,password)))
                .andReturn().getResponse();

        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode items = jsonNode.get("collection").get("items");

        for(JsonNode item : items) {
            Map<String, String> data = dataToMap(item.get("data"));
            removeDevice(data.get("id"));
        }
    }

    private Map<String, String> dataToMap(JsonNode data) {
        Map<String, String> fieldValuePairs = new HashMap<>();

        for(JsonNode field : data) {
            fieldValuePairs.put(field.get("name").asText(), field.get("value").asText());
        }

        return fieldValuePairs;
    }

    private Device getTestDevice() {
        Device device = new Device();
        device.setName("test-device");
        return device;
    }
}
