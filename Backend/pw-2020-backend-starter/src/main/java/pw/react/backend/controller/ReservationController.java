package pw.react.backend.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.dao.ReservationRepository;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;
import pw.react.backend.model.ReservationDTO;
import pw.react.backend.service.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {
    private final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    private FlatService flatService;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository, ReservationService reservationService, FlatService flatService) {
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.flatService = flatService;
    }

    @PostMapping(path = "")
    public Collection<Reservation> createReservations(@RequestBody Collection<ReservationDTO> reservationsDTOs) {
        List<Reservation> reservations = new ArrayList<Reservation>();
        for (ReservationDTO reservationDTO : reservationsDTOs) {

            Reservation reservation = Reservation.valueOf(reservationDTO);
            reservation = Reservation.valueOf(reservationDTO);

            Flat flat = flatService.findFlatById(reservationDTO.getFlatId());
            logger.error("KAWABUNGA: " + flat.getName());
            logger.error("KAWABUNGA: " + flat.getId());

            reservation.setFlat(flat);

            reservations.add(reservation);
        }

        return reservationRepository.saveAll(reservations);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/{reservationId}")
    public Reservation getReservation(@PathVariable Long reservationId) {
        return reservationRepository.findById(reservationId).orElseGet(() -> Reservation.EMPTY);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "")
    public Collection<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(path = "/{reservationId}")
    public Reservation updateReservation(@PathVariable Long reservationId,
                                  @RequestBody Reservation updatedReservation) {
        Reservation result;
        result = reservationService.updateReservation(reservationId, updatedReservation);
        return result;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(path = "/{reservationId}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long reservationId) {
        boolean deleted = reservationService.deleteReservation(reservationId);
        if (!deleted) {
            return ResponseEntity.badRequest().body(String.format("Reservation with id %s does not exists.", reservationId));
        }
        return ResponseEntity.ok(String.format("Reservation with id %s deleted.", reservationId));
    }
}
