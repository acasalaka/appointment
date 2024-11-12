package apap.ti.appointment2206829603.restservice;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.model.Patient;
import apap.ti.appointment2206829603.repository.AppointmentDb;
import apap.ti.appointment2206829603.repository.DoctorDb;
import apap.ti.appointment2206829603.repository.PatientDb;
import apap.ti.appointment2206829603.restdto.request.AddAppointmentRequestRestDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentResponseDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentStatisticsResponseDTO;
import apap.ti.appointment2206829603.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AppointmentRestServiceImpl implements AppointmentRestService {

    @Autowired
    AppointmentDb appointmentDb;

    @Autowired
    DoctorDb doctorDb;

    @Autowired
    PatientDb patientDb;

    @Override
    public AppointmentStatisticsResponseDTO getAppointmentStatistics(String period, Integer year) {
        var allAppointments = appointmentDb.findAllByDeletedAtIsNull(Sort.by(Sort.Order.by("doctor.name").ignoreCase()));

        int januaryCount = 0;
        int februaryCount = 0;
        int marchCount = 0;
        int aprilCount = 0;
        int mayCount = 0;
        int juneCount = 0;
        int julyCount = 0;
        int augustCount = 0;
        int septemberCount = 0;
        int octoberCount = 0;
        int novemberCount = 0;
        int decemberCount = 0;

        int quartal1 = 0;
        int quartal2 = 0;
        int quartal3 = 0;
        int quartal4 = 0;

        AppointmentStatisticsResponseDTO statisticsResponse = new AppointmentStatisticsResponseDTO();
        statisticsResponse.setListPeriod(new ArrayList<>());
        statisticsResponse.setTotalAppointments(new ArrayList<>());

        Calendar cal = Calendar.getInstance();

        for (Appointment appointment : allAppointments) {
            cal.setTime(appointment.getCreatedAt());
            int appointmentYear = cal.get(Calendar.YEAR);
            int appointmentMonth = cal.get(Calendar.MONTH);

            // Check if the appointment matches the given year
            if (appointmentYear == year) {
                switch (appointmentMonth) {
                    case Calendar.JANUARY:
                        januaryCount++;
                        quartal1++;
                        break;
                    case Calendar.FEBRUARY:
                        februaryCount++;
                        quartal1++;
                        break;
                    case Calendar.MARCH:
                        marchCount++;
                        quartal1++;
                        break;
                    case Calendar.APRIL:
                        aprilCount++;
                        quartal2++;
                        break;
                    case Calendar.MAY:
                        mayCount++;
                        quartal2++;
                        break;
                    case Calendar.JUNE:
                        juneCount++;
                        quartal2++;
                        break;
                    case Calendar.JULY:
                        julyCount++;
                        quartal3++;
                        break;
                    case Calendar.AUGUST:
                        augustCount++;
                        quartal3++;
                        break;
                    case Calendar.SEPTEMBER:
                        septemberCount++;
                        quartal3++;
                        break;
                    case Calendar.OCTOBER:
                        octoberCount++;
                        quartal4++;
                        break;
                    case Calendar.NOVEMBER:
                        novemberCount++;
                        quartal4++;
                        break;
                    case Calendar.DECEMBER:
                        decemberCount++;
                        quartal4++;
                        break;
                    default:
                        break;
                }
            }
        }

        if (period.equals("Monthly")) {
            statisticsResponse.getListPeriod().add("January");
            statisticsResponse.getListPeriod().add("February");
            statisticsResponse.getListPeriod().add("March");
            statisticsResponse.getListPeriod().add("April");
            statisticsResponse.getListPeriod().add("May");
            statisticsResponse.getListPeriod().add("June");
            statisticsResponse.getListPeriod().add("July");
            statisticsResponse.getListPeriod().add("August");
            statisticsResponse.getListPeriod().add("September");
            statisticsResponse.getListPeriod().add("October");
            statisticsResponse.getListPeriod().add("November");
            statisticsResponse.getListPeriod().add("December");

            statisticsResponse.getTotalAppointments().add(januaryCount);
            statisticsResponse.getTotalAppointments().add(februaryCount);
            statisticsResponse.getTotalAppointments().add(marchCount);
            statisticsResponse.getTotalAppointments().add(aprilCount);
            statisticsResponse.getTotalAppointments().add(mayCount);
            statisticsResponse.getTotalAppointments().add(juneCount);
            statisticsResponse.getTotalAppointments().add(julyCount);
            statisticsResponse.getTotalAppointments().add(augustCount);
            statisticsResponse.getTotalAppointments().add(septemberCount);
            statisticsResponse.getTotalAppointments().add(octoberCount);
            statisticsResponse.getTotalAppointments().add(novemberCount);
            statisticsResponse.getTotalAppointments().add(decemberCount);

        } else if (period.equals("Quarterly")) {
            statisticsResponse.getListPeriod().add("Q1");
            statisticsResponse.getListPeriod().add("Q2");
            statisticsResponse.getListPeriod().add("Q3");
            statisticsResponse.getListPeriod().add("Q4");

            statisticsResponse.getTotalAppointments().add(quartal1);
            statisticsResponse.getTotalAppointments().add(quartal2);
            statisticsResponse.getTotalAppointments().add(quartal3);
            statisticsResponse.getTotalAppointments().add(quartal4);
        }

        return statisticsResponse;
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(String id) throws EntityNotFoundException {
        Appointment appointmentToView = appointmentDb.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        return appointmentToAppointmentResponseDTO(appointmentToView);
    }

    @Override
    public AppointmentResponseDTO addAppointment(AddAppointmentRequestRestDTO appointmentDTO) {
        Appointment newAppointment = new Appointment();

        newAppointment.setDate(appointmentDTO.getDate());

        Doctor doctor = doctorDb.findById(appointmentDTO.getDoctorId()).orElse(null);
        newAppointment.setDoctor(doctor);

        Patient patient = patientDb.findByNik(appointmentDTO.getNik());
        newAppointment.setPatient(patient);

        newAppointment.setId(newAppointment.generateAppointmentId());

        newAppointment.setStatus(appointmentDTO.getStatus());
        newAppointment.setCreatedAt(new Date());
        newAppointment.setUpdatedAt(new Date());

        appointmentDb.save(newAppointment);

        return appointmentToAppointmentResponseDTO(newAppointment);
    }

    @Override
    public AppointmentResponseDTO appointmentToAppointmentResponseDTO(Appointment appointment) {
        AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();
        responseDTO.setId(appointment.getId());
        responseDTO.setDoctor(appointment.getDoctor());
        responseDTO.setPatient(appointment.getPatient());
        responseDTO.setDate(appointment.getDate());
        responseDTO.setDiagnosis(appointment.getDiagnosis());
        responseDTO.setTotalFee(appointment.getTotalFee());
        responseDTO.setTreatments(appointment.getTreatments());
        responseDTO.setStatus(appointment.getStatus());
        responseDTO.setCreatedAt(appointment.getCreatedAt());
        responseDTO.setUpdatedAt(appointment.getUpdatedAt());

        return responseDTO;
    }

}
