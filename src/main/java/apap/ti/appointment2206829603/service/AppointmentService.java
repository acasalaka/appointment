package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.model.Doctor;
import com.github.javafaker.App;

import java.util.Date;
import java.util.List;

public interface AppointmentService {
    void createAppointment(Appointment appointment);
    List<Appointment> getAllAppointments();
    Appointment getAppointmentById(String id);
    void deleteAppointment(Appointment appointment);
    List<Date> getNextAvailableDates(Doctor doctor);
    Appointment updateAppointment(Appointment appointment);
    List<Appointment> getTodaysAppointments();
}
