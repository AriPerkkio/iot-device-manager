package web.repository.impl;

import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import web.domain.entity.Device;
import web.domain.entity.Measurement;
import web.repository.DeviceRepository;
import web.repository.MeasurementRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeasurementRepositoryTest {

    @Autowired
    MeasurementRepository measurementRepository;

    @Autowired
    DeviceRepository deviceRepository;

    private Integer deviceId;

    /**
     * Add new device before each test. Measurement has dependency on device_icon.id
     * Manual teardown not required - @Transactional tests already take care of rollback
     */
    @Before
    public void setup() {
        Device device = new Device();
        device.setName("test-device-name");
        this.deviceId = deviceRepository.addDevice(device).getId();
    }

    /**
     * Test add_measurement returns added measurement
     */
    @Transactional
    @Test
    public void testAddMeasurementReturnsAddedMeasurement() throws Exception {
        // Given
        Measurement expected = getTestMeasurement();

        // When
        Measurement result = measurementRepository.addMeasurement(expected);

        // Then
        assertThat(result.getDeviceId(), equalTo(expected.getDeviceId()));
    }

    /**
     * Test add_measurement fails when foreign key device_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddMeasurementThrowsWhenForeignKeyConflicts() throws Exception {
        // Given
        Measurement measurement = getTestMeasurement();
        measurement.setDeviceId(999);

        // When
        measurementRepository.addMeasurement(measurement);
    }

    /**
     * Test add_measurement fails when device_id is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddMeasurementThrowsWhenDeviceIdNull() throws Exception {
        // Given
        Measurement measurement = getTestMeasurement();
        measurement.setDeviceId(null);

        // When
        measurementRepository.addMeasurement(measurement);
    }


    /**
     * Test add_measurement fails when content is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddMeasurementThrowsWhenLongitudeNull() throws Exception {
        // Given
        Measurement measurement = getTestMeasurement();
        measurement.setContent(null);

        // When
        measurementRepository.addMeasurement(measurement);
    }

    /**
     * Test add_measurement auto-generates time when time is null
     */
    @Transactional
    @Test
    public void testAddMeasurementGeneratesTimeWhenTimeNull() throws Exception {
        // Given
        Measurement measurement = getTestMeasurement();
        measurement.setTime(null);

        // When
        Measurement result = measurementRepository.addMeasurement(measurement);

        // Then
        assertNotNull(result.getTime());
    }

    /**
     * Test get_measurements without parameters finds inserted measurement
     */
    @Transactional
    @Test
    public void testGetMeasurementsWithoutParametersReturnsInsertedMeasurement() throws Exception {
        // Given
        Measurement expected = measurementRepository.addMeasurement(getTestMeasurement());

        // When
        Collection<Measurement> results = measurementRepository.getMeasurements(null, null, null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(expected));
    }

    /**
     * Test get_measurements with device_id and exact_time finds inserted measurement
     */
    @Transactional
    @Test
    public void testGetMeasurementsWithExactTimeReturnsInsertedMeasurement() throws Exception {
        // Given
        Measurement expected = measurementRepository.addMeasurement(getTestMeasurement());

        // When
        Collection<Measurement> results = measurementRepository.getMeasurements(expected.getDeviceId(), expected.getTime(), null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(expected));
    }


    /**
     * Test get_measurements with start_time finds inserted measurement
     */
    @Transactional
    @Test
    public void testGetMeasurementsWithStartTimeReturnsInsertedMeasurement() throws Exception {
        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Measurement expected = getTestMeasurement();
        expected.setTime(exactTime);

        // When
        measurementRepository.addMeasurement(expected);
        Collection<Measurement> results = measurementRepository.getMeasurements(null, null, startTime, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(expected));
    }

    /**
     * Test get_measurements with end_time finds inserted measurement
     */
    @Transactional
    @Test
    public void testGetMeasurementsWithEndTimeReturnsInsertedMeasurement() throws Exception {
        // Given
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Date endTime = formatTime("01-01-2000 00:00:02");
        Measurement expected = getTestMeasurement();
        expected.setTime(exactTime);

        // When
        measurementRepository.addMeasurement(expected);
        Collection<Measurement> results = measurementRepository.getMeasurements(null, null, null, endTime);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(expected));
    }

    /**
     * Test get_measurements with start_time and end_time finds inserted measurement
     */
    @Transactional
    @Test
    public void testGetMeasurementsWithStartAndEndTimeReturnsInsertedMeasurement() throws Exception {
        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Date endTime = formatTime("01-01-2000 00:00:02");
        Measurement expected = getTestMeasurement();
        expected.setTime(exactTime);

        // When
        measurementRepository.addMeasurement(expected);
        Collection<Measurement> results = measurementRepository.getMeasurements(null, null, startTime, endTime);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(expected));
    }


    /**
     * Test get_measurements with start_time and end_time wont find measurement outside search time window
     */
    @Transactional
    @Test
    public void testGetMeasurementsWithStartAndEndTimeWontFindMeasurementOutsideTimeWindow() throws Exception {
        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date endTime = formatTime("01-01-2000 00:00:01");
        Date exactTime = formatTime("01-01-2000 00:00:02");
        Measurement expected = getTestMeasurement();
        expected.setTime(exactTime);

        // When
        measurementRepository.addMeasurement(expected);
        Collection<Measurement> results = measurementRepository.getMeasurements(null, null, startTime, endTime);

        // Then
        assertThat(results.size(), equalTo(0));
    }

    /**
     * Test delete_measurements with device_id and exact_time deletes all measurements
     */
    @Transactional
    @Test
    public void testDeleteMeasurementsWithDeviceIdAndExactTimeDeletesAll() throws Exception {
        // Given
        Measurement measurement = getTestMeasurement();

        // When
        measurementRepository.addMeasurement(measurement);
        measurementRepository.addMeasurement(measurement);
        Collection<Measurement> resultsBefore = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);
        Boolean result = measurementRepository.deleteMeasurements(measurement.getDeviceId(), measurement.getTime(), null, null);
        Collection<Measurement> resultsAfter = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(2));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_measurements with start_time deletes all measurements
     */
    @Transactional
    @Test
    public void testDeleteMeasurementsWithStartTimeDeletesAll() throws Exception {
        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Measurement measurement = getTestMeasurement();
        measurement.setTime(exactTime);

        // When
        measurementRepository.addMeasurement(measurement);
        measurementRepository.addMeasurement(measurement);
        Collection<Measurement> resultsBefore = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);
        Boolean result = measurementRepository.deleteMeasurements(null, null, startTime, null);
        Collection<Measurement> resultsAfter = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(2));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_measurements with end_time deletes all measurements
     */
    @Transactional
    @Test
    public void testDeleteMeasurementsWithEndTimeDeletesAll() throws Exception {
        // Given
        Date exactTime = formatTime("01-01-2000 00:00:00");
        Date endTime = formatTime("01-01-2000 00:00:01");
        Measurement measurement = getTestMeasurement();
        measurement.setTime(exactTime);

        // When
        measurementRepository.addMeasurement(measurement);
        measurementRepository.addMeasurement(measurement);
        Collection<Measurement> resultsBefore = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);
        Boolean result = measurementRepository.deleteMeasurements(null, null, null, endTime);
        Collection<Measurement> resultsAfter = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(2));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_measurements with start_time and end_time deletes all measurements
     */
    @Transactional
    @Test
    public void testDeleteMeasurementsWithStartAndEndTimeDeletesAll() throws Exception {
        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Date endTime = formatTime("01-01-2000 00:00:02");
        Measurement measurement = getTestMeasurement();
        measurement.setTime(exactTime);

        // When
        measurementRepository.addMeasurement(measurement);
        measurementRepository.addMeasurement(measurement);
        Collection<Measurement> resultsBefore = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);
        Boolean result = measurementRepository.deleteMeasurements(null, null, startTime, endTime);
        Collection<Measurement> resultsAfter = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(2));
        assertThat(resultsAfter.size(), equalTo(0));
    }


    /**
     * Test delete_measurements with start_time and end_time wont delete measurement outside search time window
     */
    @Transactional
    @Test
    public void testDeleteMeasurementsWithStartAndEndTimeWontDeleteMeasurementOutsideTimeWindow() throws Exception {
        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date endTime = formatTime("01-01-2000 00:00:01");
        Date exactTime = formatTime("01-01-2000 00:00:02");
        Measurement measurement = getTestMeasurement();
        measurement.setTime(exactTime);

        // When
        measurementRepository.addMeasurement(measurement);
        measurementRepository.addMeasurement(measurement);
        Collection<Measurement> resultsBefore = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);
        Boolean result = measurementRepository.deleteMeasurements(null, null, startTime, endTime);
        Collection<Measurement> resultsAfter = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    /**
     * Test delete_measurements without parameters wont delete all
     */
    @Transactional
    @Test
    public void testDeleteMeasurementsWithoutParametersWontDeleteAll() throws Exception {
        // Given
        Measurement measurement = getTestMeasurement();

        // When
        measurementRepository.addMeasurement(measurement);
        measurementRepository.addMeasurement(measurement);
        Collection<Measurement> resultsBefore = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);
        Boolean result = measurementRepository.deleteMeasurements(null, null, null, null);
        Collection<Measurement> resultsAfter = measurementRepository.getMeasurements(measurement.getDeviceId(), null, null, null);

        // Then
        assertFalse(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    private Measurement getTestMeasurement() {
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

    private Date formatTime(String time) throws Exception {
        return new Timestamp(
            new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(time).getTime());
    }
}
