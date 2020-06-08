package lk.recruitment.management.asset.subject.controller;


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
@RequestMapping("/stream")
public class StreamController implements AbstractController<Stream, Integer> {
private final StreamService streamService;
private final SubjectService subjectService;


    public StreamController(StreamService streamService, SubjectService subjectService) {
        this.streamService = streamService;
        this.subjectService = subjectService;
    }


    private String commonThing(Model model, Boolean booleanValue, Stream streamObject) {
        model.addAttribute("streamLevels", StreamLevel.values() );
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("stream", streamObject);
        model.addAttribute("subjectList", subjectService.findAll());


        return "subject/addStream";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("streams", streamService.findAll());
        return "subject/stream";
    }

       @GetMapping("/add")
    public String form(Model model) {
        return commonThing(model, false, new Stream());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("streamDetail", streamService.findById(id));
        return "subject/stream-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        return commonThing(model, true, streamService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid @ModelAttribute Stream stream, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return commonThing(model, false, stream);
        }
        redirectAttributes.addFlashAttribute("streamDetail", streamService.persist(stream));
        return "redirect:/stream";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        streamService.delete(id);
        return "redirect:/stream";
    }


}
