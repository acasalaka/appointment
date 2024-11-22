package apap.ti.appointment2206829603.repository;

import apap.ti.appointment2206829603.model.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentDb extends JpaRepository<Appointment, String> {
    List<Appointment> findAllByIsDeletedFalse(Sort sort);
    Optional<Appointment> findByIdAndIsDeletedFalse(String appointmentId);
    List<Appointment> findAllByIdDoctorAndIsDeletedFalse(UUID idDoctor);
    List<Appointment> findAllByIdPatientAndIsDeletedFalse(UUID idPatient);
    List<Appointment> findAllByDateAndIsDeletedFalse(Date date);
    List<Appointment> findAllByIdDoctorAndDateAndIsDeletedFalse(UUID idDoctor, Date appointmentDate);
}
