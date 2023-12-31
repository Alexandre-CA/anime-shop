package com.animehost.data.pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;

    private String name;

    private String description;

    private String image;

    private Float price;

    private Float promotion = 0f;

    private int status = 1;

    private ArrayList<String> categories = new ArrayList<>();

}
