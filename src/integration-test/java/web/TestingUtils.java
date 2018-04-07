package web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockHttpServletResponse;
import web.domain.entity.Device;
import web.domain.response.ErrorCode;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

class TestingUtils {

    // Tomcat trims space automatically - behaviour cannot be changed
    private static final String APPLICATION_VND_COLLECTION_JSON = "application/vnd.collection+json; charset=utf-8";
    private static final String APPLICATION_VND_COLLECTION_JSON_WITHOUT_SPACE = "application/vnd.collection+json;charset=utf-8";

    private static final ObjectMapper mapper = new ObjectMapper();

    static void assertContentType(MockHttpServletResponse response) {
        assertThat(response.getContentType(), anyOf(is(APPLICATION_VND_COLLECTION_JSON), is(APPLICATION_VND_COLLECTION_JSON_WITHOUT_SPACE)));
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

    static void assertHref(MockHttpServletResponse response, String href) throws Exception {
        JsonNode collection = parseToCollection(response);
        assertThat(collection.get("href").toString(), containsString(href));
    }

    static void assertError(MockHttpServletResponse response, ErrorCode errorCode) throws Exception {
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

    static void assertData(String jsonValue, Object value) {
        assertThat(jsonValue, is(String.format("%s", value)));
    }

    static Device getTestDevice() {
        Device device = new Device();
        device.setName("test-device");
        return device;
    }
}
