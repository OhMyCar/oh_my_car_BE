package com.hotsix.omc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm;
import com.hotsix.omc.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CustomerController.class)
public class CustomerSignupTest {

    @MockBean
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
        public void success_RegisterCustomer() throws Exception{
        //given
        given(customerService.register(any()))
                .willReturn(CustomerSignupForm.Response.builder()
                        .email("ads@gmail.com")
                        .name("김찰스찰스")
                        .build());
        //when
        //then
        mockMvc.perform(post("/customer/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CustomerSignupForm.Request(
                                "111@111",
                                "찰스",
                                "111",
                                "01000000000",
                                "짤스"
                        )
                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ads@gmail.com"))
                .andExpect(jsonPath("$.name").value("김찰스찰스"))
                .andDo(print());

     }
}
