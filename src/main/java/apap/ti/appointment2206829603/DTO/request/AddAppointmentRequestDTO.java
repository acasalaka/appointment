package apap.ti.appointment2206829603.DTO.request;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddAppointmentRequestDTO {
    private String nik;

    @NotNull(message = "Doctor ID column must be filled!")
    private String doctorId;

    @NotNull(message = "Date column must be filled.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}