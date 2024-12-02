package apap.tk.appointment50.service;

import apap.tk.appointment50.restdto.response.DoctorResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    List<DoctorResponseDTO> getAllDoctorFromRest();
    DoctorResponseDTO getDoctorByIDFromRest(UUID id);
}
