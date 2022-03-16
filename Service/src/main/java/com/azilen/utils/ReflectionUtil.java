package com.azilen.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReflectionUtil {
    public boolean isMethodExists(String methodName , Class clazz) {
        boolean isExist = false;
        try{
            clazz.getMethod(methodName,null);
            isExist = true;
        }catch (NoSuchMethodException exception) {
            log.error("Exception in isMethodExists : ",exception);
        }
        return isExist;
    }
}
