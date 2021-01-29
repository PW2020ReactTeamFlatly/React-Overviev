package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.dao.ReservationRepository;
import pw.react.backend.model.Company;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;

@Service
class ReservationMainService implements ReservationService {
    private final Logger logger = LoggerFactory.getLogger(CompanyMainService.class);

    private ReservationRepository reservationRepository;

    ReservationMainService() { /*Needed only for initializing spy in unit tests*/}

    @Autowired
    ReservationMainService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        Reservation result = Reservation.EMPTY;
        if (reservationRepository.existsById(id)) {
            updatedReservation.setId(id);
            result = reservationRepository.save(updatedReservation);
            logger.info("Reservation with id {} updated.", id);
        }
        return result;
    }

    @Override
    public boolean deleteReservation(Long reservationId) {
        boolean result = false;
        if (reservationRepository.existsById(reservationId)) {
            reservationRepository.deleteById(reservationId);
            logger.info("Reservation with id {} deleted.", reservationId);
            result = true;
        }
        return result;
    }
}

