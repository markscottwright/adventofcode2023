package adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Day3 {
    public static class NumberRun {

        private Schematic schematic;
        private int line;
        private int from;
        private int to; // inclusive
        private long value;

        public NumberRun(Schematic schematic, int line, int from, int to) {
            this.schematic = schematic;
            this.line = line;
            this.from = from;
            this.to = to;
            String out = "";
            for (int i = from; i <= to; ++i) {
                out += schematic.grid.get(line).get(i);
            }
            this.value = Long.parseLong(out);
        }

        public long getValue() {
            return value;

        }

        boolean isAdjacentToSymbol() {
            for (int i = from; i <= to; i++) {
                if (isAdjacentToSymbol(line, i))
                    return true;
            }
            return false;
        }

        private boolean isAdjacentToSymbol(int line, int column) {
            for (Coordinate c : new Coordinate(line, column).adjacentCoordinates(schematic.grid)) {
                Character symbol = schematic.grid.get(c.line).get(c.column);
                if (!Character.isDigit(symbol) && symbol != '.')
                    return true;
            }
            return false;
        }

        public Set<Coordinate> adjacentGearSymbols() {
            var out = new HashSet<Coordinate>();
            for (int i = from; i <= to; ++i) {
                for (Coordinate c : new Coordinate(line, i).adjacentCoordinates(schematic.grid)) {
                    if (schematic.grid.get(c.line).get(c.column) == '*')
                        out.add(c);
                }
            }
            return out;
        }

        @Override
        public String toString() {
            return "NumberRun [line=" + line + ", from=" + from + ", to=" + to + ", value=" + value
                    + "]";
        }

    }

    public static class Schematic {
        ArrayList<ArrayList<Character>> grid = new ArrayList<>();

        static Schematic parse(String data) {
            Schematic schematic = new Schematic();
            for (var line : data.split("\n")) {
                ArrayList<Character> lineChars = new ArrayList<>();
                for (char c : line.toCharArray()) {
                    lineChars.add(c);
                }
                schematic.grid.add(lineChars);
            }
            return schematic;
        }

        ArrayList<NumberRun> getNumberRuns() {
            ArrayList<NumberRun> numberRuns = new ArrayList<>();
            for (int i = 0; i < grid.size(); i++) {
                for (int j = 0; j < grid.get(i).size(); ++j) {
                    
                    // start of a number? - advance to the end
                    if (Character.isDigit(grid.get(i).get(j))) {
                        
                        // go one past the end of the line so we can know the line ended with a number
                        for (int k = j; k < grid.get(i).size() + 1; ++k) {
                            // number ended at end of line or number ended before the end of the line
                            if (k == grid.get(i).size() || !Character.isDigit(grid.get(i).get(k))) {
                                numberRuns.add(new NumberRun(this, i, j, k - 1));
                                j = k;
                                break;
                            }
                        }
                    }
                }
            }

            return numberRuns;
        }

        public List<Long> getGearRatios() {
            // find position of a gear ratio symbol and all numbers next to it
            HashMap<Coordinate, HashSet<NumberRun>> possibleGearRatios = new HashMap<>();
            getNumberRuns().forEach(n -> {
                Set<Coordinate> adjacentGearSymbols = n.adjacentGearSymbols();
                adjacentGearSymbols.forEach(c -> {
                    // quick and dirty multi-map
                    possibleGearRatios.computeIfAbsent(c, k -> new HashSet<>()).add(n);
                });
            });
            
            // for each gear ratio symbol we found, if there are exactly 2 numbers next to it, it's a gear ratio
            List<Long> out = new ArrayList<>();
            for (Entry<Coordinate, HashSet<NumberRun>> entry : possibleGearRatios.entrySet()) {
                if (entry.getValue().size() == 2) {
                    Iterator<NumberRun> iterator = entry.getValue().iterator();
                    out.add(iterator.next().getValue() * iterator.next().getValue());
                }
            }
            return out;
        }

    };

    public static void main(String[] args) throws IOException {
        var schematic = Schematic.parse(Files.readString(Path.of("testdata/day3.dat")));
        long part1 = schematic.getNumberRuns().stream().filter(NumberRun::isAdjacentToSymbol)
                .mapToLong(NumberRun::getValue).sum();
        System.out.println("day 3 part 1:" + part1);
        
        long part2= schematic.getGearRatios().stream().mapToLong(v -> v).sum();
        System.out.println("day 3 part 2:" + part2);
    }
}
