package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.dao.FlatRepository;
import pw.react.backend.model.Company;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;

import java.util.Collection;
import java.util.List;

@Service
class FlatMainService implements FlatService {
    private final Logger logger = LoggerFactory.getLogger(FlatMainService.class);

    private FlatRepository flatRepository;

    FlatMainService() { /*Needed only for initializing spy in unit tests*/}

    @Autowired
    FlatMainService(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    @Override
    public Flat updateFlat(Long id, Flat updatedFlat) {
        Flat result = Flat.EMPTY;
        if (flatRepository.existsById(id)) {
            Flat oldFlat = flatRepository.findById(id).orElseGet(() -> Flat.EMPTY);
            updatedFlat.setId(id);
            // if any of updatedFlat fields wasnt set they remain unchanged (care: reserved)
            if(updatedFlat.getName() == null)
                updatedFlat.setName(oldFlat.getName());
            if(updatedFlat.getAvailableFrom() == null)
                updatedFlat.setAvailableFrom(oldFlat.getAvailableFrom());
            if(updatedFlat.getAvailableTo() == null)
                updatedFlat.setAvailableTo(oldFlat.getAvailableTo());
            if(updatedFlat.getPricePerNight() == 0)
                updatedFlat.setPricePerNight(oldFlat.getPricePerNight());
            if(updatedFlat.getCity() == null)
                updatedFlat.setCity(oldFlat.getCity());
            if(updatedFlat.getAddress() == null)
                updatedFlat.setAddress(oldFlat.getAddress());
            if(updatedFlat.getSleeps() == 0)
                updatedFlat.setSleeps(oldFlat.getSleeps());
            if(updatedFlat.getInformation() == null)
                updatedFlat.setInformation(oldFlat.getInformation());
            if(updatedFlat.getRating() == 0)
                updatedFlat.setRating(oldFlat.getRating());
            updatedFlat.setReservations(oldFlat.getReservations());
            result = flatRepository.save(updatedFlat);
            logger.info("Flat with id {} updated.", id);
        }
        return result;
    }

    @Override
    public boolean deleteFlat(Long flatId) {
        boolean result = false;
        if (flatRepository.existsById(flatId)) {
            flatRepository.deleteById(flatId);
            logger.info("Flat with id {} deleted.", flatId);
            result = true;
        }
        return result;
    }

    @Override
    public Collection<Reservation> getReservationsByFlatId(Long flatId) {
        return flatRepository.findById(flatId).get().getReservations();
    }

    @Override
    public Flat findFlatById(Long id) {
        return flatRepository.findById(id).orElseGet(() -> Flat.EMPTY);
    }
}
