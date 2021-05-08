package lk.recruitment_management.asset.gazzet.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GazzetStatus {
  AC("Active"),
  CL("Closed");
  private final String gazzetStatus;
}
