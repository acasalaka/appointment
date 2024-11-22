package apap.ti.appointment2206829603.restservice;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.restdto.request.AddAppointmentRequestRestDTO;
import apap.ti.appointment2206829603.restdto.request.UpdateAppointmentStatusRequestRestDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentResponseDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentStatisticsResponseDTO;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;

public interface AppointmentRestService {
    AppointmentStatisticsResponseDTO getAppointmentStatistics(String period, Integer year);
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(String id);
    AppointmentResponseDTO addAppointment(AddAppointmentRequestRestDTO appointmentDTO);
    AppointmentResponseDTO appointmentToAppointmentResponseDTO(Appointment appointment);
    Appointment deleteAppointment(String id) throws EntityNotFoundException;
    AppointmentResponseDTO updateAppointmentStatus(UpdateAppointmentStatusRequestRestDTO appointmentDTO);
    List<AppointmentResponseDTO> getAllAppointmentsByIdDoctor(UUID DoctorID);
    List<AppointmentResponseDTO> getAllAppointmentsByIdPatient(UUID PatientID);
    List<AppointmentResponseDTO> getAllAppointmentsByDate(Date date);

}
