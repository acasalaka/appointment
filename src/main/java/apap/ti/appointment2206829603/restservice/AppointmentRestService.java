package apap.ti.appointment2206829603.restservice;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.restdto.request.AddAppointmentRequestRestDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentResponseDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentStatisticsResponseDTO;

public interface AppointmentRestService {
    AppointmentStatisticsResponseDTO getAppointmentStatistics(String period, Integer year);
    AppointmentResponseDTO getAppointmentById(String id);
    AppointmentResponseDTO addAppointment(AddAppointmentRequestRestDTO appointmentDTO);
    AppointmentResponseDTO appointmentToAppointmentResponseDTO(Appointment appointment);
}
