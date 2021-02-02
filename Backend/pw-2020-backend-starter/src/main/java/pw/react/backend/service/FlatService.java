package pw.react.backend.service;

import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;

import java.util.Collection;

public interface FlatService {
    Flat findFlatById(Long id);
    Flat updateFlat(Long id, Flat updatedFlat);
    boolean deleteFlat(Long flatId);
    Collection<Reservation> getReservationsByFlatId(Long flatId);
}
