package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.dao.CompanyRepository;
import pw.react.backend.dao.FlatRepository;
import pw.react.backend.model.Company;
import pw.react.backend.model.Flat;

import java.util.List;

@Service
class FlatMainService implements FlatService {
    private final Logger logger = LoggerFactory.getLogger(FlatMainService.class);

    private FlatRepository repository;

    FlatMainService() { /*Needed only for initializing spy in unit tests*/}

    @Autowired
    FlatMainService(FlatRepository repository) {
        this.repository = repository;
    }

    @Override
    public Flat findFlatById(long id) {
        return repository.findById(id).orElseGet(() -> Flat.EMPTY);
    }
}
