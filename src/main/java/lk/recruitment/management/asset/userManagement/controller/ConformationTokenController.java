package lk.recruitment.management.asset.userManagement.controller;


import lk.recruitment.management.asset.userManagement.entity.ConformationToken;
import lk.recruitment.management.asset.userManagement.entity.Role;
import lk.recruitment.management.asset.userManagement.entity.User;
import lk.recruitment.management.asset.userManagement.service.ConformationTokenService;
import lk.recruitment.management.asset.userManagement.service.RoleService;
import lk.recruitment.management.asset.userManagement.service.UserService;
import lk.recruitment.management.util.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/register")
public class ConformationTokenController {
    private final ConformationTokenService conformationTokenService;
    private final EmailService emailService;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ConformationTokenController(ConformationTokenService conformationTokenService, EmailService emailService, UserService userService, RoleService roleService) {
        this.conformationTokenService = conformationTokenService;
        this.emailService = emailService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    private String sendEmailForm() {
        return "user/register";
    }

    @PostMapping("/token")
    private String sendTokenToEmail(@RequestParam("email") String email, Model model, HttpServletRequest request) {
        //check if there is any user on system
        User user = userService.findByUserName(email);
        //todo-> before create the token need to check there is user on current email
        // if not create token else send forgotten password form to fill
        if (user != null) {
            model.addAttribute("message", "There is an user on system please forgotten password reset option.");
            return "user/register";
        }
        //check if there is a valid token
        ConformationToken conformationToken = conformationTokenService.findByEmail(email);
        if (conformationToken != null && conformationToken.getEndDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("message", "There is valid token fot this email " + email + "\n Please check your email.");
            return "user/register";
        }

        ConformationToken newConformationToken = new ConformationToken(email);
        String url = request.getRequestURL().toString();
        emailService.sendEmail(email, "Email Verification (Police Recruitment Division) - Not reply",
                "Please click below link to active your account \n\t".concat(url + "/" + conformationTokenService.createToken(newConformationToken).getToken()).concat("\n  this link is valid only one day. "));
        model.addAttribute("successMessage", "Please check your email \n Your entered email is \t ".concat(email));
        return "user/successMessage";
    }

    @GetMapping("/token/{token}")
    public String passwordEnterPage(@PathVariable("token") String token, Model model) {
        //todo-> need to check token has valid or not
        //check if there is a valid token
        ConformationToken conformationToken = conformationTokenService.findByToken(token);
        if (conformationToken != null && LocalDateTime.now().isBefore(conformationToken.getEndDate())) {
            model.addAttribute("email", conformationToken.getEmail());
            return "user/password";
        }
        conformationTokenService.deleteByConformationToken(conformationToken);
        model.addAttribute("message", "There is no valid token.");
        return "user/register";
    }

    @PostMapping("/user")
    public String newUser(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("reEnterPassword") String reEnterPassword, Model model) {
        if (password.equals(reEnterPassword)) {
            User user = new User();
            user.setEnabled(true);
            System.out.println("email "+email);
            user.setUsername(email);
            user.setPassword(password);
            List<Role> roles = new ArrayList<>();
            for (Role role : roleService.findAll()) {
                if (role.getRoleName().equals("APPLICANT")) {
                    roles.add(role);
                    break;
                }
            }
            user.setRoles(roles);
            userService.persist(user);
            return "redirect:/login";
        }
        model.addAttribute("email", email);
        model.addAttribute("message", "Error is password miss match.");
        return "user/register";

    }
    //TODO-> create new user and send login page
}
