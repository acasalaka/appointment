package apap.ti.appointment2206829603.controller;

import apap.ti.appointment2206829603.DTO.request.AddAppointmentRequestDTO;
import apap.ti.appointment2206829603.DTO.request.AddDiagnosisAndTreatmentRequestDTO;
import apap.ti.appointment2206829603.DTO.request.AddPatientAndAppointmentRequestDTO;
import apap.ti.appointment2206829603.DTO.request.UpdateAppointmentRequestDTO;
import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.model.Patient;
import apap.ti.appointment2206829603.model.Treatment;
import apap.ti.appointment2206829603.service.AppointmentService;
import apap.ti.appointment2206829603.service.DoctorService;
import apap.ti.appointment2206829603.service.PatientService;
import apap.ti.appointment2206829603.service.TreatmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    PatientService patientService;

    @Autowired
    TreatmentService treatmentService;

    @GetMapping("/")
    public String home(Model model) {
        List<Appointment> todaysAppointments = appointmentService.getTodaysAppointments();
        model.addAttribute("todaysAppointments", todaysAppointments.size());
        model.addAttribute("currentPage", "home");
        return "home";
    }

    @GetMapping("/appointment/all")
    public String listAppointment(Model model) {
        try {
            List<Appointment> listAppointment = appointmentService.getAllAppointments();
            model.addAttribute("listAppointment", listAppointment);
            model.addAttribute("currentPage", "appointment");

            return "viewall-appointment";
        } catch (Exception e) {

            model.addAttribute("title",  "Error viewing Doctor");
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("currentPage", "appointment");

            return "response-appointment";
        }
    }

    @GetMapping("/appointment/{id}/update")
    public String updateAppointment(@PathVariable("id") String id, Model model) {
        var appointment = appointmentService.getAppointmentById(id);

        var appointmentDTO = new UpdateAppointmentRequestDTO();
        Doctor doctor = doctorService.getDoctorById(appointment.getDoctor().getId());

        appointmentDTO.setId(id);
        appointmentDTO.setDoctorId(doctor.getId());
        appointmentDTO.setDate(appointment.getDate());

        List<Doctor> doctors = doctorService.getAllDoctors();

        model.addAttribute("listDoctor", doctors);
        model.addAttribute("currentPage", "appointment");
        model.addAttribute("appointmentDTO", appointmentDTO);

        return "form-update-appointment";
    }

    @PostMapping("/appointment/update")
    public String updateDoctor(@ModelAttribute("appointment") UpdateAppointmentRequestDTO appointmentDTO, BindingResult result, Model model) throws ParseException {
        if (result.hasErrors()) {
            model.addAttribute("title",  "Error updating Appointment");
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("responseMessage", "Appointment cannot be updated. Please re-check the input.");
            model.addAttribute("isError", true);
            model.addAttribute("currentPage", "appointment");

            return "response-appointment";
        }

        var appointmentFromDTO = appointmentService.getAppointmentById(appointmentDTO.getId());

        appointmentFromDTO.setDoctor(doctorService.getDoctorById(appointmentDTO.getDoctorId()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(dateFormat.format(appointmentDTO.getDate()));
        appointmentFromDTO.setDate(parsedDate);
        appointmentFromDTO.setUpdatedAt(new Date());

        model.addAttribute("id", appointmentDTO.getId());
        model.addAttribute("currentPage", "appointment");
        model.addAttribute("appointmentDTO", appointmentDTO);

        var appointment = appointmentService.updateAppointment(appointmentFromDTO);

        model.addAttribute("title",  "Success Update Appointment");
        model.addAttribute("responseMessage", String.format("Appointment with ID %s is successfully updated.", appointment.getId()));
        model.addAttribute("isError", false);
        model.addAttribute("currentPage", "appointment");

        return "response-appointment";
    }

    @PostMapping("/appointment/{id}/done")
    public String markAppointmentAsDone(@PathVariable("id") String id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment != null) {
            appointment.setStatus(1);
            appointmentService.updateAppointment(appointment);
        }
        model.addAttribute("title",  "Appointment Status set to Done");
        model.addAttribute("responseMessage", "Appointment status is set to done.");
        model.addAttribute("isError", false);
        model.addAttribute("currentPage", "appointment");
        return "response-appointment";
    }

    @PostMapping("/appointment/{id}/cancel")
    public String markAppointmentAsCancel(@PathVariable("id") String id, Model model) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment != null) {
            appointment.setStatus(2);
            appointmentService.updateAppointment(appointment);
        }
        model.addAttribute("title",  "Appointment Status set to Cancel");
        model.addAttribute("responseMessage", "Appointment status is set to cancel.");
        model.addAttribute("isError", false);
        model.addAttribute("currentPage", "appointment");
        return "response-appointment";
    }

    @GetMapping("/appointment/{nik}/create")
    public String createAppointmentForm(@PathVariable("nik") String nik, Model model) {
        Patient patient = patientService.getPatientByNIK(nik);
        var appointmentDTO = new AddAppointmentRequestDTO();
        model.addAttribute("patient", patient);
        model.addAttribute("name", patient.getName());
        model.addAttribute("appointmentDTO", appointmentDTO);
        model.addAttribute("listDoctor", doctorService.getAllDoctors());

        return "form-add-appointment";
    }

    @PostMapping("/appointment/{nik}/create")
    public String addAppointment(@Valid @PathVariable("nik") String nik, @ModelAttribute("appointmentDTO") AddAppointmentRequestDTO appointmentDTO,
                                 BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());

            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("responseMessage", "Failed to add appointment. Check again!");
            model.addAttribute("isError", true);
            return "response-appointment";
        }

        Appointment appointment = new Appointment();

        Patient patient = patientService.getPatientByNIK(nik);
        Doctor doctor = doctorService.getDoctorById(appointmentDTO.getDoctorId());

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setTotalFee(doctor.getFee());
        appointment.setDiagnosis("");
        appointment.setStatus(0);
        appointment.setDate(appointmentDTO.getDate());
        appointment.setId(appointment.generateAppointmentId());

        appointmentService.createAppointment(appointment);

        model.addAttribute("patient", patient);
        model.addAttribute("responseMessage",
                String.format("Appointment %s for patient %s successfully added.", appointment.getId(), appointment.getPatient().getName()));

        return "response-appointment";
    }

    @GetMapping("/appointment/available-dates")
    @ResponseBody
    public List<String> getAvailableDates(@RequestParam("doctorId") String doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        List<Date> availableDates = appointmentService.getNextAvailableDates(doctor);

        return availableDates.stream()
                .map(date -> new SimpleDateFormat("EEEE, dd MMMM yyyy").format(date))
                .collect(Collectors.toList());
    }

    @GetMapping("/appointment/create-with-patient")
    public String createAppointmentWithPatientForm(Model model) {
        var appointmentDTO = new AddPatientAndAppointmentRequestDTO();
        model.addAttribute("appointmentDTO", appointmentDTO);
        model.addAttribute("listDoctor", doctorService.getAllDoctors());
        model.addAttribute("currentPage", "appointment");

        return "form-add-patient-appointment";
    }

    @PostMapping("/appointment/create-with-patient")
    public String addAppointmentAndPatient(@Valid @ModelAttribute AddPatientAndAppointmentRequestDTO appointmentDTO, BindingResult result, Model model) throws ParseException {
        if (result.hasErrors()) {
            model.addAttribute("title",  "Error updating Appointment");
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("responseMessage", "New Patient Created. However, the new Appointment cannot be created. Please re-check the input.");
            model.addAttribute("isError", true);
            model.addAttribute("currentPage", "appointment");

            return "response-appointment";
        }
        var patient = new Patient();
        patient.setName(appointmentDTO.getPatient().getName());
        patient.setNik(appointmentDTO.getPatient().getNik());
        patient.setEmail(appointmentDTO.getPatient().getEmail());
        patient.setGender(appointmentDTO.getPatient().isGender());
        patient.setBirthDate(appointmentDTO.getPatient().getBirthDate());
        patient.setBirthPlace(appointmentDTO.getPatient().getBirthPlace());

        patientService.createPatient(patient);

        Doctor doctor = doctorService.getDoctorById(appointmentDTO.getDoctorId());

        var appointment = new Appointment();
        appointment.setDate(appointmentDTO.getDate());
        appointment.setDoctor(doctor);
        appointment.setId(appointment.generateAppointmentId());
        appointment.setTotalFee(doctor.getFee());
        appointment.setDiagnosis("");
        appointment.setStatus(0);
        appointment.setTreatments(null);
        appointment.setPatient(patient);

        appointmentService.createAppointment(appointment);

        model.addAttribute("responseMessage",
                String.format("Appointment %s for patient %s successfully added.", appointment.getId(), appointment.getPatient().getName()));

        model.addAttribute("currentPage", "appointment");
        return "response-appointment";
    }

    @GetMapping("/appointment/{id}")
    public String detailAppointment(@PathVariable("id") String id, Model model) {
        var appointment = appointmentService.getAppointmentById(id);

        model.addAttribute("appointment", appointment);
        model.addAttribute("currentPage", "appointment");

        return "view-appointment";
    }

    @GetMapping("/appointment/{id}/note")
    public String addDiagnosisAndTreatmentNote(@PathVariable("id") String id, Model model) {
        var appointment = appointmentService.getAppointmentById(id);
        System.out.println(appointment);
        System.out.println(appointment.getStatus());
        System.out.println(appointment.statusString(appointment.getStatus()));

        AddDiagnosisAndTreatmentRequestDTO appointmentDTO = new AddDiagnosisAndTreatmentRequestDTO();

        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setDiagnosis(appointment.getDiagnosis());
        appointmentDTO.setTreatments(appointment.getTreatments() != null ? appointment.getTreatments() : new ArrayList<>());

        model.addAttribute("appointment", appointment);
        model.addAttribute("appointmentDTO", appointmentDTO);
        model.addAttribute("treatmentList", treatmentService.getAllTreatments());
        model.addAttribute("currentPage", "appointment");
        return "form-appointment-note";
    }

    @PostMapping("/appointment/note")
    public String updateAppointmentDiagnosis(@Valid @RequestParam("id") String id,
                                             @ModelAttribute("appointmentDTO") AddDiagnosisAndTreatmentRequestDTO appointmentDTO,
                                             BindingResult bindingResult, Model model) throws ParseException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("responseMessage", "Error updating appointment. Please check your input.");
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "response-appointment";
        }

        var appointment = appointmentService.getAppointmentById(id);

        if (appointment == null) {
            model.addAttribute("responseMessage", "Appointment not found.");
            return "response-appointment";
        }

        appointment.setDiagnosis(appointmentDTO.getDiagnosis());

        if (appointmentDTO.getTreatments() != null) {
            appointment.setTreatments(appointmentDTO.getTreatments());
        }

        appointment.setUpdatedAt(new Date());

        appointmentService.updateAppointment(appointment);

        model.addAttribute("responseMessage", String.format("Appointment with ID %s has been successfully updated.", appointment.getId()));

        return "response-appointment";
    }


    @PostMapping(value="/appointment/note", params=("addTreatment"))
    public String addRowTreatment(@RequestParam("id") String id, @ModelAttribute AddDiagnosisAndTreatmentRequestDTO appointmentDTO, Model model) {
        var appointment = appointmentService.getAppointmentById(id);

//        if (appointment == null) {
//            model.addAttribute("responseMessage", "Appointment not found.");
//            return "response-appointment";
//        }

        if (appointmentDTO.getTreatments() == null) {
            appointmentDTO.setTreatments(new ArrayList<>());
        }

        appointmentDTO.getTreatments().add(new Treatment());

        model.addAttribute("appointment", appointment);
        model.addAttribute("appointmentDTO", appointmentDTO);
        model.addAttribute("treatmentList", treatmentService.getAllTreatments());
        model.addAttribute("currentPage", "appointment");

        return "form-appointment-note";
    }

    @PostMapping(value="/appointment/note", params=("deleteTreatment"))
    public String deleteRowTreatment(@RequestParam("id") String id, @ModelAttribute AddDiagnosisAndTreatmentRequestDTO appointmentDTO, @RequestParam("deleteTreatment") int row, Model model) {
        var appointment = appointmentService.getAppointmentById(id);

//        if (appointment == null) {
//            model.addAttribute("responseMessage", "Appointment not found.");
//            return "response-appointment";
//        }

        if (appointmentDTO.getTreatments() == null) {
            appointmentDTO.setTreatments(new ArrayList<>());
        }

        appointmentDTO.getTreatments().remove(row);

        model.addAttribute("appointment", appointment);
        model.addAttribute("appointmentDTO", appointmentDTO);
        model.addAttribute("treatmentList", treatmentService.getAllTreatments());
        model.addAttribute("currentPage", "appointment");

        return "form-appointment-note";
    }

    @PostMapping("/appointment/delete")
    public String deleteAppointment(@RequestParam("id") String id, Model model) {
        var appointment = appointmentService.getAppointmentById(id);

        appointmentService.deleteAppointment(appointment);

        model.addAttribute("title",  "Success Delete Appointment");
        model.addAttribute("responseMessage",
                String.format("Appointment with ID %s is successfully deleted.", appointment.getId()));
        model.addAttribute("isError", false);
        model.addAttribute("currentPage", "appointment");
        return "response-appointment";
    }

    @GetMapping("/appointment/stat")
    public String getAppointmentStat(Model model) {
        model.addAttribute("currentPage", "appointment");
        return "view-appointment-statistics";
    }
}