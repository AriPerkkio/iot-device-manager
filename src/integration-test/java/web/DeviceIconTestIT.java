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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static web.TestingUtils.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
public class DeviceIconTestIT {

    private static final String URI = "/api/device-icons";
    private static final String ID_URI = URI + "/%d";
    private static final String SECOND_ICON_NAME = "second-icon.png";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void logEmptyRowBeforeEachTest() {
        log.info("");
    }

    @After
    public void cleanTestIcons() throws Exception {
        removeTestIcons();
    }

    /**
     * Test get device icons without parameters returns all icons' information
     */
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

    /**
     * Test get device icon returns error when request parameter is invalid type
     */
    @Test
    @Transactional
    public void testGetDeviceIconReturnsErrorWhenRequestParameterInvalidType() throws Exception {
        log.info("Test GET /api/device-icons returns error when request parameter is invalid type");
        // Given
        String id = "string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(URI)
                .param("id", id)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test get device icon by ID returns only the expected device icon
     */
    @Test
    @Transactional
    public void testGetDeviceIconByIdReturnsExpectedDeviceIcon() throws Exception {
        log.info("Test GET /device-icons/{id} returns only the expected device icon");

        // Given
        DeviceIcon expected = getTestIcon();
        Integer id = addIcon(expected, getTestIconFile(), mockMvc);
        addIcon(getTestIcon(SECOND_ICON_NAME), getTestIconFile(), mockMvc);
        String idUri = String.format(ID_URI, id);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertContentType(response);
        assertHref(response, idUri);

        JsonNode items = parseToItems(response);
        assertEquals(items.size(), 1);

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("name"), expected.getName());
        assertData(data.get("id"), id);
    }

