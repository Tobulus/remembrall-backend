package remembrall.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remembrall.config.user.UserPrincipal;
import remembrall.config.web.DbLocaleResolver;
import remembrall.controller.BasicController;
import remembrall.model.User;
import remembrall.model.UserDto;
import remembrall.model.repository.UserRepository;
import remembrall.model.validation.EmailExistsException;
import remembrall.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationApi implements BasicController {

    @Autowired
    private UserService users;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DbLocaleResolver dbLocaleResolver;

    @GetMapping(value = "/api/auth")
    public Map<String, String> auth(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        dbLocaleResolver.updateLang(request, getUserPrincipalOrThrow().getUsername());
        response.put("result", "success");
        return response;
    }

    @PostMapping(value = "/api/user/registration")
    public void registerUserAccount(@Valid UserDto accountDto) throws EmailExistsException {
        users.createUser(accountDto);
    }

    @PutMapping(value = "/api/user/token")
    public void updateToken(@RequestParam String token) {
        UserPrincipal principal = getUserPrincipalOrThrow();
        User user = userRepository.findById(principal.getUserId()).orElseThrow(
                () -> new InvalidParameterException("Cannot find user with Id: " + principal.getUserId()));
        user.setToken(token);
        userRepository.save(user);
    }

    @PutMapping(value = "/api/user/reset-password")
    public void resetPassword(@RequestParam String username) {
        users.resetPassword(username);
    }

    @PutMapping(value = "/api/user/change-password")
    public void changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        UserPrincipal principal = getUserPrincipalOrThrow();
        users.changePassword(principal.getUsername(), oldPassword, newPassword);
    }
}
