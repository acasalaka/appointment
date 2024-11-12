package apap.ti.appointment2206829603.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor")

public class Doctor {
    @Id
    @Column(name = "id", length = 10, unique = true)
    private String id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "specialist", nullable = false)
    private int specialist;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "gender", nullable = false)
    private boolean gender;
    // true: Perempuan
    // false: laki-laki

    @NotNull
    @Column(name = "yearsOfExperience", nullable = false)
    private int yearsOfExperience;

    @ElementCollection
    @CollectionTable(name = "schedules", joinColumns = @JoinColumn(name = "doctorSchedules"))
    @Column(name = "schedules")
    private List<Integer> schedules;

    @NotNull
    @Column(name = "fee", nullable = false)
    private Long fee;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Appointment> appointments;

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

    public String assignGender() {
        return gender ? "Perempuan" : "Laki-laki";
    }

    public String getTitle(int specialist) {
        return switch (specialist) {
            case 1  -> "drg.";
            default -> "dr.";
        };
    }

    public String getDegree(int specialist) {
        return switch (specialist) {
            case 2 -> "Sp.A";
            case 3 -> "Sp.B";
            case 4 -> "Sp.BP-RE";
            case 5 -> "Sp.JP";
            case 6 -> "Sp.KK";
            case 7 -> "Sp.M";
            case 8 -> "Sp.OG";
            case 9 -> "Sp.PD";
            case 10 -> "Sp.P";
            case 11 -> "Sp.THT-KL";
            case 12 -> "Sp.Rad";
            case 13 -> "Sp.KJ";
            case 14 -> "Sp.An";
            case 15 -> "Sp.N";
            case 16 -> "Sp.U";
            default -> "";
        };
    }

    public String getSpecializationCode(int specialist) {
        return switch (specialist) {
            case 1 -> "GGI";
            case 2 -> "ANK";
            case 3 -> "BDH";
            case 4 -> "PRE";
            case 5 -> "JPD";
            case 6 -> "KKL";
            case 7 -> "MTA";
            case 8 -> "OBG";
            case 9 -> "PDL";
            case 10 -> "PRU";
            case 11 -> "THT";
            case 12 -> "RAD";
            case 13 -> "KSJ";
            case 14 -> "ANS";
            case 15 -> "NRO";
            case 16 -> "URO";
            default -> "UMM";
        };
	}

    public String getSpecialistString() {
        return switch (specialist) {
            case 1 -> "Dokter Gigi";
            case 2 -> "Spesialis Anak";
            case 3 -> "Bedah";
            case 4 -> "Bedah Plastik, Rekonstruksi, dan Estetik";
            case 5 -> "Jantung dan Pembuluh Darah";
            case 6 -> "Kulit dan Kelamin";
            case 7 -> "Mata";
            case 8 -> "Obstetri dan Ginekologi";
            case 9 -> "Penyakit Dalam";
            case 10 -> "Paru";
            case 11 -> "Telinga, Hidung, Tenggorokan, Bedah Kepala Leher";
            case 12 -> "Radiologi";
            case 13 -> "Kesehatan Jiwa";
            case 14 -> "Anestesi";
            case 15 -> "Neurologi";
            case 16 -> "Urologi";
            default -> "Dokter Umum";
        };
    }

    public String getName(int specialist) {
        String title = getTitle(this.specialist);
        String degree = getDegree(this.specialist);

        if (!degree.isEmpty()) {
            return title + " " + this.name + ", " + degree;
        }
        return title + " " + this.name;
    }

    public String generateDoctorId(int specialist) {
        String specializationCode = getSpecializationCode(specialist);

        var random = new Random();
        int uniqueNumber = random.nextInt(900) + 100;

        return specializationCode + uniqueNumber;
    }

    public String scheduleString(int schedules) {
        return switch (schedules) {
            case 1 -> "Sunday";
            case 2 -> "Monday";
            case 3 -> "Tuesday";
            case 4 -> "Wednesday";
            case 5 -> "Thursday";
            case 6 -> "Friday";
            case 7 -> "Saturday";
            default -> throw new IllegalStateException("Unexpected value: " + schedules);
        };
    }

    public List<String> getScheduleDayStrings() {
        List<String> dayNames = new ArrayList<>();

        for (Integer schedule : schedules) {
            String dayName = scheduleString(schedule);
            dayNames.add(dayName);
        }

        return dayNames;
    }

    public String sanitizeName(String name) {
        String sanitized = name.replaceAll("(?i)^(dr\\.\\s*|drg\\.\\s*)", "");
        sanitized = sanitized.replaceAll("(?i),\\s*Sp\\.[A-Za-z]*$", "");

        return sanitized.trim();
    }
}