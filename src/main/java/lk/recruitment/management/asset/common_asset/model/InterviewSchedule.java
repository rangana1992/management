package lk.recruitment.management.asset.common_asset.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InterviewSchedule {
  private List< InterviewScheduleList > interviewScheduleLists;
}
