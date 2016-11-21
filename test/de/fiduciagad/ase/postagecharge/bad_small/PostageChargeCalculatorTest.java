package de.fiduciagad.ase.postagecharge.bad_small;

import static de.fiduciagad.ase.postagecharge.Type.EBRIEF;
import static de.fiduciagad.ase.postagecharge.Type.LETTER;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forLargestPossibleCompactLetter;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forLargestPossibleStandardLetter;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forSmallestPossibleCompactLetter;
import static de.fiduciagad.ase.postagecharge.util.Shippables.forSmallestPossibleStandardLetter;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.Test;
import org.mockito.Mockito;

import de.fiduciagad.ase.postagecharge.DefaultPostageChargeCalculator;
import de.fiduciagad.ase.postagecharge.DefaultPostageChargeCalculator.ShippableTypeAdapter;
import de.fiduciagad.ase.postagecharge.Definition;
import de.fiduciagad.ase.postagecharge.Letter;
import de.fiduciagad.ase.postagecharge.PostageCharge;
import de.fiduciagad.ase.postagecharge.PostageChargeCalculator;
import de.fiduciagad.ase.postagecharge.Range;
import de.fiduciagad.ase.postagecharge.Shippable;
import de.fiduciagad.ase.postagecharge.Type;
import de.fiduciagad.ase.postagecharge.WeightUnit;

public class PostageChargeCalculatorTest {

	private final PostageChargeCalculator sut = new DefaultPostageChargeCalculator();

	@Test
	public void testeLetterGetter_isType() {
		Letter letter = new Letter(0, 1, 2, 3);
		assertThat(letter.isType(Type.LETTER), is(true));
		assertThat(letter.isType(Type.EBRIEF), is(false));
	}

	@Test
	public void testeLetterGetter_getLength() {
		Letter letter = new Letter(0, 1, 2, 3);
		assertThat(letter.getLength(), is(0.0));
	}

	@Test
	public void testeLetterGetter_getWidth() {
		Letter letter = new Letter(0, 1, 2, 3);
		assertThat(letter.getWidth(), is(1.0));
	}

	@Test
	public void testeLetterGetter_getHeight() {
		Letter letter = new Letter(0, 1, 2, 3);
		assertThat(letter.getHeight(), is(2.0));
	}

	@Test
	public void testeLetterGetter_getWeight() {
		Letter letter = new Letter(0, 1, 2, 3);
		assertThat(letter.getWeight(WeightUnit.GRAMS), is(3));
	}

	@Test
	public void testWeightUnit_convert() {
		assertThat(WeightUnit.GRAMS.convert(3, WeightUnit.KILOS), is(3_000));
		assertThat(WeightUnit.GRAMS.convert(4, WeightUnit.TONS), is(4_000_000));
	}

	@Test
	public void testRange_inRange() {
		Range range = Range.of(1, 3);
		assertThat(range.inRange(0), is(false));
		assertThat(range.inRange(1), is(true));
		assertThat(range.inRange(2), is(true));
		assertThat(range.inRange(3), is(true));
		assertThat(range.inRange(4), is(false));
	}

	@Test
	public void testDefinition_matches() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException,
			InvocationTargetException {

		Type type = Type.LETTER;
		Range length = Range.of(0, 1);
		Range width = Range.of(2, 3);
		Range height = Range.of(4, 5);
		Range gramm = Range.of(6, 7);
		Definition definition = definition(type, length, width, height, gramm);
		assertThat(definition.matches(new Letter(0, 2, 4, 6)), is(true));
		assertThat(definition.matches(new Letter(1, 2, 4, 6)), is(true));
		assertThat(definition.matches(new Letter(0, 3, 4, 6)), is(true));
		assertThat(definition.matches(new Letter(0, 2, 5, 6)), is(true));
		assertThat(definition.matches(new Letter(0, 2, 4, 7)), is(true));

		assertThat(definition.matches(new Letter(2, 2, 4, 6)), is(false));
		assertThat(definition.matches(new Letter(0, 4, 4, 6)), is(false));
		assertThat(definition.matches(new Letter(0, 2, 6, 6)), is(false));
		assertThat(definition.matches(new Letter(0, 2, 4, 8)), is(false));

	}

	private Definition definition(Type type, Range length, Range width,
			Range height, Range gramm) throws ClassNotFoundException,
			NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Class<?> clazz = Class
				.forName("de.fiduciagad.ase.postagecharge.DefinitionBuilder$DefaultDefinition");
		Constructor<?> constructor = clazz.getConstructor(Type.class,
				Range.class, Range.class, Range.class, Range.class);
		constructor.setAccessible(true);
		return (Definition) constructor.newInstance(type, length, width,
				height, gramm);
	}

	@Test
	public void testPostageCharge() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		assertThat(postageCharge(forSmallestPossibleStandardLetter())
				.getMonAmount(), is(new BigDecimal("0.70")));
		assertThat(postageCharge(forLargestPossibleStandardLetter())
				.getMonAmount(), is(new BigDecimal("0.70")));
		assertThat(postageCharge(forSmallestPossibleCompactLetter())
				.getMonAmount(), is(new BigDecimal("0.85")));
		assertThat(postageCharge(forLargestPossibleCompactLetter())
				.getMonAmount(), is(new BigDecimal("0.85")));

		// eBrief gets adapted as regular letter so there is no need to test
		// different scenarios on eBrief. So just let's test if eBrief get's
		// really adapted
		Shippable in = forLargestPossibleCompactLetter();
		assertThat(adaptIfNeeded(shippableOfType(EBRIEF)),
				instanceOf(ShippableTypeAdapter.class));
		// letters don't get adapted
		assertThat(adaptIfNeeded(in), instanceOf(Letter.class));
	}

	@Test
	public void testServiceCharge() throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		assertThat(serviceCharge(shippableOfType(LETTER)), is(nullValue()));
		assertThat(serviceCharge(shippableOfType(EBRIEF)), is(new BigDecimal(
				"0.35")));
		assertThat(serviceCharge(shippableOfType(null)), is(nullValue()));
	}

	private Shippable adaptIfNeeded(Shippable shippable)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return (Shippable) invoke("adaptIfNeeded", shippable);
	}

	private PostageCharge postageCharge(Shippable shippable)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return (PostageCharge) invoke("postageCharge", shippable);
	}

	private BigDecimal serviceCharge(Shippable shippable)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		return (BigDecimal) invoke("serviceCharge", shippable);
	}

	private Object invoke(String name, Shippable shippable)
			throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		Method method = sut.getClass().getDeclaredMethod(name, Shippable.class);
		method.setAccessible(true);
		return method.invoke(sut, shippable);
	}

	private Shippable shippableOfType(Type type) {
		Shippable letter = Mockito.mock(Shippable.class);
		when(letter.isType(type)).thenReturn(true);
		return letter;
	}

}
