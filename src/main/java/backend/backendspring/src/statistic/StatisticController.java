package backend.backendspring.src.statistic;

import backend.backendspring.config.BaseException;
import backend.backendspring.config.BaseResponse;
import backend.backendspring.config.BaseResponseStatus;
import backend.backendspring.security.jwt.JwtTokenProvider;
import backend.backendspring.src.statistic.model.dto.ChapterResDto;
import backend.backendspring.src.statistic.model.dto.ResultReqDto;
import backend.backendspring.src.statistic.model.dto.StatisticResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("kisa/statistics")
public class StatisticController {

    private final StatisticService statisticService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 문제 풀이 결과 저장
     * [POST] "/papers/result
     */
    @PostMapping(value = "/result")
    @ResponseBody
    public BaseResponse<String> saveResult(@RequestBody ResultReqDto resultReqDto, ServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != resultReqDto.getUserIdx()) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        try {
            statisticService.saveResult(resultReqDto);
            return new BaseResponse<>("저장에 성공하였습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 챕터 별 통계 조회
     * [GET] "/:userIdx/chapter
     */
    @GetMapping(value = "/{userIdx}/chapter")
    @ResponseBody
    public BaseResponse<List<ChapterResDto>> getStatisticsByChapter(@PathVariable Long userIdx, ServletRequest request) {

        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != userIdx) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        try {
            List<ChapterResDto> chapterResDtoList = statisticService.getStatisticsByChapter(userIdx);
            return new BaseResponse<>(chapterResDtoList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 월 별 통계 조회
     * [GET] "/:userIdx?time={time} (202112)
     */
    @GetMapping(value = "/{userIdx}")
    @ResponseBody
    public BaseResponse<StatisticResDto> getStatisticsByMonth(@PathVariable Long userIdx, ServletRequest request,
                                                       @RequestParam(required = false) String time) {

        String token = jwtTokenProvider.resolveAccessToken((HttpServletRequest) request);
        Long userPK = Long.valueOf(jwtTokenProvider.getUserPK(token));

        // API 사용자와 대상 회원이 다른 경우 에러 처리
        if (userPK != userIdx) {
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        try {
            if (time == null) { // query string 인 month 가 없을 경우, 그냥 전체를 불러온다.
                StatisticResDto statisticResDto = statisticService.getStatistics(userIdx);
                return new BaseResponse<>(statisticResDto);
            }

            // query string 인 month 가 있을 경우, 조건을 만족하는 정보들을 불러온다.
            StatisticResDto statisticResDto = statisticService.getStatisticsByMonth(userIdx, time);
            return new BaseResponse<>(statisticResDto);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
