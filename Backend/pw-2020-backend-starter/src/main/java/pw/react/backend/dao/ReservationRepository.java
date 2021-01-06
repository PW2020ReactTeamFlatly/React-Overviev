package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