    /**
     * Test get device icons by ID returns error when no device icon found
     */
    @Test
    @Transactional
    public void testGetDeviceIconsByIdReturnsErrorWhenNoDeviceIconFound() throws Exception {
        log.info("Test GET /api/device-icons returns error when no device icon found");

        // Given
        String idUri = String.format(ID_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test get device icon by ID returns error when ID is invalid type
     */
    @Test
    @Transactional
    public void testGetDeviceIconByIdReturnsErrorWhenIdInvalidType() throws Exception {
        log.info("Test GET /api/device-icons/{id} returns error when ID is invalid type");

        // Given
        String idUri = URI + "/string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertContentType(response);
        assertHref(response, idUri);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test get device icon file by name returns expected file
     */
    @Test
    @Transactional
    public void testGetDeviceIconFileByNameReturnsIconFile() throws Exception {
        log.info("Test GET /api/device-icons/{name}.png returns expected file");

        // Given
        MockMultipartFile expected = getTestIconFile();
        DeviceIcon deviceIcon = getTestIcon();
        addIcon(deviceIcon, expected, mockMvc);
        String nameUri = URI + "/" + deviceIcon.getName();

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(nameUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertContentType(response, MediaType.IMAGE_PNG_VALUE);
        assertIcons(response, expected);
    }

    /**
     * Test get device icon file by name returns error when no icon found
     */
    @Test
    @Transactional
    public void testGetDeviceIconFileByNameReturnsErrorWhenIconNotFound() throws Exception {
        log.info("Test GET /api/device-icons/{name}.png returns error when icon not found");

        // Given
        DeviceIcon deviceIcon = getTestIcon();
        String nameUri = URI + "/" + deviceIcon.getName();

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(nameUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertContentType(response);
        assertHref(response, nameUri);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test add device icon inserts icon to database and file system
     */
    @Test
    @Transactional
    public void testAddDeviceIconInsertsIconToDatabaseAndFileSystem() throws Exception {
        log.info("Test POST /api/device-icons inserts icon to database and file system");

        // Given
        DeviceIcon deviceIcon = getTestIcon();
        MockMultipartFile deviceIconFile = getTestIconFile();

        // When
        MockHttpServletResponse postResponse = mockMvc
            .perform(fileUpload(URI)
                .file(deviceIconFile)
                .param("name", deviceIcon.getName())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(postResponse, HttpStatus.CREATED);
        assertContentType(postResponse);
        assertHref(postResponse, URI);
        assertIconExists(deviceIcon.getName());

        assertStatus(getResponse, HttpStatus.OK);

        JsonNode items = parseToItems(getResponse);
        assertEquals(items.size(), 1);

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("name"), deviceIcon.getName());
        assertNotNull(data.get("id"));
    }

    /**
     * Test add device without name returns error
     */
    @Test
    @Transactional
    public void testAddDeviceWithoutNameReturnsError() throws Exception {
        log.info("Test POST /api/device-icons without name returns error");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(fileUpload(URI)
                .file(getTestIconFile())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test add device without icon returns error
     */
    @Test
    @Transactional
    public void testAddDeviceWithoutIconReturnsError() throws Exception {
        log.info("Test POST /api/device-icons without icon returns error");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(URI)
                .param("name", getTestIcon().getName())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test add device with name returns error
     */
    @Test
    @Transactional
    public void testAddDeviceWithInvalidNameReturnsError() throws Exception {
        log.info("Test POST /api/device-icons with invalid name returns error");

        // Given
        MockMultipartFile iconFile = getTestIconFile();
        String invalidName = "script.sh";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(fileUpload(URI)
                .file(iconFile)
                .param("name", invalidName)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }


    /**
     * Test renamed device icon modifies icon in database and file system
     */
    @Test
    @Transactional
    public void testRenameDeviceIconModifiesDatabaseAndFileSystem() throws Exception {
        log.info("Test PUT /api/device-icons modifies icon in database and file system");

        // Given
        DeviceIcon expected = getTestIcon("renamed-device-icon.png");
        DeviceIcon initialDeviceIcon = getTestIcon();
        addIcon(initialDeviceIcon, getTestIconFile(), mockMvc);

        // When
        MockHttpServletResponse putResponse = mockMvc
            .perform(put(URI)
                .param("name", initialDeviceIcon.getName())
                .content(mapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(putResponse, HttpStatus.OK);
        assertContentType(putResponse);
        assertHref(putResponse, URI);
        assertIconExists(expected.getName());

        assertStatus(getResponse, HttpStatus.OK);

        JsonNode items = parseToItems(getResponse);
        assertEquals(items.size(), 1);

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("name"), expected.getName());
    }

    /**
     * Test renamed device icon by ID modifies icon in database and file system
     */
    @Test
    @Transactional
    public void testRenameDeviceIconByIdModifiesDatabaseAndFileSystem() throws Exception {
        log.info("Test PUT /api/device-icons/{id} modifies icon in database and file system");

        // Given
        DeviceIcon expected = getTestIcon("renamed-device-icon.png");
        DeviceIcon initialDeviceIcon = getTestIcon();
        Integer deviceIconId = addIcon(initialDeviceIcon, getTestIconFile(), mockMvc);
        String idUri = String.format(ID_URI, deviceIconId);

        // When
        MockHttpServletResponse putResponse = mockMvc
            .perform(put(idUri)
                .content(mapper.writeValueAsString(expected))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(putResponse, HttpStatus.OK);
        assertContentType(putResponse);
        assertHref(putResponse, idUri);
        assertIconExists(expected.getName());

        assertStatus(getResponse, HttpStatus.OK);

        JsonNode items = parseToItems(getResponse);
        assertEquals(items.size(), 1);

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("name"), expected.getName());
    }


    /**
     * Test rename device icon returns error when icon is not found
     */
    @Test
    @Transactional
    public void testRenameDeviceIconReturnsErrorWhenIconNotFound() throws Exception {
        log.info("Test PUT /api/device-icons returns error when icon is not found");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(URI)
                .param("name", "unknown-name.png")
                .content(mapper.writeValueAsString(getTestIcon()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response , HttpStatus.NOT_FOUND);
        assertContentType(response );
        assertHref(response , URI);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test rename device icon by ID returns error when icon is not found
     */
    @Test
    @Transactional
    public void testRenameDeviceIconByIdReturnsErrorWhenIconNotFound() throws Exception {
        log.info("Test PUT /api/device-icons/{id} returns error when icon is not found");

        // Given
        String idUri = String.format(ID_URI, 1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(idUri)
                .content(mapper.writeValueAsString(getTestIcon()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response , HttpStatus.NOT_FOUND);
        assertContentType(response );
        assertHref(response , idUri);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test rename device icon returns error when request parameters not used
     */
    @Test
    @Transactional
    public void testRenameDeviceIconReturnsErrorWhenRequestParametersNotUsed() throws Exception {
        log.info("Test PUT /api/device-icons returns error when request parameters not used");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(URI)
                .content(mapper.writeValueAsString(getTestIcon()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response , HttpStatus.BAD_REQUEST);
        assertContentType(response );
        assertHref(response , URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test rename device icon returns error when request body is missing
     */
    @Test
    @Transactional
    public void testRenameDeviceIconReturnsErrorWhenRequestBodyMissing() throws Exception {
        log.info("Test PUT /api/device-icons returns error when request body is missing");

        // Given
        DeviceIcon deviceIcon = getTestIcon();
        addIcon(deviceIcon, getTestIconFile(), mockMvc);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(URI)
                .param("name", deviceIcon.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response , HttpStatus.BAD_REQUEST);
        assertContentType(response );
        assertHref(response , URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test rename device icon returns error when request body contains invalid name
     */
    @Test
    @Transactional
    public void testRenameDeviceIconReturnsErrorWhenRequestBodyContainsInvalidName() throws Exception {
        log.info("Test PUT /api/device-icons returns error when request body contains invalid name");

        // Given
        DeviceIcon deviceIcon = getTestIcon();
        addIcon(deviceIcon, getTestIconFile(), mockMvc);

        String originalName = deviceIcon.getName();
        deviceIcon.setName("script.sh");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(URI)
                .param("name", originalName)
                .content(mapper.writeValueAsString(deviceIcon))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response , HttpStatus.BAD_REQUEST);
        assertContentType(response );
        assertHref(response , URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test rename device icon returns error when request body contains invalid name
     */
    @Test
    @Transactional
    public void testRenameDeviceIconByIdReturnsErrorWhenIdInvalidType() throws Exception {
        log.info("Test PUT /api/device-icons returns error when ID is invalid type");

        // Given
        DeviceIcon deviceIcon = getTestIcon();
        addIcon(deviceIcon, getTestIconFile(), mockMvc);

        String idUri = URI + "/string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(idUri)
                .content(mapper.writeValueAsString(deviceIcon))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response , HttpStatus.BAD_REQUEST);
        assertContentType(response );
        assertHref(response , idUri);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test delete device icon removes icon in database and file system
     */
    @Test
    @Transactional
    public void testDeleteDeviceIconRemovesIconInDatabaseAndFileSystem() throws Exception {
        log.info("Test DELETE /api/device-icons removes icon in database and file system");

        // Given
        DeviceIcon deviceIcon = getTestIcon();
        addIcon(deviceIcon, getTestIconFile(), mockMvc);

        // When
        MockHttpServletResponse deleteResponse = mockMvc
            .perform(delete(URI)
                .param("name", deviceIcon.getName())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(deleteResponse, HttpStatus.NO_CONTENT);

        assertStatus(getResponse, HttpStatus.NOT_FOUND);
        assertError(getResponse, ErrorCode.NO_ITEMS_FOUND);
        assertNoIconExists(deviceIcon.getName());
    }

    /**
     * Test delete device icon by ID removes icon in database and file system
     */
    @Test
    @Transactional
    public void testDeleteDeviceIconByIdRemovesIconInDatabaseAndFileSystem() throws Exception {
        log.info("Test DELETE /api/device-icons/{id} removes icon in database and file system");

        // Given
        DeviceIcon deviceIcon = getTestIcon();
        Integer deviceIconId = addIcon(deviceIcon, getTestIconFile(), mockMvc);
        String idUri = String.format(ID_URI, deviceIconId);

        // When
        MockHttpServletResponse deleteResponse = mockMvc
            .perform(delete(idUri)
                .param("name", deviceIcon.getName())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(deleteResponse, HttpStatus.NO_CONTENT);

        assertStatus(getResponse, HttpStatus.NOT_FOUND);
        assertError(getResponse, ErrorCode.NO_ITEMS_FOUND);
        assertNoIconExists(deviceIcon.getName());
    }

    /**
     * Test delete device icon returns error when icon not found
     */
    @Test
    @Transactional
    public void testDeleteDeviceIconReturnsErrorWhenIconNotFound() throws Exception {
        log.info("Test DELETE /api/device-icons returns error when icon not found");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(URI)
                .param("name", getTestIcon().getName())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test delete device icon by ID returns error when icon not found
     */
    @Test
    @Transactional
    public void testDeleteDeviceIconByIdReturnsErrorWhenIconNotFound() throws Exception {
        log.info("Test DELETE /api/device-icons returns error when icon not found");

        // Given
        String idUri = String.format(ID_URI, 1);

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
     * Test delete device icon by ID returns error when ID is invalid type
     */
    @Test
    @Transactional
    public void testDeleteDeviceIconByIdReturnsErrorWhenIdInvalidType() throws Exception {
        log.info("Test DELETE /api/device-icons returns error when ID is invalid type");

        // Given
        String idUri = URI + "/string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(idUri).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertHref(response, idUri);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test delete device icon returns error when no request parameters used
     */
    @Test
    @Transactional
    public void testDeleteDeviceIconReturnsErrorWhenNoRequestParameters() throws Exception {
        log.info("Test DELETE /api/device-icons returns error when no request parameters used");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

}
