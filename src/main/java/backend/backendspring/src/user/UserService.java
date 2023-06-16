package backend.backendspring.src.user;

import backend.backendspring.config.BaseException;
import backend.backendspring.config.BaseResponseStatus;
import backend.backendspring.security.jwt.JwtTokenProvider;
import backend.backendspring.src.user.model.dto.*;
import backend.backendspring.src.user.model.entity.Role;
import backend.backendspring.src.user.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {

    @Value("${file.dir}")
    private String fileDir;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
//
//    /**
//     * 이미지 포함 회원가입
//     */
//    @Transactional
//    public JoinResDto join(JoinReqDto joinReqDto, MultipartFile files) throws BaseException {
//
//        // 해당 아이디를 가진 회원이 존재하는지 -> 존재한다면 -> 에러
//        if(userRepository.findByUniqueIdAndStatus(joinReqDto.getUniqueId(), "A").isPresent()) {
//            throw new BaseException(BaseResponseStatus.ID_DUPLICATE_ERROR);
//        }
//
//        try {
//                String origName = files.getOriginalFilename();
//
//                // 파일 이름으로 쓸 uuid 생성
//                String uuid = UUID.randomUUID().toString();
//
//                // 확장자 추출(ex : .png)
//                String extension = origName.substring(origName.lastIndexOf("."));
//
//                // uuid와 확장자 결합
//                String savedName = uuid + extension;
//
//                // 파일을 불러올 때 사용할 파일 경로
//                String savedPath = fileDir + savedName;
//
//                Long userIdx = userRepository.save(UserEntity.builder()
//                        .uniqueId(joinReqDto.getUniqueId())
//                        .nickname(joinReqDto.getNickname())
//                        .password(passwordEncoder.encode(joinReqDto.getPassword()))
//                        .goal(joinReqDto.getGoal())
//                        .day(joinReqDto.getDay())
//                        .imageUrl(savedPath)
//                        .role(Role.ROLE_USER)
//                        .build()).getUserIdx();
//
//                // 실제로 로컬에 uuid를 파일명으로 저장
//                files.transferTo(new File(savedPath));
//
//                return new JoinResDto(userIdx);
//
//        } catch (Exception exception) {
//            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
//        }
//
//
//    }

    /**
     * 이미지 없이 회원가입
     */
    @Transactional
    public JoinResDto joinTest(JoinReqDto joinReqDto) throws BaseException {

        // 해당 아이디를 가진 회원이 존재하는지 -> 존재한다면 -> 에러
        if(userRepository.findByUniqueIdAndStatus(joinReqDto.getUniqueId(), "A").isPresent()) {
            throw new BaseException(BaseResponseStatus.ID_DUPLICATE_ERROR);
        }

        try {

            Long userIdx = userRepository.save(UserEntity.builder()
                    .uniqueId(joinReqDto.getUniqueId())
                    .password(passwordEncoder.encode(joinReqDto.getPassword()))
                    .role(Role.ROLE_USER)
                    .build()).getUserIdx();

            String accessToken = jwtTokenProvider.createAccessToken(userIdx, Role.ROLE_USER);
            return new JoinResDto(userIdx, accessToken);

        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }


    }

    /**
     * 초기 프로필 설정
     */
    @Transactional
    public void setProfile(JoinReqDto joinReqDto, Long userIdx) throws BaseException{

        UserEntity userEntity = userRepository.findByUserIdxAndStatus(userIdx, "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER));

        try {
            userEntity.setProfile(joinReqDto.getNickname(), joinReqDto.getGoal(), joinReqDto.getDay());
            userRepository.save(userEntity);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    /**
     * 로그인
     */
    @Transactional
    public LoginResDto login(LoginReqDto loginReqDto) throws BaseException {

        // 해당 아이디를 가진 회원이 존재하는지 -> 존재하지 않다면 -> 에러
        UserEntity userEntity = userRepository.findByUniqueIdAndStatus(loginReqDto.getUniqueId(), "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER));

        // 비밀번호 매치 검사, 비밀번호가 틀림
        if (!passwordEncoder.matches(loginReqDto.getPassword(), userEntity.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MATCH_ERROR);
        }

        try {
            String accessToken = jwtTokenProvider.createAccessToken(userEntity.getUserIdx(), userEntity.getRole());
            return new LoginResDto(userEntity.getUserIdx(), accessToken);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    /**
     * 특정 회원 조회
     */
    @Transactional
    public UserResDto getUser(Long userIdx) throws BaseException {
        UserEntity userEntity = userRepository.findByUserIdxAndStatus(userIdx, "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER));
        try {
            return new UserResDto(userEntity);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void resign(Long userIdx) throws BaseException {
        UserEntity userEntity = userRepository.findByUserIdxAndStatus(userIdx, "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER));
        try {
            userEntity.setStatus("D");
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 프로필정보수정(nickname/goal)
     */
    @Transactional
    public void editProfile(Long userIdx, EditReqDto editReqDto) throws BaseException {
        UserEntity userEntity = userRepository.findByUserIdxAndStatus(userIdx, "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER));

        try {
            userEntity.updateProfile(editReqDto.getNickname(), editReqDto.getGoal());
            userRepository.save(userEntity);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 개인정보수정(ID/PW)
     */
    @Transactional
    public void edit(Long userIdx, EditReqDto editReqDto) throws BaseException {
        UserEntity userEntity = userRepository.findByUserIdxAndStatus(userIdx, "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER));

        try {
            userEntity.update(editReqDto.getUniqueId(), passwordEncoder.encode(editReqDto.getPassword()));
            userRepository.save(userEntity);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 본인인증
     */
    @Transactional
    public void auth(AuthReqDto authReqDto) throws BaseException {

        // 해당 아이디를 가진 회원이 존재하는지 -> 존재하지 않다면 -> 에러
        UserEntity userEntity = userRepository.findByUniqueIdAndStatus(authReqDto.getUniqueId(), "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER));

        // 비밀번호 매치 검사, 비밀번호가 틀림
        if (!passwordEncoder.matches(authReqDto.getPassword(), userEntity.getPassword())) {
            throw new BaseException(BaseResponseStatus.PASSWORD_MATCH_ERROR);
        }

    }
}
