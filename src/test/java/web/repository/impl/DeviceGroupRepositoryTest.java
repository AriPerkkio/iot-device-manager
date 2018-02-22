package web.repository.impl;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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

    /**
     * Test add_device_group returns added device group
     */
    @Transactional
    @Test
    public void testAddDeviceGroupReturnsAddedDeviceGroup() {
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
        // Given
        DeviceGroup deviceGroupWithTooLongName = getTestDeviceGroup();
        deviceGroupWithTooLongName.setDescription(StringUtils.repeat("A", 101));

        // When
        deviceGroupRepository.addDeviceGroup(deviceGroupWithTooLongName);
    }

    /**
     * Test get_device_groups without parameters finds inserted device group
     */
    @Transactional
    @Test
    public void testGetDeviceGroupsWithoutParametersReturnsInsertedDeviceGroup() {
        // Given
        DeviceGroup expected = getTestDeviceGroup();

        // When
        deviceGroupRepository.addDeviceGroup(expected);
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
        // Given
        DeviceGroup expected = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());

        // When
        Collection<DeviceGroup> results = deviceGroupRepository.getDeviceGroups(expected.getId(), expected.getName());

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("description", equalTo(expected.getDescription()))));
    }

    /**
     * Test update_device_group returns updated device group
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateDeviceGroupReturnsUpdatedDeviceGroup() {
        // Given
        DeviceGroup initialDeviceGroup = getTestDeviceGroup();
        DeviceGroup expected = getTestDeviceGroup();
        expected.setName("updated-name");
        expected.setDescription("Updated description for testUpdateDeviceGroupReturnsUpdatedDeviceGroup");

        // When
        deviceGroupRepository.addDeviceGroup(initialDeviceGroup);
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
        // Given
        DeviceGroup initialDeviceGroup = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        DeviceGroup updateDeviceGroup = getTestDeviceGroup();
        updateDeviceGroup.setName(StringUtils.repeat("A", 51));

        // When
        deviceGroupRepository.addDeviceGroup(updateDeviceGroup);
        deviceGroupRepository.updateDeviceGroup(null, updateDeviceGroup.getName(), initialDeviceGroup);
    }

    /**
     * Test update_device_group fails when description is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDeviceGroupThrowsWhenDescriptionTooLong() {
        // Given
        DeviceGroup initialDeviceGroup = deviceGroupRepository.addDeviceGroup(getTestDeviceGroup());
        DeviceGroup updateDeviceGroup = getTestDeviceGroup();
        updateDeviceGroup.setDescription(StringUtils.repeat("A", 101));

        // When
        deviceGroupRepository.addDeviceGroup(updateDeviceGroup);
        deviceGroupRepository.updateDeviceGroup(null, updateDeviceGroup.getName(), initialDeviceGroup);
    }

    /**
     * Test delete_device_group with parameters deletes device group
     */
    @Transactional
    @Test
    public void testDeleteDeviceGroupWithParametersWorks() {
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
        // Given
        DeviceGroup deviceGroup = getTestDeviceGroup();

        // When
        deviceGroupRepository.addDeviceGroup(deviceGroup);
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
