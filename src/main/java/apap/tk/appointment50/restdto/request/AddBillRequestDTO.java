package apap.tk.appointment50.restdto.request;

import java.util.UUID;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddBillRequestDTO {
//    private UUID id;

    @Nullable
    private String policyId;

    @Nullable
    private String appointmentId;

    @Nullable
    private String reservationId;

    private UUID patientId;
}
