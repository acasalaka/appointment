package apap.tk.appointment50.service;

import apap.tk.appointment50.restdto.request.AddBillRequestDTO;
import apap.tk.appointment50.restdto.response.BillResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
public class BillServiceImpl implements BillService {

    private final WebClient webClient;

    public BillServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8085").build();
    }

    @Override
    public BillResponseDTO createBillByAppointmentID(BillResponseDTO billDTO){
        System.out.println("Sending request to Bill API with: " + billDTO);

        return webClient.post()
                .uri("/api/bill/createByAppointment")
                .bodyValue(billDTO)
                .retrieve()
                .bodyToMono(BillResponseDTO.class)
                .doOnNext(response -> System.out.println("Received response: " + response))
                .block();
    }
}
