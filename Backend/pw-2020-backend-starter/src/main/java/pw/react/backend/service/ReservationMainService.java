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
            Reservation oldReservation = reservationRepository.findById(id).orElseGet(() -> Reservation.EMPTY);
            updatedReservation.setId(id);
            updatedReservation.setIdFlat(oldReservation.getIdFlat());
            updatedReservation.setFlat(oldReservation.getFlat());
            if(updatedReservation.getStartDateTime() == null)
                updatedReservation.setStartDateTime(oldReservation.getStartDateTime());
            if(updatedReservation.getEndDateTime() == null)
                updatedReservation.setEndDateTime(oldReservation.getEndDateTime());
            if(updatedReservation.getPrice() == 0)
                updatedReservation.setPrice(oldReservation.getPrice());
            if(updatedReservation.getSleeps() == 0)
                updatedReservation.setSleeps(oldReservation.getSleeps());
            if(updatedReservation.getCustomerName() == null)
                updatedReservation.setCustomerName(oldReservation.getCustomerName());
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

