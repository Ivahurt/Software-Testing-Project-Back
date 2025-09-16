package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.model.User;
import fon.bg.ac.rs.istanisic.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(UserType role);
}
