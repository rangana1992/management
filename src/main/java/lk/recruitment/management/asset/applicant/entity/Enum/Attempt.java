package lk.recruitment.management.asset.applicant.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attempt {
    FIRST("First"),
    SECOND("Second");

    private final String attempt;
}
