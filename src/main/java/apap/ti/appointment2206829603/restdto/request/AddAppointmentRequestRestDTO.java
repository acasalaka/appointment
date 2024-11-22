package apap.ti.appointment2206829603.restdto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddAppointmentRequestRestDTO {
    private String nik;

    private UUID doctorId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
