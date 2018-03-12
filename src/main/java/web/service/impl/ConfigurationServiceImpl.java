package web.service.impl;

import org.springframework.stereotype.Service;
import web.domain.entity.Configuration;
import web.domain.response.ResponseWrapper;
import web.repository.ConfigurationRepository;
import web.service.ConfigurationService;

import java.util.Collections;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    final ConfigurationRepository configurationRepository;

    ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public ResponseWrapper getConfigurations(Integer id, String name) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(configurationRepository.getConfigurations(id, name));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper addConfiguration(Configuration configuration) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(configurationRepository.addConfiguration(configuration));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper updateConfiguration(Integer id, String name, Configuration configuration) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(configurationRepository.updateConfiguration(id, name, configuration));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }

    @Override
    public ResponseWrapper deleteConfiguration(Integer id, String name) {
        ResponseWrapper response = new ResponseWrapper();

        try {
            response.setPayload(configurationRepository.deleteConfiguration(id, name));
        } catch (Exception e) {
            response.setErrors(Collections.singleton(e.toString()));
        }

        return response;
    }
}
