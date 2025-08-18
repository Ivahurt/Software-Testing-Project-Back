package fon.bg.ac.rs.istanisic.controller;

import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.dto.PersonUpdateDTO;
import fon.bg.ac.rs.istanisic.service.PersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/all")
    public ResponseEntity<List<PersonDTO>> getAll() {
        return ResponseEntity.ok(personService.getAll());
    }

    @PostMapping
    public ResponseEntity<PersonDTO> savePerson(@RequestBody PersonDTO personDTO) throws Exception {
        System.out.println(personDTO);
        return ResponseEntity.ok(personService.savePerson(personDTO));
    }

    @DeleteMapping("/{uniqueId}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long uniqueId) throws Exception {
        personService.deletePerson(uniqueId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<PersonDTO> alterPerson(@Valid  @RequestBody
                                                 PersonUpdateDTO personDTO) throws Exception {
        return ResponseEntity.ok(
                personService.updatePerson(personDTO));
    }
}
