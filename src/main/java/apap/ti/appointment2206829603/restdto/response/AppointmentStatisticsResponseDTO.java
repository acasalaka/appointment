package apap.ti.appointment2206829603.restdto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentStatisticsResponseDTO {
        private List<String> listPeriod;
        private List<Integer> totalAppointments;
}
