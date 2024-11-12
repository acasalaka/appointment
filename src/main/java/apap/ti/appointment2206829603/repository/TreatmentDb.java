package apap.ti.appointment2206829603.repository;

import apap.ti.appointment2206829603.model.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreatmentDb extends JpaRepository<Treatment, Long> {
    List<Treatment> findAll();
    Optional<Treatment> findById(Long id);
}
