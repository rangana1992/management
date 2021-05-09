package lk.recruitment_management.asset.interview.controller;

import lk.recruitment_management.asset.interview.entity.Enum.InterviewName;
import lk.recruitment_management.asset.interview.entity.Enum.InterviewStatus;
import lk.recruitment_management.asset.interview.entity.Interview;
import lk.recruitment_management.asset.interview.service.InterviewService;
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
@RequestMapping("/interview")
public class InterviewController implements AbstractController<Interview, Integer> {
    private final InterviewService interviewService;
    private final InterviewParameterService interviewParameterService;

    public InterviewController(InterviewService interviewService, InterviewParameterService interviewParameterService) {
        this.interviewService = interviewService;
        this.interviewParameterService = interviewParameterService;
    }

    private String commonThing(Model model, Boolean booleanValue, Interview interview) {
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("interview", interview);
        model.addAttribute("interviewNames", InterviewName.values());
        model.addAttribute("interviewParameters",interviewParameterService.findAll());
        model.addAttribute("interviewStatuss", InterviewStatus.values());
        return "interview/addInterview";
    }


    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("interviews", interviewService.findAll());
   return "interview/interview";

    }


    @GetMapping("/add")
    public String form(Model model) {
        return commonThing(model, false, new Interview());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("interviewDetail", interviewService.findById(id));
        return "interview/interview-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, true, interviewService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid Interview interview, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return commonThing(model, true, interview);
        }
        System.out.println("Parameter count : " +interview.getInterviewParameters().size());
        redirectAttributes.addFlashAttribute("interviewDetail", interviewService.persist(interview));
        return "redirect:/interview";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        interviewService.delete(id);
        return "redirect:/interview";
    }
}
