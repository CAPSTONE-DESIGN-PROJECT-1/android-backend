package backend.backendspring.src.statistic.model.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class StatisticResDto {
    private Integer total;
    private Integer correct;
    private List<Integer> dayList;
}
