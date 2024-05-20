package me.moonways.bridgenet.jdbc.entity.oldest;

import me.moonways.bridgenet.jdbc.entity.util.search.SearchMarker;

import java.util.List;
import java.util.Optional;

public interface EntityRepository<T> {

    void delete(Long id);

    void delete(T entity);

    void deleteMany(Long... ids);

    @SuppressWarnings("unchecked")
    void deleteMany(T... entities);

    void deleteIf(SearchMarker<T> searchMarker);

    void update(T entity, Long id);

    void updateIf(T entity, SearchMarker<T> searchMarker);

    EntityID insert(T entity);

    @SuppressWarnings("unchecked")
    List<EntityID> insertMany(T... entities);

    Optional<T> search(Long id);

    Optional<T> searchIf(SearchMarker<T> searchMarker);

    List<T> searchMany(Long... ids);

    List<T> searchManyIf(SearchMarker<T> searchMarker);

    List<T> searchManyIf(int limit, SearchMarker<T> searchMarker);

    List<T> searchEach(int limit);

    List<T> searchEach();

    SearchMarker<T> newSearchMarker();
}
