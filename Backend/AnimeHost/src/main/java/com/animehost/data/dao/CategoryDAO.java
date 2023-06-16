package com.animehost.data.dao;

import com.animehost.config.connect.ConnectFactory;
import com.animehost.data.pattern.Category;
import com.animehost.data.pattern.CategoryProduct;
import com.animehost.data.pattern.Product;
import org.json.JSONObject;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CategoryDAO {
    private ConnectFactory connectFactory = new ConnectFactory();

    public Optional<JSONObject> findAll() {
        // Buscar todas as categorias
        List<Category> lista = new ArrayList<>();
        JSONObject returnRequest = new JSONObject();

        try{
            String query = "SELECT * FROM category";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Category item = new Category();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setStatus(rs.getInt("status"));
                lista.add(item);
            }

            returnRequest.put("success", true);
            returnRequest.put("msg", "Categorias encontradas.");
            returnRequest.put("data", lista);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);

        }
        return Optional.ofNullable(returnRequest);
    }
    public ArrayList<Category> listAll() {
        // Buscar todas as categorias
        ArrayList<Category> lista = new ArrayList<>();

        try{
            String query = "SELECT * FROM category";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Category item = new Category();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setStatus(rs.getInt("status"));
                lista.add(item);
            }

        } catch (SQLException e){
            throw new RuntimeException(e);

        }
        return lista;
    }
    public Optional<JSONObject> findById(String id) {
        // Buscar categoria por um determiado ID
        Category item = new Category();
        JSONObject returnRequest = new JSONObject();
        try{
            String query = "SELECT * FROM category WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setStatus(rs.getInt("status"));
            }
            returnRequest.put("success", true);
            returnRequest.put("msg", "Categoria encontrada.");
            returnRequest.put("data", item);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }
    public Optional<JSONObject> findFilters(String filter) {
        // Buscar todas as categorias filtradas
        JSONObject returnRequest = new JSONObject();
        List<Category> list = new ArrayList<>();
        try{
            String query = "SELECT * FROM category WHERE "+ filter;
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Category item = new Category();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setStatus(rs.getInt("status"));
                list.add(item);
            }
            returnRequest.put("success", true);
            returnRequest.put("msg", "Categorias encontradas.");
            returnRequest.put("data", list);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }
    public Optional<JSONObject> delete(String id) {
        // Deleta categoria por um determiado ID
        JSONObject returnRequest = new JSONObject();
        try{
            String query = "DELETE FROM category WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeUpdate();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Categoria deletada com sucesso.");
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }

    public Optional<JSONObject> create(Category body) {
        // Criar categoria
        JSONObject returnRequest = new JSONObject();
        try{
            Connection connection = ConnectFactory.getConnection();
            String query = "INSERT INTO category" +"(id,name,description,status)"+"VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,body.getId());
            statement.setString(2, body.getName());
            statement.setString(3, body.getDescription());
            statement.setInt(4, body.getStatus());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Categoria criada.");
            returnRequest.put("data", new JSONObject().put("id",body.getId()));

        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }

    public Optional<JSONObject> update(Category body) {
        // Atualizar categoria
        JSONObject returnRequest = new JSONObject();
        try{
            Connection connection = ConnectFactory.getConnection();
            String query = "UPDATE category SET name = ?,description = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, body.getName());
            statement.setString(2, body.getDescription());
            statement.setString(3,body.getId());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Categoria atualizada.");
            returnRequest.put("data", new JSONObject().put("id",body.getId()));

        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }
    public Optional<JSONObject> alterStatus(String id) {
        // Criar produto e caso tenha categorias linkar elas
        AtomicReference<JSONObject> returnRequest = new AtomicReference<>(new JSONObject());
        try{
            int status = 0;
            String query = "SELECT status FROM category WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                status = (rs.getInt("status"));
            }
            try{
                String query2 = "UPDATE category SET status = ? WhERE id = ?";
                PreparedStatement statement2 = connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
                statement2.setInt(1,(status == 1) ? 0 : 1 );
                statement2.setString(2,id);

                statement2.executeUpdate();
                returnRequest.get().put("success", true);
                returnRequest.get().put("msg", "Produto atualizado com sucesso.");
                returnRequest.get().put("data", new JSONObject().put("id",id));
            } catch (SQLException e) {
                returnRequest.get().put("success", false);
                returnRequest.get().put("msg", e);
                throw new RuntimeException(e);
            }

        } catch (SQLException e){
            returnRequest.get().put("success", false);
            returnRequest.get().put("msg", e);
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(returnRequest.get());
    }
}
