package backend.backendspring.src.paper.model.dto;

import backend.backendspring.src.paper.model.entity.PaperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PaperResDto {
    private Long paperIdx;
    private Integer number;
    private String question;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private String correctAnswer;
    private String categoryNum;

    public PaperResDto(PaperEntity paperEntity) {
        paperIdx = paperEntity.getPaperIdx();
        number = paperEntity.getNumber();
        question = paperEntity.getQuestion();
        choiceA = paperEntity.getChoiceA();
        choiceB = paperEntity.getChoiceB();
        choiceC = paperEntity.getChoiceC();
        choiceD = paperEntity.getChoiceD();
        correctAnswer = paperEntity.getCorrectAnswer();
        categoryNum = paperEntity.getCategoryNum();
    }

}
