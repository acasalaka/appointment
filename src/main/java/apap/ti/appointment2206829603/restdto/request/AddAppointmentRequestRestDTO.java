package apap.ti.appointment2206829603.restdto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "NIK column must be filled!")
    private String nik;

    @NotBlank(message = "Doctor ID column must be filled!")
    private String doctorId;

    @NotNull(message = "Status column must be filled!")
    private int status;

    @NotNull(message = "Date column must be filled.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
