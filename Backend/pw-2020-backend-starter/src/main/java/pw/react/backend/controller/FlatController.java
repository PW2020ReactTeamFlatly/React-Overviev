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
import pw.react.backend.dao.FlatRepository;
import pw.react.backend.model.Company;
import pw.react.backend.model.Flat;
import pw.react.backend.model.Reservation;
import pw.react.backend.service.CompanyService;
import pw.react.backend.service.FlatService;
import pw.react.backend.service.SecurityProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping(path = "/flats")
public class FlatController {
    private final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    private final FlatRepository flatRepository;
    private final FlatService flatService;

    @Autowired
    public FlatController(FlatRepository flatRepository, FlatService flatService) {
        this.flatRepository = flatRepository;
        this.flatService = flatService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "")
    public Collection<Flat> createFlats(@RequestBody List<Flat> flats){
        List<Flat> result = flatRepository.saveAll(flats);
        return result;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/{flatId}")
    public Flat getFlat(@PathVariable Long flatId){
        return flatRepository.findById(flatId).orElseGet(() -> Flat.EMPTY);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/res/{flatId}")
    public Collection<Reservation> getReservationsByFlat(@PathVariable Long flatId){
        return flatService.getReservationsByFlatId(flatId);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "")
    public Collection<Flat> getAllFlats(){
        return flatRepository.findAll();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/page/{pageId}")
    public Collection<Flat> getPage(@PathVariable Long pageId){
        Collection<Flat> flats = flatRepository.findAll();
        ArrayList<Flat> result = new ArrayList<Flat>();

        Iterator<Flat> it = flats.iterator();
        long index = 10 * pageId;
        long i = 0;
        while(it.hasNext())
        {
            if(i == index)
            {
                for(int j = 0; j < 10; j++)
                {
                    result.add(it.next());
                    if(!it.hasNext())
                        break;;
                }
                break;
            }
            it.next();
            i += 1;
        }


        return result;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping(path = "/{flatId}")
    public Flat updateFlat(@PathVariable Long flatId,
                           @RequestBody Flat updatedFlat) {
        Flat result;
        result = flatService.updateFlat(flatId, updatedFlat);
        return result;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(path = "/{flatId}")
    public ResponseEntity<String> deleteFlat(@PathVariable Long flatId) {
            boolean deleted = flatService.deleteFlat(flatId);
            if (!deleted) {
                return ResponseEntity.badRequest().body(String.format("Flat with id %s does not exists.", flatId));
            }
            return ResponseEntity.ok(String.format("Flat with id %s deleted.", flatId));
    }
}
