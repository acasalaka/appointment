package apap.tk.appointment50.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAppointmentStatusRequestRestDTO {
    private String id;
    private int status;
}
