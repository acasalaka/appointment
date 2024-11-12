package apap.ti.appointment2206829603.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import apap.ti.appointment2206829603.DTO.request.AddDoctorRequestDTO;
import apap.ti.appointment2206829603.DTO.request.UpdateDoctorRequestDTO;
import apap.ti.appointment2206829603.model.Doctor;
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

import javax.print.Doc;

@Controller
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/doctor/all")
    public String listDoctor(Model model) {
        try {
            var listDoctor = doctorService.getAllDoctors();
            model.addAttribute("listDoctor", listDoctor);
            model.addAttribute("currentPage", "doctor");

            return "viewall-doctor";
        } catch (Exception e) {
            model.addAttribute("title",  "Error viewing Doctor");
            model.addAttribute("isError", true);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("currentPage", "doctor");

            return "response-doctor";
        }
    }
    @GetMapping("/doctor/create")
    public String formAddDoctor(Model model) {
        var doctorDTO = new AddDoctorRequestDTO();

        model.addAttribute("currentPage", "doctor");
        model.addAttribute("doctorDTO", doctorDTO);
        model.addAttribute("listDoctor", doctorService.getAllDoctors());

        return "form-add-doctor";
    }

    @PostMapping("/doctor/create")
    public String addDoctor(@Valid @ModelAttribute("doctorDTO") AddDoctorRequestDTO doctorDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("title",  "Error updating Doctor");
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("responseMessage", "Doctor cannot be created. Please re-check the input.");
            model.addAttribute("isError", true);
            model.addAttribute("currentPage", "doctor");

            return "response-doctor";
        }

        var doctor = new Doctor();

        String id = doctor.generateDoctorId(doctorDTO.getSpecialist());
        String name = doctor.sanitizeName(doctorDTO.getName());

        doctor.setName(name);
        doctor.setId(id);
        doctor.setSpecialist(doctorDTO.getSpecialist());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setGender(doctorDTO.isGender());
        doctor.setYearsOfExperience(doctorDTO.getYearsOfExperience());

        List<Integer> uniqueSchedules = doctorDTO.getSchedules().stream().distinct().collect(Collectors.toList());
        doctorDTO.setSchedules(uniqueSchedules);

        doctor.setSchedules(doctorDTO.getSchedules());
        doctor.setFee(doctorDTO.getFee());

        doctorService.addDoctor(doctor);

        model.addAttribute("title",  "Success Add Doctor");
        model.addAttribute("responseMessage",
                String.format("Doctor %s with ID %s is successfully added.",
                        doctor.getName(), doctor.getId()));
        model.addAttribute("isError", false);
        model.addAttribute("currentPage", "doctor");
        return "response-doctor";
    }

    @PostMapping(value="/doctor/create", params=("addSchedule"))
    public String addRowDoctor(@ModelAttribute AddDoctorRequestDTO addDoctorRequestDTO, Model model){
        if (addDoctorRequestDTO.getSchedules() == null){
            addDoctorRequestDTO.setSchedules(new ArrayList<>());
        }

        if (!addDoctorRequestDTO.getSchedules().contains(null)) {
            addDoctorRequestDTO.getSchedules().add(null);
        }

        model.addAttribute("doctorDTO", addDoctorRequestDTO);
        model.addAttribute("currentPage", "doctor");

        return "form-add-doctor";
    }

    @PostMapping(value="/doctor/create", params=("deleteSchedule"))
    public String deleteRowDoctor(@ModelAttribute AddDoctorRequestDTO addDoctorRequestDTO, @RequestParam("deleteSchedule") int row, Model model){

        addDoctorRequestDTO.getSchedules().remove(row);

        model.addAttribute("doctorDTO", addDoctorRequestDTO);
        model.addAttribute("currentPage", "doctor");

        return "form-add-doctor";
    }

    @GetMapping("/doctor/{id}")
    public String detailDoctor(@PathVariable("id") String id, Model model) {
        var doctor = doctorService.getDoctorById(id);

        model.addAttribute("doctor", doctor);
        model.addAttribute("currentPage", "doctor");

        return "view-doctor";
    }

    @GetMapping("/doctor/{id}/update")
    public String updateDoctor(@PathVariable("id") String id, Model model) {
        var doctor = doctorService.getDoctorById(id);

        var doctorDTO = new UpdateDoctorRequestDTO();

        doctorDTO.setId(id);
        doctorDTO.setName(doctor.getName());
        doctorDTO.setSpecialist(doctor.getSpecialist());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setGender(doctor.isGender());
        doctorDTO.setYearsOfExperience(doctor.getYearsOfExperience());
        List<Integer> uniqueSchedules = doctor.getSchedules().stream().distinct().collect(Collectors.toList());
        doctorDTO.setSchedules(uniqueSchedules);
        doctorDTO.setFee(doctor.getFee());

        model.addAttribute("currentPage", "doctor");
        model.addAttribute("doctorDTO", doctorDTO);

        return "form-update-doctor";
    }

    @PostMapping("/doctor/update")
    public String updateDoctor(@Valid @ModelAttribute("doctor") UpdateDoctorRequestDTO doctorDTO, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("title",  "Error updating Doctor");
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("responseMessage", "Doctor cannot be updated. Please re-check the input.");
            model.addAttribute("isError", true);
            model.addAttribute("currentPage", "doctor");

            return "response-doctor";
        }

        var doctorFromDTO = doctorService.getDoctorById(doctorDTO.getId());


        String name;
        if (doctorDTO.getName().contains("dr.") || doctorDTO.getName().contains("drg.")) {
            if (doctorDTO.getSpecialist() == 0 || doctorDTO.getSpecialist() == 1) {
                name = doctorDTO.getName();
            } else {
                name = doctorDTO.getName() + " " + doctorFromDTO.getDegree(doctorDTO.getSpecialist());
            }
        } else {
            if (doctorDTO.getSpecialist() == 0 || doctorDTO.getSpecialist() == 1) {
                name = doctorFromDTO.getTitle(doctorDTO.getSpecialist()) + " " + doctorDTO.getName();
            } else {
                name = doctorFromDTO.getTitle(doctorDTO.getSpecialist()) + " " + doctorDTO.getName() + " " + doctorFromDTO.getDegree(doctorDTO.getSpecialist());
            }
        }
        doctorFromDTO.setName(name);
        doctorFromDTO.setSpecialist(doctorDTO.getSpecialist());
        doctorFromDTO.setEmail(doctorDTO.getEmail());
        doctorFromDTO.setGender(doctorDTO.isGender());
        doctorFromDTO.setYearsOfExperience(doctorDTO.getYearsOfExperience());
        List<Integer> uniqueSchedules = doctorDTO.getSchedules().stream().distinct().collect(Collectors.toList());
        doctorFromDTO.setSchedules(uniqueSchedules);
        doctorFromDTO.setFee(doctorDTO.getFee());

        model.addAttribute("currentPage", "doctor");
        model.addAttribute("doctorDTO", doctorDTO);

        var doctor = doctorService.updateDoctor(doctorFromDTO);

        model.addAttribute("title",  "Success Update Doctor");
        model.addAttribute("responseMessage", String.format("Doctor %s with ID %s is successfully updated.", doctor.getName(), doctor.getId()));
        model.addAttribute("isError", false);
        model.addAttribute("currentPage", "doctor");

        return "response-doctor";
    }

    @PostMapping(value="/doctor/update", params=("addSchedule"))
    public String updateDoctor(@ModelAttribute UpdateDoctorRequestDTO updateDoctorRequestDTO, Model model){
        if (updateDoctorRequestDTO.getSchedules() == null){
            updateDoctorRequestDTO.setSchedules(new ArrayList<>());
        }

        if (!updateDoctorRequestDTO.getSchedules().contains(null)) {
            updateDoctorRequestDTO.getSchedules().add(null);
        }

        model.addAttribute("doctorDTO", updateDoctorRequestDTO);
        model.addAttribute("currentPage", "doctor");

        return "form-update-doctor";
    }

    @PostMapping(value="/doctor/update", params=("deleteSchedule"))
    public String updateDoctor(@ModelAttribute UpdateDoctorRequestDTO updateDoctorRequestDTO, @RequestParam("deleteSchedule") int row, Model model){

        updateDoctorRequestDTO.getSchedules().remove(row);

        model.addAttribute("doctorDTO", updateDoctorRequestDTO);
        model.addAttribute("currentPage", "doctor");

        return "form-update-doctor";
    }

    @PostMapping("/doctor/delete")
    public String deleteDoctor(@RequestParam("id") String id, Model model) {
        var doctor = doctorService.getDoctorById(id);

        doctorService.deleteDoctor(doctor);


        model.addAttribute("title",  "Success Delete Doctor");
        model.addAttribute("responseMessage",
                String.format("Doctor %s with ID %s is successfully deleted.", doctor.getName(), doctor.getId()));
        model.addAttribute("isError", false);
        model.addAttribute("currentPage", "doctor");
        return "response-doctor";
    }



}