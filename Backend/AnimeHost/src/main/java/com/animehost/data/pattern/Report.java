package com.animehost.data.pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private String id;

    private String name;

    private String address;
    private String state;


    private ArrayList<String> products = new ArrayList<>();
}
