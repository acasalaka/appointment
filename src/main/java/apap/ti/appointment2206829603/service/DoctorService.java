package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DoctorService {
    Doctor addDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor getDoctorById(String id);
    Doctor updateDoctor(Doctor doctor);
    void deleteDoctor(Doctor doctor);
}
