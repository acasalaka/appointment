package apap.tk.appointment50.restservice;

import apap.tk.appointment50.model.Treatment;
import apap.tk.appointment50.restdto.response.TreatmentResponseDTO;

import java.util.List;

public interface TreatmentRestService {
    List<TreatmentResponseDTO> getAllTreatments();
    TreatmentResponseDTO treatmentToTreatmentResponseDTO(Treatment treatment);
}
