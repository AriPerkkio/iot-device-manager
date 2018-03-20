package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Configuration;
import web.domain.response.ErrorWrapper;
import web.domain.response.ResponseWrapper;
import web.repository.ConfigurationRepository;
import web.service.ConfigurationService;

import static web.domain.response.ErrorCode.INTERNAL_ERROR;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    final ConfigurationRepository configurationRepository;

    ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public ResponseWrapper getConfigurations(Integer id, String name) {
        try {
            return new ResponseWrapper(configurationRepository.getConfigurations(id, name));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper addConfiguration(Configuration configuration) {
        try {
            return new ResponseWrapper(configurationRepository.addConfiguration(configuration));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper updateConfiguration(Integer id, String name, Configuration configuration) {
        try {
            return new ResponseWrapper(configurationRepository.updateConfiguration(id, name, configuration));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }

    @Override
    public ResponseWrapper deleteConfiguration(Integer id, String name) {
        try {
            return new ResponseWrapper(configurationRepository.deleteConfiguration(id, name));
        } catch (Exception e) {
            return new ResponseWrapper(new ErrorWrapper(INTERNAL_ERROR, "TODO error handling"));
        }
    }
}
