package pw.react.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.dao.FlatRepository;
import pw.react.backend.model.Flat;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/flats")
public class FlatController {
    private final FlatRepository flatRepository;

    @Autowired
    public FlatController(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    @GetMapping(path = "")
    public Collection<Flat> getAllFlats(){
        return flatRepository.findAll();
    }

    @PostMapping(path = "")
    public Collection<Flat> createFlats(@RequestBody List<Flat> flats){
        return flatRepository.saveAll(flats);
    }

    @GetMapping(path = "/{flatId}")
    public Flat getFlat(@PathVariable Long flatId){
        return flatRepository.findById(flatId).orElseGet(() -> Flat.EMPTY);
    }
}
