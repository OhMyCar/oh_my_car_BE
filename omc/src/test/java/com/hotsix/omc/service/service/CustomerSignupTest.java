package com.hotsix.omc.service.service;


import com.hotsix.omc.components.MailComponents;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.hotsix.omc.exception.ErrorCode.ALREADY_EXIST_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerSignupTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private MailComponents mailComponents;

    @InjectMocks
    private CustomerService customerService;


    @Test
    public void success_RegisterCustomer() throws Exception {
        //given
        given(customerRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());

        given(customerRepository.save(any()))
                .willReturn(Customer.builder()
                        .email("ads@gmail.com")
                        .password("1234")
                        .name("찰스")
                        .phone("01000000000")
                        .nickname("짤스")
                        .build());


        //when
        CustomerSignupForm.Response customerSignupForm = customerService.register(new CustomerSignupForm.Request(
                "ads@gmail.com", "찰스", "1233", "01000000000", "짤짤"
        ));

        String uuid = UUID.randomUUID().toString();
        String email = customerSignupForm.getEmail();
        String subject = "Oh! My Car 에 오신 것을 환영합니다!";
        String text = "<p> OMC 가입을 축하드립니다. </p> <p>아래 링크를 클릭하셔서 가입을 완료해주세요.</p>" +
                "<div><a target='_blank' href='http://localhost:8080/customer/email-auth?id="
                + uuid + "'> 가입완료 </a></div>";
        mailComponents.sendMail(email, subject, text);

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);
        //then
        verify(customerRepository, times(1)).save(argumentCaptor.capture());
        assertEquals("01000000000", argumentCaptor.getValue().getPhone());
        assertEquals("찰스", customerSignupForm.getName());
        assertEquals("ads@gmail.com", customerSignupForm.getEmail());
        assertEquals("찰스", customerSignupForm.getName());

    }

    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 회원")
    public void fail_RegisterCustomer() throws Exception {
        //given
        given(customerRepository.findByEmail(anyString()))
                .willReturn(Optional.ofNullable(Customer.builder()
                        .email("ads@gmail.com")
                        .password("1234")
                        .name("찰스")
                        .phone("01000000000")
                        .nickname("짤스")
                        .build()));
        //when
        UsersException exception = assertThrows(UsersException.class, () -> customerService.register(new CustomerSignupForm.Request(
                "ads@gmail.com", "찰스", "1233", "01000000000", "짤짤"
        )));
        //then
        assertEquals(ALREADY_EXIST_USER, exception.getErrorCode());

    }
}
