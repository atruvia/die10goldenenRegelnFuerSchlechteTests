package de.fiduciagad.ase.postagecharge.ebrief;

import static de.fiduciagad.ase.postagecharge.Type.EBRIEF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fiduciagad.ase.postagecharge.Shippable;
import de.fiduciagad.ase.postagecharge.Type;
import de.fiduciagad.ase.postagecharge.WeightUnit;

public class EBrief implements Shippable {

	private List<Page> pages = Collections.emptyList();
	private Format format = Format.A4_PORTRAIT;
	private int foldCount = 2;

	public void setPages(List<Page> pages) {
		this.pages = Collections.unmodifiableList(new ArrayList<Page>(pages));
	}

	public List<Page> getPages() {
		return pages;
	}

	@Override
	public boolean isType(Type type) {
		return type == EBRIEF;
	}

	@Override
	public double getLength() {
		return format.getWidth();
	}

	@Override
	public double getWidth() {
		return format.getLength() / (foldCount + 1);
	}

	@Override
	public double getHeight() {
		return Math.pow(2, pageCount() * (foldCount + 1)) / 100
				+ envelopeHeight();
	}

	@Override
	public int getWeight(WeightUnit weightUnit) {
		return pageCount() * 5 + weightOfEnvelope();
	}

	private int weightOfEnvelope() {
		return 4;
	}

	private double envelopeHeight() {
		return 0.15;
	}

	private int pageCount() {
		return getPages().size();
	}

	public byte[] toByteArray() {
		// TODO to implement
		return toString().getBytes();
	}

	public String toHex() {
		return bytesToHex(toByteArray());
	}

	public static String bytesToHex(byte[] bytes) {
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	@Override
	public String toString() {
		return "EBrief [pages=" + pages + ", format=" + format + "]";
	}

}
