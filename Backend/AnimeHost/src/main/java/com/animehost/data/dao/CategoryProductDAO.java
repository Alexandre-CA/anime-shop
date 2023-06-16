package com.animehost.data.dao;

import com.animehost.config.connect.ConnectFactory;
import com.animehost.data.pattern.Category;
import com.animehost.data.pattern.CategoryProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryProductDAO {
    // Categoria_Produto e o vinculo do produto com as cateogorias
    private ConnectFactory connectFactory = new ConnectFactory();

    public Optional<Boolean> create(CategoryProduct body) {
        //Criar categoria_produto
        Boolean returnRequest = false;
        try {
            Connection connection = ConnectFactory.getConnection();
            String query = "INSERT INTO category_product" + "(id_product,id_category)" + "VALUES(?,?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, body.getId_product());
            statement.setString(2, body.getId_category());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            returnRequest = true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }

    public Optional<Boolean> createList(List<CategoryProduct> body) {
        // Criar uma lista de categoria_produto
        // metodo recebe uma lista de categoria_produto e faz o vinculo com todas elas
        Boolean returnRequest = false;
        try {
            Connection connection = ConnectFactory.getConnection();
            String query = "INSERT INTO category_product" + "(id_product,id_category)" + "VALUES"+ toStringCategoryProductArray(body);
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            returnRequest = true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }
    public Optional<Boolean> deleteProductCategorie(String id) {
        // Deleta todas os link com um determinado produto
        Boolean returnRequest = false;
        try {
            Connection connection = ConnectFactory.getConnection();
            String query = "DELETE FROM category_product WHERE id_product=?";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, id);

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            returnRequest = true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }
    private String toStringCategoryProductArray(List<CategoryProduct> list){
        //Retorna uma string com uma lista de valores(id_product,id_category)
        String returnItem = "";

        for(int i = 0;i<list.size();i++){
            returnItem += "('"+list.get(i).getId_product()+"','"+list.get(i).getId_category()+"')";
            if(i < list.size() - 1){
                returnItem += ",";
            }
        }
        return returnItem;

    }
}
