package de.fiduciagad.ase.postagecharge;

import static de.fiduciagad.ase.postagecharge.WeightUnit.GRAMS;

public class DefinitionBuilder {

	private static class DefaultDefinition implements Definition {

		private final Type type;
		private final Range length;
		private final Range width;
		private final Range height;
		private final Range gramm;

		public DefaultDefinition(Type type, Range length, Range width,
				Range height, Range gramm) {
			this.type = type;
			this.length = length;
			this.width = width;
			this.height = height;
			this.gramm = gramm;
		}

		@Override
		public boolean matches(Shippable shippable) {
			return shippable.isType(this.type)
					&& matches(length, shippable.getLength())
					&& matches(width, shippable.getWidth())
					&& matches(height, shippable.getHeight())
					&& matches(gramm, shippable.getWeight(GRAMS));
		}

		private static boolean matches(Range range, double value) {
			return range.inRange(value);
		}
	}

	private final Type type;
	private Range length;
	private Range width;
	private Range height;
	private Range gram;

	public static DefinitionBuilder definitionOfType(Type type) {
		return new DefinitionBuilder(type);
	}

	private DefinitionBuilder(Type type) {
		this.type = type;
	}

	public DefinitionBuilder length(Range length) {
		this.length = length;
		return this;
	}

	public DefinitionBuilder width(Range width) {
		this.width = width;
		return this;
	}

	public DefinitionBuilder height(Range height) {
		this.height = height;
		return this;
	}

	public DefinitionBuilder gramm(Range gram) {
		this.gram = gram;
		return this;
	}

	public Definition build() {
		return new DefaultDefinition(type, length, width, height, gram);
	}

}