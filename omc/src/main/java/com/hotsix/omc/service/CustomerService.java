package com.hotsix.omc.service;

import static com.hotsix.omc.domain.entity.type.CustomerStatus.UNAUTHORIZED;
import static com.hotsix.omc.exception.ErrorCode.*;

import com.hotsix.omc.components.MailComponents;
import com.hotsix.omc.config.jwt.JwtTokenProvider;
import com.hotsix.omc.domain.entity.Car;
import com.hotsix.omc.domain.entity.Customer;
import com.hotsix.omc.domain.form.customer.CarInfoForm;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Response;
import com.hotsix.omc.domain.form.customer.CustomerUpdateForm;
import com.hotsix.omc.domain.form.token.TokenInfo;
import com.hotsix.omc.exception.OmcException;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.repository.CarRepository;
import com.hotsix.omc.repository.CustomerRepository;
import com.hotsix.omc.repository.SellerRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hotsix.omc.service.notification.event.InspectionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final MailComponents mailComponents;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final CarRepository carRepository;
    private final ApplicationEventPublisher eventPublisher;



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


    public String emailAuth(String uuid) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmailAuthKey(uuid);
        if(!optionalCustomer.isPresent()){
            throw new UsersException(BAD_REQUEST);
        }
        Customer customer = optionalCustomer.get();
        return customer.getEmailAuthKey();
    }


    public TokenInfo login(String email, String password){
        log.info("로그인 시작===================================================================================");
        // id, pw 기반 Authentication 객체생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password, List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));

        // 검증 (비밀번호 확인)
        validateEmailAndPassword(email, password);

        // TODO loadUserByUsername 메서드 안돌아가는 부분 수정 필요함. 현재는 UsernamePasswordAuthenticationToken 에 파라미터 추가하여 진행했음.
        // authenticate 실행 될 때 loadByUserByUsername 실행
//        Authentication authentication = authenticationManagerBuilder
//                .getObject()
//                .authenticate(authenticationToken);

        return tokenProvider.generateToken(authenticationToken);
    }

    private void validateEmailAndPassword(String email, String password) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsersException(EMAIL_NOT_EXIST));

        if (!passwordEncoder.matches(password, customer.getPassword())){
            throw new UsersException(PASSWORD_NOT_MATCH);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("ClubUserDetailsService loadUserByUsername"+username+"=================@@@@@@@@@@@==============");
        Customer customer = customerRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("EMAiL NOT FOUND -> " + username));
            List<GrantedAuthority> customerGrantedAuthorities = new ArrayList<>();
            customerGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));


            //User (userName, password, Collection<? extends GrantedAuthority> authorities)
            return new User(customer.getEmail(),
                    customer.getPassword(),
                    customerGrantedAuthorities);
    }

    public Customer delete(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new UsersException(EMAIL_NOT_EXIST));

        customerRepository.delete(customer);
        return customer;
    }

    public CustomerUpdateForm.Response update(CustomerUpdateForm.Request request, Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new UsersException(EMAIL_NOT_EXIST));

        String encPw = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        customer.setPassword(encPw);
        customer.setPhone(request.getPhone());
        customerRepository.save(customer);

        return CustomerUpdateForm.Response.from(customer);
    }

    // 내 차 등록 기능
    public CarInfoForm.Response carRegister(CarInfoForm.Request request){
        carRepository.save(Car.builder()
                        .nickname(request.getNickname())
                        .distance(request.getDistance())
                        .build());
        return CarInfoForm.Response.from(request);
    }

    // 내 차 정보 수정 기능
    public CarInfoForm.Response updateCarInfo (CarInfoForm.Request request, Long carId){
        Car car = carRepository.findById(carId)
                        .orElseThrow(() -> new UsersException(CAR_NOT_FOUND));

        car.setNickname(request.getNickname());
        car.setDistance(request.getDistance());
        carRepository.save(car);


        return CarInfoForm.Response.from(request);
    }

    // 내 차량 정보 삭제 기능
    public CarInfoForm.Response deleteCar (CarInfoForm.Request request, Long carId){
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new UsersException(CAR_NOT_FOUND));

        carRepository.delete(car);
        return CarInfoForm.Response.from(request);
    }


}
