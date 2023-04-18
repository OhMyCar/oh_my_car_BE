package com.hotsix.omc.controller;


import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.domain.form.customer.CustomerReviewForm;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm;
import com.hotsix.omc.domain.form.store.StoreReviewForm;
import com.hotsix.omc.service.ReviewService;
import com.hotsix.omc.service.SellerService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final SellerService sellerService;
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<StoreRegisterForm.Response> registerStore(@RequestBody @Valid StoreRegisterForm.Request request) {
        return ResponseEntity.ok(sellerService.registerStore(request));
    }

    @PutMapping("/update/{storeId}")
    public ResponseEntity<StoreRegisterForm.Response> updateStore(@RequestBody @Valid StoreRegisterForm.Request request, @PathVariable("storeId") Long storeId) {
        return ResponseEntity.ok(sellerService.updateStore(request, storeId));
    }

    @GetMapping("/getInfo/{sellerId}")
    public ResponseEntity<List<StoreDto>> getInfo(@PathVariable("sellerId") Long sellerId) {
        return ResponseEntity.ok(sellerService.getInfo(sellerId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        sellerService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/review/{storeId}")
    public ResponseEntity<List<CustomerReviewForm.Response>> getStoreReviews(@PathVariable Long storeId) {
        List<CustomerReviewForm.Response> responseList = reviewService.getStoreReviews(storeId);
        return ResponseEntity.ok(responseList);
    }

    @PutMapping("/review/reply/{reviewId}")
    public ResponseEntity<StoreReviewForm.Response> replyReview(@PathVariable Long reviewId, @RequestBody StoreReviewForm.Request request) {
        return ResponseEntity.ok(reviewService.replyReview(reviewId, request));
    }

}
