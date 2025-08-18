package fon.bg.ac.rs.istanisic.controller;

import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.DeclareWarning;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/all")
    public ResponseEntity<List<CityDTO>> getAll() {
        return ResponseEntity.ok(cityService.getAll());
    }

    @PostMapping
    public ResponseEntity<CityDTO> saveCity(@RequestBody CityDTO cityDTO) throws Exception{
        return ResponseEntity.ok(cityService.saveCity(cityDTO));
    }

    @DeleteMapping("/{postalCode}")
    public ResponseEntity<Void> deleteCity(@PathVariable int postalCode) throws Exception{
        cityService.deleteCity(postalCode);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<CityDTO> alterCity(@Valid @RequestBody CityDTO cityDTO) throws Exception {
        return ResponseEntity.ok(cityService.updateCity(cityDTO));
    }
}
