package apap.ti.appointment2206829603.repository;

import apap.ti.appointment2206829603.model.Doctor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorDb extends JpaRepository<Doctor, String> {
    List<Doctor> findAllByDeletedAtIsNull(Sort sort);
    Optional<Doctor> findById(String id);
}
