package backend.backendspring.src.statistic.model.entity;

import backend.backendspring.config.BaseEntity;
import backend.backendspring.src.paper.model.entity.PaperEntity;
import backend.backendspring.src.user.model.entity.UserEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@ToString(exclude = {"userEntity", "paperEntity"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "statistic")
@DynamicInsert
public class StatisticEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statisticIdx;

    @Column(length = 20, nullable = false)
    private String categoryNum;

    @Column(length = 10, nullable = false)
    private String userAnswer;

    @Column(nullable = false)
    private Boolean isCorrect;

    /**
     * 엔터티 관계
     * ...
     * 코드 추가
     * ...
     */
    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩
    @JoinColumn(name = "userIdx")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paperIdx")
    private PaperEntity paperEntity;

    @Builder
    public StatisticEntity(String categoryNum, String userAnswer, Boolean isCorrect,
                           UserEntity userEntity, PaperEntity paperEntity) {
        this.categoryNum = categoryNum;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
        this.userEntity = userEntity;
        this.paperEntity = paperEntity;
    }

    public void updateStatistics(String userAnswer, Boolean isCorrect) {
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }

}
