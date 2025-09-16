package fon.bg.ac.rs.istanisic.controller;

import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.dto.UserDTO;
import fon.bg.ac.rs.istanisic.model.User;
import fon.bg.ac.rs.istanisic.service.CityService;
import fon.bg.ac.rs.istanisic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() throws Exception{
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() throws Exception{
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<Void> saveUser(@RequestBody UserDTO user) throws Exception{
        userService.saveUser(user);
        return ResponseEntity.noContent().build();
    }
}
