package apap.ti.appointment2206829603.restservice;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.repository.AppointmentDb;
import apap.ti.appointment2206829603.restdto.request.AddAppointmentRequestRestDTO;
import apap.ti.appointment2206829603.restdto.request.UpdateAppointmentStatusRequestRestDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentResponseDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentStatisticsResponseDTO;
import apap.ti.appointment2206829603.service.AppointmentService;
import apap.ti.appointment2206829603.service.DoctorService;
import apap.ti.appointment2206829603.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class AppointmentRestServiceImpl implements AppointmentRestService {

    @Autowired
    AppointmentDb appointmentDb;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Override
    public AppointmentStatisticsResponseDTO getAppointmentStatistics(String period, Integer year) {
        var allAppointments = appointmentDb.findAllByIsDeletedFalse(Sort.by(Sort.Order.by("doctor.name").ignoreCase()));

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
    public List<AppointmentResponseDTO> getAllAppointments() {

        Sort sortByName = Sort.by(Sort.Order.by("idDoctor"));
        var listAppointment = appointmentDb.findAllByIsDeletedFalse(sortByName);
        System.out.println("total appointments: " + listAppointment.size());
        var listAppointmentResponseDTO = new ArrayList<AppointmentResponseDTO>();
        listAppointment.forEach(appointment -> {
            var appointmentResponseDTO = appointmentToAppointmentResponseDTO(appointment);
            listAppointmentResponseDTO.add(appointmentResponseDTO);
        });

        return listAppointmentResponseDTO;
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(String id) {
        var appointment = appointmentDb.findByIdAndIsDeletedFalse(id).orElse(null);
        if (appointment == null) {
            return null;
        }

        return appointmentToAppointmentResponseDTO(appointment);
    }

    @Override
    public AppointmentResponseDTO addAppointment(AddAppointmentRequestRestDTO appointmentDTO) {
        Appointment newAppointment = new Appointment();
        newAppointment.setDate(appointmentDTO.getDate());

        newAppointment.setIdDoctor(appointmentDTO.getDoctorId());
        var patient = patientService.getPatientByNIKFromRest(appointmentDTO.getNik());

        newAppointment.setIdPatient(patient.getId());

        newAppointment.setStatus(0);
        newAppointment.setDiagnosis("");
        var doctor = doctorService.getDoctorByIDFromRest(appointmentDTO.getDoctorId());
        newAppointment.setTotalFee(doctor.getFee());
        newAppointment.setCreatedAt(new Date());
        newAppointment.setUpdatedAt(new Date());
        newAppointment.setDeleted(false);
        newAppointment.setId(appointmentService.generateAppointmentId(newAppointment));

        appointmentDb.save(newAppointment);

        return appointmentToAppointmentResponseDTO(newAppointment);
    }

    @Override
    public AppointmentResponseDTO appointmentToAppointmentResponseDTO(Appointment appointment) {
        AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();
        responseDTO.setId(appointment.getId());
        responseDTO.setDoctorId(appointment.getIdDoctor());
        responseDTO.setPatientId(appointment.getIdPatient());
        responseDTO.setDate(appointment.getDate());
        responseDTO.setDiagnosis(appointment.getDiagnosis());
        responseDTO.setTotalFee(appointment.getTotalFee());
        List<String> treatments = new ArrayList<>();
//        for (Treatment treatment : appointment.getTreatments()) {
//            treatments.add(treatment.getName());
//        }
//        responseDTO.setTreatments(treatments);
        responseDTO.setStatus(appointment.getStatus());
        responseDTO.setCreatedAt(appointment.getCreatedAt());
        responseDTO.setUpdatedAt(appointment.getUpdatedAt());

        return responseDTO;
    }

    @Override
    public Appointment deleteAppointment(String id) throws EntityNotFoundException {

        Appointment appointment = appointmentDb.findByIdAndIsDeletedFalse(id).orElse(null);
        if (appointment == null) {
            throw new EntityNotFoundException("Data appointment tidak ditemukan");
        }
        appointmentService.deleteAppointment(appointment);
        return appointment;
    }

    @Override
    public AppointmentResponseDTO updateAppointmentStatus(UpdateAppointmentStatusRequestRestDTO appointmentDTO) {
            Appointment appointment = appointmentDb.findByIdAndIsDeletedFalse(appointmentDTO.getId()).orElse(null);
            if (appointment == null) {
                return null;
            }

            appointment.setId(appointmentDTO.getId());
            var patient = patientService.getPatientByNIKFromRest(appointmentDTO.getNik());
            appointment.setIdPatient(patient.getId());
            var doctor = doctorService.getDoctorByIDFromRest(appointmentDTO.getDoctorId());
            appointment.setIdDoctor(doctor.getId());
            appointment.setDate(appointmentDTO.getDate());
            appointment.setStatus(appointmentDTO.getStatus());

            var updateAppointment = appointmentDb.save(appointment);
            return appointmentToAppointmentResponseDTO(updateAppointment);
        }

    @Override
    public List<AppointmentResponseDTO> getAllAppointmentsByIdDoctor(UUID IdDoctor) {

        Sort sortByName = Sort.by(Sort.Order.by("idDoctor"));
        var listAppointment = appointmentDb.findAllByIdDoctorAndIsDeletedFalse(IdDoctor);
        System.out.println("total appointments: " + listAppointment.size());
        var listAppointmentResponseDTO = new ArrayList<AppointmentResponseDTO>();
        listAppointment.forEach(appointment -> {
            var appointmentResponseDTO = appointmentToAppointmentResponseDTO(appointment);
            listAppointmentResponseDTO.add(appointmentResponseDTO);
        });

        return listAppointmentResponseDTO;
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointmentsByIdPatient(UUID IdPatient) {

        Sort sortByName = Sort.by(Sort.Order.by("idDoctor"));
        var listAppointment = appointmentDb.findAllByIdPatientAndIsDeletedFalse(IdPatient);
        System.out.println("total appointments: " + listAppointment.size());
        var listAppointmentResponseDTO = new ArrayList<AppointmentResponseDTO>();
        listAppointment.forEach(appointment -> {
            var appointmentResponseDTO = appointmentToAppointmentResponseDTO(appointment);
            listAppointmentResponseDTO.add(appointmentResponseDTO);
        });

        return listAppointmentResponseDTO;
    }
}