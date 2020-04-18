package grocery.controller.api;

import grocery.model.UserDto;
import grocery.model.validation.EmailExistsException;
import grocery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationApi {

    @Autowired
    private UserService service;

    @GetMapping(value = "/api/auth")
    public Map<String, String> auth() {
        Map<String, String> response = new HashMap<>();
        response.put("result", "success");
        return response;
    }

    @PostMapping(value = "/api/user/registration")
    public void registerUserAccount(@Valid UserDto accountDto) throws EmailExistsException {
        service.createUser(accountDto);
    }
}