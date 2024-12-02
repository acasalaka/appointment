package apap.tk.appointment50.service;

import apap.tk.appointment50.restdto.response.BaseResponseDTO;
import apap.tk.appointment50.restdto.response.DoctorResponseDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final WebClient webClient;

    public DoctorServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctorFromRest() {
        var response = webClient.get().uri("/api/doctor/viewall").retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<DoctorResponseDTO>>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }

    @Override
    public DoctorResponseDTO getDoctorByIDFromRest(UUID id) {
        var response = webClient.get().uri("/api/doctor/" + id).retrieve().bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<DoctorResponseDTO>>() {}).block();

        if (response == null) {
            return null;
        }

        if (response.getStatus() != 200) {
            return null;
        }

        return response.getData();
    }
}
