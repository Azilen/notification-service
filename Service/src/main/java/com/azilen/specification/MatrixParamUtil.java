package com.azilen.specification;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.List;
import java.util.Map;

public interface MatrixParamUtil {

    static boolean containsKey(Map<String, List<Object>> matrixVars, String key) {

        return MapUtils.isNotEmpty(matrixVars) && matrixVars.containsKey(key);
    }

    static boolean isTrue(Map<String, List<Object>> matrixVars, String key) {

        return MapUtils.isNotEmpty(matrixVars) && matrixVars.containsKey(key)
            && BooleanUtils.toBoolean(matrixVars.get(key).get(0).toString());
    }

    static boolean isFalse(Map<String, List<Object>> matrixVars, String key) {

        return MapUtils.isNotEmpty(matrixVars) && matrixVars.containsKey(key)
            && !BooleanUtils.toBoolean(matrixVars.get(key).get(0).toString());
    }
}
