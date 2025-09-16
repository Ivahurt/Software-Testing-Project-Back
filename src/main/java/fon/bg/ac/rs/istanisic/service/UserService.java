package fon.bg.ac.rs.istanisic.service;

import fon.bg.ac.rs.istanisic.converter.UserConverter;
import fon.bg.ac.rs.istanisic.dto.UserDTO;
import fon.bg.ac.rs.istanisic.model.User;
import fon.bg.ac.rs.istanisic.model.UserType;
import fon.bg.ac.rs.istanisic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public List<User> getAll() throws Exception{
        return userRepository.findAll();
    }

    public List<User> getAllUsers() throws Exception{
        return userRepository.findByRole(UserType.Korisnik);
    }

    public void saveUser(UserDTO user) throws Exception{
        userRepository.save(userConverter.toEntity(user));
    }
}
