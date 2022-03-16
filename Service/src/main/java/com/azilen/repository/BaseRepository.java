package com.azilen.repository;

import com.azilen.domain.AbstractAuditingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T extends AbstractAuditingEntity, ID extends Serializable>
    extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

   /* Optional<T> findByName(String name);

    void clear2ndLevelCache();

    Statistics getCacheStatistics();

    <KEY> Map<KEY, Long> groupByAndCount(String attribute, Specification<T> where);*/
}
