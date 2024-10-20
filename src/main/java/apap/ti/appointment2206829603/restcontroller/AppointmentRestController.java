package apap.ti.appointment2206829603.restcontroller;

import apap.ti.appointment2206829603.restdto.response.AppointmentStatisticsResponseDTO;
import apap.ti.appointment2206829603.restdto.response.BaseResponseDTO;
import apap.ti.appointment2206829603.restservice.AppointmentRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentRestController {
    @Autowired
    AppointmentRestService appointmentRestService;

    @PostMapping("/stat")
    public ResponseEntity<?> getApptStat(@RequestParam String period, @RequestParam int year) {
        var baseResponseDTO = new BaseResponseDTO<AppointmentStatisticsResponseDTO>();
        AppointmentStatisticsResponseDTO policyDTO = appointmentRestService.getAppointmentStatistics(period, year);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(policyDTO);
        baseResponseDTO.setMessage("Appointment statistics is found.");
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }


}
