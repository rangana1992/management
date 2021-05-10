package lk.recruitment_management.asset.applicant.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Nationality {

    SIN("Sinhala"),
    TML("Tamil"),
    MLM("Muslim"),
    BGR("Burgher");


    private final String nationality;
}
