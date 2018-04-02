package web.service.impl;

import javassist.NotFoundException;
import org.hibernate.HibernateError;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import web.domain.entity.Configuration;
import web.domain.response.ResponseWrapper;
import web.exception.ExceptionHandlingUtils;
import web.repository.ConfigurationRepository;
import web.service.ConfigurationService;
import web.validators.FilterValidator;

import java.util.Collection;

import static web.exception.ExceptionHandlingUtils.throwNotFoundException;
import static web.mapper.ConfigurationMapper.mapToCollection;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    final ConfigurationRepository configurationRepository;

    ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public ResponseWrapper getConfigurations(Integer id, String name) {
        try {
            Collection<Configuration> configurations = configurationRepository.getConfigurations(id, name);

            if(CollectionUtils.isEmpty(configurations)) {
                throwNotFoundException(String.format("[id: %d, name: %s]", id, name));
            }

            return new ResponseWrapper(mapToCollection(configurations));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Get configurations failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper addConfiguration(Configuration configuration) {
        try {
            Configuration addedConfiguration = configurationRepository.addConfiguration(configuration);

            return new ResponseWrapper(mapToCollection(addedConfiguration));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Add configuration failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper updateConfiguration(Integer id, String name, Configuration configuration) {
        try {
            FilterValidator.checkForMinimumFilters(id, name);
            validateConfigurationExists(id, name);

            Configuration updatedConfiguration = configurationRepository.updateConfiguration(id, name, configuration);

            // TODO, fix commit calls during single stored procedure. Currently update procedures return old item - not the updated one
            updatedConfiguration.setName(configuration.getName());
            updatedConfiguration.setDescription(configuration.getDescription());
            updatedConfiguration.setContent(configuration.getContent());

            return new ResponseWrapper(mapToCollection(updatedConfiguration));
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Update configuration failed");
        }

        return null;
    }

    @Override
    public ResponseWrapper deleteConfiguration(Integer id, String name) {
        try {
            FilterValidator.checkForMinimumFilters(id, name);
            validateConfigurationExists(id, name);

            Boolean deleteSuccessful = configurationRepository.deleteConfiguration(id, name);

            if (!deleteSuccessful) {
                throw new HibernateError("");
            }

            return new ResponseWrapper("", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ExceptionHandlingUtils.validateRepositoryExceptions(e, "Delete configuration failed");
        }

        return null;
    }

    /**
     * Valida a configuration exists
     *
     * @param id
     *      Configuration ID used as filter
     * @param name
     *      Configuration name used as filter
     * @throws NotFoundException
     *      Exception thrown when no configuration found
     */
    private void validateConfigurationExists(Integer id, String name) throws NotFoundException {
        Collection<Configuration> configurations = configurationRepository.getConfigurations(id, name);

        if(CollectionUtils.isEmpty(configurations)) {
            throwNotFoundException(String.format("[id: %d, name: %s]", id, name));
        }
    }
}
