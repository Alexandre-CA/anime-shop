package com.animehost.data.dao;

import com.animehost.config.connect.ConnectFactory;
import com.animehost.data.pattern.Category;
import com.animehost.data.pattern.CategoryProduct;
import com.animehost.data.pattern.Product;
import com.animehost.data.pattern.ProductRequest;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ProductDAO {

    private ConnectFactory connectFactory = new ConnectFactory();

    private CategoryDAO categoryDAO = new CategoryDAO();
    private CategoryProductDAO categoryProductDAO = new CategoryProductDAO();



    public Optional<JSONObject> findAll() {
        // Buscar todos os produtos
        ArrayList<ProductRequest> lista = new ArrayList<ProductRequest>();
        JSONObject returnRequest = new JSONObject();

        try{
            String query = "SELECT * FROM product";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                ProductRequest item = new ProductRequest();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getFloat("price"));
                item.setPromotion(rs.getFloat("promotion"));
                item.setStatus(rs.getInt("status"));
                item.setImage(rs.getString("image"));
                if(!item.getId().isEmpty()){
                    item.setCategories(findCategories(item.getId()));
                }
                lista.add(item);
            }
            returnRequest.put("success", true);
            returnRequest.put("msg", "Produtos encontrados.");
            returnRequest.put("data", lista);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);
    }
    public ArrayList<ProductRequest> listAll() {
        // Buscar todos os produtos
        ArrayList<ProductRequest> lista = new ArrayList<ProductRequest>();

        try{
            String query = "SELECT * FROM product";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                ProductRequest item = new ProductRequest();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getFloat("price"));
                item.setPromotion(rs.getFloat("promotion"));
                item.setStatus(rs.getInt("status"));
                item.setImage(rs.getString("image"));
                if(!item.getId().isEmpty()){
                    item.setCategories(findCategories(item.getId()));
                }
                lista.add(item);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return lista;
    }
    public List<ProductRequest> listAllProduct(){
        // Buscar todos os produtos
        List<ProductRequest> lista = new ArrayList<ProductRequest>();
        try{
            String query = "SELECT * FROM product";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                ProductRequest item = new ProductRequest();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPrice(rs.getFloat("price"));
                item.setPromotion(rs.getFloat("promotion"));
                item.setStatus(rs.getInt("status"));
                item.setImage(rs.getString("image"));
                if(!item.getId().isEmpty()){
                    item.setCategories(findCategories(item.getId()));
                }
                lista.add(item);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return lista;
    }
    public Optional<JSONObject> findFilters(String filter) {
        // Buscar todos os produtos filtrados
        List<ProductRequest> lista = new ArrayList<ProductRequest>();
        JSONObject returnRequest = new JSONObject();

        try{
            String query = "SELECT * FROM product WHERE "+ filter;
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                ProductRequest item = new ProductRequest();
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPromotion(rs.getFloat("promotion"));
                item.setPrice(rs.getFloat("price"));
                item.setStatus(rs.getInt("status"));
                item.setImage(rs.getString("image"));
                if(!item.getId().isEmpty()){
                    item.setCategories(findCategories(item.getId()));
                }
                lista.add(item);
            }
            returnRequest.put("success", true);
            returnRequest.put("msg", "Produtos encontrados.");
            returnRequest.put("data", lista);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }
    public Optional<JSONObject> findById(String id) {
        // Buscar produto por um ID especifico
        JSONObject returnRequest = new JSONObject();
        ProductRequest item = new ProductRequest();
        try{
            String query = "SELECT * FROM product WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                item.setId(rs.getString("id"));
                item.setName(rs.getString("name"));
                item.setDescription(rs.getString("description"));
                item.setPromotion(rs.getFloat("promotion"));
                item.setPrice(rs.getFloat("price"));
                item.setStatus(rs.getInt("status"));
                item.setImage(rs.getString("image"));
                if(!item.getId().isEmpty()){
                    item.setCategories(findCategories(id));
                }
            }
            returnRequest.put("success", true);
            returnRequest.put("msg", "Produtos encontrados.");
            returnRequest.put("data", item);
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }

    public ArrayList<Category> findCategories(String id) {
        // Buscar todas categorias de um produto
        ArrayList<Category> lista = new ArrayList<>();
        try{
            String query = "SELECT * FROM category_product WHERE id_product= ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                CategoryProduct item = new CategoryProduct();
                item.setId_category(rs.getString("id_category"));
                item.setId_product(rs.getString("id_product"));
                categoryDAO.findById(item.getId_category()).ifPresent((cat)->{
                    lista.add((Category)cat.get("data"));
                });
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return lista;
    }
    public Optional<JSONObject> create(Product body) {
        // Criar produto e caso tenha categorias linkar elas
        AtomicReference<JSONObject> returnRequest = new AtomicReference<>(new JSONObject());

        createProduct(body).ifPresent((res)->{

            if(res.getBoolean("success")){
                if(body.getCategories().size() > 0){
                    linkCategories(body);
                }
            }
            returnRequest.set(res);
        });

        return Optional.ofNullable(returnRequest.get());

    }
    public Optional<JSONObject> update(Product body) {
        // Criar produto e caso tenha categorias linkar elas
        AtomicReference<JSONObject> returnRequest = new AtomicReference<>(new JSONObject());
        categoryProductDAO.deleteProductCategorie(body.getId()).ifPresent((dltCat)->{
            if(dltCat){
                updateProduct(body).ifPresent((res)->{
                    if(res.getBoolean("success")){
                        ArrayList<String> cat = body.getCategories();
                        if(cat.size() > 0){
                            linkCategories(body);
                        }
                        returnRequest.set(res);
                    }
                });
            }
        });

        return Optional.ofNullable(returnRequest.get());
    }
    public Optional<JSONObject> delete(String id) {
        // Deleta produto por um determiado ID
        JSONObject returnRequest = new JSONObject();
        try{
            String query = "DELETE FROM product WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeUpdate();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Produto deletada com sucesso.");
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
            String query = "SELECT status FROM product WHERE id = ?";
            Connection connection = connectFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                status = (rs.getInt("status"));
            }
            try{
                String query2 = "UPDATE product SET status = ? WhERE id = ?";
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
    public Optional<Boolean> linkCategories(Product body) {
        // Linkar categorias com produtos
        AtomicReference<Boolean> returnRequest = new AtomicReference<>(false);
        ArrayList<CategoryProduct> listCategoryProduct = new ArrayList<>();
        for(String cat : body.getCategories()){
            listCategoryProduct.add(new CategoryProduct(body.getId(),cat));
        }
        categoryProductDAO.createList(listCategoryProduct).ifPresent((res)->{
            if(res){
                returnRequest.set(true);
            }
        });

        return Optional.ofNullable(returnRequest.get());
    }

    private Optional<JSONObject> createProduct(Product body){
        // Criar produtos
        JSONObject returnRequest = new JSONObject();
        try {
            Connection connection = ConnectFactory.getConnection();
            String query = "INSERT INTO product" +"(id,name,description,image,promotion,price,status)"+"VALUES(?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,body.getId());
            statement.setString(2, body.getName());
            statement.setString(3, body.getDescription());
            statement.setString(4, body.getImage());
            statement.setFloat(5, body.getPromotion());
            statement.setFloat(6, body.getPrice());
            statement.setInt(7, body.getStatus());
            statement.executeUpdate();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Produto criado com successo.");
            returnRequest.put("data", new JSONObject().put("id",body.getId()));
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }

    private Optional<JSONObject> updateProduct(Product body){
        // Atualizar produtos
        JSONObject returnRequest = new JSONObject();
        try {
            Connection connection = ConnectFactory.getConnection();
            String query = "UPDATE product SET name = ?,description = ?,image = ?,promotion = ?,price = ? WhERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, body.getName());
            statement.setString(2, body.getDescription());
            statement.setString(3, body.getImage());
            statement.setFloat(4, body.getPromotion());
            statement.setFloat(5, body.getPrice());
            statement.setString(6,body.getId());

            statement.executeUpdate();
            returnRequest.put("success", true);
            returnRequest.put("msg", "Produto atualizado com successo.");
            returnRequest.put("data", new JSONObject().put("id",body.getId()));
        } catch (SQLException e){
            returnRequest.put("success", false);
            returnRequest.put("msg", e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(returnRequest);

    }
}
