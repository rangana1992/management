package lk.recruitment.management.asset.applicant.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudentStatus {
    ACTIVE("Active"),
    BATCHDROP("Batch Drop"),
    COURSEDROP("Course Drop"),
    NOTCOMPLETE("Not Complete"),
    COMPLETED("Completed");

    private final String studentStatus;
}
