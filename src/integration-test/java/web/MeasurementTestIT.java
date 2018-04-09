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
import web.domain.entity.Measurement;
import web.domain.response.ErrorCode;

import java.util.Date;
import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static web.TestingUtils.*;
import static web.mapper.MapperUtils.formatTime;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = Application.class)
@AutoConfigureMockMvc
public class MeasurementTestIT {

    private static final String URI = "/api/measurements";
    private final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void logEmptyRowBeforeEachTest() {
        log.info("");
    }

    /**
     * Test get measurements without parameters returns all measurements
     */
    @Test
    @Transactional
    public void testGetMeasurements() throws Exception {
        log.info("Test GET /api/measurements without parameters returns all measurements");

        // Given
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        Measurement measurement = getTestMeasurement(deviceId);
        addMeasurement(measurement, mockMvc);

        Integer deviceIdTwo = addDevice(getTestDevice("device-two"), mockMvc);
        Measurement measurementTwo = getTestMeasurement(deviceIdTwo);
        addMeasurement(measurementTwo, mockMvc);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertContentType(response);
        assertHref(response, URI);

        JsonNode items = parseToItems(response);
        assertThat(items.size(), is(2));

        for(JsonNode item : items) {
            Map<String, String> data = dataToMap(item.get("data"));

            assertNotNull(data.get("deviceId"));
            assertData(data.get("content"), mapper.writeValueAsString(measurement.getContent()));
            assertThat(data.get("time"), anyOf(is(formatTime(measurement.getTime())), is(formatTime(measurementTwo.getTime()))));
        }
    }

    /**
     * Test get measurements with request parameters returns only matching measurements
     */
    @Test
    @Transactional
    public void testGetMeasurementsWithParametersReturnsOnlyMatchingMeasurements() throws Exception {
        log.info("Test GET /api/measurements with request parameters returns only matching measurements");

        // Given
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        Measurement measurement = getTestMeasurement(deviceId);
        addMeasurement(measurement, mockMvc);

        Integer deviceIdTwo = addDevice(getTestDevice("device-two"), mockMvc);
        Measurement measurementTwo = getTestMeasurement(deviceIdTwo);
        addMeasurement(measurementTwo, mockMvc);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(URI)
                .param("deviceId", deviceIdTwo.toString())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertContentType(response);
        assertHref(response, URI);

        JsonNode items = parseToItems(response);
        assertThat(items.size(), is(1));

        for(JsonNode item : items) {
            Map<String, String> data = dataToMap(item.get("data"));

            assertData(data.get("deviceId"), deviceIdTwo);
            assertData(data.get("content"), mapper.writeValueAsString(measurementTwo.getContent()));
            assertData(data.get("time"), formatTime(measurementTwo.getTime()));
        }
    }

    /**
     * Test get measurements with time window filtering returns only matching measurements
     */
    @Test
    @Transactional
    public void testGetMeasurementsByTime() throws Exception {
        log.info("Test GET /api/measurements using time window filtering returns only expected measurements");

        // Given
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        Measurement measurementInTimeWindow = getTestMeasurement(deviceId);
        Measurement measurementNotInTimeWindow = getTestMeasurement(deviceId);

        Date searchStartTime = stringToDate("01-01-2000 00:00:05");
        Date searchEndTime = stringToDate("01-01-2000 00:00:15");
        measurementInTimeWindow.setTime(stringToDate("01-01-2000 00:00:10"));
        measurementNotInTimeWindow.setTime(stringToDate("01-01-2000 00:00:20"));

        addMeasurement(measurementInTimeWindow, mockMvc);
        addMeasurement(measurementNotInTimeWindow, mockMvc);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(URI)
                .param("startTime", formatTime(searchStartTime))
                .param("endTime", formatTime(searchEndTime))
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.OK);
        assertHref(response, URI);
        assertContentType(response);

        JsonNode measurements = parseToItems(response);
        assertEquals(measurements.size(), 1);

