package backend.backendspring.src.user;

import backend.backendspring.config.BaseException;
import backend.backendspring.config.BaseResponse;
import backend.backendspring.config.BaseResponseStatus;
import backend.backendspring.security.jwt.JwtTokenProvider;
import backend.backendspring.src.user.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import static backend.backendspring.utils.ValidationRegex.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("kisa/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
//
//    /**
//     * 이미지 포함 회원가입
//     * [POST] "/users/signup"
//     */
//    @PostMapping("/signup")
//    @ResponseBody
//    public BaseResponse<JoinResDto> join(@RequestPart(value = "joinReqDto")JoinReqDto joinReqDto,
//                                         @RequestPart(value = "file", required = false)MultipartFile file) {
//
//        System.out.println(joinReqDto);
//        //아이디
//        if (joinReqDto.getUniqueId() == null) {
//            return new BaseResponse<>(BaseResponseStatus.ID_NULL_ERROR);
//        }
//        //아이디 정규표현: 영어, 숫자 포함 3~20자리 이내
//        if (!isRegexUniqueId(joinReqDto.getUniqueId())) {
//            return new BaseResponse<>(BaseResponseStatus.ID_REGEX_ERROR);
//        }
//
//        //비밀번호
//        if (joinReqDto.getPassword() == null) {
//            return new BaseResponse<>(BaseResponseStatus.PASSWORD_NULL_ERROR);
//        }
//        //비밀번호 정규표현: 숫자, 문자, 특수문자 포함 8~15자리 이내
//        if (!isRegexPassword(joinReqDto.getPassword())) {
//            return new BaseResponse<>(BaseResponseStatus.PASSWORD_REGEX_ERROR);
//        }
//
//        //닉네임
//        if (joinReqDto.getNickname() == null) {
//            return new BaseResponse<>(BaseResponseStatus.NICKNAME_NULL_ERROR);
//        }
//        //닉네임 정규표현: 4~20자, 영문자(대소문자), 한글, 숫자 포함
//        if (!isRegexNickname(joinReqDto.getNickname())) {
//            return new BaseResponse<>(BaseResponseStatus.NICKNAME_REGEX_ERROR);
//        }
//
//        try {
//            JoinResDto joinResDto = userService.join(joinReqDto, file);
//            return new BaseResponse<>(joinResDto);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//
//    }

    /**
     * 이미지 없이 회원가입
     * [POST] "/users/signup/test"
     */
    @PostMapping("/signup/test")
    @ResponseBody
    public BaseResponse<JoinResDto> joinTest(@RequestBody JoinReqDto joinReqDto) {

        System.out.println(joinReqDto);
        //아이디
        if (joinReqDto.getUniqueId() == null) {
            return new BaseResponse<>(BaseResponseStatus.ID_NULL_ERROR);
        }
        //아이디 정규표현: 영어, 숫자 포함 3~20자리 이내
        if (!isRegexUniqueId(joinReqDto.getUniqueId())) {
            return new BaseResponse<>(BaseResponseStatus.ID_REGEX_ERROR);
        }

        //비밀번호
        if (joinReqDto.getPassword() == null) {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_NULL_ERROR);
        }
        //비밀번호 정규표현: 숫자, 문자, 특수문자 포함 8~15자리 이내
        if (!isRegexPassword(joinReqDto.getPassword())) {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_REGEX_ERROR);
        }

        try {
            JoinResDto joinResDto = userService.joinTest(joinReqDto);
            return new BaseResponse<>(joinResDto);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 초기 프로필 설정
     * [PATCH] "/users/:userIdx/setting"
     */
    @PatchMapping("/{userIdx}/setting")
    @ResponseBody
    public BaseResponse<String> setProfile(@RequestBody JoinReqDto joinReqDto,
                                           @PathVariable Long userIdx, ServletRequest request) {

        //닉네임
        if (joinReqDto.getNickname() == null) {
            return new BaseResponse<>(BaseResponseStatus.NICKNAME_NULL_ERROR);
        }
        //닉네임 정규표현: 4~20자, 영문자(대소문자), 한글, 숫자 포함
        if (!isRegexNickname(joinReqDto.getNickname())) {
            return new BaseResponse<>(BaseResponseStatus.NICKNAME_REGEX_ERROR);
        }

        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != userIdx) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        try {
            userService.setProfile(joinReqDto, userIdx);
            return new BaseResponse<>("초기 프로필 설정에 성공하였습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 로그인
     * [POST] "/users/login"
     */
    @PostMapping("/login")
    @ResponseBody
    public BaseResponse<LoginResDto> login(@RequestBody LoginReqDto loginReqDto) {

        //아이디
        if (loginReqDto.getUniqueId() == null) {
            return new BaseResponse<>(BaseResponseStatus.ID_NULL_ERROR);
        }
        //아이디 정규표현: 영어, 숫자 포함 3~20자리 이내
        if (!isRegexUniqueId(loginReqDto.getUniqueId())) {
            return new BaseResponse<>(BaseResponseStatus.ID_REGEX_ERROR);
        }

        //비밀번호
        if (loginReqDto.getPassword() == null) {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_NULL_ERROR);
        }
        //비밀번호 정규표현: 숫자, 문자, 특수문자 포함 8~15자리 이내
        if (!isRegexPassword(loginReqDto.getPassword())) {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_REGEX_ERROR);
        }

        try {
            LoginResDto loginResDto = userService.login(loginReqDto);
            return new BaseResponse<>(loginResDto);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 특정 회원 조회
     * [GET] "/users/:userIdx"
     */
    @GetMapping("/{userIdx}")
    @ResponseBody
    public BaseResponse<UserResDto> getUser(@PathVariable Long userIdx, ServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != userIdx) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        try {
            UserResDto userResDto = userService.getUser(userIdx);
            return new BaseResponse<>(userResDto);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }


    /**
     * 회원탈퇴
     * [PATCH] "/users/:userIdx/d"
     */
    @PatchMapping("/{userIdx}/d")
    @ResponseBody
    public BaseResponse<String> resign(@PathVariable Long userIdx, ServletRequest request) throws Exception {

        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != userIdx) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        try {
            userService.resign(userIdx);
            return new BaseResponse<>("회원 탈퇴에 성공하였습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 프로필정보수정(nickname/goal)
     * [PATCH] "/users/:userIdx/edit/setting
     */
    @PatchMapping("/{userIdx}/edit/setting")
    @ResponseBody
    public BaseResponse<String> editProfile(@PathVariable Long userIdx, @RequestBody EditReqDto editReqDto, ServletRequest request) {
        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != userIdx) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        try {
            userService.editProfile(userIdx, editReqDto);
            return new BaseResponse<>("수정에 성공하였습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 개인정보수정(ID/PW)
     * [PATCH] "/users/:userIdx/edit
     */
    @PatchMapping("/{userIdx}/edit")
    @ResponseBody
    public BaseResponse<String> edit(@PathVariable Long userIdx, @RequestBody EditReqDto editReqDto, ServletRequest request) throws Exception {

        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != userIdx) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        //아이디
        if (editReqDto.getUniqueId() == null) {
            return new BaseResponse<>(BaseResponseStatus.ID_NULL_ERROR);
        }
        //아이디 정규표현: 영어, 숫자 포함 3~20자리 이내
        if (!isRegexUniqueId(editReqDto.getUniqueId())) {
            return new BaseResponse<>(BaseResponseStatus.ID_REGEX_ERROR);
        }

        //비밀번호
        if (editReqDto.getPassword() == null) {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_NULL_ERROR);
        }
        //비밀번호 정규표현: 숫자, 문자, 특수문자 포함 8~15자리 이내
        if (!isRegexPassword(editReqDto.getPassword())) {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_REGEX_ERROR);
        }

        try {
            userService.edit(userIdx, editReqDto);
            return new BaseResponse<>("수정에 성공하였습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 본인인증
     * [POST] "users/:userIdx/auth"
     */
    @PatchMapping("/{userIdx}/auth")
    @ResponseBody
    public BaseResponse<String> auth(@PathVariable Long userIdx, @RequestBody AuthReqDto authReqDto, ServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != userIdx) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        //아이디
        if (authReqDto.getUniqueId() == null) {
            return new BaseResponse<>(BaseResponseStatus.ID_NULL_ERROR);
        }
        //아이디 정규표현: 영어, 숫자 포함 3~20자리 이내
        if (!isRegexUniqueId(authReqDto.getUniqueId())) {
            return new BaseResponse<>(BaseResponseStatus.ID_REGEX_ERROR);
        }

        //비밀번호
        if (authReqDto.getPassword() == null) {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_NULL_ERROR);
        }
        //비밀번호 정규표현: 숫자, 문자, 특수문자 포함 8~15자리 이내
        if (!isRegexPassword(authReqDto.getPassword())) {
            return new BaseResponse<>(BaseResponseStatus.PASSWORD_REGEX_ERROR);
        }

        try {
            userService.auth(authReqDto);
            return new BaseResponse<>("본인 인증에 성공하였습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }
}
