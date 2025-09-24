package fon.bg.ac.rs.istanisic.controller;

import fon.bg.ac.rs.istanisic.dto.PaymentDTO;
import fon.bg.ac.rs.istanisic.dto.PersonDTO;
import fon.bg.ac.rs.istanisic.dto.PersonResidenceHistoryDTO;
import fon.bg.ac.rs.istanisic.dto.PersonUpdateDTO;
import fon.bg.ac.rs.istanisic.model.PersonResidenceHistory;
import fon.bg.ac.rs.istanisic.service.PaymentService;
import fon.bg.ac.rs.istanisic.service.PersonResidenceService;
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
    private final PersonResidenceService personResidenceService;

    private final PaymentService paymentService;

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

    @GetMapping("/{uniqueId}")
    public ResponseEntity<List<PersonResidenceHistoryDTO>> getPersonResidenceHistory(@PathVariable Long uniqueId) throws Exception{
        return ResponseEntity.ok(personResidenceService.getPersonResidenceHistory(uniqueId));
    }

    @PostMapping("/payment")
    public ResponseEntity<Void> savePersonPayment(@RequestBody PaymentDTO paymentDTO) throws Exception{
        paymentService.savePersonPayment(paymentDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payment/{uniqueId}")
    public ResponseEntity<List<PaymentDTO>> getPersonPayments(@PathVariable Long uniqueId) throws Exception{
        return ResponseEntity.ok(paymentService.getPersonPayments(uniqueId));
    }

    @GetMapping("/sum-payments/{uniqueId}")
    public ResponseEntity<PersonDTO> getPersonSumPayments(@PathVariable Long uniqueId) throws Exception{
        return ResponseEntity.ok(personService.getPersonSumPayments(uniqueId));
    }

    @GetMapping("/age/{uniqueId}")
    public ResponseEntity<PersonDTO> getPersonAgeInMonths(@PathVariable Long uniqueId) throws Exception{
        return ResponseEntity.ok(personService.getPersonAgeInMonths(uniqueId));
    }
}
