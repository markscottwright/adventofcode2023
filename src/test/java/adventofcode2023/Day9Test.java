package adventofcode2023;

import java.util.List;

import org.junit.Test;

import adventofcode2023.Day9.Reading;

import static org.assertj.core.api.Assertions.assertThat;

public class Day9Test {
    String testData = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """;

    @Test
    public void test() {
        List<Reading> readings = Day9.Reading.parse(testData);
        assertThat(readings).hasSize(3);
        assertThat(readings.get(0).nextValue()).isEqualTo(18);
        assertThat(readings.get(1).nextValue()).isEqualTo(28);
        assertThat(readings.get(2).nextValue()).isEqualTo(68);
        
        assertThat(Day9.Reading.sumOfNextValues(readings)).isEqualTo(114);
        assertThat(Day9.Reading.sumOfPreviousValues(readings)).isEqualTo(2);
    }

}
