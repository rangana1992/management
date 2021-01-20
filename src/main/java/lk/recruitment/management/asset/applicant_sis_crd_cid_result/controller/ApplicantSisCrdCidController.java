package lk.recruitment.management.asset.applicant_sis_crd_cid_result.controller;

import lk.recruitment.management.asset.applicant_sis_crd_cid_result.service.ApplicantSisCrdCidService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("/applicantSisCrdCid")
public class ApplicantSisCrdCidController {
  private final ApplicantSisCrdCidService applicantSisCrdCidService;

  public ApplicantSisCrdCidController(ApplicantSisCrdCidService applicantSisCrdCidService) {
    this.applicantSisCrdCidService = applicantSisCrdCidService;
  }

}
