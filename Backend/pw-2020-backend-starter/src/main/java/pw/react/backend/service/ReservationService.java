package pw.react.backend.service;

import pw.react.backend.model.Reservation;

public interface ReservationService {
    Reservation updateReservation(Long id, Reservation updatedReservation);
    boolean deleteReservation(Long reservationId);
}

