package backend.backendspring.src.user.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class EditReqDto {

    private String uniqueId; // 4~20자, 영문자 소문자로 시작하고 영문자, 숫자 포함

    private String password; // 8~15자, 숫자,영문자(대소문자),특수기호 포함

    private String nickname; // 4~20자, 영문자(대소문자), 한글, 숫자 포함

    private String goal;

}
