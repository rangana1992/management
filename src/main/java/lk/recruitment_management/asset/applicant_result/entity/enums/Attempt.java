package lk.recruitment_management.asset.applicant_result.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Attempt {
    FIRST("First"),
    SECOND("Second");

    private final String attempt;
}
