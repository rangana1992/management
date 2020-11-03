package lk.recruitment.management.asset.applicant.controller;


import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.NonRelative;
import lk.recruitment.management.asset.applicant.service.ApplicantService;
import lk.recruitment.management.asset.applicant.service.NonRelativeService;
import lk.recruitment.management.asset.user_management.service.UserService;
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
public class NonRelativeController {
    private final NonRelativeService nonRelativeService;
    private final UserService userService;
    private final ApplicantService applicantService;

    public NonRelativeController(NonRelativeService nonRelativeService, UserService userService, ApplicantService applicantService) {
        this.nonRelativeService = nonRelativeService;
        this.userService = userService;
        this.applicantService = applicantService;
    }

    @GetMapping
    public String findAll(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("nonRelatives", nonRelativeService.findAll().stream()
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
        for (NonRelative nonRelative : applicant.getNonRelatives()) {
            nonRelative.setApplicant(applicantService.findByEmail(authentication.getName()));
            nonRelativeService.persist(nonRelative);
        }

        return "redirect:/applicant";
    }

}