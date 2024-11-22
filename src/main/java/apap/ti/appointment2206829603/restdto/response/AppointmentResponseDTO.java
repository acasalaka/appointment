package apap.ti.appointment2206829603.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentResponseDTO {
    private String id;
    private UUID doctorId;
    private UUID patientId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    private String diagnosis;
//    private List<String> treatments;
    private Long totalFee;
    private int status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
}
