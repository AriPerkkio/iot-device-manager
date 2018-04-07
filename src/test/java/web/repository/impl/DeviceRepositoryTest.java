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
import web.domain.entity.Device;
import web.repository.DeviceRepository;

import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceRepositoryTest {

    @Autowired
    DeviceRepository deviceRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Test add_device returns added device. Validate authentication key is generated
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsAddedDeviceWithAuthenticationKey() {
        log.info("Test add_device returns added device. Validate authentication key is generated");

        // Given
        Device expected = getTestDevice();

        // When
        Device result = deviceRepository.addDevice(expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));
        assertNotNull(result.getAuthenticationKey());
        assertThat(result.getAuthenticationKey().length(), equalTo(32));
    }

    /**
     * Test add_device fails when device with duplicate name is used
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceThrowsWhenNameNotUnique() {
        log.info("Test add_device fails when device with duplicate name is used");

        // Given
        Device device = getTestDevice();

        // When
        deviceRepository.addDevice(device);
        deviceRepository.addDevice(device);
    }

    /**
     * Test add_device fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceThrowsWhenNameNull() {
        log.info("Test add_device fails when name is null");

        // Given
        Device deviceWithNullName = getTestDevice();
        deviceWithNullName.setName(null);

        // When
        deviceRepository.addDevice(deviceWithNullName);
    }

    /**
     * Test add_device fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceThrowsWhenNameTooLong() {
        log.info("Test add_device fails when name is too long");

        // Given
        Device deviceWithLongName = getTestDevice();
        deviceWithLongName.setName(StringUtils.repeat("A", 51));

        // When
        deviceRepository.addDevice(deviceWithLongName);
    }

    /**
     * Test add_device fails when foreign key configuration_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceThrowsWhenConfigurationIdConflicts() {
        log.info("Test add_device fails when foreign key configuration_id conflicts");

        // Given
        Device deviceWithConflict = getTestDevice();
        deviceWithConflict.setConfigurationId(999);

        // When
        deviceRepository.addDevice(deviceWithConflict);
    }

    /**
     * Test add_device fails when foreign key device_group_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceThrowsWhenDeviceGroupIdConflicts() {
        log.info("Test add_device fails when foreign key device_group_id conflicts");

        // Given
        Device deviceWithConflict = getTestDevice();
        deviceWithConflict.setDeviceGroupId(999);

        // When
        deviceRepository.addDevice(deviceWithConflict);
    }

    /**
     * Test add_device fails when foreign key device_type_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceThrowsWhenDeviceTypeIdConflicts() {
        log.info("Test add_device fails when foreign key device_type_id conflicts");

        // Given
        Device deviceWithConflict = getTestDevice();
        deviceWithConflict.setDeviceTypeId(999);

        // When
        deviceRepository.addDevice(deviceWithConflict);
    }

    /**
     * Test get_devices without parameters finds inserted device
     */
    @Transactional
    @Test
    public void testGetDevicesWithoutParametersReturnsInsertedDevice() {
        log.info("Test get_devices without parameters finds inserted device");

        // Given
        Device expected = deviceRepository.addDevice(getTestDevice());

        // When
        Collection<Device> resultDevices = deviceRepository.getDevices(null, null, null, null, null, null);

        // Then
        assertThat(resultDevices, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(resultDevices, IsCollectionContaining.hasItem(hasProperty("authenticationKey", equalTo(expected.getAuthenticationKey()))));
    }

    /**
     * Test get_devices with parameters finds inserted device
     */
    @Transactional
    @Test
    public void testGetDevicesWithParametersReturnsInsertedDevice() {
        log.info("Test get_devices with parameters finds inserted device");

        // Given
        Device expected = deviceRepository.addDevice(getTestDevice());

        // When
        Collection<Device> resultDevices = deviceRepository.getDevices(expected.getId(), expected.getName(), expected.getDeviceTypeId(),
            expected.getDeviceGroupId(), expected.getConfigurationId(), expected.getAuthenticationKey());

        // Then
        assertThat(resultDevices, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(resultDevices, IsCollectionContaining.hasItem(hasProperty("authenticationKey", equalTo(expected.getAuthenticationKey()))));
    }

    /**
     * Test update device with parameters works
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceWithParametersWorks() {
        log.info("Test update device with parameters works");

        // Given
        Device initialDevice = deviceRepository.addDevice(getTestDevice());
        Device expected = getTestDevice();
        expected.setName("updated-name");

        // When
        deviceRepository.updateDevice(null, initialDevice.getName(), null, expected);
        Collection<Device> results = deviceRepository.getDevices(null, expected.getName(), null, null, null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("authenticationKey", equalTo(initialDevice.getAuthenticationKey()))));

        // Update methods do not work as transactional - manual cleanup required
        deviceRepository.deleteDevice(null, expected.getName(), null);
    }

    /**
     * Test update_device without parameters fails
     */
    @Transactional
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testUpdateDeviceWithoutParametersThrows() {
        log.info("Test update_device without parameters fails");

        // When
        deviceRepository.updateDevice(null, null, null, getTestDevice());
    }

    /**
     * Test update device returns updated device
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceReturnsUpdatedDevice() {
        log.info("Test update device returns updated device");

        // Given
        Device initialDevice = deviceRepository.addDevice(getTestDevice());
        Device expected = getTestDevice();
        expected.setName("updated-name");

        // When
        Device result = deviceRepository.updateDevice(null, initialDevice.getName(), null, expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));
        assertThat(result.getAuthenticationKey(), equalTo(initialDevice.getAuthenticationKey()));

        // Update methods do not work as transactional - manual cleanup required
        deviceRepository.deleteDevice(null, result.getName(), null);
    }

    /**
     * Test update device fails when foreign key configuration_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenConfigurationIdConflicts() {
        log.info("Test update device fails when foreign key configuration_id conflicts");

        // Given
        Device initialDevice = deviceRepository.addDevice(getTestDevice());
        Device deviceWithConflict = getTestDevice();
        deviceWithConflict.setConfigurationId(999);

        // When
        deviceRepository.updateDevice(null, initialDevice.getName(), null, deviceWithConflict);
    }

    /**
     * Test update device fails when foreign key device_group_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenDeviceGroupIdConflicts() {
        log.info("Test update device fails when foreign key device_group_id conflicts");

        // Given
        Device initialDevice = deviceRepository.addDevice(getTestDevice());
        Device deviceWithConflict = getTestDevice();
        deviceWithConflict.setDeviceGroupId(999);

        // When
        deviceRepository.updateDevice(null, initialDevice.getName(), null, deviceWithConflict);
    }

    /**
     * Test update device fails when foreign key device_type_id conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenDeviceTypeIdConflicts() {
        log.info("Test update device fails when foreign key device_type_id conflicts");

        // Given
        Device initialDevice = deviceRepository.addDevice(getTestDevice());
        Device deviceWithConflict = getTestDevice();
        deviceWithConflict.setDeviceTypeId(999);

        // When
        deviceRepository.updateDevice(null, initialDevice.getName(), null, deviceWithConflict);
    }

    /**
     * Test update device fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenNameNull() {
        log.info("Test update device fails when name is null");

        // Given
        Device initialDevice = deviceRepository.addDevice(getTestDevice());
        Device updateDevice = getTestDevice();
        updateDevice.setName(null);

        // When
        deviceRepository.updateDevice(null, initialDevice.getName(), null, updateDevice);
    }

    /**
     * Test update device fails when unique key conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenNameNotUnique() {
        log.info("Test update device fails when unique key conflicts");

        // Given
        Device initialDevice = deviceRepository.addDevice(getTestDevice());
        Device updateDevice = getTestDevice();
        updateDevice.setName("non-conflicting-name");

        // When
        deviceRepository.addDevice(updateDevice);
        deviceRepository.updateDevice(null, updateDevice.getName(), null, initialDevice);
    }

    /**
     * Test update device fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenNameTooLong() {
        log.info("Test update device fails when name is too long");

        // Given
        Device initialDevice = deviceRepository.addDevice(getTestDevice());
        Device deviceWithLongName = getTestDevice();
        deviceWithLongName.setName(StringUtils.repeat("A", 51));

        // When
        deviceRepository.updateDevice(null, initialDevice.getName(), null, deviceWithLongName);
    }

    /**
     * Test delete_device with parameters deletes devices
     */
    @Transactional
    @Test
    public void testDeleteDeviceWithParametersWorks() {
        log.info("Test delete_device with parameters deletes devices");

        // Given
        Device device = getTestDevice();

        // When
        deviceRepository.addDevice(device);
        Collection<Device> resultsBefore = deviceRepository.getDevices(null, null, null, null, null, null);
        Boolean result = deviceRepository.deleteDevice(null, device.getName(), null);
        Collection<Device> resultsAfter = deviceRepository.getDevices(null, null, null, null, null, null);

        //Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(1));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_device without parameters doesn't delete anything
     */
    @Transactional
    @Test
    public void testDeleteDeviceWithoutParametersWontDeleteAll() {
        log.info("Test delete_device without parameters doesn't delete anything");

        // When
        deviceRepository.addDevice(getTestDevice());
        Collection<Device> resultsBefore = deviceRepository.getDevices(null, null, null, null, null, null);
        Boolean result = deviceRepository.deleteDevice(null, null, null);
        Collection<Device> resultsAfter = deviceRepository.getDevices(null, null, null, null, null, null);

        //Then
        assertFalse(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    private Device getTestDevice() {
        Device device = new Device();
        device.setName("test-device");
        return device;
    }
}