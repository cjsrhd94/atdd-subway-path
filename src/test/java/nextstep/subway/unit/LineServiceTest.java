package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class LineServiceTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineService lineService;

    @DisplayName("지하철 노선에 새로운 역 추가.")
    @Test
    void addSection() {
        // given
        // stationRepository와 lineRepository를 활용하여 초기값 셋팅
        Station upStation = new Station("강남역");
        Station downStation = new Station("양재역");
        Station newStation = new Station("판교역");
        stationRepository.save(upStation);
        stationRepository.save(downStation);
        stationRepository.save(newStation);

        Line line = new Line("신분당선", "green");
        lineRepository.save(line);
        line.addSection(new Section(line, upStation, downStation, 7));

        // when
        // lineService.addSection 호출
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), newStation.getId(), 3);
        lineService.addSection(line.getId(), sectionRequest);

        // then
        // line.getSections 메서드를 통해 검증
        Assertions.assertThat(line.getSections()).hasSize(2);
    }

    @DisplayName("지하철 역 저장 순서 확인")
    @Test
    void createOrderedStationResponses() {
        // given
        Station upStation = new Station("강남역");
        Station downStation = new Station("양재역");
        stationRepository.save(upStation);
        stationRepository.save(downStation);

        Line line = new Line("신분당선", "green", upStation, downStation, 10);
        lineRepository.save(line);

        // when
        Station newStation = new Station("정자역");
        stationRepository.save(newStation);
        lineService.addSection(line.getId(), new SectionRequest(1L, 3L, 5));
        lineRepository.save(line);

        LineResponse response = lineService.findById(line.getId());

        // then
        Assertions.assertThat(response
                .getStations()
                .stream()
                .map(StationResponse::getId))
                .containsExactly(1L, 3L, 2L);
    }
}
