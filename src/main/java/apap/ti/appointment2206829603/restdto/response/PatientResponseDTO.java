package apap.ti.appointment2206829603.restdto.response;

import apap.ti.appointment2206829603.model.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponseDTO {
    private UUID id = UUID.randomUUID();
    private String nik;
    private String name;
    private boolean gender;
    private String email;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String birthPlace;
    private List<Appointment> appointments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;
}
