package backend.backendspring.src.statistic.model;

import backend.backendspring.src.paper.model.entity.PaperEntity;
import backend.backendspring.src.statistic.model.entity.StatisticEntity;
import backend.backendspring.src.user.model.entity.UserEntity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ResultDto {
    private Long paperIdx;
    private String categoryNum;
    private String userAnswer;
    private Boolean isCorrect;

    public StatisticEntity toEntity(UserEntity userEntity, PaperEntity paperEntity) {
        return StatisticEntity.builder()
                .userEntity(userEntity)
                .paperEntity(paperEntity)
                .categoryNum(categoryNum)
                .userAnswer(userAnswer)
                .isCorrect(isCorrect)
                .build();
    }

}
