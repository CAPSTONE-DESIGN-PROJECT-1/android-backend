package backend.backendspring.src.paper;

import backend.backendspring.config.BaseException;
import backend.backendspring.config.BaseResponseStatus;
import backend.backendspring.src.paper.model.dto.PaperResDto;
import backend.backendspring.src.paper.model.entity.PaperEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class PaperService {

    private final PaperRepository paperRepository;

    /**
     * 모든 문제 조회
     */
    @Transactional
    public List<PaperResDto> getPapers() throws BaseException {
        List<PaperEntity> paperEntityList = paperRepository.findByStatus("A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_PAPER));
        try {
            return paperEntityList.stream().map(PaperResDto::new).collect(Collectors.toList());
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 특정 범위 문제 조회
     */
    @Transactional
    public List<PaperResDto> getPapersByNum(String categoryNum, String quantity) throws BaseException {
        List<PaperEntity> paperEntityList = paperRepository.findAll("A", categoryNum)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_PAPER));
//        Integer.parseInt(quantity)
        try {
            return paperEntityList.stream().map(PaperResDto::new).collect(Collectors.toList());
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

}
