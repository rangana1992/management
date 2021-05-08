package lk.recruitment_management.asset.gazette.controller;


import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.asset.gazette.entity.enums.GazetteStatus;
import lk.recruitment_management.asset.gazette.service.GazetteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping( "/gazette" )
public class GazetteController {
    private final GazetteService gazetteService;

    @Autowired
    public GazetteController(GazetteService gazetteService) {
        this.gazetteService = gazetteService;
    }

    private String commonMethod(Model model, boolean addStatus, Gazette gazette) {
        model.addAttribute("gazette", gazette);
        model.addAttribute("addStatus", addStatus);
        model.addAttribute("gazetteStatuses", GazetteStatus.values());
        return "gazette/addGazette";
    }

    @GetMapping
    public String authorPage(Model model) {
        model.addAttribute("gazettes", gazetteService.findAll());
        return "gazette/gazette";
    }


    @GetMapping( "/edit/{id}" )
    public String edit(@PathVariable( "id" ) Integer id, Model model) {
        return commonMethod(model, false, gazetteService.findById(id));
    }

    @GetMapping( "/add" )
    public String form(Model model) {
        return commonMethod(model, true, new Gazette());
    }

    // Above method support to send data to front end - All List, update, edit
    //Bellow method support to do back end function save, delete, update, search

    @PostMapping( value = {"/save", "/update"} )
    public String addGazette(@Valid @ModelAttribute Gazette gazette,
                                          BindingResult result, Model model) {
        if ( result.hasErrors() ) {
            return commonMethod(model, true, gazette);
        }
        gazetteService.persist(gazette);
        return "redirect:/gazette";
    }


    @RequestMapping( value = "/delete/{id}", method = RequestMethod.GET )
    public String delete(@PathVariable Integer id) {
        gazetteService.delete(id);
        return "redirect:/gazette";
    }


}
