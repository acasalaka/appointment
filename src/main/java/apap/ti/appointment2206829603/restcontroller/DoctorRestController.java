package apap.ti.appointment2206829603.restcontroller;

import apap.ti.appointment2206829603.restdto.response.BaseResponseDTO;
import apap.ti.appointment2206829603.restdto.response.DoctorResponseDTO;
import apap.ti.appointment2206829603.restservice.DoctorRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class DoctorRestController {
    @Autowired
    DoctorRestService doctorRestService;

}
