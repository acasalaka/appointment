package apap.ti.appointment2206829603.service;

import apap.ti.appointment2206829603.model.Appointment;
import apap.ti.appointment2206829603.repository.AppointmentDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentDb appointmentDb;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private AppointmentServiceImpl appointmentService;

    private List<Appointment> appointmentList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        appointmentService = new AppointmentServiceImpl(appointmentDb);

        appointmentList = new ArrayList<>();

        try {
            Appointment appointment1 = new Appointment();
            appointment1.setDate(dateFormat.parse("2024-10-10"));
            Appointment appointment2 = new Appointment();
            appointment2.setDate(dateFormat.parse("2024-10-15"));
            Appointment appointment3 = new Appointment();
            appointment3.setDate(dateFormat.parse("2024-10-20"));

            appointmentList.add(appointment1);
            appointmentList.add(appointment2);
            appointmentList.add(appointment3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllAppointmentsInDateRange() throws ParseException {
        Date startDate = dateFormat.parse("2024-10-09");
        Date endDate = dateFormat.parse("2024-10-18");

        when(appointmentDb.findAll()).thenReturn(appointmentList);

        List<Appointment> result = appointmentService.getAppointmentsInDateRange(startDate, endDate);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(appointment -> {
            try {
                return appointment.getDate().equals(dateFormat.parse("2024-10-10"));
            } catch (ParseException e) {
                return false;
            }
        }));
        assertTrue(result.stream().anyMatch(appointment -> {
            try {
                return appointment.getDate().equals(dateFormat.parse("2024-10-15"));
            } catch (ParseException e) {
                return false;
            }
        }));

        verify(appointmentDb, times(1)).findAll();
    }
}
