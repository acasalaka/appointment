package apap.tk.appointment50.restdto.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddBillRequestDTO {
    private UUID id;

    @Nullable
    private String policyId;

    @Nullable
    private String appointmentId;

    @Nullable
    private String reservationId;

    private UUID patientId;
}
