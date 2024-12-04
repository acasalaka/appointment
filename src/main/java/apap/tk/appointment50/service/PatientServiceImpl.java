package apap.tk.appointment50.service;

import apap.tk.appointment50.restdto.response.BaseResponseDTO;
import apap.tk.appointment50.restdto.response.PatientResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {
    
    private final WebClient webClient;

    public PatientServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
    }

    @Override
    public List<PatientResponseDTO> getAllPatientFromRest() {
        var response = webClient.get().uri("/api/patient/viewall").retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<PatientResponseDTO>>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }

    @Override
    public PatientResponseDTO getPatientByNIKFromRest(String nik) {
        var response = webClient.get().uri("/api/patient/get-nik/" + nik).retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PatientResponseDTO>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }

    @Override
    public PatientResponseDTO getPatientByIDFromRest(UUID id) {
        var response = webClient.get().uri("/api/patient/detail/" + id).retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PatientResponseDTO>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }
}
