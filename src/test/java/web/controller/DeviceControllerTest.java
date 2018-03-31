package web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.hamnaberg.json.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import web.Application;
import web.configuration.MvcConfig;
import web.configuration.WebSecurityConfig;
import web.domain.entity.Device;
import web.domain.response.ErrorCode;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionWrapper;
import web.mapper.DeviceMapper;
import web.service.DeviceService;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DeviceController.class)
@ContextConfiguration(classes= {Application.class, MvcConfig.class, WebSecurityConfig.class})
public class DeviceControllerTest {

    private static final String URI = "/api/devices";
    private static final String APPLICATION_VND_COLLECTION_JSON = "application/vnd.collection+json;charset=utf-8";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Value("${iotdevicemanager.username}")
    String user;

    @Value("${iotdevicemanager.password}")
    String password;

    @Before
    public void setup() {
        // Set request attributes for mappers URI building
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));
    }

    /**
     * Test get devices returns devices with correct content-type
     */
    @Test
    public void testGetDevices() throws Exception {
        // Given
        Collection jsonContent = DeviceMapper.mapToCollection(getTestDevice());
        ResponseWrapper responseWrapper = new ResponseWrapper(jsonContent);

        // When & Then
        when(deviceService.getDevices(any(Integer.class), any(String.class), any(Integer.class), any(Integer.class), any(Integer.class), any(String.class)))
                .thenReturn(responseWrapper);

        mockMvc.perform(get(URI).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(content().string(jsonContent.toString()));
    }

    /**
     * Test get devices returns error when request parameter is invalid type
     */
    @Test
    public void testGetDevicesReturnsErrorWhenRequestParameterInvalid() throws Exception {
        // Given
        String deviceGroupId = "string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc.perform(get(URI).param("deviceGroupId", deviceGroupId).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        // Then
        JsonNode error = responseToErrorNode(response);
        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test get devices returns error when service throws
     */
    @Test
    public void testGetDevicesReturnsErrorWhenServiceThrows() throws Exception {
        // Given
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper("test-exception", "test", ErrorCode.INTERNAL_ERROR);

        // When
        when(deviceService.getDevices(any(Integer.class), any(String.class), any(Integer.class), any(Integer.class), any(Integer.class), any(String.class)))
                .thenThrow(exceptionWrapper);

        // Then
        MockHttpServletResponse response = mockMvc.perform(get(URI).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(status().isInternalServerError()).andReturn().getResponse();

        JsonNode error = responseToErrorNode(response);
        assertThat(error.get("code").asText(), is(ErrorCode.INTERNAL_ERROR.getCode()));
    }

    /**
     * Test get device returns device with correct content-type
     */
    @Test
    public void testGetDevice() throws Exception {
        // Given
        Collection jsonContent = DeviceMapper.mapToCollection(getTestDevice());
        ResponseWrapper responseWrapper = new ResponseWrapper(jsonContent);

        // When & Then
        when(deviceService.getDevices(any(Integer.class), any(String.class), any(Integer.class), any(Integer.class), any(Integer.class), any(String.class)))
                .thenReturn(responseWrapper);

        mockMvc.perform(get(URI + "/1").with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(content().string(jsonContent.toString()));
    }

    /**
     * Test get device returns error when path parameter is invalid type
     */
    @Test
    public void testGetDeviceReturnsErrorWhenPathParameterInvalid() throws Exception {
        // Given
        Collection jsonContent = DeviceMapper.mapToCollection(getTestDevice());
        ResponseWrapper responseWrapper = new ResponseWrapper(jsonContent);
        String idUri = "/string-instead-of-integer";

        // When & Then
        MockHttpServletResponse response = mockMvc.perform(get(URI + idUri).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        JsonNode error = responseToErrorNode(response);
        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test add device returns device with correct content-type
     */
    @Test
    public void testAddDevice() throws Exception {
        // Given
        Device expected = getTestDevice();
        Collection jsonContent = DeviceMapper.mapToCollection(expected);

        // When
        when(deviceService.addDevice(any(Device.class)))
                .thenReturn(new ResponseWrapper(jsonContent));

        // Then
        mockMvc.perform(post(URI).content(mapper.writeValueAsString(expected)).contentType(MediaType.APPLICATION_JSON).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(content().string(jsonContent.toString()));
    }

    /**
     * Test add device returns error when request body is missing device name
     */
    @Test
    public void testAddDeviceReturnsErrorMissingDeviceName() throws Exception {
        // Given
        Device expected = getTestDevice();
        expected.setName(null);

        // When & Then
        MockHttpServletResponse response = mockMvc.perform(post(URI).content(mapper.writeValueAsString(expected)).contentType(MediaType.APPLICATION_JSON).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        JsonNode error = responseToErrorNode(response);
        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test add device returns error when service throws
     */
    @Test
    public void testAddDeviceReturnsErrorWhenServiceThrows() throws Exception {
        // Given
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper("test-exception", "test", ErrorCode.INTERNAL_ERROR);

        // When
        when(deviceService.addDevice(any(Device.class)))
                .thenThrow(exceptionWrapper);

        // Then
        MockHttpServletResponse response = mockMvc.perform(post(URI).content(mapper.writeValueAsString(getTestDevice())).contentType(MediaType.APPLICATION_JSON).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn().getResponse();

        JsonNode error = responseToErrorNode(response);
        assertThat(error.get("code").asText(), is(ErrorCode.INTERNAL_ERROR.getCode()));
    }

    /**
     * Test update device returns device with correct content-type
     */
    @Test
    public void testUpdateDevice() throws Exception {
        // Given
        Device expected = getTestDevice();
        Collection jsonContent = DeviceMapper.mapToCollection(expected);

        // When
        when(deviceService.updateDevice(any(Integer.class), any(String.class), any(String.class), any(Device.class)))
                .thenReturn(new ResponseWrapper(jsonContent));

        // Then
        mockMvc.perform(put(URI).content(mapper.writeValueAsString(expected)).contentType(MediaType.APPLICATION_JSON).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(content().string(jsonContent.toString()));

    }

    /**
     * Test update device returns error when request body is missing device name
     */
    @Test
    public void testUpdateDeviceReturnsErrorMissingDeviceName() throws Exception {
        // Given
        Device expected = getTestDevice();
        expected.setName(null);

        // When
        MockHttpServletResponse response = mockMvc.perform(put(URI).content(mapper.writeValueAsString(expected)).contentType(MediaType.APPLICATION_JSON).with(httpBasic(user,password)))
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        // Then
        JsonNode error = responseToErrorNode(response);
        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test update device returns error when request parameter is invalid type
     */
    @Test
    public void testUpdateDeviceReturnsErrorWhenRequestParameterInvalidType() throws Exception {
        // Given
        String id = "string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc.perform(put(URI)
                .content(mapper.writeValueAsString(getTestDevice()))
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", id)
                .with(httpBasic(user,password)))
                    .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                    .andExpect(status().isBadRequest())
                    .andReturn().getResponse();

        // Then
        JsonNode error = responseToErrorNode(response);
        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    /**
     * Test delete device returns correct responseWrapper with correct content-type
     */
    @Test
    public void testDeleteDevice() throws Exception {
        // When
        when(deviceService.deleteDevice(any(Integer.class), any(String.class), any(String.class)))
                .thenReturn(new ResponseWrapper("", HttpStatus.NO_CONTENT));

        // Then
        mockMvc.perform(delete(URI).with(httpBasic(user,password)))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andExpect(content().string(""));
    }

    /**
     * Test delete device returns error when request parameter is invalid type
     */
    @Test
    public void testDeleteDeviceReturnsErrorWhenRequestParameterInvalid() throws Exception {
        // Given
        String id = "string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc.perform(delete(URI).param("id", id).with(httpBasic(user,password)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
                .andReturn().getResponse();

        // Then
        JsonNode error = responseToErrorNode(response);
        assertThat(error.get("code").asText(), is(ErrorCode.PARAMETER_VALIDATION_ERROR.getCode()));
    }

    private JsonNode responseToErrorNode(MockHttpServletResponse response) throws Exception {
        JsonNode jsonNode = mapper.readTree(response.getContentAsString());
        JsonNode collection = jsonNode.get("collection");
        return collection.get("error");
    }

    private Device getTestDevice() {
        Device device = new Device();
        device.setId(1);
        device.setName("test-device");
        device.setDeviceTypeId(1);
        device.setDeviceGroupId(1);
        device.setConfigurationId(1);
        device.setAuthenticationKey("abcdefghi123456789");
        return device;
    }
}