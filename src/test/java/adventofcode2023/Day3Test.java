package adventofcode2023;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import adventofcode2023.Day3.NumberRun;
import adventofcode2023.Day3.Schematic;

import static org.assertj.core.api.Assertions.assertThat;

public class Day3Test {
    String testData = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..""";

    @Test
    public void test() {
        Schematic schematic = Schematic.parse(testData);
        assertThat(schematic).isNotNull();
        assertThat(schematic.grid.size()).isEqualTo(10);
        assertThat(schematic.getNumberRuns()).hasSize(10);

        assertThat(schematic.getNumberRuns().stream().map(NumberRun::getValue).toList())
                .containsExactly(467l, 114l, 35l, 633l, 617l, 58l, 592l, 755l, 664l, 598l);
    }
    
    @Test
    public void testPart2() {
        Schematic schematic = Schematic.parse(testData);
        List<Long> ratios = schematic.getGearRatios();
        assertThat(ratios.stream().mapToLong(v -> v).sum()).isEqualTo(467835L);
    }

}
