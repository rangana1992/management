package lk.recruitment.management.asset.ag_office.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.recruitment.management.asset.common_asset.model.Enum.Province;
import lk.recruitment.management.asset.ag_office.entity.AgOffice;
import lk.recruitment.management.asset.ag_office.service.AgOfficeService;
import lk.recruitment.management.asset.district.controller.DistrictController;
import lk.recruitment.management.asset.district.entity.District;
import lk.recruitment.management.asset.district.service.DistrictService;
import lk.recruitment.management.util.interfaces.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/agOffice")
public class AgOfficeController implements AbstractController<AgOffice, Integer> {

    private final AgOfficeService agOfficeService;
    private final DistrictService districtService;

    @Autowired
    public AgOfficeController(AgOfficeService agOfficeService, DistrictService districtService) {
        this.agOfficeService = agOfficeService;
        this.districtService = districtService;
    }

    private String commonThing(Model model, Boolean booleanValue, AgOffice agOfficeObject) {
        model.addAttribute("provinces", Province.values());
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("agOffice", agOfficeObject);
        model.addAttribute("districtURL",
                MvcUriComponentsBuilder
                        .fromMethodName(DistrictController.class, "getDistrictByProvince", "")
                        .toUriString());
        return "agOffice/addAgOffice";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("agOffices", agOfficeService.findAll());
        return "agOffice/agOffice";
    }

    @GetMapping("/add")
    public String form(Model model) {
        return commonThing(model, false, new AgOffice());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("agOfficeDetail", agOfficeService.findById(id));
        return "agOffice/agOffice-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("districts", districtService.findAll());
        return commonThing(model, true, agOfficeService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid @ModelAttribute AgOffice agOffice, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return commonThing(model, false, agOffice);
        }
        redirectAttributes.addFlashAttribute("agOfficeDetail", agOfficeService.persist(agOffice));
        return "redirect:/agOffice";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        agOfficeService.delete(id);
        return "redirect:/agOffice";
    }

    @GetMapping(value = "/getAgOffice/{id}")
    @ResponseBody
    public MappingJacksonValue getAgOfficeByDistrict(@PathVariable Integer id) {
        District district = new District();
        district.setId(id);

        //MappingJacksonValue
        List<AgOffice> agOffices = agOfficeService.findByDistrict(district);

        //Create new mapping jackson value and set it to which was need to filter
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(agOffices);

        //simpleBeanPropertyFilter :-  need to give any id to addFilter method and created filter which was mentioned
        // what parameter's necessary to provide
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name");
        //filters :-  set front end required value to before filter

        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("AgOffice", simpleBeanPropertyFilter);
        //Employee :- need to annotate relevant class with JsonFilter  {@JsonFilter("Employee") }
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}
