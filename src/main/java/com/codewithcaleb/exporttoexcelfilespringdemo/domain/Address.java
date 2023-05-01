package com.codewithcaleb.exporttoexcelfilespringdemo.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    private String country;
    private String state;
    private String city;
    private String address;
}
