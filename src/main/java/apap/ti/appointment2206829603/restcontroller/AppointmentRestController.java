package apap.ti.appointment2206829603.restcontroller;

import apap.ti.appointment2206829603.restdto.response.AppointmentResponseDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentStatisticsResponseDTO;
import apap.ti.appointment2206829603.restdto.response.BaseResponseDTO;
import apap.ti.appointment2206829603.restservice.AppointmentRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentRestController {
    @Autowired
    AppointmentRestService appointmentRestService;

    @GetMapping("/viewall")
        public ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> listAppointment() {
        List<AppointmentResponseDTO> listAppointment = appointmentRestService.getAllAppointments();

        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseDTO>>();
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listAppointment);
        baseResponseDTO.setMessage(String.format("List Appointment berhasil diambil"));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> detailAppointment(@RequestParam("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<AppointmentResponseDTO>();
        var appointment = new AppointmentResponseDTO();
    try {
        appointment = appointmentRestService.getAppointmentById(id);

    }
    catch (Exception e) {

    }
        if (appointment == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data appointment tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(appointment);
        baseResponseDTO.setMessage(String.format("Appointment dengan ID %s berhasil ditemukan", appointment.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

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
