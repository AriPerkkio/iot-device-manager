package web.controller;

import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import web.domain.entity.Configuration;
import web.domain.response.ResponseWrapper;
import web.service.ConfigurationService;

import javax.validation.Valid;

import static web.validators.FilterValidator.validateErrors;

@RestController
@RequestMapping("/api")
public class ConfigurationController {

    private static final String URI = "/configurations";
    private static final String ID_URI = URI + "/{id}";
    private static final String CONTENT_TYPE = "application/vnd.collection+json; charset=utf-8";
    private final ConfigurationService configurationService;

    ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * Get configurations matching given parameters
     *
     * @param id
     *      Configuration ID used as filter
     * @param name
     *      Configuration name used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = URI, method = RequestMethod.GET, produces = CONTENT_TYPE)
    public ResponseWrapper getConfigurations(
        @RequestParam(name="id", required = false) Integer id,
        @RequestParam(name="name", required = false) String name) {
        return configurationService.getConfigurations(id, name);
    }

    /**
     * Get configurations matching given ID
     *
     * @param id
     *      Configuration ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.GET, produces = CONTENT_TYPE)
    public ResponseWrapper getConfigurationById(
        @PathVariable Integer id) {
        return configurationService.getConfigurations(id, null);
    }

    /**
     * Add configuration from request body to the database
     *
     * @param configuration
     *      Configuration to add
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = URI, method = RequestMethod.POST, produces = CONTENT_TYPE)
    public ResponseWrapper addConfiguration(
        @Valid @RequestBody Configuration configuration,
        Errors errors) {
        validateErrors(errors);

        return configurationService.addConfiguration(configuration);
    }

    /**
     * Update configuration matching given request parameters.
     *
     * @param id
     *      Configuration ID used as filter
     * @param name
     *      Configuration name used as filter
     * @param configuration
     *      Configuration used to replace existing one
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = URI, method = RequestMethod.PUT, produces = CONTENT_TYPE)
    public ResponseWrapper updateConfiguration(
        @RequestParam(name="id", required = false) Integer id,
        @RequestParam(name="name", required = false) String name,
        @Valid @RequestBody Configuration configuration,
        Errors errors) {
        validateErrors(errors);

        return configurationService.updateConfiguration(id, name, configuration);
    }

    /**
     * Update configuration matching given ID.
     *
     * @param id
     *      Configuration ID used as filter
     * @param configuration
     *      Configuration used to replace existing one
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.PUT, produces = CONTENT_TYPE)
    public ResponseWrapper updateConfigurationById(
        @PathVariable Integer id,
        @Valid @RequestBody Configuration configuration,
        Errors errors) {
        validateErrors(errors);

        return configurationService.updateConfiguration(id, null, configuration);
    }

    /**
     * Delete configuration matching given request parameters.
     *
     * @param id
     *      Configuration ID used as filter
     * @param name
     *      Configuration name used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = URI, method = RequestMethod.DELETE, produces = CONTENT_TYPE)
    public ResponseWrapper deleteConfiguration(
        @RequestParam(name="id", required = false) Integer id,
        @RequestParam(name="name", required = false) String name) {
        return configurationService.deleteConfiguration(id, name);
    }

    /**
     * Delete configuration matching given ID.
     *
     * @param id
     *      Configuration ID used as filter
     * @return
     *      ResponseWrapper containing payload
     */
    @RequestMapping(value = ID_URI, method = RequestMethod.DELETE, produces = CONTENT_TYPE)
    public ResponseWrapper deleteConfigurationById(
        @PathVariable Integer id) {
        return configurationService.deleteConfiguration(id, null);
    }
}
