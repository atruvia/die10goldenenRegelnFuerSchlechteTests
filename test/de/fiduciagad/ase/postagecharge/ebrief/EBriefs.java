package de.fiduciagad.ase.postagecharge.ebrief;

import java.util.Arrays;
import java.util.List;

public final class EBriefs {

	private EBriefs() {
		super();
	}

	public static EBrief eBriefWithOneEmptyPage() {
		EBrief eBrief = new EBrief();
		eBrief.setPages(oneEmptyPage());
		return eBrief;
	}

	private static List<Page> oneEmptyPage() {
		return Arrays.asList(emptyPage());
	}

	private static Page emptyPage() {
		return new Page("");
	}

}
