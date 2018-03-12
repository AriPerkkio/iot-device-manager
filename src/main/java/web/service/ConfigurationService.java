package web.service;

import web.domain.entity.Configuration;
import web.domain.response.ResponseWrapper;

public interface ConfigurationService {

    /**
     * Get configurations matching given parameters
     *
     * @param id
     *      Configuration ID used as filter
     * @param name
     *      Configuration name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper getConfigurations(Integer id, String name);

    /**
     * Add configuration to the database
     *
     * @param configuration
     *      Configuration to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper addConfiguration(Configuration configuration);

    /**
     * Update configuration matching given filter parameters. Item in database is replaced using given Configuration object.
     *
     * @param id
     *      Configuration ID used as filter
     * @param name
     *      Configuration name used as filter
     * @param configuration
     *      Configuration to add
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper updateConfiguration(Integer id, String name, Configuration configuration);

    /**
     * Delete configuration matching given filter parameters. At least one parameter is required.
     *
     * @param id
     *      Configuration ID used as filter
     * @param name
     *      Configuration name used as filter
     * @return
     *      ResponseWrapper containing payload or errors
     */
    ResponseWrapper deleteConfiguration(Integer id, String name);
}
