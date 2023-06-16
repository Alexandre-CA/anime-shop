package com.animehost.data.dao;

import com.animehost.config.connect.ConnectFactory;
import com.animehost.config.encrypt.Encrypt;
import com.animehost.data.pattern.Customer;
import com.animehost.data.pattern.User;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class UserDAO {

    private ConnectFactory connectFactory = new ConnectFactory();

    public Optional<JSONObject> findAll() {
        // Buscar todos os usuários
        JSONObject returnRequest = new JSONObject();
        List<User> lista = new ArrayList<>();
        try{
            String query = "SELECT * FROM user";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                User item = new User();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setEmail(rs.getString("email"));
                item.setStatus(rs.getInt("status"));
                lista.add(item);
            }
            returnRequest.put("success", true);
            returnRequest.put("msg", "Usuários encontrados.");
            returnRequest.put("data", lista);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }
    public ArrayList<User> listAll() {
        // Buscar todos os usuários
        ArrayList<User> lista = new ArrayList<>();
        try{
            String query = "SELECT * FROM user";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                User item = new User();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setEmail(rs.getString("email"));
                item.setStatus(rs.getInt("status"));
                lista.add(item);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return lista;

    }
    public Optional<JSONObject> findById(String id) {
        // Buscar usuários por um determiado ID
        JSONObject returnRequest = new JSONObject();
        User item = new User();
        try{
            String query = "SELECT * FROM user WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setEmail(rs.getString("email"));
                item.setStatus(rs.getInt("status"));
            }
            returnRequest.put("success", true);
            returnRequest.put("msg", "Usuário encontrado.");
            returnRequest.put("data", item);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }

    public Optional<JSONObject> findFilters(String filter) {
        // Buscar todas os usuários filtradas
        JSONObject returnRequest = new JSONObject();
        List<User> lista = new ArrayList<>();
        try{
            String query = "SELECT * FROM user WHERE "+ filter;
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                User item = new User();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setEmail(rs.getString("email"));
                item.setStatus(rs.getInt("status"));
                lista.add(item);
            }
            returnRequest.put("success", true);
            returnRequest.put("msg", "Usuário encontrado.");
            returnRequest.put("data", lista);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }

    public Optional<JSONObject> create(User body) {
        // Criar usuário
        JSONObject returnRequest = new JSONObject();
        try{
            Connection connection = ConnectFactory.getConnection();
            String query = "INSERT INTO user" +"(id,name,email,status,password)"+"VALUES(?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,body.getId());
            statement.setString(2, body.getName());
            statement.setString(3, body.getEmail());
            statement.setInt(4, body.getStatus());
            statement.setString(5, Encrypt.EncryptPass(body.getPassword()));
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Usuário criado.");
            returnRequest.put("data", new JSONObject().put("id",body.getId()));

        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }

    public Optional<JSONObject> update(User body) {
        // Atualizar usuário
        JSONObject returnRequest = new JSONObject();
        try{
            Connection connection = ConnectFactory.getConnection();
            String query = "UPDATE user SET name = ?,email = ?, password = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, body.getName());
            statement.setString(2, body.getEmail());
            statement.setString(3,Encrypt.EncryptPass(body.getPassword()));
            statement.setString(4,body.getId());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Usuário atualizado.");
            returnRequest.put("data", new JSONObject().put("id",body.getId()));

        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }
    public Optional<JSONObject> delete(String id) {
        // Deleta usuário por um determiado ID
        JSONObject returnRequest = new JSONObject();
        try{
            String query = "DELETE FROM user WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeUpdate();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Usuário deletado com sucesso.");
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
            String query = "SELECT status FROM user WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                status = (rs.getInt("status"));
            }
            try{
                String query2 = "UPDATE user SET status = ? WhERE id = ?";
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

    public Optional<JSONObject> login(User body) {
        // Buscar usuários por um determiado ID
        JSONObject returnRequest = new JSONObject();
        User item = new User();
        try{
            String query = "SELECT * FROM user WHERE password = ? AND email = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,Encrypt.EncryptPass(body.getPassword()));
            statement.setString(2,body.getEmail());
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setEmail(rs.getString("email"));
                item.setStatus(rs.getInt("status"));
            }
            if(item.getId() != null){
                returnRequest.put("success", true);
                returnRequest.put("msg", "Usuário encontrado.");
                returnRequest.put("data", item);
            }
            else {
                returnRequest.put("success", false);
                returnRequest.put("msg", "E-mail ou Senha incorretos.");
            }
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }
}
