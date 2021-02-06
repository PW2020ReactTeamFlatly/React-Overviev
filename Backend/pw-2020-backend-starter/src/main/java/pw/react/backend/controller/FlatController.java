package pw.react.backend.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
import pw.react.backend.utils.PagedResponse;
import org.springframework.data.domain.Page;

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
    public ResponseEntity<String> createFlats(@RequestBody List<Flat> flats) {
        List<Flat> result = flatRepository.saveAll(flats);
        return ResponseEntity.ok(result.stream().map(c -> String.valueOf(c.getId())).collect(joining(",")));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/{flatId}")
    public ResponseEntity<Flat>  getFlat(@PathVariable Long flatId){
        return ResponseEntity.ok(flatRepository.findById(flatId).orElseGet(() -> Flat.EMPTY));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/res/{flatId}")
    public ResponseEntity<Collection<Reservation>> getReservationsByFlatId(@PathVariable Long flatId){
        return ResponseEntity.ok(flatService.getReservationsByFlatId(flatId));
    }

    /*@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "")
    public ResponseEntity<Collection<Flat>> getAllFlats(){
        return ResponseEntity.ok(flatRepository.findAll());
    }*/

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "")
    public ResponseEntity<PagedResponse<Collection<Flat>>> getAllFlats(@RequestParam(required = false) String nameOrCity,
                                                                       @RequestParam(required = false) Boolean filter,
                                                                                     @RequestParam(required = false) Boolean booked,
                                                                                     @RequestParam(defaultValue = "false") boolean sort,
                                                                                     @RequestParam(defaultValue = "0") int page,
                                                                                     @RequestParam(defaultValue = "10") int size){
        //if(authFilter.IsInvalidToken(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

        Pageable paging = PageRequest.of(page, size, sort? Sort.by("city").ascending() : Sort.by("city").descending());
        Page<Flat> pageResult;
        if(nameOrCity != null)
            pageResult = flatRepository.findByNameContainingOrCityContaining(nameOrCity, nameOrCity, paging);
        else {
            pageResult = flatRepository.findAll(paging);
        }
        PagedResponse<Collection<Flat>> response = new PagedResponse<>(pageResult.getContent(),page, size, pageResult.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/page/{pageId}")
    public ResponseEntity<Collection<Flat>> getPage(@PathVariable Long pageId){
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
        return ResponseEntity.ok(result);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/pagedresponse/{pageId}")
    public ResponseEntity<PagedResponse<Collection<Flat>>> getPagedResponse(@PathVariable Long pageId){
        Collection<Flat> flats = flatRepository.findAll();
        ArrayList<Flat> result = new ArrayList<Flat>();

        Iterator<Flat> it = flats.iterator();
        long index = 10 * pageId;
        long i = 0;
        int pageSize = 0;
        while(it.hasNext())
        {
            if(i == index)
            {
                for(int j = 0; j < 10; j++)
                {
                    result.add(it.next());
                    pageSize+=1;
                    if(!it.hasNext())
                        break;;
                }
                break;
            }
            it.next();
            i += 1;
        }
        int pID = pageId.intValue();
        Long repoCount = flatRepository.count();
        int pageCnt = repoCount.intValue();
        pageCnt = pageCnt / 10 + 1;
        return ResponseEntity.ok(new PagedResponse<Collection<Flat>>(result, pID, pageSize, pageCnt));
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
