package backend.backendspring.src.paper.model.entity;

import backend.backendspring.config.BaseEntity;
import backend.backendspring.src.statistic.model.entity.StatisticEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "paper")
@DynamicInsert
public class PaperEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paperIdx;

    @Column(length = 20, nullable = false)
    private String categoryNum;

    @Column(nullable = false)
    private Integer number;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String question;

    @Column(length = 100, nullable = false)
    private String choiceA;

    @Column(length = 100, nullable = false)
    private String choiceB;

    @Column(length = 100, nullable = false)
    private String choiceC;

    @Column(length = 100, nullable = false)
    private String choiceD;

    @Column(length = 10, nullable = false)
    private String correctAnswer;


    /**
     * 엔터티 관계
     * ...
     * 코드 추가
     * ...
     */
    @OneToMany(mappedBy = "paperEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatisticEntity> statisticEntities = new ArrayList<>();


    @Builder
    public PaperEntity(String categoryNum, Integer number, String question,
                       String choiceA, String choiceB, String choiceC, String choiceD,
                       String correctAnswer /*PaperDetailEntity paperDetailEntity*/) {
        this.categoryNum = categoryNum;
        this.number = number;
        this.question = question;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.choiceD = choiceD;
        this.correctAnswer = correctAnswer;
    }

}
