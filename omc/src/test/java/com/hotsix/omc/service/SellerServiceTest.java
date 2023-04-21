package com.hotsix.omc.service;

import com.hotsix.omc.domain.dto.StoreDto;
import com.hotsix.omc.domain.entity.Address;
import com.hotsix.omc.domain.entity.Category;
import com.hotsix.omc.domain.entity.Seller;
import com.hotsix.omc.domain.entity.Store;
import com.hotsix.omc.domain.form.seller.StoreRegisterForm;
import com.hotsix.omc.exception.UsersException;
import com.hotsix.omc.repository.SellerRepository;
import com.hotsix.omc.repository.StoreRepository;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.hotsix.omc.domain.form.seller.StoreRegisterForm.Response;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private StoreRepository storeRepository;
    @InjectMocks
    private SellerService sellerService;
    @InjectMocks
    private KakaoMapsService kakaoMapsService;
    private StoreRegisterForm.Request request;
    @Mock
    private HttpURLConnection connection;
    private Seller seller;
    private Store store;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        String email = "test@test.com";
        String open = "10:00";
        String close = "18:00";
        String name = "test";
        String tel = "010-1234-1234";
        String city = "서울특별시";
        String street = "강남구 테헤란로 114";
        String zipcode = "00000";
        List<Category> categories = List.of(new Category[]{Category.MAINTENANCE, Category.PAINT});

        request = StoreRegisterForm.Request.builder()
                .email(email)
                .open(open)
                .close(close)
                .name(name)
                .tel(tel)
                .city(city)
                .street(street)
                .zipcode(zipcode)
                .categories(categories)
                .build();
        seller = Seller.builder()
                .email(request.getEmail())
                .id(1L)
                .build();
        store = Store.builder()
                .id(1L)
                .seller(seller)
                .open(open)
                .close(close)
                .name(name)
                .tel(tel)
                .address(new Address(city, street, zipcode))
                .categories(categories)
                .build();
            when(connection.getResponseCode()).thenReturn(200);
            when(connection.getInputStream()).thenReturn(new ByteArrayInputStream("{\"documents\":[{\"address\":{\"x\":\"127.05629471248271\",\"y\":\"37.505675173512924\"}}]}".getBytes()));
        }


    @Test
    void registerStore() throws IOException, ParseException {

        when(sellerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(seller));
        when(storeRepository.save(any(Store.class))).thenReturn(new Store());

        Response response = sellerService.registerStore(request);

        assertNotNull(response);
        assertEquals(response.getName(), request.getName());
        assertEquals(response.getTel(), request.getTel());
        assertEquals(response.getAddress(), request.getCity() + " " + request.getStreet() + " " + request.getZipcode());
        assertEquals(response.getOpen(), request.getOpen());
        assertEquals(response.getClose(), request.getClose());
        assertEquals(response.getCategories(), request.getCategories());
    }

    @Test
    void sellerNotFound() {
        when(sellerRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsersException.class, () -> sellerService.registerStore(request),
                "Seller not found");
    }

    @Test
    void storeAlreadyExist() {
        when(sellerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(seller));
        when(storeRepository.findBySellerAndName(seller, request.getName())).thenReturn(Optional.of(new Store()));

        assertThrows(UsersException.class, () -> sellerService.registerStore(request),
                "Already exist store");
    }


    @Test
    void updateStore() throws IOException, ParseException {

        when(sellerRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(seller));
        when(storeRepository.findByIdAndSellerId(store.getId(), seller.getId())).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenReturn(store);
        request.setClose("15:00");
        request.setName("test2");
        request.setTel("010-5678-5678");
        request.setStreet("선릉로");

        Response response = sellerService.updateStore(request, store.getId());

        assertNotNull(response);
        assertEquals(response.getName(), request.getName());
        assertEquals(response.getTel(), request.getTel());
        assertEquals(response.getAddress(), request.getCity() + " " + request.getStreet() + " " + request.getZipcode());
        assertEquals(response.getOpen(), request.getOpen());
        assertEquals(response.getClose(), request.getClose());
        assertEquals(response.getCategories(), request.getCategories());
    }

    @Test
    void storeNotFound() {
        when(storeRepository.findBySellerAndName(seller, request.getName())).thenReturn(Optional.of(new Store()));

        assertThrows(UsersException.class, () -> sellerService.updateStore(request, store.getId()),
                "Store not found");
    }

    @Test
    void getInfo() {
        List<Store> stores = new ArrayList<>();
        stores.add(store);
        stores.add(Store.builder()
                .id(2L)
                .seller(seller)
                .open("09:00")
                .close("17:00")
                .name("test2")
                .tel("010-5678-5678")
                .address(new Address("부산시", "해운대로", "11111"))
                .categories(request.getCategories())
                .build());
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(storeRepository.findBySellerId(seller.getId())).thenReturn(stores);

        List<StoreDto> result = sellerService.getInfo(seller.getId());

        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getOpen(), stores.get(0).getOpen());
        assertEquals(result.get(0).getClose(), stores.get(0).getClose());
        assertEquals(result.get(0).getName(), stores.get(0).getName());
        assertEquals(result.get(0).getTel(), stores.get(0).getTel());
        assertEquals(result.get(0).getAddress().getCity(), stores.get(0).getAddress().getCity());
        assertEquals(result.get(0).getAddress().getStreet(), stores.get(0).getAddress().getStreet());
        assertEquals(result.get(0).getAddress().getZipcode(), stores.get(0).getAddress().getZipcode());
        assertEquals(result.get(0).getCategories(), stores.get(0).getCategories());
        assertEquals(result.get(1).getOpen(), stores.get(1).getOpen());
        assertEquals(result.get(1).getClose(), stores.get(1).getClose());
        assertEquals(result.get(1).getName(), stores.get(1).getName());
        assertEquals(result.get(1).getTel(), stores.get(1).getTel());
        assertEquals(result.get(1).getAddress().getCity(), stores.get(1).getAddress().getCity());
        assertEquals(result.get(1).getAddress().getStreet(), stores.get(1).getAddress().getStreet());
        assertEquals(result.get(1).getAddress().getZipcode(), stores.get(1).getAddress().getZipcode());
        assertEquals(result.get(1).getCategories(), stores.get(1).getCategories());
    }

    @Test
    void deleteStore() {
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        sellerService.deleteStore(store.getId());

        verify(storeRepository, times(1)).delete(store);
    }

    @Test
    public void testGetLatLnt() throws IOException, ParseException {
        Map<String, Object> resultMap = kakaoMapsService.getLatLnt("서울특별시 강남구 역삼동 823-3");
        Assert.assertEquals(resultMap.get("longitude").toString().substring(0,5), "127.0");
        Assert.assertEquals(resultMap.get("latitude").toString().substring(0,4), "37.5");
    }

}