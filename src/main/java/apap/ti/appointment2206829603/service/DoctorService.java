package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.restdto.response.DoctorResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    List<DoctorResponseDTO> getAllDoctorFromRest();
    DoctorResponseDTO getDoctorByIDFromRest(UUID id);
}
