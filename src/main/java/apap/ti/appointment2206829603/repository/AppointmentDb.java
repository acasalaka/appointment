package apap.ti.appointment2206829603.repository;

import apap.ti.appointment2206829603.model.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentDb extends JpaRepository<Appointment, String> {
    List<Appointment> findAllByIsDeletedFalse(Sort sort);
    Optional<Appointment> findByIdAndIsDeletedFalse(String appointmentId);
}
