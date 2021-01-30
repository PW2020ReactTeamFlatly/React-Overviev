package pw.react.backend.service;

import pw.react.backend.model.Flat;

public interface FlatService {
    Flat findFlatById(Long id);
    Flat updateFlat(Long id, Flat updatedFlat);
    boolean deleteFlat(Long flatId);
}
