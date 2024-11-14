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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_doctor", referencedColumnName = "id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_patient", referencedColumnName = "id", nullable = false)
    private Patient patient;

    @NotNull
    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private Date date;

    @NotNull
    @Column(name = "diagnosis", nullable = false)
    private String diagnosis;

    @ManyToMany
    @JoinTable(name = "treatment_on_appointment", joinColumns = @JoinColumn(name = "id_appointment"),
            inverseJoinColumns = @JoinColumn(name = "id_treatment"))
    private List<Treatment> treatments;

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

    public String generateAppointmentId() {
        if (this.date == null) {
            throw new IllegalArgumentException("Appointment date cannot be null");
        }

        Doctor doctor = this.doctor;

        String specializationCode = doctor.getSpecializationCode(doctor.getSpecialist());

        SimpleDateFormat formatter = new SimpleDateFormat("ddMM");
        String dateCode = formatter.format(this.date);

        int sequenceNumber = 1;
        for (Appointment appointment : doctor.getAppointments()) {
            if (appointment.getDate().equals(this.date)) {
                sequenceNumber++;
            }
        }

        String sequenceCode = String.format("%03d", sequenceNumber);

        return specializationCode + dateCode + sequenceCode;
    }

    public String statusString(int status) {
        return switch (status) {
            case 0 -> "Created";
            case 1 -> "Done";
            case 2 -> "Cancelled";
            default -> "Invalid Status";
        };
    }
}
