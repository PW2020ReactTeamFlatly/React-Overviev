package pw.react.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dao.FlatRepository;
import pw.react.backend.dao.ReservationRepository;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping(path = "")
    public Collection<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    @PostMapping(path = "")
    public Collection<Reservation> createReservations(@RequestBody List<Reservation> reservations){
        return reservationRepository.saveAll(reservations);
    }

    @GetMapping(path = "/{reservationId}")
    public Reservation getReservation(@PathVariable Long reservationId){
        return reservationRepository.findById(reservationId).orElseGet(() -> Reservation.EMPTY);
    }

    //@PutMapping(path = "/{flatId}")
    //public Flat updateFlat(@PathVariable Long flatId,
    //                       @RequestBody Flat updatedFlat){
    //    Flat workingFlat;
    //}

}
