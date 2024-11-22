package apap.ti.appointment2206829603.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponseDTO <T>{
    private int status;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date timestamp;
    private T data;
}
