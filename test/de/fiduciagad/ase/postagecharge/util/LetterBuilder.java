package de.fiduciagad.ase.postagecharge.util;

import de.fiduciagad.ase.postagecharge.Letter;
import de.fiduciagad.ase.postagecharge.Shippable;
import de.fiduciagad.ase.postagecharge.WeightUnit;

public class LetterBuilder {

	private double length;
	private double width;
	private double height;
	private int gramm;

	public LetterBuilder length(double length) {
		this.length = length;
		return this;
	}

	public LetterBuilder width(double width) {
		this.width = width;
		return this;
	}

	public LetterBuilder height(double height) {
		this.height = height;
		return this;
	}

	public LetterBuilder weight(int amount, WeightUnit weightUnit) {
		this.gramm = weightUnit.toGrams(amount);
		return this;
	}

	public Shippable build() {
		return new Letter(length, width, height, gramm);
	}

}
