package fon.bg.ac.rs.istanisic.dto;

import fon.bg.ac.rs.istanisic.model.UserType;

public record UserDTO (
        String firstName,
        String lastName,
        String username,
        String password,
        UserType role
){
}