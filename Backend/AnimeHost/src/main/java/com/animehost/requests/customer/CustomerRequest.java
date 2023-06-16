package com.animehost.requests.customer;

import com.animehost.data.dao.CustomerDAO;
import com.animehost.data.pattern.Category;
import com.animehost.data.pattern.Customer;
import com.animehost.data.pattern.Product;
import com.animehost.report.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/customer")
public class CustomerRequest {
    private CustomerDAO itemDao = new CustomerDAO();
    private ReportService report = new ReportService();

    @RequestMapping(value="", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> geItem(@RequestParam("filters") Optional<String> filter, @RequestParam("id") Optional<String> id){
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
    private Map<String,Object> postItem(@RequestBody Customer body){
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());
        Boolean success = true;
        body.setId(UUID.randomUUID().toString());
        body.setStatus(1);
        if(body.getName() == null){
            response.get().put("success",false);
            response.get().put("msg","'name' não informado");
            success = false;
        }
        else if(body.getEmail() == null){
            response.get().put("success",false);
            response.get().put("msg","'email' não informado");
            success = false;
        }
        else if(body.getPassword() == null){
            response.get().put("success",false);
            response.get().put("msg","'password' não informado");
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
    private Map<String,Object> delItem(@RequestBody Customer body){
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
    private Map<String,Object> putItem(@RequestBody Customer body){
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
        else if(body.getEmail() == null){
            response.get().put("success",false);
            response.get().put("msg","'email' não informado");
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
    private Map<String,Object> alterStatus(@RequestBody Customer body){
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

    @RequestMapping(value="/report/all",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> reportAll() throws JRException, FileNotFoundException {
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());

        report.customerReport().ifPresent((item)->{
            response.set(item);
        });
        return response.get().toMap();
    }
}
