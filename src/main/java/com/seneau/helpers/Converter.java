package com.seneau.helpers;

import com.seneau.domain.Conge;
import com.seneau.domain.CongeData;
import com.seneau.dto.CongeDto;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Converter {

    @Autowired
    ModelMapper modelMapper;

    <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
    public static <Q, U> Method getMethodOfClasse(String libelle, Class<Q> target, Class<U>  ta) throws NoSuchMethodException {

        String format  = "set" + StringUtils.capitalize(libelle);
        return target.getMethod(format, ta);
    }

    public List<Object> convertToCongeDto(List<Object> congeDto, List<Conge> conge, List<CongeData> congeData) {


        List<Object> combined = Stream.concat(conge.stream(), congeData.stream()).collect(Collectors.toList());

        return combined;
    }

    public Conge convertToCongeEntity(CongeDto congeDto) {
        Conge conge = modelMapper.map(congeDto, Conge.class);
        return conge;
    }





}