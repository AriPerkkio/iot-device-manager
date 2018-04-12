package web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.hamcrest.core.AnyOf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import web.domain.entity.Device;
import web.domain.entity.DeviceGroup;
import web.domain.entity.DeviceIcon;
import web.domain.entity.Measurement;
import web.domain.response.ErrorCode;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hibernate.internal.util.ConfigHelper.getResourceAsStream;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Component
class TestingUtils {

    private static final Logger log = LoggerFactory.getLogger(TestingUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    // Tomcat trims space automatically - behaviour cannot be changed
    private static final String APPLICATION_VND_COLLECTION_JSON = "application/vnd.collection+json; charset=utf-8";
    private static final String APPLICATION_VND_COLLECTION_JSON_WITHOUT_SPACE = "application/vnd.collection+json;charset=utf-8";

    private static final String DEVICES_URI = "/api/devices";
    private static final String GROUPS_URI = "/api/device-groups";
    private static final String MEASUREMENTS_URI = "/api/measurements";
    private static final String DEVICE_ICONS_URI = "/api/device-icons";

    static String USER;
    static String PASSWORD;

    @Value("${iotdevicemanager.username}")
    public void setUser(String user) {
        USER = user;
    }

    @Value("${iotdevicemanager.password}")
    public void setPassword(String password) {
        PASSWORD = password;
    }

    static String FILE_UPLOAD_ROOT;
    static String ITEMS_LOCATION;
    static String TEST_ICON_NAME;

    @Value("${deviceicon.upload.location.root}")
    public void setFileUploadRoot(String root) {
        FILE_UPLOAD_ROOT = root;
    }

    @Value("${deviceicon.upload.location.icons}")
    public void setItemsLocation(String iconsLocation) {
        ITEMS_LOCATION = iconsLocation;
    }

    @Value("${deviceicon.test.icon.name}")
    public void setTestIconName(String testIconName) {
        TEST_ICON_NAME = testIconName;
    }

    static void assertContentType(MockHttpServletResponse response) {
        log.debug("Assert content type is " + APPLICATION_VND_COLLECTION_JSON);

        assertThat(response.getContentType(), anyOf(is(APPLICATION_VND_COLLECTION_JSON), is(APPLICATION_VND_COLLECTION_JSON_WITHOUT_SPACE)));
    }

    static void assertContentType(MockHttpServletResponse response, String contentType) {
        log.debug("Assert content type is " + contentType);

        assertEquals(response.getContentType(), contentType);
    }

    static JsonNode parseToCollection(MockHttpServletResponse response) throws Exception {
        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        return jsonNode.get("collection");
    }

    static JsonNode parseToError(MockHttpServletResponse response) throws Exception {
        return parseToCollection(response).get("error");
    }

    static JsonNode parseToItems(MockHttpServletResponse response) throws Exception {
        return parseToCollection(response).get("items");
    }

    static void assertDevice(Map<String, String> data, Device expected) {
        assertNotNull(data.get("id"));
        assertData(data.get("name"), expected.getName());
        assertData(data.get("deviceTypeId"), expected.getDeviceTypeId());
        assertData(data.get("deviceGroupId"), expected.getDeviceGroupId());
        assertData(data.get("configurationId"), expected.getConfigurationId());
        assertNotNull(data.get("authenticationKey"));
    }

    static void assertStatus(MockHttpServletResponse response, HttpStatus httpStatus) {
        log.debug(String.format("Assert HTTP status is %s (%d)", httpStatus.name(), httpStatus.value()));

        assertEquals(response.getStatus(), httpStatus.value());
    }

    static void assertHref(MockHttpServletResponse response, String href) throws Exception {
        log.debug("Assert href is " + href);

        JsonNode collection = parseToCollection(response);
        assertThat(collection.get("href").toString(), containsString(href));
    }

    static void assertError(MockHttpServletResponse response, ErrorCode errorCode) throws Exception {
        log.debug("Assert error code is " + errorCode);

        JsonNode error = parseToError(response);
        assertThat(error.get("code").asText(), is(errorCode.getCode()));
    }

    static Map<String, String> dataToMap(JsonNode data) {
        Map<String, String> fieldValuePairs = new HashMap<>();

        for(JsonNode field : data) {
            fieldValuePairs.put(field.get("name").asText(), field.get("value").asText());
        }

        return fieldValuePairs;
    }

    static void assertData(String jsonValue, AnyOf<String> matcher) {
        log.debug(String.format("Assert %s with matcher %s", jsonValue, matcher));

        assertThat(jsonValue, matcher);
    }

    static void assertData(String jsonValue, Object value) {
        log.debug(String.format("Assert %s matches %s", jsonValue, value));

        assertThat(jsonValue, is(String.format("%s", value)));
    }


    static DeviceGroup getTestGroup() {
        return getTestGroup("test-group");
    }

    static DeviceGroup getTestGroup(String name) {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setName(name);
        return deviceGroup;
    }

    static Device getTestDevice() {
        return getTestDevice("test-device");
    }

    static Device getTestDevice(String name) {
        Device device = new Device();
        device.setName(name);
        return device;
    }

    static RequestPostProcessor getBasicAuth() {
        return httpBasic(USER, PASSWORD);
    }

    // Add device to database, returns generated ID. Should be used as helper method - not to test adding device itself.
    static Integer addDevice(Device device, MockMvc mockMvc) throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(post(DEVICES_URI)
                .with(getBasicAuth())
                .content(mapper.writeValueAsString(device))
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        JsonNode items = parseToItems(response);
        Map<String, String> data = dataToMap(items.get(0).get("data"));

        return Integer.parseInt(data.get("id"));
    }

    // Add group to database, returns generated ID. Should be used as helper method - not to test adding group itself.
    static Integer addGroup(DeviceGroup deviceGroup, MockMvc mockMvc) throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(post(GROUPS_URI)
                .with(getBasicAuth())
                .content(mapper.writeValueAsString(deviceGroup))
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

        JsonNode items = parseToItems(response);
        Map<String, String> data = dataToMap(items.get(0).get("data"));

        return Integer.parseInt(data.get("id"));
    }

    // Add measurement to database. Should be used as helper method - not to test adding measurement itself.
    static void addMeasurement(Measurement measurement, MockMvc mockMvc) throws Exception {
        mockMvc.perform(
            post(MEASUREMENTS_URI)
                .content(mapper.writeValueAsString(measurement))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()));
    }

