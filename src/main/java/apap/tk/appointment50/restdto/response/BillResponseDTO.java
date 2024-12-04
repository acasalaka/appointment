package apap.tk.appointment50.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillResponseDTO {
    private String status;
    private UUID id;
    private String appointmentId;
    private Long appointmentFee;
    private String policyId;
    private Double policyFee;
    private String reservationId;
    private Double reservationFee;
    private UUID patientId;
    private long subtotal;
}
