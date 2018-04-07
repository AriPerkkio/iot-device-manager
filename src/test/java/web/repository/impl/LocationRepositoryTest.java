package web.repository.impl;

import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import web.domain.entity.Device;
import web.domain.entity.Location;
import web.repository.DeviceRepository;
import web.repository.LocationRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationRepositoryTest {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    DeviceRepository deviceRepository;

    private Integer deviceId;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Add new device before each test. Location has dependency on device_icon.id
     * Manual teardown not required - @Transactional tests already take care of rollback
     */
    @Before
    public void setup() {
        Device device = new Device();
        device.setName("test-device-name");
        this.deviceId = deviceRepository.addDevice(device).getId();
    }

    /**
     * Test add_location returns added location
     */
    @Transactional
    @Test
    public void testAddLocationReturnsAddedLocation() {
        log.info("Test add_location returns added location");

        // Given
        Location expected = getTestLocation();

        // When
        Location result = locationRepository.addLocation(expected);

        // Then
        assertThat(result.getDeviceId(), equalTo(expected.getDeviceId()));
    }

    /**
     * Test add_location fails when foreign key device_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddLocationThrowsWhenForeignKeyConflicts() {
        log.info("Test add_location fails when foreign key device_id conflicts");

        // Given
        Location location = getTestLocation();
        location.setDeviceId(999);

        // When
        locationRepository.addLocation(location);
    }

    /**
     * Test add_location fails when device_id is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddLocationThrowsWhenDeviceIdNull() {
        log.info("Test add_location fails when device_id is null");

        // Given
        Location location = getTestLocation();
        location.setDeviceId(null);

        // When
        locationRepository.addLocation(location);
    }

    /**
     * Test add_location fails when latitude is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddLocationThrowsWhenLatitudeNull() {
        log.info("Test add_location fails when latitude is null");

        // Given
        Location location = getTestLocation();
        location.setLatitude(null);

        // When
        locationRepository.addLocation(location);
    }

    /**
     * Test add_location fails when longitude is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddLocationThrowsWhenLongitudeNull() {
        log.info("Test add_location fails when longitude is null");

        // Given
        Location location = getTestLocation();
        location.setLongitude(null);

        // When
        locationRepository.addLocation(location);
    }

    /**
     * Test add_location auto-generates time when time is null
     */
    @Transactional
    @Test
    public void testAddLocationGeneratesTimeWhenTimeNull() {
        log.info("Test add_location auto-generates time when time is null");

        // Given
        Location location = getTestLocation();
        location.setTime(null);

        // When
        Location result = locationRepository.addLocation(location);

        // Then
        assertNotNull(result.getTime());
    }

    /**
     * Test get_locations without parameters finds inserted location
     */
    @Transactional
    @Test
    public void testGetLocationsWithoutParametersReturnsInsertedLocation() {
        log.info("Test get_locations without parameters finds inserted location");

        // Given
        Location expected = locationRepository.addLocation(getTestLocation());

        // When
        Collection<Location> results = locationRepository.getLocations(null, null, null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(expected));
    }

    /**
     * Test get_locations with device_id and exact_time finds inserted location
     */
    @Transactional
    @Test
    public void testGetLocationsWithExactTimeReturnsInsertedLocation() {
        log.info("Test get_locations with device_id and exact_time finds inserted location");

        // Given
        Location expected = locationRepository.addLocation(getTestLocation());

        // When
        Collection<Location> results = locationRepository.getLocations(expected.getDeviceId(), expected.getTime(), null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(expected));
    }


    /**
     * Test get_locations with start_time finds inserted location
     */
    @Transactional
    @Test
    public void testGetLocationsWithStartTimeReturnsInsertedLocation() throws Exception {
        log.info("Test get_locations with start_time finds inserted location");

        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Location expected = getTestLocation();
        expected.setTime(exactTime);

        // When
        locationRepository.addLocation(expected);
        Collection<Location> results = locationRepository.getLocations(null, null, startTime, null);

        // Then
        assertThat(results.size(), is(1));
        Location location = results.stream().findFirst().get();
        assertThat(expected.getDeviceId(), is(location.getDeviceId()));
        assertThat(expected.getTime(), is(location.getTime()));    }

    /**
     * Test get_locations with end_time finds inserted location
     */
    @Transactional
    @Test
    public void testGetLocationsWithEndTimeReturnsInsertedLocation() throws Exception {
        log.info("Test get_locations with end_time finds inserted location");

        // Given
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Date endTime = formatTime("01-01-2000 00:00:02");
        Location expected = getTestLocation();
        expected.setTime(exactTime);

        // When
        locationRepository.addLocation(expected);
        Collection<Location> results = locationRepository.getLocations(null, null, null, endTime);

        // Then
        assertThat(results.size(), is(1));
        Location location = results.stream().findFirst().get();
        assertThat(expected.getDeviceId(), is(location.getDeviceId()));
        assertThat(expected.getTime(), is(location.getTime()));    }

    /**
     * Test get_locations with start_time and end_time finds inserted location
     */
    @Transactional
    @Test
    public void testGetLocationsWithStartAndEndTimeReturnsInsertedLocation() throws Exception {
        log.info("Test get_locations with start_time and end_time finds inserted location");

        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Date endTime = formatTime("01-01-2000 00:00:02");
        Location expected = getTestLocation();
        expected.setTime(exactTime);

        // When
        locationRepository.addLocation(expected);
        Collection<Location> results = locationRepository.getLocations(null, null, startTime, endTime);

        // Then
        assertThat(results.size(), is(1));
        Location location = results.stream().findFirst().get();
        assertThat(expected.getDeviceId(), is(location.getDeviceId()));
        assertThat(expected.getTime(), is(location.getTime()));
    }


    /**
     * Test get_locations with start_time and end_time wont find location outside search time window
     */
    @Transactional
    @Test
    public void testGetLocationsWithStartAndEndTimeWontFindLocationOutsideTimeWindow() throws Exception {
        log.info("Test get_locations with start_time and end_time wont find location outside search time window");

        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date endTime = formatTime("01-01-2000 00:00:01");
        Date exactTime = formatTime("01-01-2000 00:00:02");
        Location expected = getTestLocation();
        expected.setTime(exactTime);

        // When
        locationRepository.addLocation(expected);
        Collection<Location> results = locationRepository.getLocations(null, null, startTime, endTime);

        // Then
        assertThat(results.size(), equalTo(0));
    }

    /**
     * Test delete_locations with device_id and exact_time deletes all locations
     */
    @Transactional
    @Test
    public void testDeleteLocationsWithDeviceIdAndExactTimeDeletesAll() {
        log.info("Test delete_locations with device_id and exact_time deletes all locations");

        // Given
        Location location = getTestLocation();

        // When
        locationRepository.addLocation(location);
        locationRepository.addLocation(location);
        Collection<Location> resultsBefore = locationRepository.getLocations(location.getDeviceId(), null, null, null);
        Boolean result = locationRepository.deleteLocations(location.getDeviceId(), location.getTime(), null, null);
        Collection<Location> resultsAfter = locationRepository.getLocations(location.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(2));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_locations with start_time deletes all locations
     */
    @Transactional
    @Test
    public void testDeleteLocationsWithStartTimeDeletesAll() throws Exception {
        log.info("Test delete_locations with start_time deletes all locations");

        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Location location = getTestLocation();
        location.setTime(exactTime);

        // When
        locationRepository.addLocation(location);
        locationRepository.addLocation(location);
        Collection<Location> resultsBefore = locationRepository.getLocations(location.getDeviceId(), null, null, null);
        Boolean result = locationRepository.deleteLocations(null, null, startTime, null);
        Collection<Location> resultsAfter = locationRepository.getLocations(location.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(2));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_locations with end_time deletes all locations
     */
    @Transactional
    @Test
    public void testDeleteLocationsWithEndTimeDeletesAll() throws Exception {
        log.info("Test delete_locations with end_time deletes all locations");

        // Given
        Date exactTime = formatTime("01-01-2000 00:00:00");
        Date endTime = formatTime("01-01-2000 00:00:01");
        Location location = getTestLocation();
        location.setTime(exactTime);

        // When
        locationRepository.addLocation(location);
        locationRepository.addLocation(location);
        Collection<Location> resultsBefore = locationRepository.getLocations(location.getDeviceId(), null, null, null);
        Boolean result = locationRepository.deleteLocations(null, null, null, endTime);
        Collection<Location> resultsAfter = locationRepository.getLocations(location.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(2));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_locations with start_time and end_time deletes all locations
     */
    @Transactional
    @Test
    public void testDeleteLocationsWithStartAndEndTimeDeletesAll() throws Exception {
        log.info("Test delete_locations with start_time and end_time deletes all locations");

        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date exactTime = formatTime("01-01-2000 00:00:01");
        Date endTime = formatTime("01-01-2000 00:00:02");
        Location location = getTestLocation();
        location.setTime(exactTime);

        // When
        locationRepository.addLocation(location);
        locationRepository.addLocation(location);
        Collection<Location> resultsBefore = locationRepository.getLocations(location.getDeviceId(), null, null, null);
        Boolean result = locationRepository.deleteLocations(null, null, startTime, endTime);
        Collection<Location> resultsAfter = locationRepository.getLocations(location.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(2));
        assertThat(resultsAfter.size(), equalTo(0));
    }


    /**
     * Test delete_locations with start_time and end_time wont delete location outside search time window
     */
    @Transactional
    @Test
    public void testDeleteLocationsWithStartAndEndTimeWontDeleteLocationOutsideTimeWindow() throws Exception {
        log.info("Test delete_locations with start_time and end_time wont delete location outside search time window");

        // Given
        Date startTime = formatTime("01-01-2000 00:00:00");
        Date endTime = formatTime("01-01-2000 00:00:01");
        Date exactTime = formatTime("01-01-2000 00:00:02");
        Location location = getTestLocation();
        location.setTime(exactTime);

        // When
        locationRepository.addLocation(location);
        locationRepository.addLocation(location);
        Collection<Location> resultsBefore = locationRepository.getLocations(location.getDeviceId(), null, null, null);
        Boolean result = locationRepository.deleteLocations(null, null, startTime, endTime);
        Collection<Location> resultsAfter = locationRepository.getLocations(location.getDeviceId(), null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    /**
     * Test delete_locations without parameters wont delete all
     */
    @Transactional
    @Test
    public void testDeleteLocationsWithoutParametersWontDeleteAll() throws Exception {
        log.info("Test delete_locations without parameters wont delete all");

        // Given
        Location location = getTestLocation();

        // When
        locationRepository.addLocation(location);
        locationRepository.addLocation(location);
        Collection<Location> resultsBefore = locationRepository.getLocations(location.getDeviceId(), null, null, null);
        Boolean result = locationRepository.deleteLocations(null, null, null, null);
        Collection<Location> resultsAfter = locationRepository.getLocations(location.getDeviceId(), null, null, null);

        // Then
        assertFalse(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    private Location getTestLocation() {
        Location location = new Location();
        location.setDeviceId(deviceId);
        location.setLatitude(BigDecimal.ONE);
        location.setLongitude(BigDecimal.TEN);
        location.setTime(new Date());

        return location;
    }

    private Date formatTime(String time) throws Exception {
        return new Timestamp(
            new SimpleDateFormat("dd-MM-yyyy hh:mm:ss").parse(time).getTime());
    }
}
