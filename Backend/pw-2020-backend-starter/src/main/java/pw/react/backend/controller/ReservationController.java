package pw.react.backend.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.dao.ReservationRepository;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;
import pw.react.backend.model.ReservationDTO;
import pw.react.backend.service.CompanyService;
import pw.react.backend.service.FlatService;
import pw.react.backend.service.LogoService;
import pw.react.backend.service.SecurityProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {

    private final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationRepository reservationRepository;
    private FlatService FlatService;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository, FlatService flatService) {
        this.reservationRepository = reservationRepository;
        this.FlatService = flatService;
    }

    @GetMapping(path = "")
    public Collection<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    @PostMapping(path = "")
    public Collection<Reservation> createReservations(@RequestBody List<ReservationDTO> reservationsDTOs){
        List<Reservation> reservations = new ArrayList<Reservation>();

        for (ReservationDTO reservationDTO : reservationsDTOs) {
            Flat flat = FlatService.findFlatById(reservationDTO.FlatId);
            Reservation reservation = Reservation.valueOf(reservationDTO);
           //reservation.setFlat(flat);

            reservations.add(reservation);
        }

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
