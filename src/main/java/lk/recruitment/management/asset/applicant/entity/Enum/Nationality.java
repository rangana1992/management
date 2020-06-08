package lk.recruitment.management.asset.applicant.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Nationality {

    SIN("Sinhala"),
    TML("Tamil"),
    MLM("Muslim"),
    BGR("Burghers");


    private final String nationality;
}
