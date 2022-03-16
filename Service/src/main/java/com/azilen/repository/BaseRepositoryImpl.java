package com.azilen.repository;

import com.azilen.domain.AbstractAuditingEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;

@NoRepositoryBean
@Slf4j
public class BaseRepositoryImpl<T extends AbstractAuditingEntity, ID extends Serializable>
    extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?>
                                           entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }
}
