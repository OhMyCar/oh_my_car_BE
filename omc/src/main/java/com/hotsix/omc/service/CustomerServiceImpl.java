package com.hotsix.omc.service;

import com.hotsix.omc.components.MailComponents;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Response;
import com.hotsix.omc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.hotsix.omc.domain.entity.CustomerStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
    private final CustomerRepository customerRepository;
    private final MailComponents mailComponents;

    @Override
    public Response register(CustomerSignupForm.Request request) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(request.getEmail());
        if (optionalCustomer.isPresent()){
            throw new UsernameNotFoundException("ALREADY_EXIST_USER");
        }
        String encPw = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Customer customer = Customer.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(encPw)
                .phone(request.getPhone())
                .emailAuthKey(uuid)
                .Auth(UNAUTHORIZED)
                .build();
        customerRepository.save(customer);

        String email = request.getEmail();
        String subject = "Oh! My Car 에 오신 것을 환영합니다!";
        String text = "<p> OMC 가입을 축하드립니다. </p> <p>아래 링크를 클릭하셔서 가입을 완료해주세요.</p>" +
                "<div><a target='_blank' href='http://localhost:8080/customer/email-auth?id="
                + uuid + "'> 가입완료 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return Response.from(request);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
