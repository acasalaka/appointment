package apap.ti.appointment2206829603.restservice;

import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.restdto.response.AppointmentResponseDTO;
import apap.ti.appointment2206829603.restdto.response.DoctorResponseDTO;

import java.util.List;

public interface DoctorRestService {
    List<DoctorResponseDTO> getAllDoctors();
    DoctorResponseDTO getDoctorById(String id);
    DoctorResponseDTO doctorToDoctorResponseDTO(Doctor doctor);
}
