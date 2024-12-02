package apap.tk.appointment50.service;

import apap.tk.appointment50.restdto.response.PatientResponseDTO;

import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<PatientResponseDTO> getAllPatientFromRest();
    PatientResponseDTO getPatientByNIKFromRest(String nik);
    PatientResponseDTO getPatientByIDFromRest(UUID id);
}
