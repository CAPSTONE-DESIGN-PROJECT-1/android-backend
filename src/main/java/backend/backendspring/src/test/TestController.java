package backend.backendspring.src.test;

import backend.backendspring.config.BaseException;
import backend.backendspring.config.BaseResponse;
import backend.backendspring.src.test.model.dto.TestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // JSON 형태 결과값을 반환해줌.
@RequiredArgsConstructor // final 객체를 Constructor Injection 해줌. Autowired 역할
public class TestController {

    private final TestService testService;

    @GetMapping(value = "/test")
    public BaseResponse<?> test(){
        try {
            TestDto testDto = testService.test();
            return new BaseResponse<>(testDto);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
