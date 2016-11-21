package de.fiduciagad.ase.postagecharge;

import static de.fiduciagad.ase.postagecharge.Definitions.KOMPAKT_LETTER;
import static de.fiduciagad.ase.postagecharge.Definitions.STANDARD_LETTER;
import static de.fiduciagad.ase.postagecharge.Type.EBRIEF;
import static de.fiduciagad.ase.postagecharge.Type.LETTER;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultPostageChargeCalculator implements PostageChargeCalculator {

	public static final class ShippableTypeAdapter implements Shippable {

		private final Shippable shippable;
		private final Type adaptedType;

		private ShippableTypeAdapter(Shippable shippable, Type adaptedType) {
			this.shippable = shippable;
			this.adaptedType = adaptedType;
		}

		@Override
		public boolean isType(Type type) {
			return type == adaptedType;
		}

		@Override
		public double getLength() {
			return shippable.getLength();
		}

		@Override
		public double getWidth() {
			return shippable.getWidth();
		}

		@Override
		public double getHeight() {
			return shippable.getHeight();
		}

		@Override
		public int getWeight(WeightUnit weightUnit) {
			return shippable.getWeight(weightUnit);
		}

		@Override
		public String toString() {
			return "Adapted " + shippable;
		}
	}

	private static final Currency EUR = Currency.getInstance("EUR");
	private static final Map<Definition, PostageCharge> postages = postageCharges();

	private static Map<Definition, PostageCharge> postageCharges() {
		Map<Definition, PostageCharge> postageCharges = new HashMap<>();
		postageCharges.put(STANDARD_LETTER, new PostageCharge(new BigDecimal(
				"0.70"), EUR));
		postageCharges.put(KOMPAKT_LETTER, new PostageCharge(new BigDecimal(
				"0.85"), EUR));
		return sortByCost(postageCharges);
	}

	private static Map<Definition, PostageCharge> sortByCost(
			Map<Definition, PostageCharge> postageCharges) {
		Map<Definition, PostageCharge> sortedPostages = new LinkedHashMap<>();
		for (Entry<Definition, PostageCharge> entry : sortEntriesByValues(postageCharges)) {
			sortedPostages.put(entry.getKey(), entry.getValue());
		}
		return sortedPostages;
	}

	private static List<Entry<Definition, PostageCharge>> sortEntriesByValues(
			Map<Definition, PostageCharge> postageCharges) {
		List<Entry<Definition, PostageCharge>> entries = new LinkedList<>(
				postageCharges.entrySet());
		Collections.sort(entries, compareByMonAmount());
		return entries;
	}

	private static Comparator<Entry<Definition, PostageCharge>> compareByMonAmount() {
		return new Comparator<Entry<Definition, PostageCharge>>() {
			@Override
			public int compare(Entry<Definition, PostageCharge> entry1,
					Entry<Definition, PostageCharge> entry2) {
				return value(entry1).compareTo(value(entry2));
			}

			private BigDecimal value(Entry<Definition, PostageCharge> entry) {
				return entry.getValue().getMonAmount();
			}

		};
	}
	
	@Override
	public PostageCharge calculatePostageCharge(Shippable shippable) {
		PostageCharge postageCharge = postageCharge(shippable);
		BigDecimal serviceCharge = serviceCharge(shippable);
		if (serviceCharge == null || serviceCharge.signum() == 0) {
			return postageCharge;
		}
		// TODO check service charge to be same currency
		return new PostageCharge(postageCharge.getMonAmount()
				.add(serviceCharge), postageCharge.getCurrency());
	}

	private PostageCharge postageCharge(Shippable shippable) {
		Shippable calculateFor = adaptIfNeeded(shippable);
		for (Entry<Definition, PostageCharge> entry : postages.entrySet()) {
			if (entry.getKey().matches(calculateFor)) {
				return entry.getValue();
			}
		}
		throw new IllegalStateException("Cannot calculate postage of "
				+ shippable);
	}

	private Shippable adaptIfNeeded(Shippable shippable) {
		return shippable.isType(EBRIEF) ? new ShippableTypeAdapter(shippable,
				LETTER) : shippable;
	}

	private BigDecimal serviceCharge(Shippable shippable) {
		return shippable.isType(EBRIEF) ? new BigDecimal("0.35") : null;
	}

}
