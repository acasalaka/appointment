package apap.ti.appointment2206829603.controller;

import apap.ti.appointment2206829603.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/patient/search")
    private String formSearchPatient(Model model) {

        return "form-search-patient";
    }

    @GetMapping("/patient/search/{nik}")
    private String searchPatient(@PathVariable("nik") String nik, Model model) {
        var patient = patientService.getPatientByNIKFromRest(nik);

        if (patient == null) {
            model.addAttribute("nik", nik);
            model.addAttribute("currentPage", "appointment");
            return "response-patient-not-found";
        } else {
            model.addAttribute("patient", patient);
            model.addAttribute("currentPage", "appointment");
            return "response-patient-found";
        }
    }
}