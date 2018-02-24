package web.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import web.domain.entity.Configuration;
import web.repository.ConfigurationRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.Collection;

@Repository
public class ConfigurationRepositoryImpl implements ConfigurationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Collection<Configuration> getConfigurations(Integer id, String name) {
        StoredProcedureQuery getConfigurationsQuery =
            entityManager.createNamedStoredProcedureQuery("get_configurations")
                .setParameter("f_id", id)
                .setParameter("f_name", name);

        return getConfigurationsQuery.getResultList();
    }

    @Override
    public Configuration addConfiguration(Configuration configuration) throws JsonProcessingException {

        // Convert HashMap into JSON string
        String contentString = objectMapper.writeValueAsString(configuration.getContent());

        StoredProcedureQuery addConfigurationQuery =
            entityManager.createNamedStoredProcedureQuery("add_configuration")
                .setParameter("p_name", configuration.getName())
                .setParameter("p_description", configuration.getDescription())
                .setParameter("p_content", contentString);

        return (Configuration) addConfigurationQuery.getSingleResult();
    }

    @Override
    public Configuration updateConfiguration(Integer id, String name, Configuration configuration) throws JsonProcessingException {

        // Convert HashMap into JSON string
        String contentString = objectMapper.writeValueAsString(configuration.getContent());

        StoredProcedureQuery updateConfigurationQuery =
            entityManager.createNamedStoredProcedureQuery("update_configuration")
                .setParameter("f_id", id)
                .setParameter("f_name", name)
                .setParameter("p_name", configuration.getName())
                .setParameter("p_description", configuration.getDescription())
                .setParameter("p_content", contentString);

        return (Configuration) updateConfigurationQuery.getSingleResult();
    }

    @Override
    public Boolean deleteConfiguration(Integer id, String name) {
        StoredProcedureQuery deleteConfigurationQuery =
            entityManager.createNamedStoredProcedureQuery("delete_configuration")
                .setParameter("f_id", id)
                .setParameter("f_name", name);

        return BigInteger.ONE.equals(deleteConfigurationQuery.getSingleResult());
    }
}
