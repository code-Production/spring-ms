package com.geekbrains.springms.address.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)

@Entity
@Table(schema = "spring_shop", name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @ManyToOne
    @JoinColumn(name = "street_id")
    private Street street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "apartment_number")
    private String apartmentNumber;

}
