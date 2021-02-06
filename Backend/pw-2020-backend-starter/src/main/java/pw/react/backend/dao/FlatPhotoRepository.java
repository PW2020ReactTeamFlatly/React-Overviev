package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import pw.react.backend.model.FlatPhoto;

import java.util.Optional;

@Transactional
public interface FlatPhotoRepository extends JpaRepository<FlatPhoto, String> {
    Optional<FlatPhoto> findByFlatId(long flatId);
    void deleteByFlatId(long flatId);
}
