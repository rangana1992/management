package lk.recruitment.management.asset.applicant.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("PoliceStation")
@ToString
public class PoliceStation extends AuditEntity {

}
