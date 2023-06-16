package backend.backendspring.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {

    // 아이디 형식 체크 (숫자, 영어 포함 3~20자리 이내)
    public static boolean isRegexUniqueId(String target) {
        String regex = "^[a-z]+[a-z0-9]{3,20}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    // 닉네임 형식 체크 (4~20자, 영문자(대소문자), 한글, 숫자 포함)
    public static boolean isRegexNickname(String target) {
        String regex = "^[ㄱ-힣A-Za-z0-9]{3,20}$"; // 000-000-0000 or 000-0000-0000
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    // 비밀번호 형식 체크 (숫자, 문자, 특수문자 포함 8~15자리 이내)
    public static boolean isRegexPassword(String target) {
        String regex = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

    // 날짜 형식, 전화 번호 형식 등 여러 Regex 인터넷에 검색하면 나옴.

}
