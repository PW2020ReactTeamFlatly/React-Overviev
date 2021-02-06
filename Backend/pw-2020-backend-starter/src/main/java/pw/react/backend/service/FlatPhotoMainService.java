package pw.react.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pw.react.backend.appException.InvalidFileException;
import pw.react.backend.appException.ResourceNotFoundException;
import pw.react.backend.dao.CompanyLogoRepository;
import pw.react.backend.dao.FlatPhotoRepository;
import pw.react.backend.model.Flat;
import pw.react.backend.model.FlatPhoto;

import java.io.IOException;

@Service
class FlatPhotoMainService implements FlatPhotoService {

    private final Logger logger = LoggerFactory.getLogger(CompanyLogoService.class);

    private final FlatPhotoRepository repository;

    @Autowired
    public FlatPhotoMainService(FlatPhotoRepository repository) {
        this.repository = repository;
    }

    @Override
    public FlatPhoto storeLogo(long flatId, MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new InvalidFileException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            FlatPhoto newFlatPhoto = new FlatPhoto(fileName, file.getContentType(), flatId, file.getBytes());
            repository.findByFlatId(flatId).ifPresent(flatPhoto -> newFlatPhoto.setId(flatPhoto.getId()));
            return repository.save(newFlatPhoto);
        } catch (IOException ex) {
            throw new InvalidFileException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public FlatPhoto getFlatPhoto(long flatId) {
        return repository.findByFlatId(flatId)
                .orElseThrow(() -> new ResourceNotFoundException("File not found with flatId " + flatId));
    }

    @Override
    public void deleteFlatPhoto(long flatId) {
        repository.deleteByFlatId(flatId);
        logger.info("Photo for the flat with id {} deleted.", flatId);
    }
}
