package backend.backendspring.src.statistic;

import backend.backendspring.config.BaseException;
import backend.backendspring.config.BaseResponseStatus;
import backend.backendspring.src.paper.PaperRepository;
import backend.backendspring.src.paper.model.entity.PaperEntity;
import backend.backendspring.src.statistic.model.ResultDto;
import backend.backendspring.src.statistic.model.dto.ChapterResDto;
import backend.backendspring.src.statistic.model.dto.ResultReqDto;
import backend.backendspring.src.statistic.model.dto.StatisticResDto;
import backend.backendspring.src.statistic.model.entity.StatisticEntity;
import backend.backendspring.src.user.UserRepository;
import backend.backendspring.src.user.model.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;
    private final UserRepository userRepository;
    private final PaperRepository paperRepository;

    /**
     * 문제 풀이 결과 저장
     */
    @Transactional
    public void saveResult(ResultReqDto resultReqDto) throws BaseException{


        System.out.println(resultReqDto);
        List<ResultDto> results = resultReqDto.getResultDtoList();
        System.out.println(results);

        UserEntity userEntity = userRepository.findByUserIdxAndStatus(resultReqDto.getUserIdx(), "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER));


        try {

            System.out.println(results.isEmpty());

            if(!results.isEmpty()) {

                for(ResultDto result : results) {

                    System.out.println(1);
                    System.out.println(result);

                    PaperEntity paperEntity = paperRepository.findByPaperIdxAndStatus(result.getPaperIdx(), "A")
                            .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_PAPER));

//                    // 다시 푼 경우
//                    if(!statisticRepository.findByPaperEntity_PaperIdxAndStatus(result.getPaperIdx(), "A").isEmpty()) {
//                        StatisticEntity resolveEntity = statisticRepository.findByPaperEntity_PaperIdxAndStatus(result.getPaperIdx(), "A")
//                                .orElseThrow(()-> new BaseException(BaseResponseStatus.ABSENT_PAPER));
//
//                        continue;
//                    }

                    StatisticEntity statisticEntity = result.toEntity(userEntity, paperEntity);

                    System.out.println(statisticEntity);

                    statisticRepository.save(statisticEntity);

                }
            } else {
                throw new BaseException(BaseResponseStatus.ABSENT_RESULT);
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 챕터 별 통계 조회
     */
    @Transactional
    public List<ChapterResDto> getStatisticsByChapter(Long userIdx) throws BaseException{

        List<StatisticEntity> statisticEntityList = statisticRepository.findByUserEntity_UserIdxAndStatus(userIdx, "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER)); // -> 없는 통계

        // 20210105: 2021년도 01회차 05챕터
        // 20210105 % 100 -> 5
        // 100으로 나눈 나머지 => 챕터
        try {
            List<ChapterResDto> chapterResDtoList = new ArrayList<>();

            int[] total = new int[5];
            int[] correct = new int[5];
            if(! statisticEntityList.isEmpty()) {
                for(StatisticEntity statisticEntity : statisticEntityList) {
                    Integer chapterNum = Integer.parseInt(statisticEntity.getCategoryNum()) % 100;
                    if(chapterNum == 1) {
                        total[0] += 1;
                        if(statisticEntity.getIsCorrect() == Boolean.TRUE) {
                            correct[0] += 1;
                        }
                    } else if (chapterNum == 2) {
                        total[1] += 1;
                        if(statisticEntity.getIsCorrect() == Boolean.TRUE) {
                            correct[1] += 1;
                        }
                    } else if (chapterNum == 3) {
                        total[2] += 1;
                        if(statisticEntity.getIsCorrect() == Boolean.TRUE) {
                            correct[2] += 1;
                        }
                    } else if (chapterNum == 4) {
                        total[3] += 1;
                        if(statisticEntity.getIsCorrect() == Boolean.TRUE) {
                            correct[3] += 1;
                        }
                    } else if (chapterNum == 5) {
                        total[4] += 1;
                        if(statisticEntity.getIsCorrect() == Boolean.TRUE) {
                            correct[4] += 1;
                        }
                    }
                }
            }

            chapterResDtoList.add(new ChapterResDto(1, correct[0], total[0]));
            chapterResDtoList.add(new ChapterResDto(2, correct[1], total[1]));
            chapterResDtoList.add(new ChapterResDto(3, correct[2], total[2]));
            chapterResDtoList.add(new ChapterResDto(4, correct[3], total[3]));
            chapterResDtoList.add(new ChapterResDto(5, correct[4], total[4]));

            return chapterResDtoList;
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    /**
     * 전체 통계 조회
     */
    @Transactional
    public StatisticResDto getStatistics(Long userIdx) throws BaseException {

        List<StatisticEntity> statisticEntityList = statisticRepository.findByUserEntity_UserIdxAndStatus(userIdx, "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER)); // -> 없는 통계

        try {
            List<Integer> dayList = new ArrayList<>();
            int total = 0;
            int correct = 0;
            if(!statisticEntityList.isEmpty()) {
                for (StatisticEntity entity : statisticEntityList) {
                    total += 1;
                    if (entity.getIsCorrect() == Boolean.TRUE) {
                        correct += 1;
                    }

                }
            }
            return new StatisticResDto(total, correct, dayList);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    /**
     * 월 별 통계 조회 (문제 푼 날짜(일)도 결과에 포함)
     */
    @Transactional
    public StatisticResDto getStatisticsByMonth(Long userIdx, String time) throws BaseException {

        List<StatisticEntity> statisticEntityList = statisticRepository.findByUserEntity_UserIdxAndStatus(userIdx, "A")
                .orElseThrow(() -> new BaseException(BaseResponseStatus.ABSENT_USER)); // -> 없는 통계

        try {
            List<Integer> dayList = new ArrayList<>();
            int total = 0;
            int correct = 0;
            if(!statisticEntityList.isEmpty()) {
                for (StatisticEntity entity : statisticEntityList) {
                    System.out.println(entity.getUpdatedAt().getYear());
                    System.out.println(entity.getUpdatedAt().getMonthValue());
                    System.out.println(Integer.parseInt(time.substring(0,4)));
                    System.out.println(Integer.parseInt(time.substring(4,6)));

                    System.out.println(entity.getUpdatedAt().getMonthValue() == Integer.parseInt(time.substring(4,6), 10));
                    System.out.println(entity.getUpdatedAt().getYear() == Integer.parseInt(time.substring(0,4)));

                    if (entity.getUpdatedAt().getYear() == Integer.parseInt(time.substring(0,4))
                            && entity.getUpdatedAt().getMonthValue() == Integer.parseInt(time.substring(4,6), 10)) {
                        total += 1; // total = total + 1
                        dayList.add(entity.getUpdatedAt().getDayOfMonth());
                        System.out.println(total);
                        if (entity.getIsCorrect() == Boolean.TRUE) {
                            correct += 1;
                            System.out.println(correct);
                        }
                    }
                }
            }
            System.out.println(total + " " + correct);
            return new StatisticResDto(total, correct, dayList);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
