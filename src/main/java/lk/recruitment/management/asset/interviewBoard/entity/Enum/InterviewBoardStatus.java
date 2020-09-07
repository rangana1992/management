package lk.recruitment.management.asset.interviewBoard.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterviewBoardStatus {
    ACT("Active"),
    CL("Close");

    private final String interviewBoardStatus;
}
