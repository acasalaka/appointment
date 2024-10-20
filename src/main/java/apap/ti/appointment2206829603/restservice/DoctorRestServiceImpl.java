package apap.ti.appointment2206829603.restservice;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.repository.DoctorDb;
import apap.ti.appointment2206829603.restdto.response.DoctorResponseDTO;
import apap.ti.appointment2206829603.restdto.response.AppointmentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorRestServiceImpl implements DoctorRestService {
    @Autowired
    DoctorDb doctorDb;

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {

        var listDoctors = doctorDb.findAll();
        var listDoctorResponseDTO = new ArrayList<DoctorResponseDTO>();
        listDoctors.forEach(doctor -> {
            var doctorResponseDTO = doctorToDoctorResponseDTO(doctor);
            listDoctorResponseDTO.add(doctorResponseDTO);
        });

        return listDoctorResponseDTO;
    }

    @Override
    public DoctorResponseDTO getDoctorById(String id) {
        var doctor = doctorDb.findById(id).orElse(null);

        if (doctor == null) {
            return null;
        }

        return doctorToDoctorResponseDTO(doctor);
    }

    @Override
    public DoctorResponseDTO doctorToDoctorResponseDTO(Doctor doctor) {
        var doctorResponseDTO = new DoctorResponseDTO();
        doctorResponseDTO.setId(doctor.getId());
        doctorResponseDTO.setName(doctor.getName());
        doctorResponseDTO.setSpecialist(doctor.getSpecialist());
        doctorResponseDTO.setEmail(doctor.getEmail());
        doctorResponseDTO.setGender(doctor.isGender());
        doctorResponseDTO.setYearsOfExperience(doctor.getYearsOfExperience());
        doctorResponseDTO.setSchedules(doctor.getSchedules());
        doctorResponseDTO.setFee(doctor.getFee());
        doctorResponseDTO.setAppointments(doctor.getAppointments());
        doctorResponseDTO.setCreatedAt(doctor.getCreatedAt());
        doctorResponseDTO.setUpdatedAt(doctor.getUpdatedAt());

        return doctorResponseDTO;
    }
}
