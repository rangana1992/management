package lk.recruitment.management.asset.applicant.controller;


import lk.recruitment.management.asset.applicant.entity.ApplicantSubjectResult;
import lk.recruitment.management.asset.applicant.entity.Enum.Result;
import lk.recruitment.management.asset.applicant.service.ApplicantSubjectResultService;
import lk.recruitment.management.asset.employee.entity.Enum.StreamLevel;
import lk.recruitment.management.asset.subject.entity.Stream;
import lk.recruitment.management.asset.subject.service.StreamService;
import lk.recruitment.management.asset.subject.service.SubjectService;
import lk.recruitment.management.util.interfaces.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/applicantSubjectResult")
public class ApplicantSubjectResultController implements AbstractController<ApplicantSubjectResult, Integer> {

    private final ApplicantSubjectResultService applicantSubjectResultService;
    private final SubjectService subjectService;
    private final StreamService streamService;

    public ApplicantSubjectResultController(ApplicantSubjectResultService applicantSubjectResultService, SubjectService subjectService, StreamService streamService) {
        this.applicantSubjectResultService = applicantSubjectResultService;
        this.subjectService = subjectService;
        this.streamService = streamService;
    }


    private String commonThing(Model model, Boolean booleanValue, ApplicantSubjectResult applicantSubjectResultObject) {
        model.addAttribute("streamLevels",streamService.findAll() );
        model.addAttribute("results", Result.values() );
        model.addAttribute("subject", subjectService.findAll() );
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("applicantSubjectResult", applicantSubjectResultObject);
        return "applicant/applicantSubjectResult";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("applicantSubjectResults", applicantSubjectResultService.findAll());
        return "applicant/applicantSubjectResult";
    }

       @GetMapping("/add")
    public String form(Model model) {
        return commonThing(model, false, new ApplicantSubjectResult());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("applicantSubjectResultDetail", applicantSubjectResultService.findById(id));
        return "applicantSubjectResult/applicantSubjectResult-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, true, applicantSubjectResultService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid @ModelAttribute ApplicantSubjectResult applicantSubjectResult, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return commonThing(model, false, applicantSubjectResult);
        }
        redirectAttributes.addFlashAttribute("applicantSubjectResultDetail", applicantSubjectResultService.persist(applicantSubjectResult));
        return "redirect:/applicantSubjectResult";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        applicantSubjectResultService.delete(id);
        return "redirect:/applicantSubjectResult";
    }


}
