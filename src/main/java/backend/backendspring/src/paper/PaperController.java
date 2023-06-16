package backend.backendspring.src.paper;

import backend.backendspring.config.BaseException;
import backend.backendspring.config.BaseResponse;
import backend.backendspring.config.BaseResponseStatus;
import backend.backendspring.src.paper.model.dto.PaperResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // JSON 형태 결과값을 반환해줌.
@RequiredArgsConstructor // final 객체를 Constructor Injection 해줌. Autowired 역할
@RequestMapping("kisa/papers")
public class PaperController {

    private final PaperService paperService;

    /**
     * 문제 전체 조회
     * [GET] "/papers"
     */
    @GetMapping(value = "")
    @ResponseBody
    public BaseResponse<List<PaperResDto>> getPapers(){

        try {
            List<PaperResDto> paperResDtoList = paperService.getPapers();
            return new BaseResponse<>(paperResDtoList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 특정 범위 문제 조회(연도회차챕터, 문제 양)
     * [GET] "/papers/:categoryNum?quantity={quantity}"
     */
    @GetMapping(value = "/{categoryNum}")
    @ResponseBody
    public BaseResponse<List<PaperResDto>> getPapersByNum(@PathVariable String categoryNum,
                                                          @RequestParam(required = false) String quantity) {

        if (quantity == null) { // query string 이 없을 경우, 그냥 에러
            return new BaseResponse<>(BaseResponseStatus.INVALID_USER);
        }

        try {
            // query string 이 있을 경우, 조건을 만족하는 정보들을 불러온다.
            List<PaperResDto> paperResDtoList = paperService.getPapersByNum(categoryNum, quantity);
            return new BaseResponse<>(paperResDtoList);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
