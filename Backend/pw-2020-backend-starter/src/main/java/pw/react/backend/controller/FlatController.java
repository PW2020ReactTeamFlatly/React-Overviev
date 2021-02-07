package pw.react.backend.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pw.react.backend.appException.UnauthorizedException;
import pw.react.backend.dao.FlatPhotoRepository;
import pw.react.backend.dao.FlatRepository;
import pw.react.backend.model.*;
import pw.react.backend.service.FlatPhotoService;
import pw.react.backend.service.FlatService;
import pw.react.backend.service.SecurityProvider;
import pw.react.backend.utils.PagedResponse;
import org.springframework.data.domain.Page;
import pw.react.backend.web.UploadFileResponse;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping(path = "/flats")
public class FlatController {
    private final Logger logger = LoggerFactory.getLogger(CompanyController.class);
    private final FlatRepository flatRepository;
    private final FlatService flatService;
    private FlatPhotoService flatPhotoService;
    private FlatPhotoRepository flatPhotoRepository;
    private final SecurityProvider securityService;

    @Autowired
    public FlatController(FlatRepository flatRepository,
                          FlatService flatService,
                          FlatPhotoRepository flatPhotoRepository,
                          FlatPhotoService flatPhotoService,
                            SecurityProvider securityService) {
        this.flatRepository = flatRepository;
        this.flatService = flatService;
        this.flatPhotoRepository = flatPhotoRepository;
        this.flatPhotoService = flatPhotoService;
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "")
    public ResponseEntity<String> createFlats(@RequestHeader HttpHeaders headers, @RequestBody List<Flat> flats) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access to resources.");
        }
        Date currentDate = new Date();
        LocalDateTime currentLocalDate = LocalDateTime.ofInstant(currentDate.toInstant(), ZoneId.systemDefault());
        for(Flat flat : flats)
        {
            if(currentLocalDate.compareTo(flat.getAvailableFrom()) > 0 && currentLocalDate.compareTo(flat.getAvailableTo()) < 0)
                flat.setActive(true);
            else
                flat.setActive(false);
        }
        List<Flat> result = flatRepository.saveAll(flats);
        return ResponseEntity.ok(result.stream().map(c -> String.valueOf(c.getId())).collect(joining(",")));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/{flatId}")
    public ResponseEntity<Flat>  getFlat(@RequestHeader HttpHeaders headers, @PathVariable Long flatId){
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Flat.EMPTY);
        }

        return ResponseEntity.ok(flatRepository.findById(flatId).orElseGet(() -> Flat.EMPTY));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/res/{flatId}")
    public ResponseEntity<Collection<Reservation>> getReservationsByFlatId(@RequestHeader HttpHeaders headers, @PathVariable Long flatId){
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(flatService.getReservationsByFlatId(flatId));
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "")
    public ResponseEntity<PagedResponse<Collection<Flat>>> getAllFlats(@RequestHeader HttpHeaders headers,
                                                                       @RequestParam(required = false) String nameOrCity,
                                                                       @RequestParam(required = false,defaultValue = "inactive") String filter,
                                                                       @RequestParam(defaultValue = "false") boolean sort,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size){
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Pageable paging = PageRequest.of(page, size, sort? Sort.by("city").ascending() : Sort.by("city").descending());
        Page<Flat> pageResult;
        if (filter == "active")
        {
            if (nameOrCity != null)
                pageResult = flatRepository.findByNameContainingOrCityContainingAndActive(nameOrCity, nameOrCity,true, paging);
            else
                pageResult = flatRepository.findByActive(true, paging);
        }
        else
        {
            if(nameOrCity != null)
                pageResult = flatRepository.findByNameContainingOrCityContaining(nameOrCity, nameOrCity, paging);
            else
                pageResult = flatRepository.findAll(paging);
        }
        PagedResponse<Collection<Flat>> response = new PagedResponse<>(pageResult.getContent(),page, size, pageResult.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/page/{pageId}")
    public ResponseEntity<Collection<Flat>> getPage(@RequestHeader HttpHeaders headers, @PathVariable Long pageId){
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

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
    public ResponseEntity<PagedResponse<Collection<Flat>>> getPagedResponse(@RequestHeader HttpHeaders headers, @PathVariable Long pageId){
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

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
    public ResponseEntity<Flat> updateFlat(@RequestHeader HttpHeaders headers,
                           @PathVariable Long flatId,
                           @RequestBody Flat updatedFlat) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Flat.EMPTY);
        }

        Flat result;
        result = flatService.updateFlat(flatId, updatedFlat);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(path = "/{flatId}")
    public ResponseEntity<String> deleteFlat(@RequestHeader HttpHeaders headers, @PathVariable Long flatId) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        boolean deleted = flatService.deleteFlat(flatId);
        if (!deleted) {
            return ResponseEntity.badRequest().body(String.format("Flat with id %s does not exists.", flatId));
        }
            return ResponseEntity.ok(String.format("Flat with id %s deleted.", flatId));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/{flatId}/photo")
    public ResponseEntity<UploadFileResponse> uploadFlatPhoto(@RequestHeader HttpHeaders headers, @PathVariable Long flatId,
                                                     @RequestParam("file") MultipartFile file) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        FlatPhoto flatPhoto = flatPhotoService.storeFlatPhoto(flatId,file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/flats/" + flatId + "/photo/")
                .path(flatPhoto.getFileName())
                .toUriString();

        return ResponseEntity.ok(new UploadFileResponse(
                flatPhoto.getFileName(), fileDownloadUri, file.getContentType(), file.getSize()
        ));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/{flatId}/photo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody byte[] getPhoto(@RequestHeader HttpHeaders headers, @PathVariable Long flatId) {
        logHeaders(headers);
        throw new UnauthorizedException("Request is unauthorized");


        //FlatPhoto flatPhoto = flatPhotoService.getFlatPhoto(flatId);
        //return flatPhoto.getData();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/{flatId}/photo2")
    public ResponseEntity<Resource> getPhoto2(@RequestHeader HttpHeaders headers, @PathVariable Long flatId) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        FlatPhoto flatPhoto = flatPhotoService.getFlatPhoto(flatId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(flatPhoto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + flatPhoto.getFileName() + "\"")
                .body(new ByteArrayResource(flatPhoto.getData()));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping(value = "/{flatId}/photo")
    public ResponseEntity<String> removePhoto(@RequestHeader HttpHeaders headers, @PathVariable String flatId) {
        logHeaders(headers);
        if (!securityService.isAuthorized(headers))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        flatPhotoService.deleteFlatPhoto(Long.parseLong(flatId));
        return ResponseEntity.ok().body(String.format("Photo for the flat with id %s removed.", flatId));
    }
}
