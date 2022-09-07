package sit.int221.clinicservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.clinicservice.dtos.MatchUserDTO;
import sit.int221.clinicservice.services.MatchService;

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
}
