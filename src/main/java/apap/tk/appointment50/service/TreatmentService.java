package apap.tk.appointment50.service;

import apap.tk.appointment50.model.Treatment;

import java.util.List;

public interface TreatmentService {
    void createTreatment(Treatment treatment);
    List<Treatment> getAllTreatments();
    Treatment getTreatmentById(Long id);
    void insertTreatmentData();
}
