package com.github.peacetrue.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.github.peacetrue.result.exception.ResultException;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author xiayx
 */
@Getter
@Setter
public class ResultConverterImpl implements ResultConverter {

    private Consumer<Map> checker;
    private BiFunction<Map, JavaType, Object> converter;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Map result, JavaType javaType) throws ResultException {
        checker.accept(result);
        return (T) converter.apply(result, javaType);
    }

}
