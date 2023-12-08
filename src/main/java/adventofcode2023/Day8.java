package adventofcode2023;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.primes.Primes;

public class Day8 {
    static public class DesertMap {
        @Override
        public String toString() {
            return "DesertMap [directions=" + directions + ", nodeMap=" + nodeMap + "]";
        }

        HashMap<String, List<String>> nodeMap = new HashMap<>();
        public String directions;

        public static DesertMap parse(String input) {
            Pattern nodePattern = Pattern.compile("([A-Z0-9]+) = \\(([A-Z0-9]+), ([A-Z0-9]+)\\)");
            String[] directionsAndNodes = input.split("\n\n");
            DesertMap desertMap = new DesertMap();
            desertMap.directions = directionsAndNodes[0];
            for (String node : directionsAndNodes[1].split("\n")) {
                Matcher matcher = nodePattern.matcher(node);
                matcher.matches();
                desertMap.nodeMap.put(matcher.group(1),
                        List.of(matcher.group(2), matcher.group(3)));
            }
            return desertMap;
        }

        public int stepsToEnd() {
            int step = 0;
            int directionsPos = 0;
            String currentNode = "AAA";
            while (!currentNode.equals("ZZZ")) {
                List<String> neighbors = nodeMap.get(currentNode);
                if (directionsPos >= directions.length())
                    directionsPos = 0;
                char nextDirection = directions.charAt(directionsPos);
                // System.out.print("step " + step + ": currentNode=" + currentNode + "
                // direction="
                // + nextDirection + " neighbors=" + neighbors);
                currentNode = neighbors.get(nextDirection == 'L' ? 0 : 1);
                // System.out.println(" next=" + currentNode);
                step++;
                directionsPos++;
            }
            return step;
        }

        public int stepsToAnEnd(String startNode) {
            int step = 0;
            int directionsPos = 0;
            String currentNode = startNode;
            while (!currentNode.endsWith("Z")) {
                List<String> neighbors = nodeMap.get(currentNode);
                if (directionsPos >= directions.length())
                    directionsPos = 0;
                char nextDirection = directions.charAt(directionsPos);
                // System.out.print("step " + step + ": currentNode=" + currentNode + "
                // direction="
                // + nextDirection + " neighbors=" + neighbors);
                currentNode = neighbors.get(nextDirection == 'L' ? 0 : 1);
                // System.out.println(" next=" + currentNode);
                step++;
                directionsPos++;
            }
            return step;
        }

        /**
         * this doesn't work for large data
         */
        public int stepsToEndFollowingAllPaths() {
            int step = 0;
            int directionsPos = 0;
            List<String> currentNodes = nodeMap.keySet().stream().filter(n -> n.endsWith("A"))
                    .toList();
            while (!currentNodes.stream().allMatch(n -> n.endsWith("Z"))) {
                ArrayList<String> newNodes = new ArrayList<>();
                for (String currentNode : currentNodes) {
                    List<String> neighbors = nodeMap.get(currentNode);
                    char nextDirection = directions.charAt(directionsPos);
                    // System.out.print("step " + step + ": currentNode=" + currentNode + "
                    // direction="
                    // + nextDirection + " neighbors=" + neighbors);
                    currentNode = neighbors.get(nextDirection == 'L' ? 0 : 1);
                    // System.out.println(" next=" + currentNode);
                    newNodes.add(currentNode);
                }
                currentNodes = newNodes;
                step++;
                directionsPos++;
                if (directionsPos >= directions.length())
                    directionsPos = 0;
            }
            return step;
        }

        /**
         * figure out how long each path to an end is, then figure out what multiple of those lengths meet.
         */
        public BigInteger stepsToEndAsGhost() {
            List<String> allStarts = nodeMap.keySet().stream().filter(n -> n.endsWith("A"))
                    .toList();
            HashSet<Integer> allFactors = new HashSet<>();
            for (String start : allStarts) {
                int stepsToAnEnd = stepsToAnEnd(start);
                allFactors.addAll(Primes.primeFactors(stepsToAnEnd));
            }

            BigInteger minSteps = BigInteger.ONE;
            for (Integer f : allFactors)
                minSteps = minSteps.multiply(BigInteger.valueOf(f));

            return minSteps;
        }

    }

    public static void main(String[] args) throws IOException {
        String data = Files.readString(Path.of("testdata/day8.dat"));
        DesertMap desertMap = DesertMap.parse(data);
        int steps = desertMap.stepsToEnd();
        System.out.println("day 8 part 1: " + steps);

        BigInteger minSteps = desertMap.stepsToEndAsGhost();
        System.out.println("day 8 part 2: " + minSteps);
    }

}
