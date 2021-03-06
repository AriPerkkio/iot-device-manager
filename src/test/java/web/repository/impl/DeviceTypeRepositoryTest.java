package web.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import web.domain.entity.DeviceType;
import web.repository.DeviceTypeRepository;

import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceTypeRepositoryTest {

    @Autowired
    DeviceTypeRepository deviceTypeRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Test add_device_type returns added device type
     */
    @Transactional
    @Test
    public void testAddDeviceTypeReturnsAddedDeviceType() {
        log.info("Test add_device_type returns added device type");

        // Given
        DeviceType expected = getTestDeviceType();

        // When
        DeviceType result = deviceTypeRepository.addDeviceType(expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));
    }

    /**
     * Test add_device_type fails when device type with duplicate name is used
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceTypeThrowsWhenNameNotUnique() {
        log.info("Test add_device_type fails when device type with duplicate name is used");

        // Given
        DeviceType deviceType = getTestDeviceType();

        // When
        deviceTypeRepository.addDeviceType(deviceType);
        deviceTypeRepository.addDeviceType(deviceType);
    }

    /**
     * Test add_device_type fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceTypeThrowsWhenNameNull() {
        log.info("Test add_device_type fails when name is null");

        // Given
        DeviceType deviceTypeWithNameNull = getTestDeviceType();
        deviceTypeWithNameNull.setName(null);

        // When
        deviceTypeRepository.addDeviceType(deviceTypeWithNameNull);
    }

    /**
     * Test add_device_type fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceTypeThrowsWhenNameTooLong() {
        log.info("Test add_device_type fails when name is too long");

        // Given
        DeviceType deviceTypeWithTooLongName = getTestDeviceType();
        deviceTypeWithTooLongName.setName(StringUtils.repeat("A", 51));

        // When
        deviceTypeRepository.addDeviceType(deviceTypeWithTooLongName);
    }

    /**
     * Test add_device_type fails when foreign key device_icon_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceTypeThrowsWhenForeignKeyConflicts() {
        log.info("Test add_device_type fails when foreign key device_icon_id conflicts");

        // Given
        DeviceType deviceTypeWithInvalidDeviceIconId = getTestDeviceType();
        deviceTypeWithInvalidDeviceIconId.setDeviceIconId(999);

        // When
        deviceTypeRepository.addDeviceType(deviceTypeWithInvalidDeviceIconId);
    }

    /**
     * Test get_device_types without parameters finds inserted device type
     */
    @Transactional
    @Test
    public void testGetDeviceTypesWithoutParametersReturnsInsertedDeviceType() {
        log.info("Test get_device_types without parameters finds inserted device type");

        // Given
        DeviceType expected = deviceTypeRepository.addDeviceType(getTestDeviceType());

        // When
        Collection<DeviceType> results = deviceTypeRepository.getDeviceTypes(null, null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
    }

    /**
     * Test get_device_types with parameters finds inserted device type
     */
    @Transactional
    @Test
    public void testGetDeviceTypesWithParametersReturnsInsertedDeviceType() {
        log.info("Test get_device_types with parameters finds inserted device type");

        // Given
        DeviceType expected = deviceTypeRepository.addDeviceType(getTestDeviceType());

        // When
        Collection<DeviceType> results = deviceTypeRepository.getDeviceTypes(expected.getId(), expected.getName(), expected.getDeviceIconId());

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
    }

    /**
     * Test update_device_type with parameters works
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceTypeWithParametersWorks() {
        log.info("Test update_device_type with parameters works");

        // Given
        DeviceType initialDeviceType = deviceTypeRepository.addDeviceType(getTestDeviceType());
        DeviceType expected = getTestDeviceType();
        expected.setName("updated-name");

        // When
        deviceTypeRepository.updateDeviceType(null, initialDeviceType.getName(), expected);
        Collection<DeviceType> results = deviceTypeRepository.getDeviceTypes(null, expected.getName(), null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));

        // Update methods do not work as transactional - manual cleanup required
        deviceTypeRepository.deleteDeviceType(null, expected.getName());
    }

    /**
     * Test update_device_type without parameters fails
     */
    @Transactional
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testUpdateDeviceTypeWithoutParametersThrows() {
        log.info("Test update_device_type without parameters fails");

        // When
        deviceTypeRepository.updateDeviceType(null, null, getTestDeviceType());
    }

    /**
     * Test update_device_type returns updated device type
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceTypeReturnsUpdatedDeviceType() {
        log.info("Test update_device_type returns updated device type");

        // Given
        DeviceType initialDeviceType = deviceTypeRepository.addDeviceType(getTestDeviceType());
        DeviceType expected = getTestDeviceType();
        expected.setName("updated-name");

        // When
        DeviceType result = deviceTypeRepository.updateDeviceType(null, initialDeviceType.getName(), expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));

        // Update methods do not work as transactional - manual cleanup required
        deviceTypeRepository.deleteDeviceType(null, result.getName());
    }

    /**
     * Test update_device_type fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenNameNull() {
        log.info("Test update_device_type fails when name is null");

        // Given
        DeviceType initialDevice = deviceTypeRepository.addDeviceType(getTestDeviceType());
        DeviceType deviceTypeWithNameNull = getTestDeviceType();
        deviceTypeWithNameNull.setName(null);

        // When
        deviceTypeRepository.updateDeviceType(null, initialDevice.getName(), deviceTypeWithNameNull);
    }

    /**
     * Test update_device_type fails when unique key conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceTypeThrowsWhenNameNotUnique() {
        log.info("Test update_device_type fails when unique key conflicts");

        // Given
        DeviceType initialDeviceType = deviceTypeRepository.addDeviceType(getTestDeviceType());
        DeviceType updateDeviceType = getTestDeviceType();
        updateDeviceType.setName("non-conflicting-name");

        // When
        deviceTypeRepository.addDeviceType(updateDeviceType);
        deviceTypeRepository.updateDeviceType(null, updateDeviceType.getName(), initialDeviceType);
    }

    /**
     * Test update_device_type fails when foreign key device_type_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceTypeThrowsWhenDeviceTypeIdConflicts() {
        log.info("Test update_device_type fails when foreign key device_type_id conflicts");

        // Given
        DeviceType initialDeviceType = deviceTypeRepository.addDeviceType(getTestDeviceType());
        DeviceType updateDeviceType = getTestDeviceType();
        updateDeviceType.setDeviceIconId(999);

        // When
        deviceTypeRepository.updateDeviceType(null, initialDeviceType.getName(), updateDeviceType);
    }

    /**
     * Test update_device_type fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceTypeThrowsWhenNameTooLong() {
        log.info("Test update_device_type fails when name is too long");

        // Given
        DeviceType initialDeviceType = deviceTypeRepository.addDeviceType(getTestDeviceType());
        DeviceType updateDeviceType = getTestDeviceType();
        updateDeviceType.setName(StringUtils.repeat("A", 51));

        // When
        deviceTypeRepository.updateDeviceType(null, initialDeviceType.getName(), updateDeviceType);
    }

    /**
     * Test delete_device_type with parameters deletes device type
     */
    @Transactional
    @Test
    public void testDeleteDeviceTypeWithParametersWorks() {
        log.info("Test delete_device_type with parameters deletes device type");

        // Given
        DeviceType deviceType = getTestDeviceType();

        // When
        deviceTypeRepository.addDeviceType(deviceType);
        Collection<DeviceType> resultsBefore = deviceTypeRepository.getDeviceTypes(null, null, null);
        Boolean result = deviceTypeRepository.deleteDeviceType(null, deviceType.getName());
        Collection<DeviceType> resultsAfter = deviceTypeRepository.getDeviceTypes(null, null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(1));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_device_type without parameters doesn't delete all device types
     */
    @Transactional
    @Test
    public void testDeleteDeviceTypeWithoutParametersWontDeleteAll() {
        log.info("Test delete_device_type without parameters doesn't delete all device types");

        // When
        deviceTypeRepository.addDeviceType(getTestDeviceType());
        Collection<DeviceType> resultsBefore = deviceTypeRepository.getDeviceTypes(null, null, null);
        Boolean result = deviceTypeRepository.deleteDeviceType(null, null);
        Collection<DeviceType> resultsAfter = deviceTypeRepository.getDeviceTypes(null, null, null);

        // Then
        assertFalse(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    private DeviceType getTestDeviceType() {
        DeviceType deviceType = new DeviceType();
        deviceType.setName("test-device-type-name");
        return deviceType;
    }
}
