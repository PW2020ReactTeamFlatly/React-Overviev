package pw.react.backend.service;

import org.springframework.web.multipart.MultipartFile;
import pw.react.backend.model.FlatPhoto;

public interface FlatPhotoService {
    FlatPhoto storeLogo(long flatId, MultipartFile file);
    FlatPhoto getFlatPhoto(long flatId);
    void deleteFlatPhoto(long flatId);
}
