package apap.ti.appointment2206829603.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddAppointmentRequestRestDTO {
    private String nik;

    private String doctorId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
