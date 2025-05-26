package mg.itu.ticketing.repository;

import mg.itu.ticketing.model.Reservation;
import mg.itu.ticketing.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Find reservations by status
    List<Reservation> findByStatus(Status status);
}
