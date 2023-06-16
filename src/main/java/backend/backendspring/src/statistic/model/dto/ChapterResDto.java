package backend.backendspring.src.statistic.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ChapterResDto {
    private Integer chapter;
    private Integer correct;
    private Integer total;
}
