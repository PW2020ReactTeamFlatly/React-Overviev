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
            updatedFlat.setId(id);
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
