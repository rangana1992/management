package lk.recruitment.management.asset.applicant.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
    A("A Pass"), B("B Pass"), C("C Pass"), S("S Pass"), F("Fail");
    private final String result;
}
