package com.animehost.requests.product;

import com.animehost.data.dao.ProductDAO;
import com.animehost.data.pattern.Category;
import com.animehost.data.pattern.Product;
import com.animehost.data.pattern.Report;
import com.animehost.report.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/product")
public class ProductRequest {

    private ProductDAO itemDao = new ProductDAO();
    private ReportService report = new ReportService();
    @RequestMapping(value="", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getItem(@RequestParam("filters") Optional<String> filter, @RequestParam("id") Optional<String> id,@RequestParam("searchWord") Optional<String> searchWord){
        AtomicReference<JSONObject> response = new AtomicReference<>();
        id.ifPresentOrElse((x)->{
            itemDao.findById(x).ifPresent((item) -> {
                response.set(item);
            });
        },()->{
            filter.ifPresentOrElse((y)->{
                String filterString ="";
                String[] filters = y.split("::");
                for(int i = 0;i < filters.length;i++){
                    String[] id_filter = filters[i].split(":");
                    String[] multFilter = id_filter[1].split("#");
                    for(int j=0;j < multFilter.length;j++){
                        filterString += id_filter[0]+"='"+multFilter[j]+"'";
                        if(j < multFilter.length - 1){
                            filterString += " OR ";
                        }
                    }
                    if(i < filters.length -1){
                        filterString += " AND ";
                    }
                }
                itemDao.findFilters(filterString).ifPresent((item)->{
                    response.set(item);
                });
            },()->{
                itemDao.findAll().ifPresent((item)->{
                    response.set(item);

                });
            });
        });
        return response.get().toMap();

    }
    @RequestMapping(value="",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> postItem(@RequestBody Product body){
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());
        Boolean success = true;
        body.setId(UUID.randomUUID().toString());
        body.setStatus(1);
        if(body.getName() == null){
            response.get().put("success",false);
            response.get().put("msg","'name' não informado");
            success = false;
        }
        else if(body.getPrice() == null){
            response.get().put("success",false);
            response.get().put("msg","'price' não informado");
            success = false;
        }
        if(success){
            itemDao.create(body).ifPresent((item)->{
                response.set(item);
            });
        }
        return response.get().toMap();
    }
    @RequestMapping(value="",method = RequestMethod.DELETE)
    @ResponseBody
    private Map<String,Object> delItem(@RequestBody Product body){
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());
        Boolean success = true;
        if(body.getId() == null){
            response.get().put("success",false);
            response.get().put("msg","'id' não informado");
            success = false;
        }
        if(success){

            itemDao.delete(body.getId()).ifPresent((item)->{
                response.set(item);
            });
        }
        return response.get().toMap();
    }
    @RequestMapping(value="",method = RequestMethod.PUT)
    @ResponseBody
    private Map<String,Object> putItem(@RequestBody Product body){
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());
        Boolean success = true;
        if(body.getId() == null){
            response.get().put("success",false);
            response.get().put("msg","'id' não informado");
            success = false;
        }
        else if(body.getName() == null){
            response.get().put("success",false);
            response.get().put("msg","'name' não informado");
            success = false;
        }
        else if(body.getPrice() == null){
            response.get().put("success",false);
            response.get().put("msg","'price' não informado");
            success = false;
        }
        if(success){
            itemDao.update(body).ifPresent((item)->{
                response.set(item);
            });
        }
        return response.get().toMap();
    }
    @RequestMapping(value="/status",method = RequestMethod.PUT)
    @ResponseBody
    private Map<String,Object> alterStatus(@RequestBody Product body){
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());
        Boolean success = true;
        if(body.getId() == null){
            response.get().put("success",false);
            response.get().put("msg","'id' não informado");
            success = false;
        }
        if(success){
            itemDao.alterStatus(body.getId()).ifPresent((item)->{
                response.set(item);
            });
        }
        return response.get().toMap();
    }
    @RequestMapping(value="/report",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> report(@RequestBody Report body) throws JRException, FileNotFoundException {
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());
        Boolean success = true;
        if(body.getName() == null){
            response.get().put("success",false);
            response.get().put("msg","'name' não informado");
            success = false;
        }
        else if(body.getAddress() == null){
            response.get().put("success",false);
            response.get().put("msg","'address' não informado");
            success = false;
        }
        else if(body.getState() == null){
            response.get().put("success",false);
            response.get().put("msg","'state' não informado");
            success = false;
        }
        if(success){
            report.exportReport(body).ifPresent((item)->{
                response.set(item);
            });
        }
        return response.get().toMap();
    }
    @RequestMapping(value="/report/all",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> reportAll() throws JRException, FileNotFoundException {
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());

            report.productsReport().ifPresent((item)->{
                response.set(item);
            });
        return response.get().toMap();
    }
}
