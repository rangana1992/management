package lk.recruitment_management.asset.interview_parameter.controller;

import lk.recruitment_management.asset.interview_parameter.entity.InterviewParameter;
import lk.recruitment_management.asset.interview_parameter.service.InterviewParameterService;
import lk.recruitment_management.util.interfaces.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/interviewParameter")
public class InterviewParameterController implements AbstractController<InterviewParameter, Integer> {
    private final InterviewParameterService interviewParameterService;

    public InterviewParameterController(InterviewParameterService interviewParameterService) {
        this.interviewParameterService = interviewParameterService;
    }

    private String commonThing(Model model, Boolean booleanValue, InterviewParameter interviewParameter) {
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("interviewParameter", interviewParameter);
        return "interviewParameter/addInterviewParameter";
    }


    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("interviewParameters", interviewParameterService.findAll());
        return "interviewParameter/interviewParameter";
    }

    @GetMapping("/add")
    public String form(Model model) {
        return commonThing(model, false, new InterviewParameter());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("interviewParameterDetail", interviewParameterService.findById(id));
        return "interviewParameter/interviewParameter-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, true, interviewParameterService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid InterviewParameter interviewParameter, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return commonThing(model, true, interviewParameter);
        }
        redirectAttributes.addFlashAttribute("interviewParameterDetail", interviewParameterService.persist(interviewParameter));
        return "redirect:/interviewParameter";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        interviewParameterService.delete(id);
        return "redirect:/interviewParameter";
    }
}
