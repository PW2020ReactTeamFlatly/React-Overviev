package pw.react.backend.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.model.Flat;

public interface FlatRepository extends JpaRepository<Flat, Long> {
    Page<Flat> findByNameContainingOrCityContaining(String name, String city, Pageable pageable);
    Page<Flat> findByNameContainingOrCityContainingAndActive(String name, String city, Boolean reserved, Pageable pageable);
    Page<Flat> findByActive(Boolean reserved, Pageable pageable);
}
