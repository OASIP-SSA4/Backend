package sit.int221.clinicservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchUserDTO implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;
    private String password;
    private String email;
}
