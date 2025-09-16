package fon.bg.ac.rs.istanisic.converter;

import fon.bg.ac.rs.istanisic.dto.CityDTO;
import fon.bg.ac.rs.istanisic.dto.UserDTO;
import fon.bg.ac.rs.istanisic.model.City;
import fon.bg.ac.rs.istanisic.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements DTOEntityConverter<UserDTO, User>{
    @Override
    public User toEntity(UserDTO userDTO) {
        return userDTO == null ? null:
                User.builder()
                        .firstName(userDTO.firstName())
                        .lastName(userDTO.lastName())
                        .username(userDTO.username())
                        .password(userDTO.password())
                        .role(userDTO.role()).build();
    }

    @Override
    public UserDTO toDto(User user) {
        return user == null ? null:
                new UserDTO(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getRole()
                );
    }
}