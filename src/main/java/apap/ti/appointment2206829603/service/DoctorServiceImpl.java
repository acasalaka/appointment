package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.repository.DoctorDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    DoctorDb doctorDb;

    @Override
    public Doctor addDoctor(Doctor doctor) {
        return doctorDb.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        Sort sortByName = Sort.by(Sort.Order.by("name").ignoreCase());
        return doctorDb.findAllByDeletedAtIsNull(sortByName);
    };

    @Override
    public Doctor getDoctorById(String idDoctor) {
        List<Doctor> activeDoctors = getAllDoctors();

        for (Doctor doctor : activeDoctors) {
            if (doctor.getId().equals(idDoctor)) {
                return doctor;
            }
        }

        return null;
    }

    @Override
    public Doctor updateDoctor(Doctor doctor) {
        Doctor getDoctor = getDoctorById(doctor.getId());
        if (getDoctor != null) {
            String name = getDoctor.sanitizeName(doctor.getName());
            getDoctor.setName(name);
            getDoctor.setSpecialist(doctor.getSpecialist());
            getDoctor.setEmail(doctor.getEmail());
            getDoctor.setGender(doctor.isGender());
            getDoctor.setYearsOfExperience(doctor.getYearsOfExperience());
            getDoctor.setSchedules(doctor.getSchedules());
            getDoctor.setFee(doctor.getFee());
            doctorDb.save(getDoctor);

            return getDoctor;
        }

        return null;
    }

    @Override
    public void deleteDoctor(Doctor doctor) {
        doctor.setDeletedAt(new Date());
        doctorDb.save(doctor);
    }
}
