package de.fiduciagad.ase.postagecharge.ebrief;

public enum Format {

	A4_PORTRAIT() {

		@Override
		double getLength() {
			return 29.7;
		}

		@Override
		double getWidth() {
			return 21;
		}
	};

	abstract double getLength();

	abstract double getWidth();

}
