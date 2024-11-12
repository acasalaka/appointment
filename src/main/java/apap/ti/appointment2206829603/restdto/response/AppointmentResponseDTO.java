package apap.ti.appointment2206829603.restdto.response;

import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.model.Patient;
import apap.ti.appointment2206829603.model.Treatment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentResponseDTO {
    private String id;
    private Doctor doctor;
    private Patient patient;
    private Date date;
    private String diagnosis;
    private List<Treatment> treatments;
    private Long totalFee;
    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date deletedAt;

}
