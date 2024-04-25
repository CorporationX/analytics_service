package faang.school.analytics.mapper;

import java.util.List;

public interface AbstractMapper< E, D> {

    E toEntity(D dto);

    D toDto (E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDtoList(List<E> entityList);

}

