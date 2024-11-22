package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.restdto.response.BaseResponseDTO;
import apap.ti.appointment2206829603.restdto.response.PatientResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

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
        var response = webClient.get().uri("/api/patient/" + nik).retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<PatientResponseDTO>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }
}
