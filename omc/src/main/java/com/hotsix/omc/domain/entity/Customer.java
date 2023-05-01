package com.hotsix.omc.domain.entity;

import com.hotsix.omc.domain.entity.type.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
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
    private List<Review> review = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<Car> cars = new ArrayList<>();
    @OneToOne
    private NotificationPermit notificationPermit;
    @OneToMany(mappedBy = "customer")
    private List<Notification> notification;
    @OneToMany List<Reservation> reservation = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private CustomerStatus Auth;
}
