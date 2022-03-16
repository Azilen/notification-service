package com.azilen.specification;

import com.azilen.web.rest.errors.InternalServerErrorException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.azilen.specification.AzilenSpecificationConstant.*;

public class AzilenSpecification<T> implements Specification<T> {

    private final SearchCriteria criteria;

    public AzilenSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(OP_GT)) {
            Path expression = root.get(criteria.getKey());
            if(criteria.getSuffixOperation().equals(SUFFIX_DATE_GT) || criteria.getSuffixOperation().equals(SUFFIX_DATETIME_GT)) {
                return builder.greaterThan(root.get(criteria.getKey()), Instant.parse(criteria.getValue().toString()));
            }
            return builder.greaterThan(expression, criteria.getValue().toString());

        } else if (criteria.getOperation().equalsIgnoreCase(OP_LT)) {
            if(criteria.getSuffixOperation().equals(SUFFIX_DATE_LT) || criteria.getSuffixOperation().equals(SUFFIX_DATETIME_LT)) {
                return builder.lessThan(root.get(criteria.getKey()), Instant.parse(criteria.getValue().toString()));
            }

            return builder.lessThan(
                root.get(criteria.getKey()), criteria.getValue().toString());

        } else if (criteria.getOperation().equalsIgnoreCase(OP_GTEQ)) {
            if(criteria.getSuffixOperation().equals(SUFFIX_DATE_GTEQ) || criteria.getSuffixOperation().equals(SUFFIX_DATETIME_GTEQ)) {
                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), Instant.parse(criteria.getValue().toString()));
            }
            return builder.greaterThanOrEqualTo(
                root.get(criteria.getKey()), criteria.getValue().toString());

        } else if (criteria.getOperation().equalsIgnoreCase(OP_LTEQ)) {
            if(criteria.getSuffixOperation().equals(SUFFIX_DATE_LTEQ) || criteria.getSuffixOperation().equals(SUFFIX_DATETIME_LTEQ)) {
                return builder.lessThanOrEqualTo(root.get(criteria.getKey()), Instant.parse(criteria.getValue().toString()));
            }
            return builder.lessThanOrEqualTo(
                root.get(criteria.getKey()), criteria.getValue().toString());


        } else if (criteria.getOperation().equalsIgnoreCase(OP_EQUAL)) {


            Path<Object> objectPath = getObjectPath(root);

            return builder.equal(objectPath, getSingleValue(objectPath));

        } else if (criteria.getOperation().equalsIgnoreCase(OP_NOT_EQUAL)) {

            Path<Object> objectPath = getObjectPath(root);

            return builder.notEqual(objectPath, getSingleValue(objectPath));

        } else if (criteria.getOperation().equalsIgnoreCase(OP_IN)) {
            Path<Object> objectPath = getObjectPath(root);

            return builder.in(objectPath).value(getListValue(objectPath));

        } else if (criteria.getOperation().equalsIgnoreCase(OP_NOT_IN)) {


            Path<Object> objectPath = getObjectPath(root);
            return builder.in(objectPath).value(getListValue(objectPath)).not();


        } else if (criteria.getOperation().equalsIgnoreCase(OP_LIKE)) {

            if (root.get(criteria.getKey()).getJavaType() == String.class) {

                return builder.like(
                    builder.lower(root.get(criteria.getKey())), "%" + StringUtils.lowerCase(String.valueOf(criteria.getValue())) + "%");
            } else {

                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

    private Object getSingleValue(Path<? extends Object> objectPath) {

        if (objectPath.getJavaType() == UUID.class) {

            return UUID.fromString(criteria.getValue().toString());

        } else if (objectPath.getJavaType() == Boolean.class) {

            return BooleanUtils.toBoolean(criteria.getValue().toString());

        } else if (objectPath.getJavaType() == Long.class) {

            return NumberUtils.toLong(criteria.getValue().toString());

        } else if (objectPath.getJavaType() == Instant.class) {

            return Instant.parse(criteria.getValue().toString());

        } else if (objectPath.getJavaType().isEnum()) {

            Class<?> javaType = objectPath.getJavaType();
            Enum value = null;

            try {

                value = Enum.valueOf((Class<? extends Enum>) Class.forName(javaType.getCanonicalName()), criteria.getValue().toString());

            } catch (ClassNotFoundException e) {

                throw new InternalServerErrorException("Error in search criteria: " + e.getMessage());
            }

            return value;

        } else {

            return criteria.getValue();
        }
    }

    private Object getListValue(Path<Object> objectPath) {

        List<String> values = (List<String>) criteria.getValue();

        System.out.println("JAVA type" + objectPath.getJavaType());

        if (objectPath.getJavaType() == UUID.class) {

            return values.parallelStream().map(UUID::fromString).collect(Collectors.toList());

        } else if (objectPath.getJavaType() == Boolean.class) {

            return values.parallelStream().map(BooleanUtils::toBoolean).collect(Collectors.toList());

        } else if (objectPath.getJavaType() == Long.class) {

            return values.parallelStream().map(NumberUtils::toLong).collect(Collectors.toList());

        } else if (objectPath.getJavaType() == List.class) {

            return values.parallelStream().collect(Collectors.toList());

        }else if (objectPath.getJavaType().isEnum()) {

            Class<?> javaType = objectPath.getJavaType();

            return values.parallelStream().map(value ->
                {
                    try {
                        return Enum.valueOf((Class<? extends Enum>) Class.forName(javaType.getCanonicalName()), value);
                    } catch (ClassNotFoundException e) {
                        throw new InternalServerErrorException("Error in search criteria: " + e.getMessage());
                    }
                }
            ).collect(Collectors.toList());

        } else {

            return values;
        }
    }


    private Path<Object> getObjectPath(Root<T> root) {

        Path<Object> objectPath = null;

        if (StringUtils.contains(criteria.getKey(), ".")) {

            String[] keys = StringUtils.split(criteria.getKey(), ".");

            int i = 0;
            Path<Object> nestedPath = null;

            for (String key : keys) {

                if (i == 0) {

                    nestedPath = root.get(key);
                    i++;
                } else {

                    nestedPath = nestedPath.get(key);
                }
            }
            objectPath = nestedPath;

        } else {

            objectPath = root.get(criteria.getKey());
        }

        return objectPath;
    }
}
