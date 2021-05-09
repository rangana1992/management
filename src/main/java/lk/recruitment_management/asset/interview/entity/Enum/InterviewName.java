package lk.recruitment_management.asset.interview.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterviewName {
FIRST("First Interview"),
SECOND("Second Interview"),
THIRD("Third Interview"),
FOURTH("Fourth Interview");
private final String interviewName;
}
