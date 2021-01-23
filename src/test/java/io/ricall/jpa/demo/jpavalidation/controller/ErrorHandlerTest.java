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

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ErrorHandlerTest {

    private ErrorHandler subject = new ErrorHandler();

    @Test
    public void verifyErrorHandlerConvertsObjectErrorsCorrectly() {
        val exception = new BindException(this, "foo");
        exception.addError(new ObjectError("test", "object error"));

        val response = subject.handleBindException(exception);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        val body = response.getBody();
        assertThat(body.getDescription()).isEqualTo("Invalid request");
        assertThat(body.getErrors()).hasSize(1);
        val error = body.getErrors().get(0);
        assertThat(error.getCode()).isEqualTo("E999");
        assertThat(error.getCodeDescription()).isEqualTo("Object Error");
        assertThat(error.getDescription()).isEqualTo("Error in object 'test': codes []; arguments []; default message [object error]");
    }
}
