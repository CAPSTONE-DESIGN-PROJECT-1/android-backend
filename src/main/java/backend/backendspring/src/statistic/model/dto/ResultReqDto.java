package backend.backendspring.src.statistic.model.dto;

import backend.backendspring.src.paper.model.entity.PaperEntity;
import backend.backendspring.src.statistic.model.ResultDto;
import backend.backendspring.src.statistic.model.entity.StatisticEntity;
import backend.backendspring.src.user.model.entity.UserEntity;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ResultReqDto {

    private Long userIdx;

    private List<ResultDto> resultDtoList;

}
