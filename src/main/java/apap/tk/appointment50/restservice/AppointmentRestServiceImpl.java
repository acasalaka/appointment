package apap.tk.appointment50.restservice;

import apap.tk.appointment50.model.Appointment;
import apap.tk.appointment50.model.Treatment;
import apap.tk.appointment50.repository.AppointmentDb;
import apap.tk.appointment50.restdto.request.AddAppointmentRequestRestDTO;
import apap.tk.appointment50.restdto.request.UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO;
import apap.tk.appointment50.restdto.request.UpdateAppointmentStatusRequestRestDTO;
import apap.tk.appointment50.restdto.response.AppointmentResponseDTO;
import apap.tk.appointment50.restdto.response.BillResponseDTO;
import apap.tk.appointment50.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
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

    @Autowired
    TreatmentService treatmentService;

    @Autowired
    BillService billService;

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
        List<Treatment> treatments = new ArrayList<>();
        newAppointment.setTreatments(treatments);
        var doctor = doctorService.getDoctorByIDFromRest(appointmentDTO.getDoctorId());
        newAppointment.setTotalFee(doctor.getFee());
        newAppointment.setCreatedAt(new Date());
        newAppointment.setUpdatedAt(new Date());
        newAppointment.setDeleted(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        newAppointment.setCreatedBy(username);
        newAppointment.setUpdatedBy(username);
        newAppointment.setId(appointmentService.generateAppointmentId(newAppointment));

        BillResponseDTO billDTO = new BillResponseDTO();
        billDTO.setAppointmentId(newAppointment.getId());
        billDTO.setPatientId(newAppointment.getIdPatient());

        try {
            billService.createBillByAppointmentID(billDTO);
            System.out.println("create bill with appointment id " + newAppointment.getId() + "with patient ID " + newAppointment.getIdPatient());
        } catch (Exception e) {
            System.err.println("Failed to create bill: " + e.getMessage());
        }
        appointmentDb.save(newAppointment);

        return appointmentToAppointmentResponseDTO(newAppointment);
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
            appointment.setStatus(appointmentDTO.getStatus());
            appointment.setUpdatedBy(appointmentDTO.getUpdatedBy());

            log.info("UPDATED BY " + appointmentDTO.getUpdatedBy());

            var updateAppointment = appointmentDb.save(appointment);
            return appointmentToAppointmentResponseDTO(updateAppointment);
    }

    @Override
    public AppointmentResponseDTO updateAppointmentTreatments(UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO appointmentDTO) {
        Appointment appointment = appointmentDb.findByIdAndIsDeletedFalse(appointmentDTO.getId()).orElse(null);
        if (appointment == null) {
            return null;
        }

        appointment.setId(appointmentDTO.getId());
        var doctor = doctorService.getDoctorByIDFromRest(appointment.getIdDoctor());
        appointment.setDiagnosis(appointmentDTO.getDiagnosis());

        long totalFee = doctor.getFee();
        List<Long> treatmentIds = appointmentDTO.getTreatments();
        List<Treatment> treatments = new ArrayList<>();
        for (Long treatmentId : treatmentIds) {
            Treatment treatment = treatmentService.getTreatmentById(treatmentId);
            if (treatment != null) {
                treatments.add(treatment);
                totalFee += treatment.getPrice();
            }
        }
        appointment.setTotalFee(totalFee);
        appointment.setTreatments(treatments);
        appointment.setUpdatedBy(appointmentDTO.getUpdatedBy());

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

    @Override
    public List<AppointmentResponseDTO> getAllAppointmentsByDate(Date startDate, Date endDate) {
        Sort sortByIdDoctor = Sort.by(Sort.Order.asc("idDoctor"));

        var listAppointment = appointmentDb.findAllByDateBetweenAndIsDeletedFalse(startDate, endDate, sortByIdDoctor);

        var listAppointmentResponseDTO = new ArrayList<AppointmentResponseDTO>();
        listAppointment.forEach(appointment -> {
            var appointmentResponseDTO = appointmentToAppointmentResponseDTO(appointment);
            listAppointmentResponseDTO.add(appointmentResponseDTO);
        });

        return listAppointmentResponseDTO;
    }

    @Override
    public AppointmentResponseDTO appointmentToAppointmentResponseDTO(Appointment appointment) {
        AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();
        responseDTO.setId(appointment.getId());
        var doctor = doctorService.getDoctorByIDFromRest(appointment.getIdDoctor());
        responseDTO.setDoctorName(doctor.getName());
        var patient = patientService.getPatientByIDFromRest(appointment.getIdPatient());
        responseDTO.setPatientName(patient.getName());
        responseDTO.setDate(appointment.getDate());
        responseDTO.setDiagnosis(appointment.getDiagnosis());
        responseDTO.setTotalFee(appointment.getTotalFee());
        List<String> treatments = new ArrayList<>();
        for (Treatment treatment : appointment.getTreatments()) {
            treatments.add(treatment.getName());
        }
        responseDTO.setTreatments(treatments);
        responseDTO.setStatus(appointmentService.statusString(appointment.getStatus()));
        responseDTO.setCreatedAt(appointment.getCreatedAt());
        responseDTO.setUpdatedAt(appointment.getUpdatedAt());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        responseDTO.setCreatedBy(username);
        responseDTO.setUpdatedBy(appointment.getUpdatedBy());

        return responseDTO;
    }
}