package de.fiduciagad.ase.postagecharge;

public class Range {

	public static class RangeBuilder {

		private final double min;

		public RangeBuilder(double min) {
			this.min = min;
		}

		public static RangeBuilder from(double min) {
			return new RangeBuilder(min);
		}

		public Range to(double max) {
			return Range.of(min, max);
		}

	}

	private final double min;
	private final double max;

	private Range(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public static Range of(double min, double max) {
		return new Range(min, max);
	}

	public boolean inRange(double value) {
		return value >= min && value <= max;
	}

}
