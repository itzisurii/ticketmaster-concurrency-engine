package edu.iCET.repository;

import edu.iCET.model.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :id")
    Seat findSeatForUpdate(@Param("id") Long id);

    List<Seat> findByEventId(Long eventId);
}
