package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    Patient createPatient(Patient patient);
    List<Patient> getAllPatients();
    Patient getPatientByNIK(String NIK);
}
