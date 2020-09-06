package lk.recruitment.management.asset.interviewBoard.controller;

import lk.recruitment.management.asset.employee.service.EmployeeService;
import lk.recruitment.management.asset.interview.entity.Enum.InterviewStatus;
import lk.recruitment.management.asset.interviewBoard.entity.Enum.InterviewBoardStatus;
import lk.recruitment.management.asset.interviewBoard.entity.InterviewBoard;
import lk.recruitment.management.asset.interviewBoard.service.InterviewBoardService;
import lk.recruitment.management.util.interfaces.AbstractController;
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
@RequestMapping("/interviewBoard")
public class InterviewBoardController implements AbstractController<InterviewBoard, Integer> {
    private final InterviewBoardService interviewBoardService;
    private final EmployeeService employeeService;

    public InterviewBoardController(InterviewBoardService interviewBoardService, EmployeeService employeeService) {
        this.interviewBoardService = interviewBoardService;
        this.employeeService = employeeService;
    }


    private String commonThing(Model model, Boolean booleanValue, InterviewBoard interviewBoard) {
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("interviewBoard", interviewBoard);
        model.addAttribute("employees", employeeService.findAll());
        model.addAttribute("interviewBoardStatus", InterviewBoardStatus.values());
        return "interviewBoard/addInterview";
    }


    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("interviewBoards", interviewBoardService.findAll());
        return "interviewBoard/interviewBoard";

    }

    @GetMapping("/add")
    public String form(Model model) {
        return commonThing(model, false, new InterviewBoard());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("interviewBoardDetail", interviewBoardService.findById(id));
        return "interviewBoard/interviewBoard-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, true, interviewBoardService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid InterviewBoard interviewBoard, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            return commonThing(model, true, interviewBoard);
        }
        redirectAttributes.addFlashAttribute("interviewBoardDetail", interviewBoardService.persist(interviewBoard));
        return "redirect:/interviewBoard";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        interviewBoardService.delete(id);
        return "redirect:/interviewBoard";
    }
}
