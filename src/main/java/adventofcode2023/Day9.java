package adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {
    static class Reading {
        private List<Integer> readings;

        public Reading(List<Integer> readings) {
            this.readings = readings;
        }

        public static List<Reading> parse(String testData) {
            return Arrays.stream(testData.split("\n"))
                    .map(l -> Arrays.stream(l.split("\\s+")).map(Integer::parseInt).toList())
                    .map(Reading::new).toList();
        }

        public int nextValue() {
            var derived = buildDerivedReadings();

            // create the next values
            derived.get(derived.size() - 1).add(0);
            for (int i = derived.size() - 2; i >= 0; --i) {
                var current = derived.get(i);
                var above = derived.get(i + 1);
                Integer newValue = current.get(current.size() - 1) + above.get(above.size() - 1);
                current.add(newValue);
            }

            return readings.get(readings.size() - 1)
                    + derived.get(0).get(derived.get(0).size() - 1);
        }

        public int previousValue() {
            var derived = buildDerivedReadings();

            // create the previous values
            derived.get(derived.size() - 1).add(0, 0);
            for (int i = derived.size() - 2; i >= 0; --i) {
                var current = derived.get(i);
                var above = derived.get(i + 1);
                Integer newValue = current.get(0) - above.get(0);
                current.add(0, newValue);
            }

            return readings.get(0) - derived.get(0).get(0);
        }

        private ArrayList<List<Integer>> buildDerivedReadings() {
            var derived = new ArrayList<List<Integer>>();

            // create the triangle of values
            List<Integer> above = readings;
            // System.out.println(above);
            while (true) {
                derived.add(new ArrayList<>());
                var current = derived.get(derived.size() - 1);
                for (int i = 1; i < above.size(); i++)
                    current.add(above.get(i) - above.get(i - 1));
                // System.out.println(current);
                if (current.stream().allMatch(v -> v == 0))
                    break;
                above = current;
            }
            return derived;
        }

        public static long sumOfNextValues(List<Reading> readings) {
            return readings.stream().mapToLong(Reading::nextValue).sum();
        }

        public static long sumOfPreviousValues(List<Reading> readings) {
            return readings.stream().mapToLong(Reading::previousValue).sum();
        }
    };

    public static void main(String[] args) throws IOException {
        var readings = Reading.parse(Files.readString(Path.of("testdata/day9.dat")));
        System.out.println("day 9 part 1: " + Reading.sumOfNextValues(readings));
        System.out.println("day 9 part 2: " + Reading.sumOfPreviousValues(readings));
    }

}
