package adventofcode2023;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import adventofcode2023.Day8.DesertMap;

public class Day8Test {
    String testData = """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """;

    String testData2 = """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
            """;
    @Test
    public void testPart1() {
        DesertMap desertMap = Day8.DesertMap.parse(testData);
        
        Assertions.assertThat(desertMap.stepsToEnd()).isEqualTo(2);
    }

    @Test
    public void testPart2() {
        DesertMap desertMap = Day8.DesertMap.parse(testData2);
        
        Assertions.assertThat(desertMap.stepsToEndFollowingAllPaths()).isEqualTo(6);
        Assertions.assertThat(desertMap.stepsToEndAsGhost()).isEqualTo(6);
    }
}
