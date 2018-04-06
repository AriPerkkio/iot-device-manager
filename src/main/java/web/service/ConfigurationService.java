package web.service;

import web.domain.entity.Configuration;
import web.domain.entity.Device;
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

    /**
     * Get configuration's devices
     *
     * @param id
     *      Configuration ID used as filter
     * @param deviceTypeId
     *      Device type ID used as filter
     * @param deviceGroupId
     *      Device group ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    ResponseWrapper getConfigurationsDevices(Integer id, Integer deviceTypeId, Integer deviceGroupId);

    /**
     * Add device with configuration
     *
     * @param id
     *      Configuration ID used as filter
     * @param device
     *      Device to add with given configuration
     * @return
     *      ResponseWrapper containing payload
     */
    ResponseWrapper addDeviceWithConfiguration(Integer id, Device device);
}
