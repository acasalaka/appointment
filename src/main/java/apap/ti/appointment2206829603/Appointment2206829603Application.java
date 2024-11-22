package apap.ti.appointment2206829603;

import apap.ti.appointment2206829603.service.TreatmentService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Appointment2206829603Application {

    public static void main(String[] args) {
        SpringApplication.run(Appointment2206829603Application.class, args);
    }

	@Bean
	@Transactional
	CommandLineRunner run(TreatmentService treatmentService) {
		return args -> {
			treatmentService.insertTreatmentData();
		};
	}
}