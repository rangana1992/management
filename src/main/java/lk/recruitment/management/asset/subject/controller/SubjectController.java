package lk.recruitment.management.asset.subject.controller;


import lk.recruitment.management.asset.applicant.entity.Enum.StreamLevel;
import lk.recruitment.management.asset.subject.entity.Subject;
import lk.recruitment.management.asset.subject.service.SubjectService;
import lk.recruitment.management.util.interfaces.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/subject")
public class SubjectController implements AbstractController<Subject, Integer> {

    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private String commonThing(Model model, Boolean booleanValue, Subject subjectObject) {
        model.addAttribute("streamLevels", StreamLevel.values() );
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("subject", subjectObject);
        return "subject/addSubject";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("subjects", subjectService.findAll());
        return "subject/subject";
    }

       @GetMapping("/add")
    public String form(Model model) {
        return commonThing(model, false, new Subject());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("subjectDetail", subjectService.findById(id));
        return "subject/subject-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, true, subjectService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid @ModelAttribute Subject subject, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return commonThing(model, false, subject);
        }
        redirectAttributes.addFlashAttribute("subjectDetail", subjectService.persist(subject));
        return "redirect:/subject";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        subjectService.delete(id);
        return "redirect:/subject";
    }


}
