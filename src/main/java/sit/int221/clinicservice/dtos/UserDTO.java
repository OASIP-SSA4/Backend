package sit.int221.clinicservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.clinicservice.Role;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer userId;
    private String name;
    private String email;
    private Role role = Role.student;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedOn;
}
