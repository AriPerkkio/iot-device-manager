package web.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    /**
     * Test add_device returns added device. Validate authentication key is generated
     */
    @Transactional
    @Test
    public void testAddDeviceReturnsAddedDeviceWithAuthenticationKey() {
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
        // Given
        Device deviceWithLongName = getTestDevice();
        deviceWithLongName.setName(null);

        // When
        deviceRepository.addDevice(deviceWithLongName);
    }

    /**
     * Test add_device fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceThrowsWhenNameTooLong() {
        // Given
        Device deviceWithLongName = getTestDevice();
        deviceWithLongName.setName(StringUtils.repeat("A", 51));

        // When
        deviceRepository.addDevice(deviceWithLongName);
    }

    /**
     * Test add_device fails when foreign key conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceThrowsWhenForeignKeyConflicts() {
        // Given
        Device deviceWithConflict = getTestDevice();
        deviceWithConflict.setConfigurationId(999);

        // When
        deviceRepository.addDevice(deviceWithConflict);
    }

    /**
     * Test get_devices without parameters finds inserted device
     */
    @Transactional
    @Test
    public void testGetDevicesWithoutParametersReturnsInsertedDevice() {
        // Given
        Device expected = getTestDevice();

        // When
        deviceRepository.addDevice(expected);
        Collection<Device> resultDevices = deviceRepository.getDevices(null, null, null, null, null, null);

        // Then
        assertThat(resultDevices, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
    }

    /**
     * Test get_devices with parameters finds inserted device
     */
    @Transactional
    @Test
    public void testGetDevicesWithParametersReturnsInsertedDevice() {
        // Given
        Device expected = deviceRepository.addDevice(getTestDevice());

        // When
        Collection<Device> resultDevices = deviceRepository.getDevices(expected.getId(), expected.getName(), expected.getDeviceTypeId(),
            expected.getDeviceGroupId(), expected.getConfigurationId(), expected.getAuthenticationKey());

        // Then
        assertThat(resultDevices, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
    }

    /**
     * Test update device with parameters works
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceWithParametersWorks() {
        // Given
        Device initialDevice = getTestDevice();
        Device expected = getTestDevice();
        expected.setName("updated-name");

        // When
        deviceRepository.addDevice(initialDevice);
        deviceRepository.updateDevice(null, initialDevice.getName(), null, expected);
        Collection<Device> results = deviceRepository.getDevices(null, expected.getName(), null, null, null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));

        // Update methods do not work as transactional - manual cleanup required
        deviceRepository.deleteDevice(null, expected.getName(), null);
    }

    /**
     * Test update_device without parameters fails
     */
    @Transactional
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testUpdateDeviceWithoutParametersThrows() {
        // Given
        deviceRepository.updateDevice(null, null, null, getTestDevice());
    }

    /**
     * Test update device returns updated device
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceReturnsUpdatedDevice() {
        // Given
        Device initialDevice = getTestDevice();
        Device expected = getTestDevice();
        expected.setName("updated-name");

        // When
        deviceRepository.addDevice(initialDevice);
        Device result = deviceRepository.updateDevice(null, initialDevice.getName(), null, expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));

        // Update methods do not work as transactional - manual cleanup required
        deviceRepository.deleteDevice(null, result.getName(), null);
    }

    /**
     * Test update device fails when foreign key conflicts
     */
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testUpdateDeviceThrowsWhenForeignKeyConflicts() {
        // Given
        Device device = getTestDevice();
        Device deviceWithConflict = getTestDevice();
        deviceWithConflict.setConfigurationId(999);

        // When
        deviceRepository.addDevice(device);
        deviceRepository.updateDevice(null, device.getName(), null, deviceWithConflict);
    }

    /**
     * Test update device fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenNameNull() {
        // Given
        Device initialDevice = getTestDevice();
        Device updateDevice = getTestDevice();
        updateDevice.setName(null);

        // When
        deviceRepository.addDevice(initialDevice);
        deviceRepository.addDevice(updateDevice);
        deviceRepository.updateDevice(null, updateDevice.getName(), null, initialDevice);
    }

    /**
     * Test update device fails when unique key conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenNameNotUnique() {
        // Given
        Device initialDevice = getTestDevice();
        Device updateDevice = getTestDevice();
        updateDevice.setName("non-conflicting-name");

        // When
        deviceRepository.addDevice(initialDevice);
        deviceRepository.addDevice(updateDevice);
        deviceRepository.updateDevice(null, updateDevice.getName(), null, initialDevice);
    }

    /**
     * Test update device fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenNameTooLong() {
        // Given
        Device initialDevice = getTestDevice();
        Device deviceWithLongName = getTestDevice();
        deviceWithLongName.setName(StringUtils.repeat("A", 51));

        // When
        deviceRepository.addDevice(initialDevice);
        deviceRepository.updateDevice(null, initialDevice.getName(), null, deviceWithLongName);
    }

    /**
     * Test delete_device with parameters deletes devices
     */
    @Transactional
    @Test
    public void testDeleteDeviceWithParametersWorks() {
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
        // Given
        Device device = getTestDevice();

        // When
        deviceRepository.addDevice(device);
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