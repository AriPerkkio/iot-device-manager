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
import web.domain.entity.Configuration;
import web.repository.ConfigurationRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigurationRepositoryTest {

    @Autowired
    ConfigurationRepository configurationRepository;

    /**
     * Test add_configuration returns added configuration
     */
    @Transactional
    @Test
    public void testAddConfigurationReturnsAddedConfiguration() throws Exception {
        // Given
        Configuration expected = getTestConfiguration();

        // When
        Configuration result = configurationRepository.addConfiguration(expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));
        assertThat(result.getDescription(), equalTo(expected.getDescription()));
        assertThat(result.getContent(), equalTo(expected.getContent()));
    }

    /**
     * Test add_configuration fails when configuration with duplicate name is used
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddConfigurationThrowsWhenNameNotUnique() throws Exception {
        // Given
        Configuration configuration = getTestConfiguration();

        // When
        configurationRepository.addConfiguration(configuration);
        configurationRepository.addConfiguration(configuration);
    }


    /**
     * Test add_configuration fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddConfigurationThrowsWhenNameNull() throws Exception {
        // Given
        Configuration configuration = getTestConfiguration();
        configuration.setName(null);

        // When
        configurationRepository.addConfiguration(configuration);
    }


    /**
     * Test add_configuration fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddConfigurationThrowsWhenNameTooLong() throws Exception {
        // Given
        Configuration configuration = getTestConfiguration();
        configuration.setName(StringUtils.repeat("A", 51));

        // When
        configurationRepository.addConfiguration(configuration);
    }

    /**
     * Test add_configuration fails when description is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testAddConfigurationThrowsWhenDescriptionTooLong() throws Exception {
        // Given
        Configuration configuration = getTestConfiguration();
        configuration.setDescription(StringUtils.repeat("A", 101));

        // When
        configurationRepository.addConfiguration(configuration);
    }

    /**
     * Test get_configurations without parameters finds inserted configuration
     */
    @Transactional
    @Test
    public void testGetConfigurationsWithoutParametersReturnsInsertedConfiguration() throws Exception {
        // Given
        Configuration expected = configurationRepository.addConfiguration(getTestConfiguration());

        // When
        Collection<Configuration> results = configurationRepository.getConfigurations(null, null);

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("description", equalTo(expected.getDescription()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("content", equalTo(expected.getContent()))));
    }

    /**
     * Test get_configurations with parameters finds inserted configuration
     */
    @Transactional
    @Test
    public void testGetConfigurationsWithParametersReturnsInsertedConfiguration() throws Exception {
        // Given
        Configuration expected = configurationRepository.addConfiguration(getTestConfiguration());

        // When
        Collection<Configuration> results = configurationRepository.getConfigurations(expected.getId(), expected.getName());

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("description", equalTo(expected.getDescription()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("content", equalTo(expected.getContent()))));
    }

    /**
     * Test update_configuration with parameters works
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateConfigurationWithParametersWorks() throws Exception {
        // Given
        Configuration initialConfiguration = configurationRepository.addConfiguration(getTestConfiguration());
        Configuration expected = getTestConfiguration();
        expected.setName("updated-name");
        expected.setDescription("Updated description");
        expected.setContent(new HashMap<String, Object>(Collections.singletonMap("updated-key", "updated-value")));

        // When
        configurationRepository.updateConfiguration(null, initialConfiguration.getName(), expected);
        Collection<Configuration> results = configurationRepository.getConfigurations(null, expected.getName());

        // Then
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("name", equalTo(expected.getName()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("description", equalTo(expected.getDescription()))));
        assertThat(results, IsCollectionContaining.hasItem(hasProperty("content", equalTo(expected.getContent()))));

        // Update methods do not work as transactional - manual cleanup required
        configurationRepository.deleteConfiguration(null, expected.getName());
    }

    /**
     * Test update_configuration without parameters fails
     */
    @Transactional
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testUpdateConfigurationWithoutParametersThrows() throws Exception {
        // When
        configurationRepository.updateConfiguration(null, null, getTestConfiguration());
    }

    /**
     * Test update_configuration returns updated configuration
     * Note: Not @Transactional due to update operation usage
     */
    @Test
    public void testUpdateConfigurationReturnsUpdatedConfiguration() throws Exception {
        // Given
        Configuration initialConfiguration = configurationRepository.addConfiguration(getTestConfiguration());
        Configuration expected = getTestConfiguration();
        expected.setName("updated-name");
        expected.setDescription("Updated description");
        expected.setContent(new HashMap<String, Object>(Collections.singletonMap("updated-key", "updated-value")));

        // When
        Configuration result = configurationRepository.updateConfiguration(null, initialConfiguration.getName(), expected);

        // Then
        assertThat(result.getName(), equalTo(expected.getName()));
        assertThat(result.getDescription(), equalTo(expected.getDescription()));
        assertThat(result.getContent(), equalTo(expected.getContent()));

        // Update methods do not work as transactional - manual cleanup required
        configurationRepository.deleteConfiguration(null, expected.getName());
    }

    /**
     * Test update_configuration fails when name is null
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateConfigurationThrowsWhenNameNull() throws Exception {
        // Given
        Configuration initialConfiguration = configurationRepository.addConfiguration(getTestConfiguration());
        Configuration configurationWithNullName = getTestConfiguration();
        configurationWithNullName.setName(null);

        // When
        configurationRepository.updateConfiguration(null, initialConfiguration.getName(), configurationWithNullName);
    }

    /**
     * Test update_configuration fails when unique key conflicts
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateConfigurationThrowsWhenNameNotUnique() throws Exception {
        // Given
        Configuration initialConfiguration = configurationRepository.addConfiguration(getTestConfiguration());
        Configuration updateConfiguration = getTestConfiguration();
        updateConfiguration.setName("non-conflicting-name");

        // When
        configurationRepository.addConfiguration(updateConfiguration);
        configurationRepository.updateConfiguration(null, updateConfiguration.getName(), initialConfiguration);
    }

    /**
     * Test update_configuration fails when name is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateConfigurationThrowsWhenNameTooLong() throws Exception {
        // Given
        Configuration initialConfiguration = configurationRepository.addConfiguration(getTestConfiguration());
        Configuration updateConfiguration = getTestConfiguration();
        updateConfiguration.setName(StringUtils.repeat("A", 51));

        // When
        configurationRepository.updateConfiguration(null, initialConfiguration.getName(), updateConfiguration);
    }

    /**
     * Test update_configuration fails when description is too long
     */
    @Transactional
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateConfigurationThrowsWhenDescriptionTooLong() throws Exception {
        // Given
        Configuration initialConfiguration = configurationRepository.addConfiguration(getTestConfiguration());
        Configuration updateConfiguration = getTestConfiguration();
        updateConfiguration.setDescription(StringUtils.repeat("A", 101));

        // When
        configurationRepository.updateConfiguration(null, initialConfiguration.getName(), updateConfiguration);
    }

    /**
     * Test delete_configuration with parameters deletes configuration
     */
    @Transactional
    @Test
    public void testDeleteConfigurationWithParametersWorks() throws Exception {
        // Given
        Configuration configuration = getTestConfiguration();

        // When
        configurationRepository.addConfiguration(configuration);
        Collection<Configuration> resultsBefore = configurationRepository.getConfigurations(null, null);
        Boolean result = configurationRepository.deleteConfiguration(null, configuration.getName());
        Collection<Configuration> resultsAfter = configurationRepository.getConfigurations(null, null);

        // Then
        assertTrue(result);
        assertThat(resultsBefore.size(), equalTo(1));
        assertThat(resultsAfter.size(), equalTo(0));
    }


    /**
     * Test delete_configuration without parameters doesn't delete all configurations
     */
    @Transactional
    @Test
    public void testDeleteConfigurationWithoutParametersWontDeleteAll() throws Exception {
        // When
        configurationRepository.addConfiguration(getTestConfiguration());
        Collection<Configuration> resultsBefore = configurationRepository.getConfigurations(null, null);
        Boolean result = configurationRepository.deleteConfiguration(null, null);
        Collection<Configuration> resultsAfter = configurationRepository.getConfigurations(null, null);

        // Then
        assertFalse(result);
        assertThat(resultsBefore.size(), equalTo(resultsAfter.size()));
    }

    private Configuration getTestConfiguration() {
        Configuration configuration = new Configuration();

        configuration.setName("test-configuration-name");
        configuration.setDescription("Description for test configuration");

        HashMap<String, Object> content = new HashMap<>();
        content.put("test-string-key", "test-string-value");
        content.put("test-array-key", Arrays.asList("test-array-value-one", "test-array-value-two"));
        content.put("test-object-key", Collections.singletonMap("object-key", "object-value"));

        configuration.setContent(content);
        return configuration;
    }
}
