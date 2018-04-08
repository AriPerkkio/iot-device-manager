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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import web.domain.entity.Measurement;
import web.domain.response.ErrorCode;

import java.util.Map;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        assertEquals(response.getStatus(), HttpStatus.OK.value());
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
        assertEquals(response.getStatus(), HttpStatus.OK.value());
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
//    @Test
//  @Transactional
    public void testGetMeasurementsByTime() throws Exception {

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
        assertEquals(response.getStatus(), HttpStatus.NOT_FOUND.value());
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
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        assertContentType(response);
        assertHref(response, URI);
        assertError(response, ErrorCode.PARAMETER_VALIDATION_ERROR);
    }
}
