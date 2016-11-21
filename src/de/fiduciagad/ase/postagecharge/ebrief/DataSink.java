package de.fiduciagad.ase.postagecharge.ebrief;

import java.io.IOException;

public interface DataSink {

	void store(Account account, EBrief eBrief) throws IOException;

}
