package de.fiduciagad.ase.postagecharge;

import static de.fiduciagad.ase.postagecharge.Type.LETTER;
import static de.fiduciagad.ase.postagecharge.WeightUnit.GRAMS;

public class Letter implements Shippable {

	private double length;
	private double width;
	private double height;
	private int gramm;

	public Letter(double length, double width, double height, int gramm) {
		this.length = length;
		this.width = width;
		this.height = height;
		this.gramm = gramm;
	}

	@Override
	public boolean isType(Type type) {
		return type == LETTER;
	}

	@Override
	public double getLength() {
		return length;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public int getWeight(WeightUnit weightUnit) {
		return GRAMS.convert(gramm, weightUnit);
	}

	@Override
	public String toString() {
		return "Letter [length=" + length + ", width=" + width + ", height="
				+ height + ", gramm=" + gramm + "]";
	}

}
