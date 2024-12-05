package apap.tk.appointment50.service;

import apap.tk.appointment50.model.Appointment;
import apap.tk.appointment50.repository.AppointmentDb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDb appointmentDb;

    @Autowired
    DoctorService doctorService;

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
        return appointmentDb.findAllByIsDeletedFalse(sortByName);
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
        appointment.setDeleted(true);
        appointmentDb.save(appointment);
    }

    @Override
    public List<Date> getNextAvailableDates(UUID doctorId) {
        List<Date> availableDates = new ArrayList<>();
        Calendar current = Calendar.getInstance();

        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);

        int dayLimit = 28;
        int addedDays = 0;

        var doctor = doctorService.getDoctorByIDFromRest(doctorId);

        while (addedDays < dayLimit) {
            int currentDayOfWeek = current.get(Calendar.DAY_OF_WEEK);

            if (doctor.getSchedules().contains(currentDayOfWeek)) {
                availableDates.add(current.getTime());
                addedDays++;
            }

            current.add(Calendar.DAY_OF_MONTH, 1);
        }

        return availableDates;
    }


    @Override
    public Appointment updateAppointment(Appointment appointment) {
        Appointment getAppointment = getAppointmentById(appointment.getId());
        if (getAppointment != null) {
            getAppointment.setIdDoctor(appointment.getIdDoctor());
            getAppointment.setDate(appointment.getDate());
            getAppointment.setUpdatedAt(new Date());
            log.info("UPDATED BY " + appointment.getUpdatedBy());
            getAppointment.setUpdatedBy(appointment.getUpdatedBy());
            getAppointment.setTotalFee(doctorService.getDoctorByIDFromRest(appointment.getIdDoctor()).getFee());
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

        List<Appointment> allAppointments = appointmentDb.findAllByIsDeletedFalse(Sort.by(Sort.Order.by("doctor.name").ignoreCase()));

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
        List<Appointment> allAppointments = appointmentDb.findAllByIsDeletedFalse(Sort.by(Sort.Order.by("doctor.name").ignoreCase()));

        return allAppointments.stream()
                .filter(appointment -> !appointment.getDate().before(startDate) && !appointment.getDate().after(endDate))
                .collect(Collectors.toList());
    }

    public String generateAppointmentId(Appointment appointment) {
        if (appointment.getDate() == null) {
            throw new IllegalArgumentException("Appointment date cannot be null");
        }

        var doctor = doctorService.getDoctorByIDFromRest(appointment.getIdDoctor());

        String specializationCode = doctor.getSpecialist();

        SimpleDateFormat formatter = new SimpleDateFormat("ddMM");
        String dateCode = formatter.format(appointment.getDate());

        int sequenceNumber = getAppointmentSequenceForDoctor(appointment.getIdDoctor(), appointment.getDate());

        String sequenceCode = String.format("%03d", sequenceNumber);

        return specializationCode + dateCode + sequenceCode;
    }

    private int getAppointmentSequenceForDoctor(UUID idDoctor, Date appointmentDate) {
        List<Appointment> existingAppointments = appointmentDb.findAllByIdDoctorAndDateAndIsDeletedFalse(idDoctor, appointmentDate);

        return existingAppointments.size() + 1;
    }

    public String statusString(int status) {
        return switch (status) {
            case 0 -> "Created";
            case 1 -> "Done";
            case 2 -> "Cancelled";
            default -> "Invalid Status";
        };
    }
}