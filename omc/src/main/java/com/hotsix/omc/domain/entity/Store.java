package com.hotsix.omc.domain.entity;


import com.hotsix.omc.domain.entity.type.Address;
import com.hotsix.omc.domain.entity.type.Category;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
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
    private List<Review> review = new ArrayList<>();
    @OneToMany(mappedBy = "store")
    private List<Reservation> reservation = new ArrayList<>();
    @Embedded
    private Address address;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Category> categories;
    private Double latitude;
    private Double longitude;
}
