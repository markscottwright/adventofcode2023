package adventofcode2023;

import java.util.ArrayList;

import org.junit.Test;

import adventofcode2023.Day6.Race;

import static org.assertj.core.api.Assertions.assertThat;

public class Day6Test {

    String TEST_DATA = """
            Time:      7  15   30
            Distance:  9  40  200
            """;

    @Test
    public void testParsing() {
        ArrayList<Race> races = Day6.Race.parse(TEST_DATA);
        assertThat(races).hasSize(3);
    }
    
    @Test
    public void testBoatDistance() {
        ArrayList<Race> races = Day6.Race.parse(TEST_DATA);
        Race race1 = races.get(0);
        assertThat(race1.millimetersTravelledAfterHoldingButton(0)).isEqualTo(0);
        assertThat(race1.millimetersTravelledAfterHoldingButton(1)).isEqualTo(6);
        assertThat(race1.millimetersTravelledAfterHoldingButton(2)).isEqualTo(10);
        assertThat(race1.millimetersTravelledAfterHoldingButton(3)).isEqualTo(12);
        assertThat(race1.millimetersTravelledAfterHoldingButton(5)).isEqualTo(10);
        assertThat(race1.millimetersTravelledAfterHoldingButton(6)).isEqualTo(6);
        assertThat(race1.millimetersTravelledAfterHoldingButton(7)).isEqualTo(0);
        assertThat(race1.beatRecordAfterHoldingButton(0)).isFalse();
        assertThat(race1.beatRecordAfterHoldingButton(1)).isFalse();
        assertThat(race1.beatRecordAfterHoldingButton(2)).isTrue();
        assertThat(race1.beatRecordAfterHoldingButton(3)).isTrue();
        assertThat(race1.beatRecordAfterHoldingButton(4)).isTrue();
        assertThat(race1.beatRecordAfterHoldingButton(5)).isTrue();
        assertThat(race1.beatRecordAfterHoldingButton(6)).isFalse();
        assertThat(race1.beatRecordAfterHoldingButton(7)).isFalse();
    }
    
    @Test
    public void testSolving() {
        var races = Day6.Race.parse(TEST_DATA);
        var race1 = races.get(0);
        assertThat(race1.numWaysToBeat()).isEqualTo(4);
        
        assertThat(Race.totalWaysToBeatRecord(races)).isEqualTo(288);
    }


}
