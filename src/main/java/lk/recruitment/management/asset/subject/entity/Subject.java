package lk.recruitment.management.asset.subject.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.recruitment.management.asset.applicant.entity.ApplicantResult;
import lk.recruitment.management.asset.applicant.entity.Enum.StreamLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 60, message = "Your name length should be 13")
    private String name;

    @Enumerated(EnumType.STRING)
    private StreamLevel streamLevel;


    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @Fetch( FetchMode.SUBSELECT)
    @JoinTable(name = "subject_stream",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "stream_id"))
    private List<Stream> streams;
    @OneToMany(mappedBy ="applicant" )
    private  List<ApplicantResult> applicantResults;

}
