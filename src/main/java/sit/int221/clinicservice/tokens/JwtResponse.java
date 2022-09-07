package sit.int221.clinicservice.tokens;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @AllArgsConstructor @Setter
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private String title;
    private String message;
    private final String token;
}
