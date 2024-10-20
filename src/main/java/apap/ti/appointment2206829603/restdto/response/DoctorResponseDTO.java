package apap.ti.appointment2206829603.restdto.response;

import java.time.LocalDate;

import apap.ti.appointment2206829603.model.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.temporal.ChronoUnit;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class DoctorResponseDTO {
    private String id;
    private String name;
    private int specialist;
    private String email;
    private boolean gender;
    private int yearsOfExperience;
    private List<Integer> schedules;
    private Long fee;
    private List<Appointment> appointments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date deletedAt;
}