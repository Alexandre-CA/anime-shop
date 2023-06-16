package com.animehost.data.pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private String id;
    private String name;
    private String description;
    private int status = 1;
}
