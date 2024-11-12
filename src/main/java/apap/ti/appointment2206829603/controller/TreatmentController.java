package apap.ti.appointment2206829603.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

@Controller
public class TreatmentController {

//    @Autowired
//    AppointmentService appointmentService;

//    @Autowired
//    private DoctorService doctorService;

    @Autowired
    private TreatmentService treatmentService;

//    @Autowired
//    private PatientService patientService;

    @GetMapping("/treatment/all")
    public String listDoctor(Model model) {
        try {
            var listTreatment = treatmentService.getAllTreatments();
            model.addAttribute("listTreatment", listTreatment);
            model.addAttribute("currentPage", "treatment");

            return "viewall-treatment";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("currentPage", "treatment");

            return "response-error-treatment";
        }
    }

}