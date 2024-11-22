package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.restdto.response.PatientResponseDTO;

import java.util.List;

public interface PatientService {
    List<PatientResponseDTO> getAllPatientFromRest();
    PatientResponseDTO getPatientByNIKFromRest(String nik);
}
