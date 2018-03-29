package web.controller;

import net.hamnaberg.json.Collection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import web.Application;
import web.configuration.MvcConfig;
import web.configuration.WebSecurityConfig;
import web.domain.entity.Device;
import web.domain.response.ResponseWrapper;
import web.mapper.DeviceMapper;
import web.service.DeviceService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DeviceController.class)
@ContextConfiguration(classes= {Application.class, MvcConfig.class, WebSecurityConfig.class})
public class DeviceControllerTest {

    private static final String APPLICATION_VND_COLLECTION_JSON = "application/vnd.collection+json; charset=utf-8";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceService deviceService;

    @Before
    public void setup() {
        // Set request attributes for mappers URI building
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    /**
     * temp
     */
    @Test
    public void testGetDevices() throws Exception {
        // Given
        Collection jsonContent = DeviceMapper.mapToCollection(getTestDevice());
        ResponseWrapper responseWrapper = new ResponseWrapper(jsonContent);
        when(deviceService.getDevices(any(Integer.class), any(String.class), any(Integer.class), any(Integer.class), any(Integer.class), any(String.class)))
            .thenReturn(responseWrapper);

        // When & Then
        mockMvc.perform(get("/api/devices"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_VND_COLLECTION_JSON))
            .andExpect(content().string(jsonContent.toString()));
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

    /*
     *
    @Test
    public void test() {
        // Given

        // When

        // Then
    }
    */
}