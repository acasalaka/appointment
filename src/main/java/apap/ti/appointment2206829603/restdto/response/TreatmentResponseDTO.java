package apap.ti.appointment2206829603.restdto.response;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.model.Patient;
import apap.ti.appointment2206829603.model.Treatment;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TreatmentResponseDTO {
    private Long id;
    private String name;
    private Long price;
    private Appointment appointments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;
}
