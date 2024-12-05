package apap.tk.appointment50;

import apap.tk.appointment50.restcontroller.AppointmentRestController;
import apap.tk.appointment50.restdto.request.AddAppointmentRequestRestDTO;
import apap.tk.appointment50.restdto.request.UpdateAppointmentStatusRequestRestDTO;
import apap.tk.appointment50.restdto.request.UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO;
import apap.tk.appointment50.restdto.response.AppointmentResponseDTO;
import apap.tk.appointment50.restdto.response.BaseResponseDTO;
import apap.tk.appointment50.restservice.AppointmentRestService;
import apap.tk.appointment50.repository.AppointmentDb;

import jakarta.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class Appointment50Test {

	@Mock
	private AppointmentRestService appointmentRestService;

	@Mock
	private AppointmentDb appointmentDb;

	@InjectMocks
	private AppointmentRestController appointmentRestController;

	private AppointmentResponseDTO mockAppointmentResponse;
	private String mockDoctorName;
	private String mockPatientName;
	private UUID mockDoctorId;
	private UUID mockPatientId;

	@Before
	public void setUp() {
		mockDoctorName = "Test Doctor";
		mockPatientName = "Test Patient";
		mockDoctorId = UUID.randomUUID();
		mockPatientId = UUID.randomUUID();

		mockAppointmentResponse = new AppointmentResponseDTO();
		mockAppointmentResponse.setId(UUID.randomUUID().toString());
		mockAppointmentResponse.setDoctorName(mockDoctorName);
		mockAppointmentResponse.setPatientName(mockPatientName);
	}

	@Test
	public void testGetAppointment_Success() {
		List<AppointmentResponseDTO> mockAppointments = new ArrayList<>();
		mockAppointments.add(mockAppointmentResponse);

		when(appointmentRestService.getAllAppointments()).thenReturn(mockAppointments);

		ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> response =
				appointmentRestController.listAppointment();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
		assertEquals(mockAppointments, response.getBody().getData());
	}

	@Test
	public void testDetailAppointment_Success() {
		String appointmentId = UUID.randomUUID().toString();
		when(appointmentRestService.getAppointmentById(appointmentId)).thenReturn(mockAppointmentResponse);

		ResponseEntity<?> response = appointmentRestController.detailAppointment(appointmentId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.OK.value(), baseResponse.getStatus());
		assertEquals(mockAppointmentResponse, baseResponse.getData());
	}

	@Test
	public void testDetailAppointment_NotFound() {
		String appointmentId = UUID.randomUUID().toString();
		when(appointmentRestService.getAppointmentById(appointmentId)).thenReturn(null);

		ResponseEntity<?> response = appointmentRestController.detailAppointment(appointmentId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.NOT_FOUND.value(), baseResponse.getStatus());
		assertNull(baseResponse.getData());
	}

	@Test
	public void testAddAppointment_Success() {
		AddAppointmentRequestRestDTO requestDTO = new AddAppointmentRequestRestDTO();
		requestDTO.setDoctorId(mockDoctorId);
		requestDTO.setDate(new Date());

		BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "appointmentDTO");

		when(appointmentDb.existsByIdDoctorAndDate(any(), any())).thenReturn(false);
		when(appointmentRestService.addAppointment(requestDTO)).thenReturn(mockAppointmentResponse);

		ResponseEntity<?> response = appointmentRestController.addAppointment(requestDTO, bindingResult);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.CREATED.value(), baseResponse.getStatus());
		assertEquals(mockAppointmentResponse, baseResponse.getData());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAppointment_DuplicateAppointment() {
		AddAppointmentRequestRestDTO requestDTO = new AddAppointmentRequestRestDTO();
		requestDTO.setDoctorId(mockDoctorId);
		requestDTO.setDate(new Date());

		BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "appointmentDTO");

		when(appointmentDb.existsByIdDoctorAndDate(any(), any())).thenReturn(true);

		appointmentRestController.addAppointment(requestDTO, bindingResult);
	}

	@Test
	public void testAddAppointment_ValidationError() {
		AddAppointmentRequestRestDTO requestDTO = new AddAppointmentRequestRestDTO();
		requestDTO.setDoctorId(mockDoctorId);
		requestDTO.setDate(null); // null date triggers validation error

		BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "appointmentDTO");
		bindingResult.addError(new FieldError("appointmentDTO", "date", "Date is required"));

		when(appointmentDb.existsByIdDoctorAndDate(any(), any())).thenReturn(false);

		ResponseEntity<?> response = appointmentRestController.addAppointment(requestDTO, bindingResult);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.BAD_REQUEST.value(), baseResponse.getStatus());
		assertTrue(baseResponse.getMessage().contains("Date is required"));
	}

	@Test
	public void testDeleteAppointment_Success() {
		String appointmentId = UUID.randomUUID().toString();

		// Mocking the return of a successful delete operation
		apap.tk.appointment50.model.Appointment mockDeletedAppointment = new apap.tk.appointment50.model.Appointment();
		mockDeletedAppointment.setId(appointmentId);

		when(appointmentRestService.deleteAppointment(appointmentId)).thenReturn(mockDeletedAppointment);

		ResponseEntity<?> response = appointmentRestController.deleteAppt(appointmentId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.OK.value(), baseResponse.getStatus());
	}

	@Test
	public void testDeleteAppointment_NotFound() {
		String appointmentId = UUID.randomUUID().toString();
		when(appointmentRestService.deleteAppointment(appointmentId))
				.thenThrow(new EntityNotFoundException("Appointment not found"));

		ResponseEntity<?> response = appointmentRestController.deleteAppt(appointmentId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.NOT_FOUND.value(), baseResponse.getStatus());
		assertTrue(baseResponse.getMessage().contains("Appointment not found"));
	}

	@Test
	public void testUpdateAppointmentStatus_Success() {
		UpdateAppointmentStatusRequestRestDTO requestDTO = new UpdateAppointmentStatusRequestRestDTO();
		requestDTO.setId(mockAppointmentResponse.getId());
		requestDTO.setStatus(1);
		requestDTO.setUpdatedBy("admin");

		BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "appointmentDTO");
		when(appointmentRestService.updateAppointmentStatus(requestDTO)).thenReturn(mockAppointmentResponse);

		ResponseEntity<?> response = appointmentRestController.updateAppointment(requestDTO, bindingResult);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.OK.value(), baseResponse.getStatus());
		assertEquals(mockAppointmentResponse, baseResponse.getData());
	}

	@Test
	public void testUpdateAppointmentStatus_NotFound() {
		UpdateAppointmentStatusRequestRestDTO requestDTO = new UpdateAppointmentStatusRequestRestDTO();
		requestDTO.setId("non-existent-id");
		requestDTO.setStatus(2);
		requestDTO.setUpdatedBy("doctor");

		BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "appointmentDTO");
		when(appointmentRestService.updateAppointmentStatus(requestDTO)).thenReturn(null);

		ResponseEntity<?> response = appointmentRestController.updateAppointment(requestDTO, bindingResult);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.NOT_FOUND.value(), baseResponse.getStatus());
		assertNull(baseResponse.getData());
	}

	@Test
	public void testUpdateAppointmentTreatments_Success() {
		UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO requestDTO =
				new UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO();
		requestDTO.setId(mockAppointmentResponse.getId());
		requestDTO.setDiagnosis("Flu");
		requestDTO.setTreatments(Arrays.asList(1L, 2L));
		requestDTO.setUpdatedBy("doctor");

		BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "appointmentDTO");
		when(appointmentRestService.updateAppointmentTreatments(requestDTO)).thenReturn(mockAppointmentResponse);

		ResponseEntity<?> response = appointmentRestController.updateAppointmentDiagnosis(requestDTO, bindingResult);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.OK.value(), baseResponse.getStatus());
		assertEquals(mockAppointmentResponse, baseResponse.getData());
	}

	@Test
	public void testUpdateAppointmentTreatments_NotFound() {
		UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO requestDTO =
				new UpdateAppointmentDiagnosisAndTreatmentRequestRestDTO();
		requestDTO.setId("non-existent-id");
		requestDTO.setDiagnosis("Cold");
		requestDTO.setTreatments(Arrays.asList(3L, 4L));
		requestDTO.setUpdatedBy("nurse");

		BindingResult bindingResult = new BeanPropertyBindingResult(requestDTO, "appointmentDTO");
		when(appointmentRestService.updateAppointmentTreatments(requestDTO)).thenReturn(null);

		ResponseEntity<?> response = appointmentRestController.updateAppointmentDiagnosis(requestDTO, bindingResult);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		BaseResponseDTO<?> baseResponse = (BaseResponseDTO<?>) response.getBody();
		assertNotNull(baseResponse);
		assertEquals(HttpStatus.NOT_FOUND.value(), baseResponse.getStatus());
		assertNull(baseResponse.getData());
	}

	@Test
	public void testGetAllAppointmentByDoctor_Success() {
		List<AppointmentResponseDTO> mockAppointments = new ArrayList<>();
		mockAppointments.add(mockAppointmentResponse);

		when(appointmentRestService.getAllAppointmentsByIdDoctor(mockDoctorId)).thenReturn(mockAppointments);

		ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> response =
				appointmentRestController.listAppointmentByDoctorId(mockDoctorId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
		assertEquals(mockAppointments, response.getBody().getData());
	}

	@Test
	public void testGetAllAppointmentByDoctor_NotFound() {
		when(appointmentRestService.getAllAppointmentsByIdDoctor(mockDoctorId)).thenReturn(new ArrayList<>());

		ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> response =
				appointmentRestController.listAppointmentByDoctorId(mockDoctorId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testGetAllAppointmentByPatient_Success() {
		List<AppointmentResponseDTO> mockAppointments = new ArrayList<>();
		mockAppointments.add(mockAppointmentResponse);

		when(appointmentRestService.getAllAppointmentsByIdPatient(mockPatientId)).thenReturn(mockAppointments);

		ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> response =
				appointmentRestController.listAppointmentByPatientId(mockPatientId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
		assertEquals(mockAppointments, response.getBody().getData());
	}

	@Test
	public void testGetAllAppointmentByPatient_NotFound() {
		when(appointmentRestService.getAllAppointmentsByIdPatient(mockPatientId)).thenReturn(new ArrayList<>());

		ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> response =
				appointmentRestController.listAppointmentByPatientId(mockPatientId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testGetAllAppointmentByDate_Success() {
		Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + 86400000);
		List<AppointmentResponseDTO> mockAppointments = new ArrayList<>();
		mockAppointments.add(mockAppointmentResponse);

		when(appointmentRestService.getAllAppointmentsByDate(startDate, endDate)).thenReturn(mockAppointments);

		ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> response =
				appointmentRestController.listAppointmentByDate(startDate, endDate);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK.value(), response.getBody().getStatus());
		assertEquals(mockAppointments, response.getBody().getData());
	}

	@Test
	public void testGetAllAppointmentByDate_NotFound() {
		Date startDate = new Date();
		Date endDate = new Date(startDate.getTime() + 86400000);

		when(appointmentRestService.getAllAppointmentsByDate(startDate, endDate)).thenReturn(new ArrayList<>());

		ResponseEntity<BaseResponseDTO<List<AppointmentResponseDTO>>> response =
				appointmentRestController.listAppointmentByDate(startDate, endDate);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
		assertNull(response.getBody().getData());
	}
}
