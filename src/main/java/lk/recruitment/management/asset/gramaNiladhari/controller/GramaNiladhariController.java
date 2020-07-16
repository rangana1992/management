package lk.recruitment.management.asset.gramaNiladhari.controller;


import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lk.recruitment.management.asset.agOffice.controller.AgOfficeController;
import lk.recruitment.management.asset.agOffice.service.AgOfficeService;
import lk.recruitment.management.asset.commonAsset.model.Enum.Province;
import lk.recruitment.management.asset.district.controller.DistrictController;
import lk.recruitment.management.asset.district.service.DistrictService;
import lk.recruitment.management.asset.gramaNiladhari.entity.GramaNiladhari;
import lk.recruitment.management.asset.gramaNiladhari.service.GramaNiladhariService;

import lk.recruitment.management.asset.policeStation.controller.PoliceStationController;
import lk.recruitment.management.asset.policeStation.entity.PoliceStation;
import lk.recruitment.management.asset.policeStation.Service.PoliceStationService;
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
@RequestMapping("/gramaNiladhari")
public class GramaNiladhariController implements AbstractController<GramaNiladhari, Integer> {

    private final GramaNiladhariService gramaNiladhariService;
    private final DistrictService districtService;
    private final AgOfficeService agOfficeService;
    private final PoliceStationService policeStationService;

    @Autowired
    public GramaNiladhariController(GramaNiladhariService gramaNiladhariService, DistrictService districtService, AgOfficeService agOfficeService, PoliceStationService policeStationService) {
        this.gramaNiladhariService = gramaNiladhariService;
        this.districtService = districtService;
        this.agOfficeService = agOfficeService;
        this.policeStationService = policeStationService;
    }

    private String commonThing(Model model, Boolean booleanValue, GramaNiladhari gramaNiladhariObject) {
        model.addAttribute("addStatus", booleanValue);
        model.addAttribute("gramaNiladhari", gramaNiladhariObject);
        model.addAttribute("provinces", Province.values());
        model.addAttribute("districtURL",
                MvcUriComponentsBuilder
                        .fromMethodName(DistrictController.class, "getDistrictByProvince", "")
                        .toUriString());
        model.addAttribute("agOfficeURL",
                MvcUriComponentsBuilder
                        .fromMethodName(AgOfficeController.class, "getAgOfficeByDistrict", "")
                        .toUriString());
        model.addAttribute("policeStationURL",
                MvcUriComponentsBuilder
                        .fromMethodName(PoliceStationController.class, "getPoliceStationByAgOffice", "")
                        .toUriString());
        return "gramaNiladhari/addGramaNiladhari";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("gramaNiladharis", gramaNiladhariService.findAll());
        return "gramaNiladhari/gramaNiladhari";
    }

    @GetMapping("/add")
    public String form(Model model) {
        return commonThing(model, false, new GramaNiladhari());
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Integer id, Model model) {
        model.addAttribute("gramaNiladhariDetail", gramaNiladhariService.findById(id));
        return "gramaNiladhari/gramaNiladhari-detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("agOffices", agOfficeService.findAll());
        model.addAttribute("policeStations", policeStationService.findAll());
        return commonThing(model, true, gramaNiladhariService.findById(id));
    }

    @PostMapping(value = {"/save", "/update"})
    public String persist(@Valid @ModelAttribute GramaNiladhari gramaNiladhari, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            return commonThing(model, false, gramaNiladhari);
        }
        redirectAttributes.addFlashAttribute("gramaNiladhariDetail", gramaNiladhariService.persist(gramaNiladhari));
        return "redirect:/gramaNiladhari";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        gramaNiladhariService.delete(id);
        return "redirect:/gramaNiladhari";
    }
    @GetMapping(value = "/getGramaNiladhari/{id}")
    @ResponseBody
    public MappingJacksonValue getGramaNiladhariByPolice(@PathVariable int id) {
        PoliceStation policeStation=new PoliceStation();
        policeStation.setId(id);

        //MappingJacksonValue
        List<GramaNiladhari> gramaNiladharis = gramaNiladhariService.findByPoliceStation(policeStation);
        //employeeService.findByWorkingPlace(workingPlaceService.findById(id));

        //Create new mapping jackson value and set it to which was need to filter
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(gramaNiladharis);

        //simpleBeanPropertyFilter :-  need to give any id to addFilter method and created filter which was mentioned
        // what parameter's necessary to provide
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name");
        //filters :-  set front end required value to before filter

        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("GramaNiladhari", simpleBeanPropertyFilter);
        //Employee :- need to annotate relevant class with JsonFilter  {@JsonFilter("Employee") }
        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }

}
