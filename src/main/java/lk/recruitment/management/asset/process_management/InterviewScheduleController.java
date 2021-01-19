package lk.recruitment.management.asset.process_management;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/interviewSchedule")
public class InterviewScheduleController {

  @GetMapping
  public String form(){
    return "";
  }

  @PostMapping
  public String dateCount(){
    return "";
  }

}
