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
import web.domain.entity.DeviceGroup;
import web.repository.DeviceGroupRepository;

import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceGroupRepositoryTest {

    @Autowired
    DeviceGroupRepository deviceGroupRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Test add_device_group returns added device group
     */
    @Transactional
    @Test
    public void testAddDeviceGroupReturnsAddedDeviceGroup() {
        log.info("Test add_device_group returns added device group");

        // Given
        DeviceGroup expected = getTestDeviceGroup();

        // When
        DeviceGroup result = deviceGroupRepository.addDeviceGroup(expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));
        assertThat(result.getDescription(), equalTo(expected.getDescription()));
    }

    /**
     * Test add_device_group fails when device group with duplicate name is used
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceGroupThrowsWhenNameNotUnique() {
        log.info("Test add_device_group fails when device group with duplicate name is used");

        // Given
        DeviceGroup deviceGroup = getTestDeviceGroup();

        // When
        deviceGroupRepository.addDeviceGroup(deviceGroup);
        deviceGroupRepository.addDeviceGroup(deviceGroup);
    }

    /**
     * Test add_device_group fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceGroupThrowsWhenNameNull() {
        log.info("Test add_device_group fails when name is null");

        // Given
        DeviceGroup deviceGroupWithNameNull = getTestDeviceGroup();
        deviceGroupWithNameNull.setName(null);

        // When
        deviceGroupRepository.addDeviceGroup(deviceGroupWithNameNull);
    }

    /**
     * Test add_device_group fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceGroupThrowsWhenNameTooLong() {
        log.info("Test add_device_group fails when name is too long");

        // Given
        DeviceGroup deviceGroupWithTooLongName = getTestDeviceGroup();
        deviceGroupWithTooLongName.setName(StringUtils.repeat("A", 51));

        // When
        deviceGroupRepository.addDeviceGroup(deviceGroupWithTooLongName);
    }

    /**
     * Test add_device_group fails when description is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddDeviceGroupThrowsWhenDescriptionTooLong() {
        log.info("testAddDeviceGroupThrowsWhenDescriptionTooLong");

        // Given
        DeviceGroup deviceGroupWithTooLongDescription = getTestDeviceGroup();
        deviceGroupWithTooLongDescription.setDescription(StringUtils.repeat("A", 101));

        // When
        deviceGroupRepository.addDeviceGroup(deviceGroupWithTooLongDescription);
    }

    /**
     * Test get_device_groups without parameters finds inserted device group
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsWithoutParametersReturnsInsertedDeviceGroup() {
        log.info("Test get_device_groups without parameters finds inserted device group");

        // Given
        DeviceGroup expected = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());

        // When
        Collection<DeviceGroup> results = deviceGroupRepository.getDeviceGroups(null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("description", equalTo(expected.getDescription()))));

    }

    /**
     * Test get_device_groups with parameters finds inserted device group
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsWithParametersReturnsInsertedDeviceGroup() {
        log.info("Test get_device_groups with parameters finds inserted device group");

        // Given
        DeviceGroup expected = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());

        // When
        Collection<DeviceGroup> results = deviceGroupRepository.getDeviceGroups(expected.getId(), expected.getName());

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("description", equalTo(expected.getDescription()))));
    }

    /**
     * Test update_device_group with parameters works
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceGroupWithParametersWorks() {
        log.info("Test update_device_group with parameters works");

        // Given
        DeviceGroup initialDeviceGroup = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        DeviceGroup expected = getTestDeviceGroup();
        expected.setName("updated-name");
        expected.setDescription("Updated description for testUpdateDeviceGroupReturnsUpdatedDeviceGroup");

        // When
        deviceGroupRepository.updateDeviceGroup(null, initialDeviceGroup.getName(), expected);
        Collection<DeviceGroup> results = deviceGroupRepository.getDeviceGroups(null, expected.getName());

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("description", equalTo(expected.getDescription()))));

        // Update methods do not work as transactional - manual cleanup required
        deviceGroupRepository.deleteDeviceGroup(null, expected.getName());
    }

    /**
     * Test update_device_group without parameters fails
     */
    @Transactional
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testUpdateDeviceGroupWithoutParametersThrows() {
        log.info("Test update_device_group without parameters fails");

        // When
        deviceGroupRepository.updateDeviceGroup(null, null, getTestDeviceGroup());
    }

    /**
     * Test update_device_group returns updated device group
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceGroupReturnsUpdatedDeviceGroup() {
        log.info("Test update_device_group returns updated device group");

        // Given
        DeviceGroup initialDeviceGroup = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        DeviceGroup expected = getTestDeviceGroup();
        expected.setName("updated-name");
        expected.setDescription("Updated description for testUpdateDeviceGroupReturnsUpdatedDeviceGroup");

        // When
        DeviceGroup result = deviceGroupRepository.updateDeviceGroup(null, initialDeviceGroup.getName(), expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));
        assertThat(result.getDescription(), equalTo(expected.getDescription()));

        // Update methods do not work as transactional - manual cleanup required
        deviceGroupRepository.deleteDeviceGroup(null, result.getName());
    }

    /**
     * Test update_device_group fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceThrowsWhenNameNull() {
        log.info("Test update_device_group fails when name is null");

        // Given
        DeviceGroup initialDevice = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        DeviceGroup deviceGroupWithNameNull = getTestDeviceGroup();
        deviceGroupWithNameNull.setName(null);

        // When
        deviceGroupRepository.updateDeviceGroup(null, initialDevice.getName(), deviceGroupWithNameNull);
    }

    /**
     * Test update_device_group fails when unique key conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceGroupThrowsWhenNameNotUnique() {
        log.info("Test update_device_group fails when unique key conflicts");

        // Given
        DeviceGroup initialDeviceGroup = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        DeviceGroup updateDeviceGroup = getTestDeviceGroup();
        updateDeviceGroup.setName("non-conflicting-name");

        // When
        deviceGroupRepository.addDeviceGroup(updateDeviceGroup);
        deviceGroupRepository.updateDeviceGroup(null, updateDeviceGroup.getName(), initialDeviceGroup);
    }

    /**
     * Test update_device_group fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceGroupThrowsWhenNameTooLong() {
        log.info("Test update_device_group fails when name is too long");

        // Given
        DeviceGroup initialDeviceGroup = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        DeviceGroup updateDeviceGroup = getTestDeviceGroup();
        updateDeviceGroup.setName(StringUtils.repeat("A", 51));

        // When
        deviceGroupRepository.updateDeviceGroup(null, initialDeviceGroup.getName(), updateDeviceGroup);
    }

    /**
     * Test update_device_group fails when description is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceGroupThrowsWhenDescriptionTooLong() {
        log.info("Test update_device_group fails when description is too long");

        // Given
        DeviceGroup initialDeviceGroup = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        DeviceGroup updateDeviceGroup = getTestDeviceGroup();
        updateDeviceGroup.setDescription(StringUtils.repeat("A", 101));

        // When
        deviceGroupRepository.updateDeviceGroup(null, initialDeviceGroup.getName(), updateDeviceGroup);
    }

    /**
     * Test delete_device_group with parameters deletes device group
     */
    @Transactional
    @Test
    public void testDeleteDeviceGroupWithParametersWorks() {
        log.info("Test delete_device_group with parameters deletes device group");

        // Given
        DeviceGroup deviceGroup = getTestDeviceGroup();

        // When
        deviceGroupRepository.addDeviceGroup(deviceGroup);
        Collection<DeviceGroup> resultsBefore = deviceGroupRepository.getDeviceGroups(null, null);
        Boolean result = deviceGroupRepository.deleteDeviceGroup(null, deviceGroup.getName());
        Collection<DeviceGroup> resultsAfter = deviceGroupRepository.getDeviceGroups(null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(1));
        assertThat(resultsAfter.size(), equalTo(0));
    }

    /**
     * Test delete_device_group without parameters doesn't delete all device groups
     */
    @Transactional
    @Test
    public void testDeleteDeviceGroupWithoutParametersWontDeleteAll() {
        log.info("Test delete_device_group without parameters doesn't delete all device groups");

        // When
        deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        Collection<DeviceGroup> resultsBefore = deviceGroupRepository.getDeviceGroups(null, null);
        Boolean result = deviceGroupRepository.deleteDeviceGroup(null, null);
        Collection<DeviceGroup> resultsAfter = deviceGroupRepository.getDeviceGroups(null, null);

        // Then
        assertFalse(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    private DeviceGroup getTestDeviceGroup() {
        DeviceGroup deviceGroup = new DeviceGroup();
        deviceGroup.setName("test-device-group-name");
        deviceGroup.setDescription("Test description for DeviceGroupRepositoryTest");

        return deviceGroup;
    }
}
