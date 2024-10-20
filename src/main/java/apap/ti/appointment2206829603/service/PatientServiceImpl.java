package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Patient;
import apap.ti.appointment2206829603.repository.PatientDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    PatientDb patientDb;

    @Override
    public Patient createPatient(Patient patient) {
        return patientDb.save(patient);
    }
    @Override
    public List<Patient> getAllPatients() {
        Sort sortByName = Sort.by(Sort.Order.by("name").ignoreCase());
        return patientDb.findAll(sortByName);
    };

    @Override
    public Patient getPatientByNIK(String NIK) {
        List<Patient> activePatients = getAllPatients();

        for (Patient patient : activePatients) {
            if (patient.getNik().equals(NIK)) {
                return patient;
            }
        }

        return null;
    }
}
