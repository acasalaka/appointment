package apap.tk.appointment50.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO {
    private String id;
    private String diagnosis;
    private List<Long> treatments;
    private String updatedBy;
}
