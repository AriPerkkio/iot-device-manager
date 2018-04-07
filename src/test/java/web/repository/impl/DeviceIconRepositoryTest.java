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
import web.domain.entity.DeviceIcon;
import web.repository.DeviceIconRepository;

import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceIconRepositoryTest {

    @Autowired
    DeviceIconRepository deviceIconRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Test add_device_icon returns added deviceIcon
     */
    @Transactional
    @Test
    public void testAddDeviceIconReturnsAddedDeviceIcon() {
        log.info("Test add_device_icon returns added deviceIcon");

        // Given
        DeviceIcon expected = getTestDeviceIcon();

        // When
        DeviceIcon result = deviceIconRepository.addDeviceIcon(expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));
    }

    /**
     * Test add_device_icon fails when device icon with duplicate name is used
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceIconThrowsWhenNameNotUnique() {
        log.info("Test add_device_icon fails when device icon with duplicate name is used");

        // Given
        DeviceIcon deviceIcon = getTestDeviceIcon();

        // When
        deviceIconRepository.addDeviceIcon(deviceIcon);
        deviceIconRepository.addDeviceIcon(deviceIcon);
    }

    /**
     * Test add_device_icon fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceIconThrowsWhenNameTooLong() {
        log.info("Test add_device_icon fails when name is too long");

        // Given
        DeviceIcon deviceIconWithLongName = getTestDeviceIcon();
        deviceIconWithLongName.setName(StringUtils.repeat("A", 26));

        // When
        deviceIconRepository.addDeviceIcon(deviceIconWithLongName);
    }

    /**
     * Test add_device_icon fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceIconThrowsWhenNameNull() {
        log.info("Test add_device_icon fails when name is null");

        // Given
        DeviceIcon deviceIconWithLongName = getTestDeviceIcon();
        deviceIconWithLongName.setName(null);

        // When
        deviceIconRepository.addDeviceIcon(deviceIconWithLongName);
    }

    /**
     * Test get_device_icons without parameters finds inserted device icon
     */
    @Transactional
    @Test
    public void testGetDeviceIconsWithoutParametersReturnsInsertedDeviceIcon() {
        log.info("Test get_device_icons without parameters finds inserted device icon");

        // Given
        DeviceIcon expected = deviceIconRepository.addDeviceIcon(getTestDeviceIcon());

        // When
        Collection<DeviceIcon> results = deviceIconRepository.getDeviceIcons(null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
    }

    /**
     * Test get_device_icons with parameters finds inserted device icon
     */
    @Transactional
    @Test
    public void testGetDeviceIconsWithParametersReturnsInsertedDeviceIcon() {
        log.info("Test get_device_icons with parameters finds inserted device icon");

        // Given
        DeviceIcon expected = deviceIconRepository.addDeviceIcon(getTestDeviceIcon());

        // When
        Collection<DeviceIcon> results = deviceIconRepository.getDeviceIcons(expected.getId(), expected.getName());

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
    }

    /**
     * Test update_device_icon with parameters works
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceIconWithParametersWorks() {
        log.info("Test update_device_icon with parameters works");

        // Given
        DeviceIcon initialDeviceIcon = deviceIconRepository.addDeviceIcon(getTestDeviceIcon());
        DeviceIcon expected = getTestDeviceIcon();
        expected.setName("updated-name");

        // When
        deviceIconRepository.updateDeviceIcon(null, initialDeviceIcon.getName(), expected);
        Collection<DeviceIcon> results = deviceIconRepository.getDeviceIcons(null, expected.getName());

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));

        // Update methods do not work as transactional - manual cleanup required
        deviceIconRepository.deleteDeviceIcon(null, expected.getName());
    }

    /**
     * Test update_device_icon without parameters fails
     */
    @Transactional
    @Test (expected = InvalidDataAccessApiUsageException.class)
    public void testUpdateDeviceIconWithoutParametersThrows() {
        log.info("Test update_device_icon without parameters fails");

        // When
        deviceIconRepository.updateDeviceIcon(null, null, getTestDeviceIcon());
    }

    /**
     * Test update_device_icon returns updated device
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceIconReturnsUpdatedDeviceIcon() {
        log.info("Test update_device_icon returns updated device");

        // Given
        DeviceIcon initialDeviceIcon = deviceIconRepository.addDeviceIcon(getTestDeviceIcon());
        DeviceIcon expected = getTestDeviceIcon();
        expected.setName("updated-name");

        // When
        DeviceIcon result = deviceIconRepository.updateDeviceIcon(null, initialDeviceIcon.getName(), expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));

        // Update methods do not work as transactional - manual cleanup required
        deviceIconRepository.deleteDeviceIcon(null, result.getName());
    }

    /**
     * Test update_device_icon fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceIconThrowsWhenNameNNull() {
        log.info("Test update_device_icon fails when name is null");

        // Given
        DeviceIcon initialDeviceIcon = deviceIconRepository.addDeviceIcon(getTestDeviceIcon());
        DeviceIcon updateDeviceIcon = getTestDeviceIcon();
        updateDeviceIcon.setName(null);

        // When
        deviceIconRepository.updateDeviceIcon(null, initialDeviceIcon.getName(), updateDeviceIcon);
    }

    /**
     * Test update_device_icon fails when unique key conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceIconThrowsWhenNameNotUnique() {
        log.info("Test update_device_icon fails when unique key conflicts");

        // Given
        DeviceIcon initialDeviceIcon = deviceIconRepository.addDeviceIcon(getTestDeviceIcon());
        DeviceIcon updateDeviceIcon = getTestDeviceIcon();
        updateDeviceIcon.setName("non-conflicting-name");

        // When
        deviceIconRepository.addDeviceIcon(updateDeviceIcon);
        deviceIconRepository.updateDeviceIcon(null, updateDeviceIcon.getName(), initialDeviceIcon);
    }

    /**
     * Test update_device_icon fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceIconThrowsWhenNameTooLong() {
        log.info("Test update_device_icon fails when name is too long");

        // Given
        DeviceIcon initialDeviceIcon = deviceIconRepository.addDeviceIcon(getTestDeviceIcon());
        DeviceIcon deviceIconWithLongName = getTestDeviceIcon();
        deviceIconWithLongName.setName(StringUtils.repeat("A", 26));

        // When
        deviceIconRepository.updateDeviceIcon(null, initialDeviceIcon.getName(), deviceIconWithLongName);
    }


    /**
     * Test delete_device_icon with parameters deletes device icon
     */
    @Transactional
    @Test
    public void testDeleteDeviceIconWithParametersWorks() {
        log.info("Test delete_device_icon with parameters deletes device icon");

        // Given
        DeviceIcon deviceIcon = getTestDeviceIcon();

        // When
        deviceIconRepository.addDeviceIcon(deviceIcon);
        Collection<DeviceIcon> resultsBefore = deviceIconRepository.getDeviceIcons(null, null);
        Boolean result = deviceIconRepository.deleteDeviceIcon(null, deviceIcon.getName());
        Collection<DeviceIcon> resultsAfter = deviceIconRepository.getDeviceIcons(null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(1));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_device_icon without parameters doesn't delete anything
     */
    @Transactional
    @Test
    public void testDeleteDeviceIconWithoutParametersWontDeleteAll() {
        log.info("Test delete_device_icon without parameters doesn't delete anything");

        // When
        deviceIconRepository.addDeviceIcon(getTestDeviceIcon());
        Collection<DeviceIcon> resultsBefore = deviceIconRepository.getDeviceIcons(null, null);
        Boolean result = deviceIconRepository.deleteDeviceIcon(null, null);
        Collection<DeviceIcon> resultsAfter = deviceIconRepository.getDeviceIcons(null, null);

        // Then
        assertFalse(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    private DeviceIcon getTestDeviceIcon() {
        DeviceIcon deviceIcon = new DeviceIcon();
        deviceIcon.setName("test-icon.png");
        return deviceIcon;
    }
}
