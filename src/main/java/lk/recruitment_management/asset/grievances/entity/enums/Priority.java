package lk.recruitment_management.asset.grievances.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {

    HIG("Immediate"),
    NOM("Normal"),
    LOW("Medium");

    private final String priority;
}
