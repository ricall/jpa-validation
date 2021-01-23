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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ricall.jpa.demo.jpavalidation.entity.Product;
import io.ricall.jpa.demo.jpavalidation.service.ProductService;
import io.ricall.jpa.demo.jpavalidation.service.ProductTypeService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductTypeService productTypeService;

    @Test
    public void verifyNullValuesThrowsAnError() throws Exception {
        mockMvc.perform(post("/product")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Product())))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.description").value("Invalid request"))
                .andExpect(jsonPath("$.errors.size()").value(5));
    }

    @Test
    public void verifyWeCanFindAProductById() throws Exception {
        val expected = productService.save(Product.builder()
                .name("existing product")
                .type(productTypeService.fromType("X1"))
                .category("category")
                .subCategory("subCategory")
                .roleStart(LocalDateTime.of(2021, Month.JANUARY, 15, 12,0, 0))
                .build());

        mockMvc.perform(get("/product/" + expected.getId())
                .accept("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.name").value("existing product"))
                .andExpect(jsonPath("$.type").value("X1"))
                .andExpect(jsonPath("$.category").value("category"))
                .andExpect(jsonPath("$.subCategory").value("subCategory"))
                .andExpect(jsonPath("$.roleStart").value("2021-01-15T12:00:00"))
                .andExpect(jsonPath("$.roleEnd").value(nullValue()));
    }

    @Test
    public void verifyWeCanAddAProduct() throws Exception {
        val newProduct = Product.builder()
                .name("new product")
                .type(productTypeService.fromType("R3"))
                .category("category")
                .subCategory("subCategory")
                .roleStart(LocalDateTime.of(2021, Month.JANUARY, 16, 15,5, 20))
                .build();

        mockMvc.perform(post("/product")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.name").value("new product"))
                .andExpect(jsonPath("$.type").value("R3"))
                .andExpect(jsonPath("$.category").value("category"))
                .andExpect(jsonPath("$.subCategory").value("subCategory"))
                .andExpect(jsonPath("$.roleStart").value("2021-01-16T15:05:20"))
                .andExpect(jsonPath("$.roleEnd").value(nullValue()));
    }

    @Test
    public void verifyWeCanModifyAProduct() throws Exception {
        val existing = productService.save(Product.builder()
                .name("existing product")
                .type(productTypeService.fromType("X1"))
                .category("category")
                .subCategory("subCategory")
                .roleStart(LocalDateTime.of(2021, Month.JANUARY, 15, 12,0, 0))
                .build());

        val updated = productService.save(Product.builder()
                .name("updated product")
                .type(productTypeService.fromType("R4"))
                .category("category2")
                .subCategory("subCategory2")
                .roleStart(LocalDateTime.of(2020, Month.FEBRUARY, 16, 15,32, 1))
                .roleEnd(LocalDateTime.of(2021, Month.FEBRUARY, 16, 15,32, 1))
                .build());

        mockMvc.perform(patch("/product/" + existing.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(existing.getId()))
                .andExpect(jsonPath("$.name").value("updated product"))
                .andExpect(jsonPath("$.type").value("R4"))
                .andExpect(jsonPath("$.category").value("category2"))
                .andExpect(jsonPath("$.subCategory").value("subCategory2"))
                .andExpect(jsonPath("$.roleStart").value("2020-02-16T15:32:01"))
                .andExpect(jsonPath("$.roleEnd").value("2021-02-16T15:32:01"));
    }

}
