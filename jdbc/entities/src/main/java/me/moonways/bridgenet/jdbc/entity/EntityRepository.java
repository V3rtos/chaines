package me.moonways.bridgenet.jdbc.entity;

import me.moonways.bridgenet.jdbc.entity.util.search.SearchMarker;

public interface EntityRepository<T> {

    void delete(Long id);

    void delete(T entity);

    void deleteMany(Long... ids);

    @SuppressWarnings("unchecked")
    void deleteMany(T... entities);

    void deleteIf(SearchMarker<T> searchMarker);

    void update(T entity, Long id);

    void updateIf(T entity, SearchMarker<T> searchMarker);

    SingleFuture<EntityID> insert(T entity);

    ListFuture<EntityID> insertMany(T... entities);

    SingleFuture<T> search(Long id);

    SingleFuture<T> searchIf(SearchMarker<T> searchMarker);

    ListFuture<T> searchMany(Long... ids);

    ListFuture<T> searchManyIf(SearchMarker<T> searchMarker);

    ListFuture<T> searchManyIf(int limit, SearchMarker<T> searchMarker);

    ListFuture<T> searchEach(int limit);

    ListFuture<T> searchEach();

    SearchMarker<T> newSearchMarker();
}
