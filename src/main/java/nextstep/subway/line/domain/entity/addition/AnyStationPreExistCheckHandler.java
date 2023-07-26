package nextstep.subway.line.domain.entity.addition;

import nextstep.subway.common.exception.CreationValidationException;
import nextstep.subway.line.domain.entity.Section;
import nextstep.subway.line.domain.vo.Sections;

public class AnyStationPreExistCheckHandler extends SectionAdditionHandler {
    @Override
    public boolean checkApplicable(Sections sections, Section section) {
        return true;
    }


    @Override
    public void validate(Sections sections, Section section) {
        validateOnlyOneStationIsEnrolledInLine(sections, section);
    }

    @Override
    public void apply(Sections sections, Section newSection) {
        return;
    }

    private void validateOnlyOneStationIsEnrolledInLine(Sections sections, Section section) {
        if (sections.checkUpStationsContains(section.getUpStation()) &&
                sections.checkDownStationsContains(section.getDownStation())) {
            throw new CreationValidationException("section.0003");
        }
    }
}