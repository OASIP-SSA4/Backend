package sit.int221.clinicservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.clinicservice.dtos.MatchUserDTO;
import sit.int221.clinicservice.services.MatchService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api")
public class MatchController {
    private final MatchService service;

    public MatchController(MatchService service){
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<Object>createAuthenticationToken(@RequestBody MatchUserDTO matchUserDTO){
        return service.match(matchUserDTO);
    }

    @GetMapping("/refresh")
    public ResponseEntity<Object> refreshLogin(@Valid HttpServletRequest request){
        return service.refreshToken(request);
    }

    @PostMapping("/match")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity userLoginDTO(@Valid @RequestBody MatchUserDTO matchUserDTO) {
        return service.matchCheck(matchUserDTO);
    }
}
