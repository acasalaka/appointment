package apap.ti.appointment2206829603.restdto.restservice;


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
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppointmentResponseDTO {
    @Id
    @Column(name = "id", length = 10)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idDoctor", referencedColumnName = "id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPatient", referencedColumnName = "id", nullable = false)
    private Patient patient;

    @NotNull
    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private Date date;

    @NotNull
    @Column(name = "diagnosis", nullable = false)
    private String diagnosis;

    @ManyToMany
    @JoinTable(name = "treatmentOnAppointment", joinColumns = @JoinColumn(name = "idAppointment"),
            inverseJoinColumns = @JoinColumn(name = "idTreatment"))
    private List<Treatment> treatments;

    @NotNull
    @Column(name = "totalFee", nullable = false)
    private Long totalFee;

    @NotNull
    @Column(name = "status", nullable = false)
    private int status;
    // 0: Created
    // 1: Done
    // 2: Cancelled

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdAt", updatable = false, nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updatedAt", nullable = false)
    private Date updatedAt;

    @Column(name = "deletedAt")
    private Date deletedAt;

}
