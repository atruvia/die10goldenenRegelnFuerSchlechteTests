package de.fiduciagad.ase.postagecharge.ebrief;

import java.math.BigDecimal;
import java.util.Currency;

public interface Account {

	void charge(BigDecimal amount, Currency currency);

}
