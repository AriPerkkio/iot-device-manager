package web.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import web.domain.entity.Measurement;
import web.repository.MeasurementRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

@Repository
public class MeasurementRepositoryImpl implements MeasurementRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Collection<Measurement> getMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        StoredProcedureQuery getMeasurementsQuery =
            entityManager.createNamedStoredProcedureQuery("get_measurements")
                .setParameter("f_device_id", deviceId)
                .setParameter("f_exact_time", exactTime)
                .setParameter("f_start_time", startTime)
                .setParameter("f_end_time", endTime);

        return getMeasurementsQuery.getResultList();
    }

    @Override
    public Measurement addMeasurement(Measurement measurement) throws JsonProcessingException {

        // Prevent inserting "null" text into CONTENT caused by json-string conversion
        if(null == measurement.getContent()) {
            throw new DataIntegrityViolationException("measurement.content cannot be null");
        }

        // Convert HashMap into JSON string
        String contentString = objectMapper.writeValueAsString(measurement.getContent());

        StoredProcedureQuery addMeasurementQuery =
            entityManager.createNamedStoredProcedureQuery("add_measurement")
                .setParameter("p_device_id", measurement.getDeviceId())
                .setParameter("p_content", contentString)
                .setParameter("p_time", measurement.getTime());

        return (Measurement) addMeasurementQuery.getResultList().get(0);
    }

    @Override
    public Boolean deleteMeasurements(Integer deviceId, Date exactTime, Date startTime, Date endTime) {
        StoredProcedureQuery deleteMeasurementsQuery =
            entityManager.createNamedStoredProcedureQuery("delete_measurements")
                .setParameter("f_device_id", deviceId)
                .setParameter("f_exact_time", exactTime)
                .setParameter("f_start_time", startTime)
                .setParameter("f_end_time", endTime);

        return BigInteger.ONE.equals(deleteMeasurementsQuery.getSingleResult());
    }
}

