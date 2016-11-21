package de.fiduciagad.ase.postagecharge.bad_noarchitecture;

import de.fiduciagad.ase.postagecharge.PostageCharge;
import de.fiduciagad.ase.postagecharge.ebrief.Account;

public final class AccountUtil {

	private AccountUtil() {
		super();
	}

	public static void charge(Account account, PostageCharge charge) {
		account.charge(charge.getMonAmount(), charge.getCurrency());
	}

}
