package apap.tk.appointment50.restservice;

import apap.tk.appointment50.model.Appointment;
import apap.tk.appointment50.restdto.request.AddAppointmentRequestRestDTO;
import apap.tk.appointment50.restdto.request.UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO;
import apap.tk.appointment50.restdto.request.UpdateAppointmentStatusRequestRestDTO;
import apap.tk.appointment50.restdto.response.AppointmentResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;

public interface AppointmentRestService {
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(String id);
    AppointmentResponseDTO addAppointment(AddAppointmentRequestRestDTO appointmentDTO);
    AppointmentResponseDTO appointmentToAppointmentResponseDTO(Appointment appointment);
    Appointment deleteAppointment(String id) throws EntityNotFoundException;
    AppointmentResponseDTO updateAppointmentStatus(UpdateAppointmentStatusRequestRestDTO appointmentDTO);
    AppointmentResponseDTO updateAppointmentTreatments(UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO appointmentDTO);
    List<AppointmentResponseDTO> getAllAppointmentsByIdDoctor(UUID DoctorID);
    List<AppointmentResponseDTO> getAllAppointmentsByIdPatient(UUID PatientID);
    List<AppointmentResponseDTO> getAllAppointmentsByDate(Date startDate, Date endDate);

}
