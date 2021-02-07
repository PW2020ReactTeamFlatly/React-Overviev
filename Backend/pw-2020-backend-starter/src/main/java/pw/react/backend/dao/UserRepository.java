package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;
import pw.react.backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}