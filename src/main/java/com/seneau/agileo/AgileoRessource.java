package com.seneau.agileo;

import com.seneau.agileo.dtos.CongeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for managing {@link com.seneau.domain.Conge}.
 */

@RestController
@CrossOrigin
@RequestMapping("/api/agileo/conges")
public class AgileoRessource {

    private final AgileoService agileoService;

    public AgileoRessource(AgileoService agileoService) {
        this.agileoService = agileoService;
    }

    @GetMapping("")
    public List<CongeDTO> getAllConge()  {
        return agileoService.getAllConge();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CongeDTO> getAllCongeByAgentMatricule(@PathVariable Long id, @RequestParam String type)  {
        return  new ResponseEntity<>(agileoService.getCongeByAgent(id,type), HttpStatus.OK);
    }
}
