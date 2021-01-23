/*
 * Copyright (c) 2021 Richard Allwood
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.ricall.jpa.demo.jpavalidation.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import io.ricall.jpa.demo.jpavalidation.model.BindError;
import io.ricall.jpa.demo.jpavalidation.model.BindErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<BindErrorMessage> handleBindException(BindException bindException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BindErrorMessage.builder()
                        .description("Invalid request")
                        .errors(bindException.getAllErrors().stream()
                                .map(this::convertObjectError)
                                .collect(Collectors.toList())
                        )
                        .build());

    }

    private BindError convertObjectError(ObjectError error) {
        if (error instanceof FieldError) {
            return convertFieldError((FieldError) error);
        }
        return BindError.builder()
                .code("E999")
                .codeDescription("Object Error")
                .description(error.toString())
                .build();
    }

    private BindError convertFieldError(FieldError error) {
        return BindError.builder()
                .code("E001")
                .codeDescription("Field Error")
                .description(String.format("Field error in object '%s' on field '%s' rejected value: %s",
                        error.getObjectName(),
                        error.getField(),
                        ObjectUtils.nullSafeToString(error.getRejectedValue())))
                .build();
    }

}
