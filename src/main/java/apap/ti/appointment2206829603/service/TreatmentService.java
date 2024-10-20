package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Treatment;
import org.springframework.stereotype.Service;

import java.util.List;

public interface TreatmentService {
    void createTreatment(Treatment treatment);
    List<Treatment> getAllTreatments();
    Treatment getTreatmentById(Long id);
    void insertTreatmentData();
}
