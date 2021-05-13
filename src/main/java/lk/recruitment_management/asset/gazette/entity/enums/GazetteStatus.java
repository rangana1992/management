package lk.recruitment_management.asset.gazette.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GazetteStatus {
  AC("Active"),
  IN("Candidate in interviewing process"),
  CL("Closed");
  private final String gazetteStatus;
}
