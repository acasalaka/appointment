package apap.ti.appointment2206829603;

import apap.ti.appointment2206829603.model.Doctor;
import apap.ti.appointment2206829603.model.Patient;
import apap.ti.appointment2206829603.service.AppointmentService;
import apap.ti.appointment2206829603.service.DoctorService;
import apap.ti.appointment2206829603.service.PatientService;
import apap.ti.appointment2206829603.service.TreatmentService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class Appointment2206829603Application {

    public static void main(String[] args) {
        SpringApplication.run(Appointment2206829603Application.class, args);
    }

	@Bean
	@Transactional
	CommandLineRunner run(DoctorService doctorService, PatientService patientService, TreatmentService treatmentService, AppointmentService appointmentService) {
		return args -> {
			var faker = new Faker(new Locale("in-ID"));
			var random = new Random();
			var doctor = new Doctor();

			for (int i = 0; i < 3; i++) {
				int specialization = random.nextInt(17);

				doctor.setName(faker.name().fullName());
				doctor.setId(doctor.generateDoctorId(specialization));
				doctor.setSpecialist(specialization);
				doctor.setEmail("fakedoctor@test.com");
				doctor.setGender(random.nextBoolean());
				doctor.setYearsOfExperience(random.nextInt(30));

				List<Integer> schedules = new ArrayList<>();
				int scheduleCount = random.nextInt(7) + 1;
				for (int j = 0; j < scheduleCount; j++) {
					schedules.add(random.nextInt(7) + 1);
				}
				List<Integer> uniqueSchedules = schedules.stream().distinct().collect(Collectors.toList());
				doctor.setSchedules(uniqueSchedules);

				long min = 250000;
				long max = 1000000;
				long randomNumber = (long) (Math.random() * (max - min + 1)) + min;
				doctor.setFee(randomNumber);

				doctorService.addDoctor(doctor);
			}

			var patient = new Patient();

			StringBuilder number = new StringBuilder();
			number.append(random.nextInt(9) + 1);

			for (int i = 0; i < 15; i++) {
				number.append(random.nextInt(10));
			}

			String nik = number.toString();
			patient.setNik(nik);
			patient.setName(faker.name().fullName());
			patient.setGender(random.nextBoolean());
			patient.setEmail("fakepatient@test.com");
			patient.setBirthDate(faker.date().birthday());
			patient.setBirthPlace(faker.address().city());
			patient.setAppointments(new ArrayList<>());
			patientService.createPatient(patient);

			treatmentService.insertTreatmentData();
		};
	}
}