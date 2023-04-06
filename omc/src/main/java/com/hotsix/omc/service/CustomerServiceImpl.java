package com.hotsix.omc.service;

import com.hotsix.omc.components.MailComponents;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.entity.Seller;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Response;
import com.hotsix.omc.domain.form.token.TokenInfo;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.jwt.JwtTokenProvider;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hotsix.omc.domain.entity.CustomerStatus.UNAUTHORIZED;
import static com.hotsix.omc.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
    private final CustomerRepository customerRepository;
    private final MailComponents mailComponents;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public Response register(CustomerSignupForm.Request request) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(request.getEmail());
        if (optionalCustomer.isPresent()){
            throw new UsersException(ALREADY_EXIST_USER);
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

    public TokenInfo login(String email, String password){
        // id, pw 기반 Authentication 객체생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        // 검증 (비밀번호 확인)
        // authenticate 실행 될 때 loadByUserByUsername 실행
        validateEmailAndPassword(email, password);
        Authentication authentication = authenticationManagerBuilder
                .getObject()
                .authenticate(authenticationToken);

        return tokenProvider.generateToken(authentication);
    }

    private void validateEmailAndPassword(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsersException(EMAIL_NOT_EXIST));

        if (!passwordEncoder.matches(password, customer.getPassword())){
            throw new UsersException(PASSWORD_NOT_MATCH);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        if (customerRepository.findByEmail(email).isPresent()){
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("EMAiL NOT FOUND -> " + email));
            List<GrantedAuthority> customerGrantedAuthorities = new ArrayList<>();
            customerGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

            return new User(customer.getEmail(),
                    customer.getPassword(),
                    customerGrantedAuthorities);
        }

        else if (sellerRepository.findByEmail(email).isPresent()) {
            Seller seller = sellerRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("EMAiL NOT FOUND -> " + email));

            List<GrantedAuthority> sellerGrantedAuthorities = new ArrayList<>();
            sellerGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
            return new User(seller.getEmail(),
                    seller.getPassword(),
                    sellerGrantedAuthorities);
        }
        return null;
    }
}
