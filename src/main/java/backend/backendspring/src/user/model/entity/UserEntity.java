package backend.backendspring.src.user.model.entity;

import backend.backendspring.config.BaseEntity;
import backend.backendspring.src.statistic.model.entity.StatisticEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 의미없는 객체 생성을 방지하기 위함 / 빌더와 같이 사용할 수 없음 -> 생성자 메소드에 빌더 붙임
@Entity
@Table(name = "user")
@DynamicInsert
@ToString
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(length = 20, unique = true, nullable = false)
    private String uniqueId;

    @Column(length = 100, unique = true, nullable = false)
    private String password;

//    @Column(length = 20, unique = true, nullable = false)
//    private String nickname;

    @Column(length = 20)
    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String goal;

    @Column(columnDefinition = "TEXT")
    private String day;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * 엔터티 관계
     * ...
     * 코드 추가
     * ...
     */
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatisticEntity> statisticEntities = new ArrayList<>();

    @Builder
    public UserEntity(String uniqueId, String password, String nickname,
                      String goal, String day, String imageUrl, Role role) {
        this.uniqueId = uniqueId;
        this.password = password;
        this.nickname = nickname;
        this.goal = goal;
        this.day = day;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public void update(String uniqueId, String password) {
        this.uniqueId = uniqueId;
        this.password = password;
    }

    public void setProfile(String nickname, String goal,String day) {
        this.nickname = nickname;
        this.goal = goal;
        this.day = day;
    }

    public void updateProfile(String nickname, String goal) {
        this.nickname = nickname;
        this.goal = goal;
    }

}
