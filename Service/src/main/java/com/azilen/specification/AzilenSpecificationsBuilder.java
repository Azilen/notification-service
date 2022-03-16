package com.azilen.specification;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AzilenSpecificationsBuilder<T> {

    protected List<SearchCriteria> criteriaList = Lists.newArrayList();
    protected final Class clazz;

    public AzilenSpecificationsBuilder(Class clazz) {
        this.clazz = clazz;
    }

    public Specification<T> build() {

        List<Specification<T>> specs = new ArrayList<>();

        for (SearchCriteria param : criteriaList) {
            specs.add(new AzilenSpecification<T>(param));
            log.info("Criteria: {}", param);
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = result.and(specs.get(i));
        }
        return result;
    }
}
