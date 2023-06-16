package backend.backendspring.src.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LoginReqDto {

    @NotNull(message = "아이디는 필수 값입니다.")
    @Pattern(regexp = "^[a-z]+[a-z0-9]{3,20}$")
    private String uniqueId;

    @NotNull(message = "비밀번호는 필수 값입니다.")
    @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

}
