package apap.ti.appointment2206829603.DTO.request;

import java.util.List;

import apap.ti.appointment2206829603.model.Doctor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDoctorRequestDTO {
    @Valid
    @NotBlank(message = "Name column cannot be empty.")
    private String name;

    @NotNull(message = "Specialist column cannot be empty.")
    private int specialist;

    @NotBlank(message = "Email column cannot be empty.")
    private String email;

    @NotNull(message = "Gender column cannot be empty.")
    private boolean gender;

    @Min(value = 0, message = "Years of experience must be a positive number.")
    @NotNull(message = "Years of Experience column cannot be empty.")
    private int yearsOfExperience;

    @NotNull(message = "Schedule column cannot be empty.")
    private List<Integer> schedules;

    @Min(value = 0, message = "Fee must be a positive number.")
    @NotNull(message = "Fee column cannot be empty.")
    private Long fee;
}