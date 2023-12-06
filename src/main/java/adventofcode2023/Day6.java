package adventofcode2023;

import java.util.ArrayList;

public class Day6 {
    public static class Race {
        private long timeMilliseconds;
        private long distanceMillimeters;

        public Race(long timeMilliseconds, long distanceMillimeters) {
            this.timeMilliseconds = timeMilliseconds;
            this.distanceMillimeters = distanceMillimeters;
        }

        static ArrayList<Race> parse(String data) {
            ArrayList<Race> out = new ArrayList<Race>();
            String[] timesAndDistanceString = data.split("\n");
            String[] timeStrings = timesAndDistanceString[0].split("\\s+");
            String[] distanceStrings = timesAndDistanceString[1].split("\\s+");
            for (int i = 1; i < timeStrings.length; ++i) {
                out.add(new Race(Integer.parseInt(timeStrings[i]),
                        Integer.parseInt(distanceStrings[i])));
            }
            return out;
        }

        public long millimetersTravelledAfterHoldingButton(long holdMilliseconds) {
            assert holdMilliseconds <= timeMilliseconds;
            long millimetersPerMillisecond = holdMilliseconds;
            long millimetersTravelled = (timeMilliseconds - holdMilliseconds)
                    * millimetersPerMillisecond;
            return millimetersTravelled;
        }

        public boolean beatRecordAfterHoldingButton(int holdMilliseconds) {
            return millimetersTravelledAfterHoldingButton(holdMilliseconds) > distanceMillimeters;
        }

        public int numWaysToBeat() {
            int out = 0;
            for (int holdMilliseconds = 1; holdMilliseconds < timeMilliseconds; ++holdMilliseconds)
                if (beatRecordAfterHoldingButton(holdMilliseconds))
                    out++;
            return out;
        }

        public static long totalWaysToBeatRecord(ArrayList<Race> races) {
            long solution = 1;
            for (Race race : races) {
                solution *= race.numWaysToBeat();
            }
            return solution;
        }
    }

    public static void main(String[] args) {
        String input = """
                Time:        48     98     90     83
                Distance:   390   1103   1112   1360
                """;
        long part1 = Race.totalWaysToBeatRecord(Race.parse(input));
        System.out.println("day 6 part 1: " + part1);
        
        long part2 = new Race(48989083, 390110311121360L).numWaysToBeat();
        System.out.println("day 6 part 2: " + part2);
    }
}
