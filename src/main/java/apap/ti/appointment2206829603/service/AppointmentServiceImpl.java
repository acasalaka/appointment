package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.repository.AppointmentDb;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDb appointmentDb;

    public AppointmentServiceImpl(AppointmentDb appointmentDb) {
        this.appointmentDb = appointmentDb;
    }

    @Override
    public void createAppointment(Appointment appointment) {
        appointmentDb.save(appointment);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        Sort sortByName = Sort.by(Sort.Order.by("doctor.name").ignoreCase());
        return appointmentDb.findAllByDeletedAtIsNull(sortByName);
    };

    @Override
    public Appointment getAppointmentById(String idAppointment) {
        List<Appointment> activeAppointments = getAllAppointments();

        for (Appointment appointment : activeAppointments) {
            if (appointment.getId().equals(idAppointment)) {
                return appointment;
            }
        }

        return null;
    }

    @Override
    public void deleteAppointment(Appointment appointment) {
        appointment.setDeletedAt(new Date());
        appointmentDb.save(appointment);
    }

    public List<Date> getNextAvailableDates(Doctor doctor) {
        List<Date> availableDates = new ArrayList<>();
        Calendar current = Calendar.getInstance();

        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);

        int dayLimit = 28;
        int addedDays = 0;

        while (addedDays < dayLimit) {
            int currentDayOfWeek = current.get(Calendar.DAY_OF_WEEK);

            if (doctor.getSchedules().contains(currentDayOfWeek)) {

                boolean hasAppointment = doctor.getAppointments().stream()
                        .anyMatch(app -> app.getDate().equals(current.getTime()));

                if (!hasAppointment) {
                    availableDates.add(current.getTime());
                    addedDays++;
                }
            }

            current.add(Calendar.DAY_OF_MONTH, 1);
        }

        return availableDates;
    }



    public Appointment updateAppointment(Appointment appointment) {
        Appointment getAppointment = getAppointmentById(appointment.getId());
        if (getAppointment != null) {
            getAppointment.setDoctor(appointment.getDoctor());
            getAppointment.setDate(appointment.getDate());
            getAppointment.setUpdatedAt(new Date());
            getAppointment.setTotalFee(appointment.getDoctor().getFee());
            appointmentDb.save(getAppointment);
        }
        return getAppointment;
    }

    @Override
    public List<Appointment> getTodaysAppointments() {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);
        Date today = todayCal.getTime();

        List<Appointment> allAppointments = appointmentDb.findAllByDeletedAtIsNull(Sort.by(Sort.Order.by("doctor.name").ignoreCase()));

        return allAppointments.stream()
                .filter(appointment -> {
                    Calendar appointmentCal = Calendar.getInstance();
                    appointmentCal.setTime(appointment.getDate());
                    appointmentCal.set(Calendar.HOUR_OF_DAY, 0);
                    appointmentCal.set(Calendar.MINUTE, 0);
                    appointmentCal.set(Calendar.SECOND, 0);
                    appointmentCal.set(Calendar.MILLISECOND, 0);
                    Date appointmentDate = appointmentCal.getTime();

                    return today.equals(appointmentDate);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getAppointmentsInDateRange(Date startDate, Date endDate) {
        List<Appointment> allAppointments = appointmentDb.findAllByDeletedAtIsNull(Sort.by(Sort.Order.by("doctor.name").ignoreCase()));

        return allAppointments.stream()
                .filter(appointment -> !appointment.getDate().before(startDate) && !appointment.getDate().after(endDate))
                .collect(Collectors.toList());
    }
}