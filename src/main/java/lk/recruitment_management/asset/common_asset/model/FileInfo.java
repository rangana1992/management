package lk.recruitment_management.asset.common_asset.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileInfo {
    private String filename;
    private LocalDateTime createAt;
    private String url;
}

