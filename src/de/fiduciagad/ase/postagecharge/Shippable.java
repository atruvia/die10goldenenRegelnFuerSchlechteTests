package de.fiduciagad.ase.postagecharge;

public interface Shippable {

	boolean isType(Type type);

	double getLength();

	double getWidth();

	double getHeight();

	int getWeight(WeightUnit weightUnit);

}
