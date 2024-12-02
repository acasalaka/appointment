package apap.tk.appointment50.repository;

import apap.tk.appointment50.model.Appointment;
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
    List<Appointment> findAllByDateBetweenAndIsDeletedFalse(Date startDate, Date endDate, Sort sort);
    List<Appointment> findAllByIdDoctorAndDateAndIsDeletedFalse(UUID idDoctor, Date appointmentDate);
    boolean existsByIdDoctorAndDate(UUID idDoctor, Date date);
}
