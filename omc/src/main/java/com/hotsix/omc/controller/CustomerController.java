package com.hotsix.omc.controller;

import com.hotsix.omc.domain.form.customer.CarInfoForm;
import com.hotsix.omc.domain.form.customer.CustomerLoginForm;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Request;
import com.hotsix.omc.domain.form.customer.CustomerSignupForm.Response;
import com.hotsix.omc.domain.form.customer.CustomerUpdateForm;
import com.hotsix.omc.domain.form.token.TokenInfo;
import com.hotsix.omc.service.CustomerService;
import com.hotsix.omc.service.ReviewService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;
    private final ReviewService reviewService;

    @PostMapping("/signup")
    public ResponseEntity<Response> registerMember(
            @RequestBody @Valid Request request) {
        return ResponseEntity.ok(customerService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(
            @RequestBody @Valid CustomerLoginForm form){
        TokenInfo customerToken = customerService.login(form.getEmail(), form.getPassword());
        return ResponseEntity.ok(customerToken);
    }

    @GetMapping("/email-auth")
    public ResponseEntity<String> emailAuth(HttpServletRequest request){
        String uuid = request.getParameter("id");
        log.info("uuid = " + uuid.toString());

        String emailAuthKey = customerService.emailAuth(uuid);
        return ResponseEntity.ok(emailAuthKey);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerUpdateForm.Response> update(
            @RequestHeader String token,
            @RequestBody @Valid CustomerUpdateForm.Request request,
            @PathVariable Long id) {
        return ResponseEntity.ok(customerService.update(request, id));
    }

    @PostMapping("/review")
    public ResponseEntity<CustomerReviewForm.Response> addReview(
            @RequestBody CustomerReviewForm.Request request) throws Exception {
        return ResponseEntity.ok(reviewService.addCustomerReview(request));
    }

    @PutMapping("/review/update/{reviewId}")
    public ResponseEntity<CustomerReviewForm.Response> updateReview(
            @RequestBody CustomerReviewForm.Request request,
            @PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.updateCustomerReview(request, reviewId));
    }

    @DeleteMapping("/review/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/car")
    public ResponseEntity<CarInfoForm.Response> registerCar(
            @RequestHeader String token,
            @RequestBody CarInfoForm.Request request){
        return ResponseEntity.ok(customerService.carRegister(request));
    }

    @PutMapping("/car/update/{carId}")
    public ResponseEntity<CarInfoForm.Response> updateCar(
            @RequestBody CarInfoForm.Request request,
            @PathVariable Long carId) {
        return ResponseEntity.ok(customerService.updateCarInfo(request, carId));
    }

    @DeleteMapping("/car/delete/{carId}")
    public ResponseEntity<CarInfoForm.Response> deleteCar(
            @RequestBody CarInfoForm.Request request,
            @PathVariable Long carId) {
        return ResponseEntity.ok(customerService.deleteCar(request, carId));
    }
}