        Map<String, String> data = dataToMap(measurements.get(0).get("data"));
        assertData(data.get("deviceId"), measurementInTimeWindow.getDeviceId());
        assertData(data.get("time"), formatTime(measurementInTimeWindow.getTime()));
        assertData(data.get("content"), mapper.writeValueAsString(measurementInTimeWindow.getContent()));
    }

    /**
     * Test get measurements returns error when no measurements found
     */
    @Test
    @Transactional
    public void testGetMeasurementsReturnsErrorWhenDeviceHasNoMeasurements() throws Exception {
        log.info("Test GET /api/measurements returns error when no measurements found");

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
     * Test get measurements returns error when request parameter is invalid type
     */
    @Test
    @Transactional
    public void testGetMeasurementsReturnsErrorWhenRequestParameterInvalidType() throws Exception {
        log.info("Test GET /api/measurements returns error when request parameter is invalid type");

        // Given
        String deviceId = "string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(get(URI)
                .param("deviceId", deviceId)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test add measurement inserts new measurement
     */
    @Test
    @Transactional
    public void testAddMeasurement() throws Exception {
        log.info("Test POST /api/measurements inserts new measurement");

        // Given
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        Measurement measurement = getTestMeasurement(deviceId);

        // When
        MockHttpServletResponse postResponse = mockMvc
            .perform(post(URI)
                .content(mapper.writeValueAsString(measurement))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(postResponse, HttpStatus.CREATED);
        assertHref(postResponse, URI);
        assertContentType(postResponse);

        assertStatus(getResponse, HttpStatus.OK);

        JsonNode items = parseToItems(getResponse);
        assertEquals(items.size(), 1);
        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("deviceId"), measurement.getDeviceId());
        assertData(data.get("time"), formatTime(measurement.getTime()));
        assertData(data.get("content"), mapper.writeValueAsString(measurement.getContent()));
    }

    /**
     * Test add measurement returns error when device not found
     */
    @Test
    @Transactional
    public void testAddMeasurementReturnsErrorWhenDeviceNotFound() throws Exception {
        log.info("Test POST /api/measurements returns error when device not found");

        // Given
        Measurement measurement = getTestMeasurement(1);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(URI)
                .content(mapper.writeValueAsString(measurement))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }

    /**
     * Test add measurement returns error when request body is missing
     */
    @Test
    @Transactional
    public void testAddMeasurementReturnsErrorWhenRequestBodyIsMissing() throws Exception {
        log.info("Test POST /api/measurements returns error when request body is missing");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(URI)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test add measurement returns error when request body is invalid
     */
    @Test
    @Transactional
    public void testAddMeasurementReturnsErrorWhenRequestBodyIsInvalid() throws Exception {
        log.info("Test POST /api/measurements returns error when request body is invalid");

        // Given
        String body = mapper.writeValueAsString(getTestMeasurement(1))
            .replace("deviceId\":1", "deviceId\":\"string-value\"");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(post(URI)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertHref(response, URI);
        assertContentType(response);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test update measurement returns error
     */
    @Test
    @Transactional
    public void testUpdateMeasurementReturnsError() throws Exception {
        log.info("Test PUT /api/measurements returns error with NOT_IMPLEMENTED status");

        // Given
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        Measurement measurement = getTestMeasurement(deviceId);

        // When
        MockHttpServletResponse response = mockMvc
            .perform(put(URI)
                .content(mapper.writeValueAsString(measurement))
                .contentType(MediaType.APPLICATION_JSON)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_IMPLEMENTED);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.INTERNAL_ERROR);
    }

    /**
     * Test delete measurements with request parameter deviceId removes all device's measurements
     */
    @Test
    @Transactional
    public void testDeleteMeasurementsWithDeviceId() throws Exception {
        log.info("Test DELETE /api/measurements with request parameter deviceId removes all device's measurements");

        // Given
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        addMeasurement(getTestMeasurement(deviceId), mockMvc);
        addMeasurement(getTestMeasurement(deviceId), mockMvc);

        // When
        MockHttpServletResponse deleteResponse = mockMvc
            .perform(delete(URI)
                .param("deviceId", deviceId.toString())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI)
                .param("deviceId", deviceId.toString())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(deleteResponse, HttpStatus.NO_CONTENT);
        assertContentType(deleteResponse);
        assertStatus(getResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Test delete measurements with time window filter removes correct measurements
     */
    @Test
    @Transactional
    public void testDeleteMeasurementsWithTimeWindowFiltering() throws Exception {
        log.info("Test DELETE /api/measurements with time window filter removes correct measurements");

        // Given
        Integer deviceId = addDevice(getTestDevice(), mockMvc);
        Measurement measurementInTimeWindow = getTestMeasurement(deviceId);
        Measurement measurementNotInTimeWindow = getTestMeasurement(deviceId);

        Date searchStartTime = stringToDate("01-01-2000 00:00:05");
        Date searchEndTime = stringToDate("01-01-2000 00:00:15");
        measurementInTimeWindow.setTime(stringToDate("01-01-2000 00:00:10"));
        measurementNotInTimeWindow.setTime(stringToDate("01-01-2000 00:00:20"));

        addMeasurement(measurementInTimeWindow, mockMvc);
        addMeasurement(measurementNotInTimeWindow, mockMvc);

        // When
        MockHttpServletResponse deleteResponse = mockMvc
            .perform(delete(URI)
                .param("startTime", formatTime(searchStartTime))
                .param("endTime", formatTime(searchEndTime))
                .with(getBasicAuth()))
            .andReturn().getResponse();

        MockHttpServletResponse getResponse = mockMvc
            .perform(get(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(deleteResponse, HttpStatus.NO_CONTENT);
        assertContentType(deleteResponse);
        assertStatus(getResponse, HttpStatus.OK);

        JsonNode items = parseToItems(getResponse);
        assertEquals(items.size(), 1);

        Map<String, String> data = dataToMap(items.get(0).get("data"));
        assertData(data.get("deviceId"), measurementNotInTimeWindow.getDeviceId());
        assertData(data.get("time"), formatTime(measurementNotInTimeWindow.getTime()));
        assertData(data.get("content"), mapper.writeValueAsString(measurementNotInTimeWindow.getContent()));
    }

    /**
     * Test delete measurements without request parameters returns error
     */
    @Test
    @Transactional
    public void testDeleteMeasurementsWithoutRequestParametersReturnsError() throws Exception {
        log.info("Test DELETE /api/measurements without request parameters returns error");

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(URI).with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test delete measurements returns error when request parameter is invalid type
     */
    @Test
    @Transactional
    public void testDeleteMeasurementsReturnsErrorWhenRequestParameterIsInvalidType() throws Exception {
        log.info("Test DELETE /api/measurements returns error when request parameter is invalid type");

        // Given
        String deviceId = "string-instead-of-integer";

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(URI)
                .param("deviceId", deviceId)
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.BAD_REQUEST);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }

    /**
     * Test delete measurements returns error when device is not found
     */
    @Test
    @Transactional
    public void testDeleteMeasurementsReturnsErrorWhenDeviceIsNotFound() throws Exception {
        log.info("Test DELETE /api/measurements returns error when device is not found");

        // Given
        Integer deviceId = 1;

        // When
        MockHttpServletResponse response = mockMvc
            .perform(delete(URI)
                .param("deviceId", deviceId.toString())
                .with(getBasicAuth()))
            .andReturn().getResponse();

        // Then
        assertStatus(response, HttpStatus.NOT_FOUND);
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.NO_ITEMS_FOUND);
    }
}
