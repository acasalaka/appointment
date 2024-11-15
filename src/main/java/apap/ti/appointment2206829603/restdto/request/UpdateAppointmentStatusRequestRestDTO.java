package apap.ti.appointment2206829603.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAppointmentStatusRequestRestDTO extends AddAppointmentRequestRestDTO {
    private String id;
    private int status;
}
