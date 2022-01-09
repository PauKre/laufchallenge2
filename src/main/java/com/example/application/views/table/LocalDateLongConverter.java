//package com.example.application.views.table;
//
//import com.vaadin.flow.data.binder.Result;
//import com.vaadin.flow.data.binder.ValueContext;
//import com.vaadin.flow.data.converter.Converter;
//
//import java.time.LocalDate;
//
//public class LocalDateLongConverter implements Converter<LocalDate, Long> {
//
//    @Override
//    public Result<Long> convertToModel(LocalDate localDate, ValueContext valueContext) {
//        return new Long(localDate.toEpochDay());
//    }
//
//    @Override
//    public LocalDate convertToPresentation(Long l, ValueContext valueContext) {
//        return LocalDate.ofEpochDay(l.longValue());
//    }
//
//
//}
