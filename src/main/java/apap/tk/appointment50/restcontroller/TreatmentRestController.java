package apap.tk.appointment50.restcontroller;

import apap.tk.appointment50.restdto.response.BaseResponseDTO;
import apap.tk.appointment50.restdto.response.TreatmentResponseDTO;
import apap.tk.appointment50.restservice.TreatmentRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/treatment")
public class TreatmentRestController {
    @Autowired
    TreatmentRestService treatmentRestService;

    @GetMapping("/viewall")
    public ResponseEntity<BaseResponseDTO<List<TreatmentResponseDTO>>> listTreatment() {
        List<TreatmentResponseDTO> listTreatment = treatmentRestService.getAllTreatments();

        var baseResponseDTO = new BaseResponseDTO<List<TreatmentResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listTreatment);
        baseResponseDTO.setMessage(String.format("List treatment berhasil diambil"));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
