package de.fiduciagad.ase.postagecharge.ebrief;

import java.io.IOException;

import de.fiduciagad.ase.postagecharge.PostageCharge;
import de.fiduciagad.ase.postagecharge.PostageChargeCalculator;

public class EBriefUploader {

	private final DataSink dataSink;
	private final PostageChargeCalculator calculator;

	public EBriefUploader(PostageChargeCalculator calculator, DataSink dataSink) {
		this.dataSink = dataSink;
		this.calculator = calculator;
	}

	public void upload(Account account, EBrief eBrief) throws IOException {
		// TODO check account on positive balance
		this.dataSink.store(account, eBrief);
		PostageCharge charge = calculator.calculatePostageCharge(eBrief);
		account.charge(charge.getMonAmount(), charge.getCurrency());
	}

}
