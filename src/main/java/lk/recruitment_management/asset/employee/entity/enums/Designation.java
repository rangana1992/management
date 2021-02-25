package lk.recruitment_management.asset.employee.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Designation {
  IGP("Inspector Genaral of Police"),
  SDIG("Senior Deputy Inspector Genaral"),
  WSDIG("Woman Senior Deputy Inspector Genaral"),
  DIG("Deputy Inspector Genaral"),
  WDIG("Woman Deputy Inspector Genaral"),
  SSP("Senior Superintendent of Police"),
  WSSP("Woman Senior Superintendent of Police"),
  SP("Superintendent of Police"),
  WSP("Woman Superintendent of Police"),
  ASP("Assistant Superintendent of Police"),
  WASP("Woman Assistant Superintendent of Police"),
  CI("Chief Inspector"),
  WCI("Woman Chief Inspector"),
  IP("Inspector"),
  WIP("Woman Inspector"),
  SI("Sub Inspector"),
  WSI("Woman Sub Inspector"),
  PS("Police Sargent"),
  WPS("Woman Police Sargent"),
  PC("Police Constable"),
  WPC("Woman Police Constable");




  private final String designation;
}
