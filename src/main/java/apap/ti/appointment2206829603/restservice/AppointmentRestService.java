package apap.ti.appointment2206829603.restservice;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.restdto.request.AddAppointmentRequestRestDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentResponseDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentStatisticsResponseDTO;

import java.util.List;

public interface AppointmentRestService {
    AppointmentStatisticsResponseDTO getAppointmentStatistics(String period, Integer year);
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(String id);
    AppointmentResponseDTO addAppointment(AddAppointmentRequestRestDTO appointmentDTO);
    AppointmentResponseDTO appointmentToAppointmentResponseDTO(Appointment appointment);
}
