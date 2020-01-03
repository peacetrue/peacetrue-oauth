package com.github.peacetrue.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.peacetrue.result.exception.ResultException;

import java.util.Map;

/**
 * 结果转换器
 *
 * @author xiayx
 * @see JavaType
 */
public interface ResultConverter {

    /**
     * 转换结果
     *
     * @param result   待转换的结果对象
     * @param javaType 想要转换成的java类型
     * @param <T>      转换成的java类型
     * @return 转换后的目标对象
     * @throws ResultException 如果是异常结果则抛出结果异常
     */
    default <T> T convert(Map result, Class<T> javaType) throws ResultException {
        return convert(result, TypeFactory.defaultInstance().constructType(javaType));
    }

    /**
     * 转换结果
     *
     * @param result   待转换的结果对象
     * @param javaType 想要转换成的java类型
     * @param <T>      转换成的java类型
     * @return 转换后的目标对象
     * @throws ResultException 如果是异常结果则抛出结果异常
     */
    <T> T convert(Map result, JavaType javaType) throws ResultException;


}
