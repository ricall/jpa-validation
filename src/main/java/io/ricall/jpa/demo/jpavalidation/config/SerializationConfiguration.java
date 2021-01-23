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

package io.ricall.jpa.demo.jpavalidation.config;

import io.ricall.jpa.demo.jpavalidation.serdes.ProductTypeDeserializer;
import io.ricall.jpa.demo.jpavalidation.serdes.ProductTypeSerializer;
import io.ricall.jpa.demo.jpavalidation.service.implementation.DefaultProductTypeService;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializationConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizeJackson() {
        return builder -> builder
                .serializers(productTypeSerializer())
                .deserializers(productTypeDeserializer(null));
    }

    @Bean
    public ProductTypeSerializer productTypeSerializer() {
        return new ProductTypeSerializer();
    }

    @Bean
    public ProductTypeDeserializer productTypeDeserializer(DefaultProductTypeService service) {
        return new ProductTypeDeserializer(service);
    }
}
