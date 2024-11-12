package apap.ti.appointment2206829603.DTO.request;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.model.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPatientAndAppointmentRequestDTO {
    Patient patient;
    Appointment appointment;
    String doctorId;
    Date date;
}
