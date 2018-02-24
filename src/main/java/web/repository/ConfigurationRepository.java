package web.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import web.domain.entity.Configuration;

import java.util.Collection;

public interface ConfigurationRepository {

    /**
     * Get configurations matching given parameters. Parameters are optional and ignored when null value passed.
     * @param id
     *          Configuration ID used as filter
     * @param name
     *          Configuration name used as filter
     * @return
     *          Configurations matching given parameters
     */
    Collection<Configuration> getConfigurations(Integer id, String name);

    /**
     * Add given configuration to the database
     *
     * @param configuration
     *          Configuration to add
     * @return
     *          Inserted configuration including generated ID
     * @throws JsonProcessingException
     *          Exception thrown when configuration's content cannot be parsed into JSON string using Jackson's object mapper
     */
    Configuration addConfiguration(Configuration configuration) throws JsonProcessingException;

    /**
     * Update configuration matching given filter parameters. Item in database is replaced using given Configuration object.
     * At least one filter is required. Update operation is ignored when no filters passed.
     *
     * @param id
     *          Configuration ID used as filter
     * @param name
     *          Configuration name used as filter
     * @param configuration
     *          Configuration used to replace existing one. All the attributes are updated - even NULL ones.
     * @return
     *          Configuration with updated attributes
     * @throws JsonProcessingException
     *          Exception thrown when configuration's content cannot be parsed into JSON string using Jackson's object mapper
     */
    Configuration updateConfiguration(Integer id, String name, Configuration configuration) throws JsonProcessingException;

    /**
     * Delete configuration matching given parameters
     *
     * @param id
     *          Configuration ID used as filter
     * @param name
     *          Configuration name used as filter
     * @return
     *          True when operation is successful. False when operation failed.
     */
    Boolean deleteConfiguration(Integer id, String name);
}
