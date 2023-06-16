package com.animehost;

import com.animehost.data.dao.ProductDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class AnimeHostApplication {
    public static void main(String[] args) {
       SpringApplication.run(AnimeHostApplication.class, args);
    }

}
