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
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STORE_ID")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SELLER_ID")
    private Seller seller;
    private String open;
    private String close;
    private double rating;
    private String name;
    private String tel;
    @OneToMany(mappedBy = "store")
    private List<Reservation> reservations = new ArrayList<>();
    @Embedded
    private Address address;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Category> categories;

}
