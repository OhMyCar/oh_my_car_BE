package com.hotsix.omc.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long id;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String phone;
    private String emailAuthKey;
    @Column(unique = true)
    private String nickname;

    @OneToMany(mappedBy = "customer")
    private List<Car> cars = new ArrayList<>();
    @OneToMany List<Reservation> reservations = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private CustomerStatus Auth;

    public void setId(long l) {
    }
}
