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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
        JsonNode collection = jsonNode.get("collection");
        JsonNode items = collection.get("items");

        assertThat(items.size(), is(1));
        Map<String, String> data = dataToMap(items.get(0).get("data"));

        assertNotNull(data.get("id"));
        assertData(data.get("name"), expected.getName());
        assertData(data.get("deviceTypeId"), expected.getDeviceTypeId());
        assertData(data.get("deviceGroupId"), expected.getDeviceGroupId());
        assertData(data.get("configurationId"), expected.getConfigurationId());
        assertNotNull(data.get("authenticationKey"));
    }

    private void assertData(String jsonValue, Object value) {
        assertThat(jsonValue, is(String.format("%s", value)));
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
