package lk.recruitment_management.asset.common_asset.controller;

import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.user_management.entity.PasswordChange;
import lk.recruitment_management.asset.user_management.entity.Role;
import lk.recruitment_management.asset.user_management.entity.User;
import lk.recruitment_management.asset.user_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class ProfileController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicantService applicantService;

    @Autowired
    public ProfileController(UserService userService, PasswordEncoder passwordEncoder, ApplicantService applicantService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.applicantService = applicantService;
    }

    @GetMapping( value = "/profile" )
    public String userProfile(Model model, Principal principal) {
        User authUser = userService.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        for ( Role role : authUser.getRoles() ) {
            Applicant applicant = applicantService.findByEmail(authUser.getUsername());
            if ( role.getRoleName().equals("APPLICANT") ) {
                return "redirect:/applicant/"+applicant.getId();
            }
        }

        model.addAttribute("addStatus", true);
        model.addAttribute("employeeDetail", userService.findByUserName(principal.getName()).getEmployee());
        return "employee/employee-detail";
    }

    @GetMapping( value = "/passwordChange" )
    public String passwordChangeForm(Model model) {
        model.addAttribute("pswChange", new PasswordChange());
        return "login/passwordChange";
    }

    @PostMapping( value = "/passwordChange" )
    public String passwordChange(@Valid @ModelAttribute PasswordChange passwordChange,
                                 BindingResult result, RedirectAttributes redirectAttributes) {
        User user =
                userService.findById(userService.findByUserIdByUserName(SecurityContextHolder.getContext().getAuthentication().getName()));

        if ( passwordEncoder.matches(passwordChange.getOldPassword(), user.getPassword()) && !result.hasErrors() && passwordChange.getNewPassword().equals(passwordChange.getNewPasswordConform()) ) {

            user.setPassword(passwordChange.getNewPassword());
            userService.persist(user);

            redirectAttributes.addFlashAttribute("message", "Congratulations .!! Success password is changed");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
            return "redirect:/home";

        }
        redirectAttributes.addFlashAttribute("message", "Failed");
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        return "redirect:/passwordChange";

    }
}
