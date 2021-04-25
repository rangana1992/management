package lk.recruitment_management.asset.gazzet.controller;


import lk.recruitment_management.asset.gazzet.entity.Gazzet;
import lk.recruitment_management.asset.gazzet.service.GazzetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping( "/gazzet" )
public class GazzetController {
    private final GazzetService gazzetService;

    @Autowired
    public GazzetController(GazzetService gazzetService) {
        this.gazzetService = gazzetService;
    }

    private String commonMethod(Model model, boolean addStatus, Gazzet gazzet) {
        model.addAttribute("gazzet", gazzet);
        model.addAttribute("addStatus", addStatus);
        return "gazzet/addGazzet";
    }

    @GetMapping
    public String authorPage(Model model) {
        model.addAttribute("authors", gazzetService.findAll());
        return "gazzet/gazzet";
    }


    @GetMapping( "/edit/{id}" )
    public String edit(@PathVariable( "id" ) Integer id, Model model) {
        return commonMethod(model, false, gazzetService.findById(id));
    }

    @GetMapping( "/add" )
    public String form(Model model) {
        return commonMethod(model, true, new Gazzet());
    }

    // Above method support to send data to front end - All List, update, edit
    //Bellow method support to do back end function save, delete, update, search

    @PostMapping( value = {"/save", "/update"} )
    public String addGazzet(@Valid @ModelAttribute Gazzet gazzet,
                                          BindingResult result, Model model) {
        if ( result.hasErrors() ) {
            return commonMethod(model, true, gazzet);
        }
        gazzetService.persist(gazzet);
        return "redirect:/gazzet";
    }


    @RequestMapping( value = "/delete/{id}", method = RequestMethod.GET )
    public String delete(@PathVariable Integer id) {
        gazzetService.delete(id);
        return "redirect:/gazzet";
    }


}
