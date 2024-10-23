package com.doan.backend.dto.request;

import jakarta.persistence.*;

public class AddressRequest {
    @Column(name = "street", nullable = false)
    String street;

    @Column(name = "city", nullable = false)
    String city;

    @Column(name = "state")
    String state;

    @Column(name = "country", nullable = false)
    String country;
}