    static DeviceIcon getTestIcon() {
        return getTestIcon("test-icon.png");
    }

    static DeviceIcon getTestIcon(String name) {
        DeviceIcon deviceIcon = new DeviceIcon();
        deviceIcon.setName(name);
        return deviceIcon;
    }

    static MockMultipartFile getTestIconFile() throws Exception {
        return new MockMultipartFile("icon", "test-icon", "image/png", getResourceAsStream(TEST_ICON_NAME));
    }

    // Assert icons by comparing content
    static void assertIcons(MockHttpServletResponse response, MockMultipartFile expected) throws Exception {
        log.debug(String.format("Assert response contains icon file with length of %d.", expected.getSize() ));

        assertEquals(new String(expected.getBytes()), new String(response.getContentAsByteArray()));
    }

    static Integer addIcon(DeviceIcon deviceIcon, MockMultipartFile iconFile, MockMvc mockMvc) throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(fileUpload(DEVICE_ICONS_URI)
                .file(iconFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("name", deviceIcon.getName())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        JsonNode items = parseToItems(response);
        Map<String, String> data = dataToMap(items.get(0).get("data"));

        return Integer.parseInt(data.get("id"));
    }

    static void assertIconExists(String name) {
        String location = FILE_UPLOAD_ROOT + ITEMS_LOCATION;
        Path path = Paths.get(location);

        log.debug(String.format("Assert file called %s exists at %s", name, location));

        Boolean exists = Files.exists(path.resolve(name));
        assertTrue(exists);
    }

    static void assertNoIconExists(String name) {
        String location = FILE_UPLOAD_ROOT + ITEMS_LOCATION;
        Path path = Paths.get(location);

        log.debug(String.format("Assert file called %s doesn't exists at %s", name, location));

        Boolean exists = Files.exists(path.resolve(name));
        assertFalse(exists);
    }

    static void removeTestIcons() throws Exception {
        String location = FILE_UPLOAD_ROOT + ITEMS_LOCATION;
        Path path = Paths.get(location);
        String[] iconNames = new File(location).list((dir, file) -> file.endsWith(".png"));


        if(ArrayUtils.isEmpty(iconNames)) {
            return;
        }

        log.debug(String.format("Cleaning icons. Found %s", String.join(", ", iconNames)));

        for(String iconName: iconNames) {
            log.debug(String.format("Removing %s", iconName));
            Files.delete(path.resolve(iconName));
        }
    }

    static Measurement getTestMeasurement(Integer deviceId) {
        Measurement measurement = new Measurement();
        measurement.setDeviceId(deviceId);
        measurement.setTime(new Date());

        HashMap<String, Object> content = new HashMap<>();
        content.put("test-string-key", "test-string-value");
        content.put("test-array-key", Arrays.asList("test-array-value-one", "test-array-value-two"));
        content.put("test-object-key", Collections.singletonMap("object-key", "object-value"));

        measurement.setContent(content);
        return measurement;
    }

    static void removeDevice(String id, MockMvc mockMvc) throws Exception {
        mockMvc.perform(delete(DEVICES_URI).param("id", id).with(getBasicAuth()));
    }

    static void removeGroup(String id, MockMvc mockMvc) throws Exception {
        mockMvc.perform(delete(GROUPS_URI).param("id", id).with(getBasicAuth()));
    }

    // Helper method to delete all devices. Update operations do not work as @Transactional.
    static void clearDatabase(MockMvc mockMvc) throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get(DEVICES_URI).with(getBasicAuth()))
            .andReturn().getResponse();

        for(JsonNode item : parseToItems(response)) {
            Map<String, String> data = dataToMap(item.get("data"));
            removeDevice(data.get("id"), mockMvc);
            removeGroup(data.get("deviceGroupId"), mockMvc);
        }
    }

    static Date stringToDate(String time) throws Exception{
        return new Timestamp(
            new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(time).getTime());
    }
}
