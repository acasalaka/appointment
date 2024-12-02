package apap.tk.appointment50;

import apap.tk.appointment50.service.TreatmentService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Appointment50Application {

    public static void main(String[] args) {
        SpringApplication.run(Appointment50Application.class, args);
    }

	@Bean
	@Transactional
	CommandLineRunner run(TreatmentService treatmentService) {
		return args -> {
			treatmentService.insertTreatmentData();
		};
	}
}