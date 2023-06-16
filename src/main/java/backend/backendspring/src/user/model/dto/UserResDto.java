package backend.backendspring.src.user.model.dto;

import backend.backendspring.src.user.model.entity.UserEntity;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class UserResDto {
    private Long userIdx;
    private String imageUrl;
    private String nickname;
    private String goal;
    private String day;

    public UserResDto(UserEntity userEntity) {
        userIdx = userEntity.getUserIdx();
        imageUrl = userEntity.getImageUrl();
        nickname = userEntity.getNickname();
        goal = userEntity.getGoal();
        day = userEntity.getDay();
    }
}
