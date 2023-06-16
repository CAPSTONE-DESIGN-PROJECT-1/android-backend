package backend.backendspring.src.test;

import backend.backendspring.config.BaseException;
import backend.backendspring.src.test.model.dto.TestDto;
import backend.backendspring.src.test.model.entity.TestEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static backend.backendspring.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

    private final TestRepository testRepository;

    public TestDto test() throws BaseException {

        try {
            Optional<TestEntity> test = testRepository.findById(1L);

            TestDto testDto = TestDto.builder()
                    .testId(test.get().getTestId())
                    .build();

            return testDto;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
