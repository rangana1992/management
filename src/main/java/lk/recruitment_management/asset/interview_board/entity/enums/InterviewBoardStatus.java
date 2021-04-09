package lk.recruitment_management.asset.interview_board.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InterviewBoardStatus {
    ACT("Active"),
    CL("Close");

    private final String interviewBoardStatus;
}
