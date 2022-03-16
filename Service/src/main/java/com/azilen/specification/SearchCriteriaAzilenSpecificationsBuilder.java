package com.azilen.specification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Slf4j
public class SearchCriteriaAzilenSpecificationsBuilder<T> extends AzilenSpecificationsBuilder<T> {

    public SearchCriteriaAzilenSpecificationsBuilder(List<SearchCriteria> searchCriteria, Class clazz) {

        super(clazz);
        this.criteriaList = searchCriteria;
    }

    public Specification<T> build() {

        return super.build();
    }
}
