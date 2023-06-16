package com.animehost.data.pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private String id;
    private String name;
    private String email;
    private int status;
    private String cep;
    private String password;

}
