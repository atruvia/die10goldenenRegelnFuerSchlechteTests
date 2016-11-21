package de.fiduciagad.ase.postagecharge;

public enum WeightUnit {

	GRAMS {
		@Override
		public int toGrams(int amount) {
			return amount;
		}

		@Override
		public int toKilos(int amount) {
			return amount / 1_000;
		}

		@Override
		public int toTons(int amount) {
			return amount / 1_000_000;
		}

		@Override
		public int convert(int amount, WeightUnit source) {
			return source.toGrams(amount);
		}
	},
	KILOS {

		@Override
		public int toGrams(int amount) {
			return amount * 1000;
		}

		@Override
		public int toKilos(int amount) {
			return amount;
		}

		@Override
		public int toTons(int amount) {
			return amount / 1_000;
		}

		@Override
		public int convert(int amount, WeightUnit source) {
			return source.toKilos(amount);
		}
	},
	TONS {

		@Override
		public int toGrams(int amount) {
			return amount * 1_000_000;
		}

		@Override
		public int toKilos(int amount) {
			return amount * 1_000;
		}

		@Override
		public int toTons(int amount) {
			return amount;
		}

		@Override
		public int convert(int amount, WeightUnit source) {
			return source.toTons(amount);
		}
	};

	public abstract int toGrams(int amount);

	public abstract int toKilos(int amount);

	public abstract int toTons(int amount);

	public abstract int convert(int amount, WeightUnit source);

}
