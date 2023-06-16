package com.animehost.report;

import com.animehost.data.dao.CategoryDAO;
import com.animehost.data.dao.CustomerDAO;
import com.animehost.data.dao.ProductDAO;
import com.animehost.data.dao.UserDAO;
import com.animehost.data.pattern.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportService {

    private ProductDAO productDAO = new ProductDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private UserDAO userDAO = new UserDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

   public Optional<JSONObject> exportReport(Report report) throws FileNotFoundException, JRException {
        String path = "C:\\Users\\Alexa\\Downloads";
        ArrayList<ProductRequest> products = new ArrayList<>();
        JSONObject response= new JSONObject();

        for(String id : report.getProducts()){
            Boolean productQtd = false;
            for(int i = 0;i < products.size() && !productQtd;i++){
                if(products.get(i).getId().equals(id)){
                    products.get(i).setQtd(products.get(i).getQtd() + 1);
                    productQtd = true;
                }
            }
            if(!productQtd){
                productDAO.findById(id).ifPresent(x->{
                    products.add((ProductRequest) x.get("data"));
                });
            }
        }
        File file = ResourceUtils.getFile("classpath:invoice.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("shipName",report.getName());
        parameters.put("shipAddress",report.getAddress());
        parameters.put("shipState",report.getState());
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,path +"\\report.pdf");
        response.put("success",true);
        response.put("msg","Report Gerado com sucesso");

        return Optional.ofNullable(response);
    }
    public Optional<JSONObject> productsReport() throws FileNotFoundException, JRException {
        String path = "C:\\Users\\Alexa\\Downloads";
        JSONObject response = new JSONObject();

        File file = ResourceUtils.getFile("classpath:products.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(productDAO.listAll());
        Map<String,Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,path +"\\produtos.pdf");
        response.put("success",true);
        response.put("msg","Report Gerado com sucesso");

        return Optional.ofNullable(response);
    }
    public Optional<JSONObject> categoryReport() throws FileNotFoundException, JRException {
        String path = "C:\\Users\\Alexa\\Downloads";
        JSONObject response = new JSONObject();

        File file = ResourceUtils.getFile("classpath:categories.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(categoryDAO.listAll());
        Map<String,Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,path +"\\categorias.pdf");
        response.put("success",true);
        response.put("msg","Report Gerado com sucesso");

        return Optional.ofNullable(response);
    }
    public Optional<JSONObject> userReport() throws FileNotFoundException, JRException {
        String path = "C:\\Users\\Alexa\\Downloads";
        JSONObject response = new JSONObject();
        File file = ResourceUtils.getFile("classpath:users.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userDAO.listAll());
        Map<String,Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,path +"\\usuario.pdf");
        response.put("success",true);
        response.put("msg","Report Gerado com sucesso");

        return Optional.ofNullable(response);
    }
    public Optional<JSONObject> customerReport() throws FileNotFoundException, JRException {
        String path = "C:\\Users\\Alexa\\Downloads";
        JSONObject response = new JSONObject();
        File file = ResourceUtils.getFile("classpath:customers.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(customerDAO.listAll());
        Map<String,Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,path +"\\cliente.pdf");
        response.put("success",true);
        response.put("msg","Report Gerado com sucesso");

        return Optional.ofNullable(response);
    }

}
