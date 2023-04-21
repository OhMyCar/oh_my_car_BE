package com.hotsix.omc.service;


import com.hotsix.omc.components.MailComponents;
import com.hotsix.omc.config.jwt.JwtTokenProvider;
import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.domain.entity.*;
import com.hotsix.omc.domain.form.seller.SellerSignupForm;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm.Response;
import com.hotsix.omc.domain.form.token.TokenInfo;
import com.hotsix.omc.exception.ErrorCode;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.repository.SellerRepository;
import com.hotsix.omc.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
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

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.hotsix.omc.domain.entity.CustomerStatus.UNAUTHORIZED;
import static com.hotsix.omc.exception.ErrorCode.*;
import static com.hotsix.omc.exception.ErrorCode.PASSWORD_NOT_MATCH;

@Service
@RequiredArgsConstructor
public class SellerService implements UserDetailsService {
    private final SellerRepository sellerRepository;
    private final StoreRepository storeRepository;
    private final MailComponents mailComponents;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final KakaoMapsService kakaoMapsService;



    public SellerSignupForm.Response register(SellerSignupForm.Request request) {
        Optional<Seller> optionalSeller = sellerRepository.findByEmail(request.getEmail());
        if (optionalSeller.isPresent()){
            throw new UsersException(ALREADY_EXIST_USER);
        }
        String encPw = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        String uuid = UUID.randomUUID().toString();

        Seller seller = Seller.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(encPw)
                .phone(request.getPhone())
                .emailAuthKey(uuid)
                .Auth(UNAUTHORIZED)
                .build();
        sellerRepository.save(seller);

        String email = request.getEmail();
        String subject = "Oh! My Car 에 오신 것을 환영합니다!";
        String text = "<p> OMC 가입을 축하드립니다. </p> <p>아래 링크를 클릭하셔서 가입을 완료해주세요.</p>" +
                "<div><a target='_blank' href='http://localhost:8080/customer/email-auth?id="
                + uuid + "'> 가입완료 </a></div>";
        mailComponents.sendMail(email, subject, text);

        return SellerSignupForm.Response.from(request);
    }

    public String emailAuth(String uuid) {
        Optional<Seller> optionalSeller = sellerRepository.findByEmailAuthKey(uuid);
        if(!optionalSeller.isPresent()){
            throw new UsersException(BAD_REQUEST);
        }
        Seller seller = optionalSeller.get();
        return seller.getEmailAuthKey();
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
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new UsersException(EMAIL_NOT_EXIST));

        if (!passwordEncoder.matches(password, seller.getPassword())){
            throw new UsersException(PASSWORD_NOT_MATCH);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("EMAiL NOT FOUND -> " + email));
        List<GrantedAuthority> customerGrantedAuthorities = new ArrayList<>();
        customerGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        return new User(seller.getEmail(),
                seller.getPassword(),
                customerGrantedAuthorities);
    }

    public Response registerStore(StoreRegisterForm.Request request) throws IOException, ParseException {
        Seller seller = sellerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsersException(SELLER_NOT_FOUND));

        Optional<Store> optionalStore = storeRepository.findBySellerAndName(seller, request.getName());
        if (optionalStore.isPresent()){
            throw new UsersException(ALREADY_EXIST_STORE);
        }

        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());
        Store store = Store.builder()
                .seller(seller)
                .open(request.getOpen())
                .close(request.getClose())
                .name(request.getName())
                .tel(request.getTel())
                .address(address)
                .categories(request.getCategories())
                .build();
        saveStoreLocation(store, address);

        storeRepository.save(store);

        return Response.from(request);
    }

    public Response updateStore(StoreRegisterForm.Request request, Long storeId) throws IOException, ParseException {
        Seller seller = sellerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsersException(ErrorCode.SELLER_NOT_FOUND));
        Store store = storeRepository.findByIdAndSellerId(storeId, seller.getId())
                .orElseThrow(() -> new UsersException(ErrorCode.STORE_NOT_FOUND));

        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());
        store.setOpen(request.getOpen());
        store.setClose(request.getClose());
        store.setName(request.getName());
        store.setTel(request.getTel());
        store.setAddress(address);
        store.setCategories(request.getCategories());
        saveStoreLocation(store, address);

        storeRepository.save(store);

        return Response.from(request);
    }

    public void saveStoreLocation(Store store, Address address) throws IOException, ParseException {
        String fullAddress = address.getFullAddress(address);
        Map<String, Object> geocodeData = kakaoMapsService.getLatLnt(fullAddress);

        store.setLatitude((Double) geocodeData.get("latitude"));
        store.setLongitude((Double) geocodeData.get("longitude"));
    }

    public List<StoreDto> getInfo(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new UsersException(ErrorCode.SELLER_NOT_FOUND));
        List<Store> stores = storeRepository.findBySellerId(sellerId);

        return stores.stream().map(StoreDto::from).collect(Collectors.toList());
    }

    public void deleteStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id " + id));

        storeRepository.delete(existingStore);
    }


}
