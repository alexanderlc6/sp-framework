package com.sp.framework.common.utils.bean;

import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@Slf4j
@Component
public class DozerUtil {
    static DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();

    private DozerUtil() {
    }

    public static void map(Object source, Object destination) {
        dozerBeanMapper.map(source, destination);
    }

    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozerBeanMapper.map(source, destinationClass);
    }

    public static <T, U> List<U> mapList(final List<T> source, final Class<U> destType) {
        final List<U> dest = new ArrayList<>();
        if (CollectionUtils.isEmpty(source)) {
            return dest;
        }
        for (T element : source) {
            dest.add(dozerBeanMapper.map(element, destType));
        }
        return dest;
    }

    public static <T, U> void mapList(final List<T> source, final Class<U> destType, List<U> dest) {
        if (CollectionUtils.isEmpty(source)) {
            return;
        }
        for (T element : source) {
            dest.add(dozerBeanMapper.map(element, destType));
        }
        return;
    }

    public static void setDozerBeanMapper(DozerBeanMapper param) {
        dozerBeanMapper = param;
    }
}
