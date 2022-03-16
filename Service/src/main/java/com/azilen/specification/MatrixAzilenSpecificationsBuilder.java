package com.azilen.specification;

import com.azilen.common.Constants;
import com.azilen.security.SecurityUtils;
import com.azilen.utils.ReflectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import static com.azilen.specification.AzilenSpecificationConstant.*;

@Slf4j
public class MatrixAzilenSpecificationsBuilder<T> extends AzilenSpecificationsBuilder<T> {
    private final Map<String, List<Object>> matrixVars;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public MatrixAzilenSpecificationsBuilder(Map<String, List<Object>> matrixVars, Class clazz) {
        super(clazz);

        this.matrixVars = Maps.newHashMap(matrixVars);
    }

    public Specification<T> build() {

        if (!matrixVars.containsKey(ENABLED) && !matrixVars.containsKey(DELETED)) {

            matrixVars.put(ENABLED, Lists.newArrayList(true));
            matrixVars.put(DELETED, Lists.newArrayList(false));
        }

        Set<String> keys = Sets.newHashSet(matrixVars.keySet());
        for (String key : keys) {

            List<Object> values = matrixVars.get(key);
            log.debug("key: {}, values: {}", key, values);

            if (CollectionUtils.isEmpty(values)) {
                log.debug("Ignoring key: {} due to no values", key);
                continue;
            }

            if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATE)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATE), OP_EQUAL, formatDate(objValue , "Date")  , SUFFIX_DATE));

            }else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATE_LTEQ)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATE_LTEQ), OP_LTEQ, formatDate(objValue , "Date") , SUFFIX_DATE_LTEQ));

            }else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATE_GTEQ)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATE_GTEQ), OP_GTEQ, formatDate(objValue , "Date")  , SUFFIX_DATE_GTEQ));

            }else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATE_GT)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATE_GT), OP_GT, formatDate(objValue , "Date")  , SUFFIX_DATE_GT));

            }else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATE_LT)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATE_LT), OP_LT, formatDate(objValue , "Date")  , SUFFIX_DATE_LT));

            }else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATETIME)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATETIME), OP_EQUAL, formatDate(objValue , "DateTime")  , SUFFIX_DATETIME));

            } else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATETIME_LTEQ)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATETIME_LTEQ), OP_LTEQ, formatDate(objValue , "DateTime") , SUFFIX_DATETIME_LTEQ));

            } else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATETIME_GTEQ)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATETIME_GTEQ), OP_GTEQ, formatDate(objValue , "DateTime") , SUFFIX_DATETIME_GTEQ));

            } else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATETIME_GT)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATETIME_GT), OP_GT, formatDate(objValue , "DateTime") , SUFFIX_DATETIME_GT));

            } else if(StringUtils.endsWithIgnoreCase(key, SUFFIX_DATETIME_LT)) {
                String objValue = String.valueOf(values.get(0));
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_DATETIME_LT), OP_LT, formatDate(objValue , "DateTime") , SUFFIX_DATETIME_LT));

            } else if (StringUtils.endsWithIgnoreCase(key, SUFFIX_GT)) {
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_GT), OP_GT, values.get(0) ,SUFFIX_GT));

            } else if (StringUtils.endsWithIgnoreCase(key, SUFFIX_LT)) {
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_LT), OP_LT, values.get(0) ,SUFFIX_LT));

            } else if (StringUtils.endsWithIgnoreCase(key, SUFFIX_LIKE)) {
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_LIKE), OP_LIKE, values.get(0) ,SUFFIX_LIKE));

            } else if (StringUtils.endsWithIgnoreCase(key, SUFFIX_GTEQ)) {
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_GTEQ), OP_GTEQ, values.get(0) ,SUFFIX_GTEQ));

            } else if (StringUtils.endsWithIgnoreCase(key, SUFFIX_LTEQ)) {
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_LTEQ), OP_LTEQ, values.get(0) , SUFFIX_LTEQ));

            } else if (StringUtils.endsWithIgnoreCase(key, SUFFIX_LTEQ)) {
                criteriaList.add(new SearchCriteria(StringUtils.removeEndIgnoreCase(key, SUFFIX_LTEQ), OP_LTEQ, values.get(0) ,SUFFIX_LTEQ));

            } else if (StringUtils.endsWithIgnoreCase(key, SUFFIX_NOTEQ)) {

                key = StringUtils.removeEndIgnoreCase(key, SUFFIX_NOTEQ);

                if (values.size() == 1) {

                    Object value = values.get(0);
                    criteriaList.add(new SearchCriteria(key, OP_NOT_EQUAL, value , SUFFIX_NOTEQ));


                } else {
                    criteriaList.add(new SearchCriteria(key, OP_NOT_IN, values , SUFFIX_NOTEQ));
                }
            } else {

                if (values.size() == 1) {

                    Object value = values.get(0);

                    criteriaList.add(new SearchCriteria(key, OP_EQUAL, value , OP_EQUAL));

                } else {

                    criteriaList.add(new SearchCriteria(key, OP_IN, values , OP_IN));

                }
            }
        }

        return super.build();
    }

    public Instant formatDate(String objValue , String type) {
        SimpleDateFormat  format = type.equals("Date") ? dateFormat : dateTimeFormat;
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Instant instantDate = null;
        try {
            instantDate = dateFormat.parse(objValue).toInstant();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return instantDate;
    }
}
