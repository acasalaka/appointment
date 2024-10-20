package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Treatment;
import apap.ti.appointment2206829603.repository.TreatmentDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentServiceImpl implements TreatmentService {
    @Autowired
    TreatmentDb treatmentDb;

    @Override
    public void createTreatment(Treatment treatment) {
        treatmentDb.save(treatment);
    }

    @Override
    public List<Treatment> getAllTreatments() {
        return treatmentDb.findAll();
    };

    @Override
    public Treatment getTreatmentById(Long idTreatment) {
        List<Treatment> activeTreatments = getAllTreatments();

        for (Treatment treatment : activeTreatments) {
            if (treatment.getId().equals(idTreatment)) {
                return treatment;
            }
        }

        return null;
    }

    public void insertTreatmentData() {
        List<Treatment> listTreatments = List.of(
                new Treatment(1L, "X-ray", 150000L, null, null, null),
                new Treatment(2L, "CT Scan", 1000000L, null, null, null),
                new Treatment(3L, "MRI", 2500000L, null, null, null),
                new Treatment(4L, "Ultrasound", 300000L, null, null, null),
                new Treatment(5L, "Blood Clotting Test", 50000L, null, null, null),
                new Treatment(6L, "Blood Glucose Test", 30000L, null, null, null),
                new Treatment(7L, "Liver Function Test", 75000L, null, null, null),
                new Treatment(8L, "Complete Blood Count", 50000L, null, null, null),
                new Treatment(9L, "Urinalysis", 40000L, null, null, null),
                new Treatment(10L, "COVID-19 testing", 150000L, null, null, null),
                new Treatment(11L, "Cholesterol Test", 60000L, null, null, null),
                new Treatment(12L, "Inpatient care", 1000000L, null, null, null),
                new Treatment(13L, "Surgery", 7000000L, null, null, null),
                new Treatment(14L, "ICU", 2000000L, null, null, null),
                new Treatment(15L, "ER", 500000L, null, null, null),
                new Treatment(16L, "Flu shot", 100000L, null, null, null),
                new Treatment(17L, "Hepatitis vaccine", 200000L, null, null, null),
                new Treatment(18L, "COVID-19 Vaccine", 200000L, null, null, null),
                new Treatment(19L, "MMR Vaccine", 350000L, null, null, null),
                new Treatment(20L, "HPV Vaccine", 800000L, null, null, null),
                new Treatment(21L, "Pneumococcal Vaccine", 900000L, null, null, null),
                new Treatment(22L, "Herpes Zoster Vaccine", 1500000L, null, null, null),
                new Treatment(23L, "Physical exam", 250000L, null, null, null),
                new Treatment(24L, "Mammogram", 500000L, null, null, null),
                new Treatment(25L, "Colonoscopy", 3000000L, null, null, null),
                new Treatment(26L, "Dental X-ray", 200000L, null, null, null),
                new Treatment(27L, "Fillings", 400000L, null, null, null),
                new Treatment(28L, "Dental scaling", 500000L, null, null, null),
                new Treatment(29L, "Physical therapy", 250000L, null, null, null),
                new Treatment(30L, "Occupational therapy", 300000L, null, null, null),
                new Treatment(31L, "Speech therapy", 300000L, null, null, null),
                new Treatment(32L, "Psychiatric evaluation", 600000L, null, null, null),
                new Treatment(33L, "Natural delivery", 3500000L, null, null, null),
                new Treatment(34L, "C-section", 12000000L, null, null, null)
        );

        for (Treatment treatment : listTreatments) {
            treatmentDb.save(treatment);
        }
    }
}
