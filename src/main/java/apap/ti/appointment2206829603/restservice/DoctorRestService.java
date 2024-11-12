package apap.ti.appointment2206829603.restservice;

import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.restdto.response.DoctorResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface DoctorRestService {
    List<DoctorResponseDTO> getAllDoctors();
    DoctorResponseDTO getDoctorById(String id);
    DoctorResponseDTO doctorToDoctorResponseDTO(Doctor doctor);
}
