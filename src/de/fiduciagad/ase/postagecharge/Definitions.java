package de.fiduciagad.ase.postagecharge;

import static de.fiduciagad.ase.postagecharge.DefinitionBuilder.definitionOfType;
import static de.fiduciagad.ase.postagecharge.Range.RangeBuilder.from;
import static de.fiduciagad.ase.postagecharge.Type.LETTER;

public final class Definitions {

	private Definitions() {
		super();
	}

	public static final Definition STANDARD_LETTER = definitionOfType(LETTER)
			.length(from(14).to(23.5)).width(from(9).to(12.5))
			.height(from(0).to(0.5)).gramm(from(0).to(20)).build();

	public static final Definition KOMPAKT_LETTER = definitionOfType(LETTER)
			.length(from(10).to(23.5)).width(from(7).to(12.5))
			.height(from(0).to(1)).gramm(from(0).to(50)).build();

}
