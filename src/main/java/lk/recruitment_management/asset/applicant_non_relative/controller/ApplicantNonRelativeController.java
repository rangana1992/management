package lk.recruitment_management.asset.applicant_non_relative.controller;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_non_relative.entity.ApplicantNonRelative;
import lk.recruitment_management.asset.applicant.service.ApplicantService;
import lk.recruitment_management.asset.applicant_non_relative.service.ApplicantNonRelativeService;
import lk.recruitment_management.asset.user_management.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/nonRelative")
public class ApplicantNonRelativeController {
    private final ApplicantNonRelativeService applicantNonRelativeService;
    private final UserService userService;
    private final ApplicantService applicantService;

    public ApplicantNonRelativeController(ApplicantNonRelativeService applicantNonRelativeService, UserService userService, ApplicantService applicantService) {
        this.applicantNonRelativeService = applicantNonRelativeService;
        this.userService = userService;
        this.applicantService = applicantService;
    }

    @GetMapping
    public String findAll(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("nonRelatives", applicantNonRelativeService.findAll().stream()
                .filter(nonRelative -> nonRelative.getApplicant().equals(applicantService.findByEmail(authentication.getName())))
                .collect(Collectors.toList()));
        return "nonRelative/nonRelative";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("applicant", new Applicant());
        model.addAttribute("addStatus", false);
        return "nonRelative/addNonRelative";
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@ModelAttribute Applicant applicant, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("applicant", applicant);
            model.addAttribute("addStatus", false);
            return "nonRelative/addNonRelative";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for ( ApplicantNonRelative applicantNonRelative : applicant.getApplicantNonRelatives()) {
            applicantNonRelative.setApplicant(applicantService.findByEmail(authentication.getName()));
            applicantNonRelativeService.persist(applicantNonRelative);
        }

        return "redirect:/applicant";
    }

}
