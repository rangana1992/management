package lk.recruitment.management.asset.common_asset.service;

import lk.recruitment.management.asset.common_asset.model.Enum.BloodGroup;
import lk.recruitment.management.asset.common_asset.model.Enum.CivilStatus;
import lk.recruitment.management.asset.common_asset.model.Enum.Gender;
import lk.recruitment.management.asset.common_asset.model.Enum.Title;
import lk.recruitment.management.asset.employee.controller.EmployeeRestController;
import lk.recruitment.management.asset.employee.entity.Enum.Designation;
import lk.recruitment.management.asset.employee.entity.Enum.EmployeeStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.regex.Pattern;

@Service
public class CommonService {
    //common things to employee and offender - start
    public void commonUrlBuilder(Model model) {
        model.addAttribute("addStatus", true);
        model.addAttribute("designations", Designation.values());
/*        model.addAttribute("provinces", Province.values());
        model.addAttribute("districtUrl", MvcUriComponentsBuilder
                .fromMethodName(WorkingPlaceRestController.class, "getDistrict", "")
                .build()
                .toString());
        model.addAttribute("stationUrl", MvcUriComponentsBuilder
                .fromMethodName(WorkingPlaceRestController.class, "getStation", "")
                .build()
                .toString());*/
        Object[] arg = {"designation", "id"};
        model.addAttribute("employeeUrl", MvcUriComponentsBuilder
                .fromMethodName(EmployeeRestController.class, "getEmployeeByWorkingPlace", arg)
                .build()
                .toString());
    }

    public void commonEmployeeAndOffender(Model model) {
        model.addAttribute("title", Title.values());
        model.addAttribute("gender", Gender.values());
        model.addAttribute("civilStatus", CivilStatus.values());
        model.addAttribute("employeeStatus", EmployeeStatus.values());
        model.addAttribute("designation", Designation.values());
        model.addAttribute("bloodGroup", BloodGroup.values());
    }

    //common things to employee and offender - end
    //common mobile number length employee,offender,guardian, petitioner - start
    // private final mobile length validator
    public String commonMobileNumberLengthValidator(String number) {
        if (number.length() == 9) {
            number = "0".concat(number);
        }
        return number;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
