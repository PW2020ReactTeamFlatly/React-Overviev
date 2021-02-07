package pw.react.backend.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.appException.UnauthorizedException;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.dao.ReservationRepository;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;
import pw.react.backend.model.ReservationDTO;
import pw.react.backend.service.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {
    private final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;
    private final SecurityProvider securityService;
    private FlatService flatService;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository, ReservationService reservationService, FlatService flatService, SecurityProvider securityService) {
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.flatService = flatService;
        this.securityService = securityService;
    }

    private void logHeaders(@RequestHeader HttpHeaders headers) {
        logger.info("Controller request headers {}",
                headers.entrySet()
                        .stream()
                        .map(entry -> String.format("%s->[%s]", entry.getKey(), String.join(",", entry.getValue())))
                        .collect(joining(","))
        );
    }

    @PostMapping(path = "")
    public ResponseEntity<Collection<Reservation>> createReservations(@RequestHeader HttpHeaders headers, @RequestBody Collection<ReservationDTO> reservationsDTOs) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<Reservation> reservations = new ArrayList<Reservation>();

        Date currentDate = new Date();
        LocalDateTime currentLocalDate = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());

        for (ReservationDTO reservationDTO : reservationsDTOs) {

            Reservation reservation = Reservation.valueOf(reservationDTO);
            reservation = Reservation.valueOf(reservationDTO);

            Flat flat = flatService.findFlatById(reservationDTO.getFlatId());
            if(flat == null)
            {
                throw new UnauthorizedException("Flat with id:" + reservationDTO.getFlatId() + " does not exist.");
            }

            Iterator<Reservation> iterator = flat.getReservations().iterator();
            while (iterator.hasNext()) {
                Reservation res = iterator.next();
                if(res.getStartDateTime().compareTo(reservationDTO.getStartDateTime()) >= 0)
                {
                    if(res.getStartDateTime().compareTo(reservationDTO.getEndDateTime()) <= 0)
                    {
                        throw new UnauthorizedException("There is already a reservation in this time period");
                    }
                }

                if(res.getStartDateTime().compareTo(reservationDTO.getStartDateTime()) <= 0)
                {
                    if(res.getStartDateTime().compareTo(reservationDTO.getEndDateTime()) >= 0)
                    {
                        throw new UnauthorizedException("There is already a reservation in this time period");
                    }
                }

            }

            if(currentLocalDate.compareTo(reservation.getStartDateTime()) > 0 && currentLocalDate.compareTo(reservation.getEndDateTime()) < 0)
                flat.setActive(false);

            reservation.setFlat(flat);
            reservation.setFlatName(flat.getName());

            reservations.add(reservation);
        }

        return ResponseEntity.ok(reservationRepository.saveAll(reservations));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/{reservationId}")
    public ResponseEntity<Reservation> getReservation(@RequestHeader HttpHeaders headers, @PathVariable Long reservationId) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Reservation.EMPTY);
        }

        return ResponseEntity.ok(reservationRepository.findById(reservationId).orElseGet(() -> Reservation.EMPTY));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "")
    public ResponseEntity<Collection<Reservation>> getAllReservations(@RequestHeader HttpHeaders headers) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(path = "/{reservationId}")
    public ResponseEntity<Reservation> updateReservation(@RequestHeader HttpHeaders headers, @PathVariable Long reservationId,
                                  @RequestBody Reservation updatedReservation) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Reservation.EMPTY);
        }

        Reservation result;
        result = reservationService.updateReservation(reservationId, updatedReservation);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(path = "/{reservationId}")
    public ResponseEntity<String> deleteReservation(@RequestHeader HttpHeaders headers, @PathVariable Long reservationId) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Request is unauthorized");
        }
        Reservation reservation = reservationRepository.findById(reservationId).orElseGet(() -> Reservation.EMPTY);
        boolean deleted = reservationService.deleteReservation(reservationId);
        if (!deleted) {
            return ResponseEntity.badRequest().body(String.format("Reservation with id %s does not exists.", reservationId));
        }

        Flat flat = flatService.findFlatById(reservation.getIdFlat());
        Date currentDate = new Date();
        LocalDateTime currentLocalDate = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        if(currentLocalDate.compareTo(reservation.getStartDateTime()) > 0 && currentLocalDate.compareTo(reservation.getEndDateTime()) < 0)
            flat.setActive(true);


        return ResponseEntity.ok(String.format("Reservation with id %s deleted.", reservationId));
    }
}
