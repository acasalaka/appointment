package apap.tk.appointment50.service;

import apap.tk.appointment50.model.Appointment;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {
    void createAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(String id);
    void deleteAppointment(Appointment appointment);
    List<Date> getNextAvailableDates(UUID doctorId);
    Appointment updateAppointment(Appointment appointment);
    List<Appointment> getTodaysAppointments();
    List<Appointment> getAppointmentsInDateRange(Date startDate, Date endDate);
    String generateAppointmentId(Appointment appointment);
    String statusString(int status);

}
