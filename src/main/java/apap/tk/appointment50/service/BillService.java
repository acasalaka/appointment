package apap.tk.appointment50.service;

import apap.tk.appointment50.restdto.response.BillResponseDTO;

import java.util.UUID;

public interface BillService {
    BillResponseDTO createBillByAppointmentID(String appointmentId, UUID patientId);
}
