package fon.bg.ac.rs.istanisic.repository;

import fon.bg.ac.rs.istanisic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
