package web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import web.domain.entity.DeviceIcon;
import web.domain.response.ErrorCode;

import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static web.TestingUtils.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class DeviceIconTestIT {

    private static final String URI = "/api/device-icons";
    private static final String SECOND_ICON_NAME = "second-icon.png";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void logEmptyRowBeforeEachTest() {
        log.info("");
    }

    @After
    public void cleanTestIcons() throws Exception {
        removeTestIcon(getTestIcon().getName());
        removeTestIcon(SECOND_ICON_NAME);
    }

    /**
     * Test get device icons returns error when no device icons are found
     */
    @Test
    @Transactional
    public void testGetDeviceIconsReturnsErrorWhenNoDeviceIconsFound() throws Exception {
        log.info("Test GET /api/device-icons returns error when no device icons found");

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    @Test
    @Transactional
    public void testGetDeviceIconsWithoutParametersReturnsAllIcons() throws Exception {
        log.info("Test GET /api/device-icons without request parameters returns all icons' information");

        // Given
        DeviceIcon deviceIcon = getTestIcon();
        DeviceIcon deviceIconTwo = getTestIcon(SECOND_ICON_NAME);

        addIcon(deviceIcon, getTestIconFile(), mockMvc);
        addIcon(deviceIconTwo, getTestIconFile(), mockMvc);

        // When
        MockHttpServletResponse response = mockMvc
                .perform(get(URI).with(getBasicAuth()))
                .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertContentType(response);
        assertHref(response, URI);

        JsonNode items = parseToItems(response);
        assertEquals(items.size(), 2);

        for(JsonNode item : items) {
            Map<String, String> data = dataToMap(item.get("data"));
            assertData(data.get("name"), anyOf(is(deviceIcon.getName()), is(deviceIconTwo.getName())));
            assertNotNull(data.get("id"));
        }
    }
}
