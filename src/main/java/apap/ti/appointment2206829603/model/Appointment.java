package apap.ti.appointment2206829603.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @Column(name = "id", length = 10)
    private String id;

    @NotNull
    @Column(name = "id_doctor")
    private UUID idDoctor;

    @NotNull
    @Column(name = "id_patient")
    private UUID idPatient;

    @NotNull
    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private Date date;

    @NotNull
    @Column(name = "diagnosis", nullable = false)
    private String diagnosis;

//    @ManyToMany
//    @JoinTable(name = "treatment_on_appointment", joinColumns = @JoinColumn(name = "id_appointment"),
//            inverseJoinColumns = @JoinColumn(name = "id_treatment"))
//    private List<Treatment> treatments;

    @NotNull
    @Column(name = "total_fee", nullable = false)
    private Long totalFee;

    @NotNull
    @Column(name = "status", nullable = false)
    private int status;
    // 0: Created
    // 1: Done
    // 2: Cancelled

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
}
