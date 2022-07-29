package nextstep.subway.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class LineServiceTest {
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private LineService lineService;

	private Station upStation;
	private Station downStation;
	private Line line;

	@BeforeEach
	void setUp() {
		upStation = new Station("서현역");
		downStation = new Station("이매역");
		stationRepository.save(upStation);
		stationRepository.save(downStation);
		line = new Line("분당선", "yellow");
		lineRepository.save(line);
	}

	@DisplayName("구간 추가 테스트")
	@Test
	void addSection() {
		// given
		// stationRepository와 lineRepository를 활용하여 초기값 셋팅

		// when
		// lineService.addSection 호출
		lineService.addSection(line.getId(), new SectionRequest(upStation.getId(), downStation.getId(), 10));

		// then
		// line.getSections 메서드를 통해 검증
		assertThat(line.getSections()).hasSize(1);
	}

	@DisplayName("구간 삭제 테스트")
	@Test
	void deleteSectionTest() {
		//given
		lineService.addSection(line.getId(), new SectionRequest(upStation.getId(), downStation.getId(), 10));

		//when
		lineService.deleteSection(line.getId(), downStation.getId());

		//then
		assertThat(line.getSections()).isEmpty();
	}

}
