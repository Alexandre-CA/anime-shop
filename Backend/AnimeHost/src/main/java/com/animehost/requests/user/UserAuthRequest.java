package com.animehost.requests.user;

import com.animehost.data.dao.UserDAO;
import com.animehost.data.pattern.User;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/auth/user")
public class UserAuthRequest {

    private UserDAO itemDao = new UserDAO();

    @RequestMapping(value="/login",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> login(@RequestBody User body){
        AtomicReference<JSONObject> response = new AtomicReference<>(new JSONObject());
        Boolean success = true;
        if(body.getEmail() == null){
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
            itemDao.login(body).ifPresent((item)->{
                response.set(item);
            });
        }
        return response.get().toMap();
    }

}