package apap.ti.appointment2206829603.DTO.request;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAppointmentRequestDTO extends AddAppointmentRequestDTO{
    @Valid
    private String id;

    @NotBlank(message = "Doctor cannot be null!")
    private UUID doctorId;

    @NotNull(message = "Doctor schedule cannot be null!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}