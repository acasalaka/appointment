package apap.ti.appointment2206829603.repository;

import apap.ti.appointment2206829603.model.Patient;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientDb extends JpaRepository<Patient, UUID> {
    List<Patient> findAll(Sort sort);
    Patient findByNik(String nik);
}
