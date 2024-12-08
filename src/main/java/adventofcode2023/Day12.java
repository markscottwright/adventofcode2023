package adventofcode2023;

import java.util.Arrays;
import java.util.List;

public class Day12 {
	static class ConditionRecord {
		@Override
		public String toString() {
			return "ConditionRecord [springs=" + Arrays.toString(springs) + ", contiguiousGroupSizes="
					+ contiguiousGroupSizes + "]";
		}

		private char[] springs;
		private List<Integer> contiguiousGroupSizes;

		public ConditionRecord(char[] springs, List<Integer> contiguiousGroupSizes) {
			this.springs = springs;
			this.contiguiousGroupSizes = contiguiousGroupSizes;
		}

		static ConditionRecord parse(String line) {
			String springs = line.split(" ")[0];
			String contiguiousGroupSizes = line.split(" ")[1];

			return new ConditionRecord(springs.toCharArray(),
					Arrays.stream(contiguiousGroupSizes.split(",")).map(Integer::parseInt).toList());
		}
	}

	public static void main(String[] args) {

	}
}
