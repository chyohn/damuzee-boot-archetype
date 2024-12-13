package com.damuzee.boot.spec.jackson.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class InnerDateTimeInitBinder {

    private final DateTimeFormatter fullDf;
    private final DateTimeFormatter df;
    private final DateTimeFormatter tf;

    public InnerDateTimeInitBinder(JacksonProperties properties) {
        fullDf = DateTimeFormatter.ofPattern(properties.getDateTimeFormat());
        df = DateTimeFormatter.ofPattern(properties.getDateFormat());
        tf = DateTimeFormatter.ofPattern(properties.getTimeFormat());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, fullDf));
            }
        });

        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, df));
            }
        });

        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalTime.parse(text, tf));
            }
        });

    }

}
