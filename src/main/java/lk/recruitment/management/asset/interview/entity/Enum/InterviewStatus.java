package lk.recruitment.management.asset.interview.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterviewStatus {
ACT("Active"),
    CL("Close");

private final String interviewStatus;
}
