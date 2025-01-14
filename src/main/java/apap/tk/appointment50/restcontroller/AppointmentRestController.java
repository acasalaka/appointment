package apap.tk.appointment50.restcontroller;

import apap.tk.appointment50.model.Appointment;
import apap.tk.appointment50.repository.AppointmentDb;
import apap.tk.appointment50.restdto.request.AddAppointmentRequestRestDTO;
import apap.tk.appointment50.restdto.request.UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO;
import apap.tk.appointment50.restdto.request.UpdateAppointmentStatusRequestRestDTO;
import apap.tk.appointment50.restdto.response.AppointmentResponseDTO;
import apap.tk.appointment50.restdto.response.BaseResponseDTO;
import apap.tk.appointment50.restservice.AppointmentRestService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentRestController {
    @Autowired
    AppointmentRestService appointmentRestService;

    @Autowired
    AppointmentDb appointmentDb;

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

        AppointmentResponseDTO appointment = appointmentRestService.getAppointmentById(id);

        try {
            if (appointment == null) {
                baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
                baseResponseDTO.setMessage("Data appointment tidak ditemukan");
                baseResponseDTO.setTimestamp(new Date());
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(appointment);
            baseResponseDTO.setMessage(String.format("Appointment dengan ID %s berhasil ditemukan", appointment.getId()));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (SecurityException e) {
            baseResponseDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
            baseResponseDTO.setMessage("Unauthorized: You do not have permission to delete this appointment.");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Internal Server Error: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAppointment(@Valid @RequestBody AddAppointmentRequestRestDTO appointmentDTO,
                                            BindingResult bindingResult) {

        boolean isAppointmentExists = appointmentDb.existsByIdDoctorAndDate(
                appointmentDTO.getDoctorId(),
                appointmentDTO.getDate()
        );

        if (isAppointmentExists) {
            throw new IllegalArgumentException(String.format(
                    "Appointment dengan Doctor ID %s pada tanggal %s sudah ada.",
                    appointmentDTO.getDoctorId(), appointmentDTO.getDate()
            ));
        }

        var baseResponseDTO = new BaseResponseDTO<AppointmentResponseDTO>();

        try {
            if (bindingResult.hasFieldErrors()) {
                String errorMessages = "";
                List<FieldError> errors = bindingResult.getFieldErrors();
                for (FieldError error : errors) {
                    errorMessages += error.getDefaultMessage() + "; ";
                }

                baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
                baseResponseDTO.setMessage(errorMessages);
                baseResponseDTO.setTimestamp(new Date());
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
            }

            AppointmentResponseDTO appointment = appointmentRestService.addAppointment(appointmentDTO);
            if (appointment == null) {
                baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
                baseResponseDTO.setMessage("Data appointment tidak ditemukan");
                baseResponseDTO.setTimestamp(new Date());
                return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
            }

            baseResponseDTO.setStatus(HttpStatus.CREATED.value());
            baseResponseDTO.setData(appointment);
            baseResponseDTO.setMessage(String.format("Appointment dengan ID %s berhasil ditambahkan", appointment.getId()));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);

        } catch (SecurityException e) {
            // Handle unauthorized access (401)
            baseResponseDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
            baseResponseDTO.setMessage("Unauthorized: You do not have permission to delete this appointment.");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            // Handle server errors (500)
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Internal Server Error: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{idAppointment}/delete")
    public ResponseEntity<?> deleteAppt(@PathVariable("idAppointment") String idAppointment) {
        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseDTO>>();

        try {
            appointmentRestService.deleteAppointment(idAppointment);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Appointment dengan ID %s berhasil dihapus", idAppointment));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format(e.getMessage()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } catch (SecurityException e) {
            // Handle unauthorized access (401)
            baseResponseDTO.setStatus(HttpStatus.UNAUTHORIZED.value());
            baseResponseDTO.setMessage("Unauthorized: You do not have permission to delete this appointment.");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            // Handle server errors (500)
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage("Internal Server Error: " + e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateAppointment(@Valid @RequestBody UpdateAppointmentStatusRequestRestDTO appointmentDTO,
                                               BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<AppointmentResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            String errorMessages = "";
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages += error.getDefaultMessage() + "; ";
            }

            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        AppointmentResponseDTO appointment = appointmentRestService.updateAppointmentStatus(appointmentDTO);
        if (appointment == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data appointment tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(appointment);
        baseResponseDTO.setMessage(String.format("Status appointment dengan ID %s berhasil diubah", appointment.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/update-treatments")
    public ResponseEntity<?> updateAppointmentDiagnosis(@Valid @RequestBody UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO appointmentDTO,
                                               BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<AppointmentResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            String errorMessages = "";
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages += error.getDefaultMessage() + "; ";
            }

            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }

        AppointmentResponseDTO appointment = appointmentRestService.updateAppointmentTreatments(appointmentDTO);
        if (appointment == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data appointment tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(appointment);
        baseResponseDTO.setMessage(String.format("Diagnosis dan Treatment appointment dengan ID %s berhasil diubah", appointment.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/by-doctor")
    public ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> listAppointmentByDoctorId(@RequestParam("idDoctor") UUID idDoctor) {
        List<AppointmentResponseDTO> listAppointment = appointmentRestService.getAllAppointmentsByIdDoctor(idDoctor);
        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseDTO>>();

        if (listAppointment.isEmpty()) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Doctor dengan ID %s belum memiliki appointment", idDoctor));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listAppointment);
        baseResponseDTO.setMessage(String.format("List Appointment Berdasarkan ID Doctor %s Berhasil Diambil", idDoctor));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/by-patient")
    public ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> listAppointmentByPatientId(@RequestParam("idPatient") UUID idPatient) {
        List<AppointmentResponseDTO> listAppointment = appointmentRestService.getAllAppointmentsByIdPatient(idPatient);
        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseDTO>>();

        if (listAppointment.isEmpty()) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Patient dengan ID %s belum memiliki appointment", idPatient));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listAppointment);
        baseResponseDTO.setMessage(String.format("List Appointment Berdasarkan ID Patient %s Berhasil Diambil", idPatient));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/by-date")
    public ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> listAppointmentByDate(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate
    ) {
        List<AppointmentResponseDTO> listAppointment = appointmentRestService.getAllAppointmentsByDate(startDate, endDate);
        var baseResponseDTO = new BaseResponseDTO<List<AppointmentResponseDTO>>();

        if (listAppointment.isEmpty()) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Tidak ada appointment dari tanggal %s hingga %s", startDate, endDate));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listAppointment);
        baseResponseDTO.setMessage(String.format("List Appointment dari tanggal %s hingga %s berhasil diambil", startDate, endDate));
        baseResponseDTO.setTimestamp(new Date());
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

}
