package apap.tk.appointment50.restservice;

import apap.tk.appointment50.model.Treatment;
import apap.tk.appointment50.repository.TreatmentDb;
import apap.tk.appointment50.restdto.response.AppointmentResponseDTO;
import apap.tk.appointment50.restdto.response.TreatmentResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TreatmentRestServiceImpl implements TreatmentRestService {
    @Autowired
    TreatmentDb treatmentDb;

    @Override
    public List<TreatmentResponseDTO> getAllTreatments() {

        var listTreatments = treatmentDb.findAll();
        var listTreatmentsResponseDTO = new ArrayList<TreatmentResponseDTO>();
        listTreatments.forEach(treatment -> {
            var treatmentResponseDTO = treatmentToTreatmentResponseDTO(treatment);
            listTreatmentsResponseDTO.add(treatmentResponseDTO);
        });

        return listTreatmentsResponseDTO;
    }
    @Override
    public TreatmentResponseDTO treatmentToTreatmentResponseDTO(Treatment treatment) {
        TreatmentResponseDTO responseDTO = new TreatmentResponseDTO();
        responseDTO.setId(treatment.getId());
        responseDTO.setName(treatment.getName());
        responseDTO.setPrice(treatment.getPrice());
        responseDTO.setCreatedAt(treatment.getCreatedAt());
        responseDTO.setUpdatedAt(treatment.getUpdatedAt());

        return responseDTO;
    }
}
