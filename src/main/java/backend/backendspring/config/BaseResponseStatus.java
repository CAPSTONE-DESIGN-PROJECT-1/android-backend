package backend.backendspring.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * 에러 코드 관리
     */

    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    /**
     * 2000 : Request 오류
     */
    ABSENT_USER(false, 2000, "존재하지 않는 회원입니다."),
    PASSWORD_MATCH_ERROR(false, 2001, "비밀번호가 틀렸습니다."),
    PASSWORD_REGEX_ERROR(false, 2002, "비밀번호 형식을 확인해주세요."),
    PASSWORD_NULL_ERROR(false, 2003, "비밀번호를 입력해주세요."),
    ID_NULL_ERROR(false, 2004, "아이디를 입력해주세요."),
    ID_REGEX_ERROR(false, 2005, "아이디 형식을 확인해주세요."),
    NICKNAME_NULL_ERROR(false, 2006, "닉네임 입력해주세요."),
    NICKNAME_REGEX_ERROR(false, 2007, "닉네임 형식을 확인해주세요."),

    ID_DUPLICATE_ERROR(false, 2008, "중복되는 아이디입니다."),

    INVALID_USER(false, 2009, "유효하지 않은 회원입니다."),

    ABSENT_PAPER(false, 2010, "존재하지 않는 문제입니다."),
    ABSENT_RESULT(false, 2011, "결과 값이 널 값입니다."),

    /**
     * 3000 : Response 오류
     */

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다.")

    /**
     * 5000 :
     */

    /**
     * 6000 :
     */

    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
