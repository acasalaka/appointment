package apap.ti.appointment2206829603.DTO.request;

import apap.ti.appointment2206829603.model.Treatment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDiagnosisAndTreatmentRequestDTO {
    private String id;
    private String diagnosis;
    private List<Treatment> treatments;
}
