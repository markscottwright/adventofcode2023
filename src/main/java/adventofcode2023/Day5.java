package adventofcode2023;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.lang3.time.StopWatch;

import static java.nio.file.Files.readString;

public class Day5 {
    public static class SeedRange {

        private long start;
        private long length;

        public SeedRange(long start, long length) {
            this.start = start;
            this.length = length;
        }

        boolean inRange(long seed) {
            return seed >= start && seed < start + length;
        }

        @Override
        public String toString() {
            return "SeedRange [start=" + start + ", length=" + length + "]";
        }

    }

    public static class MappingRange {

        private long source;
        private long destination;
        private long length;

        public MappingRange(long source, long destination, long length) {
            this.source = source;
            this.destination = destination;
            this.length = length;
        }

        public Optional<Long> map(long value) {
            if (value >= source && value < source + length)
                return Optional.of(value - source + destination);
            return Optional.empty();
        }

        @Override
        public String toString() {
            return "MappingRange [source=" + source + ", destination=" + destination + ", length="
                    + length + "]";
        }

        public Optional<Long> reverseMap(long value) {
            if (value >= destination && value < destination + length)
                return Optional.of(value - destination + source);
            return Optional.empty();
        }
    }

    static public class AlmanacMap {
        String from;
        String to;
        ArrayList<MappingRange> ranges = new ArrayList<>();

        static AlmanacMap parse(String input) {
            String[] inputLines = input.split("\n");
            String[] fromAndToFields = inputLines[0].split(" ")[0].split("-");
            AlmanacMap almanacMap = new AlmanacMap();
            almanacMap.from = fromAndToFields[0];
            almanacMap.to = fromAndToFields[2];

            Arrays.stream(inputLines).skip(1).forEach(l -> {
                String[] fields = l.split("\\s+");
                long source = Long.parseLong(fields[1]);
                long destination = Long.parseLong(fields[0]);
                long length = Long.parseLong(fields[2]);
                almanacMap.ranges.add(new MappingRange(source, destination, length));
            });

            return almanacMap;
        }

        @Override
        public String toString() {
            return "AlmanacMap [from=" + from + ", to=" + to + ", ranges=" + ranges + "]";
        }

        public long mapValue(long value) {
            for (MappingRange mappingRange : ranges) {
                Optional<Long> maybeMappedValue = mappingRange.map(value);
                if (maybeMappedValue.isPresent())
                    return maybeMappedValue.get();
            }
            return value;
        }

        public long reverseMapValue(long value) {
            for (MappingRange mappingRange : ranges) {
                Optional<Long> maybeMappedValue = mappingRange.reverseMap(value);
                if (maybeMappedValue.isPresent())
                    return maybeMappedValue.get();
            }
            return value;
        }
    }

    static public class Almanac {
        ArrayList<Long> seeds = new ArrayList<>();
        HashMap<String, AlmanacMap> maps = new HashMap<>();
        HashMap<String, AlmanacMap> reverseMaps = new HashMap<>();
        ArrayList<SeedRange> seedRanges = new ArrayList<>();

        static public Almanac parse(String input) {
            Almanac almanac = new Almanac();
            String[] sections = input.split("\n\n");
            String[] seedsStrings = sections[0].split(": ")[1].split(" ");
            for (String seed : seedsStrings) {
                almanac.seeds.add(Long.parseLong(seed));
            }
            for (int i = 0; i < almanac.seeds.size(); i += 2) {
                almanac.seedRanges
                        .add(new SeedRange(almanac.seeds.get(i), almanac.seeds.get(i + 1)));
            }
            Arrays.stream(sections).skip(1).map(AlmanacMap::parse).forEach(m -> {
                almanac.maps.put(m.from, m);
                almanac.reverseMaps.put(m.to, m);
            });

            return almanac;
        }

        @Override
        public String toString() {
            return "Almanac [seeds=" + seeds + ", seedRanges=" + seedRanges + ", maps=" + maps
                    + "]";
        }

        public long mapSeedTo(long seed, String terminalMap) {
            String position = "seed";
            long value = seed;
            while (!position.equals(terminalMap)) {
                AlmanacMap almanacMap = maps.get(position);
                // System.out.print(" Value:" + value);
                // System.out.print(" From:" + position);
                value = almanacMap.mapValue(value);
                position = almanacMap.to;
                // System.out.print(" To:" + position);
                // System.out.println(" Value:" + value);
            }
            // System.out.println();
            return value;
        }

        public long mapLocationToSeed(long location) {
            String position = "location";
            long value = location;
            while (!position.equals("seed")) {
                AlmanacMap almanacMap = reverseMaps.get(position);
                // System.out.print(" Value:" + value);
                // System.out.print(" From:" + position);
                value = almanacMap.reverseMapValue(value);
                position = almanacMap.from;
                // System.out.print(" To:" + position);
                // System.out.println(" Value:" + value);
            }
            // System.out.println();
            return value;
        }

        public long lowestSeedLocation() {
            return seeds.stream().map(s -> mapSeedTo(s, "location")).mapToLong(v -> v).min()
                    .getAsLong();
        }

        public long iterativeLowestSeedLocation() {
            long location = 0;
            while (location < 100_000_000) {
                long seed = mapLocationToSeed(location);
                for (SeedRange seedRange : seedRanges) {
                    if (seedRange.inRange(seed)) {
                        System.out.println();
                        return location;
                    }
                }
                location++;
                if (location % 1_000_000 == 0)
                    System.out.print(".");
            }
            
            throw new RuntimeException("cant find location");
        }
    }

    public static void main(String[] args) throws IOException {
        Almanac almanac = Almanac.parse(readString(Paths.get("testdata/day5.dat")));
        System.out.println("day 5 part 1: " + almanac.lowestSeedLocation());
        StopWatch stopWatch = StopWatch.createStarted();
        System.out.println("day 5 part 2: " + almanac.iterativeLowestSeedLocation());
        System.out.println(stopWatch.formatTime());
    }
}
