package ru.droogcompanii.application.test.data.offers;

import junit.framework.TestCase;

import java.util.Calendar;

import ru.droogcompanii.application.util.CalendarUtils;
import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.offers.OfferImpl;

public class TestOfferImpl extends TestCase {
	
	private static final CalendarRange duration = prepareDuration();
	private static final int id = 12;
	private static final int partnerId = 123546;
	private static final String shortDescription = "This is short description";
	private static final String fullDescription = "ggj ke\n r th rh n \n rtnb \n";
	private static final String imageUrl = "http://www.droogcompanii.ru/path/to/image.jpg";

	private static final CalendarRange differentDuration = prepareDifferentDuration();
	private static final int differentId = 246;
	private static final int differentPartnerId = 953981042;
	private static final String differentShortDescription = shortDescription + "different";
	private static final String differentFullDescription = fullDescription + "different";
	private static final String differentImageUrl = imageUrl + "different";

	private static CalendarRange prepareDuration() {
		final Calendar from = CalendarUtils.createByDayMonthYear(1, Calendar.DECEMBER, 2013);
		final Calendar to = CalendarUtils.createByDayMonthYear(2, Calendar.MARCH, 2014);
		return new CalendarRange(from, to);
	}

	private static CalendarRange prepareDifferentDuration() {
		final Calendar from = CalendarUtils.createByDayMonthYear(10, Calendar.SEPTEMBER, 2013);
		final Calendar to = CalendarUtils.createByDayMonthYear(14, Calendar.APRIL, 2019);
		return new CalendarRange(from, to);
	}
	
	
	private OfferImpl offer;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		offer = new OfferImpl();
	}
	
	public void testDefaultConstructor() {
		assertEquals(0, offer.getId());
		assertEquals(0, offer.getPartnerId());
		assertNull(offer.getDuration());
		assertTrue(offer.isSpecial());
		assertNull(offer.getShortDescription());
		assertNull(offer.getFullDescription());
		assertNull(offer.getImageUrl());
	}
	
	public void test() {
		offer = prepareOffer();
		assertEquals(id, offer.getId());
		assertEquals(partnerId, offer.getPartnerId());
		assertEquals(duration, offer.getDuration());
		assertEquals(shortDescription, offer.getShortDescription());
		assertEquals(fullDescription, offer.getFullDescription());
		assertEquals(imageUrl, offer.getImageUrl());
	}
	
	private static OfferImpl prepareOffer() {
		OfferImpl prepared = new OfferImpl();
		prepared.id = id;
		prepared.partnerId = partnerId;
		prepared.duration = duration;
		prepared.shortDescription = shortDescription;
		prepared.fullDescription = fullDescription;
		prepared.imageUrl = imageUrl;
		return prepared;
	}
	
	public void testOfferWithoutDurationIsSpecial() {
		offer.duration = null;
		assertTrue(offer.isSpecial());
	}
	
	public void testOfferWithDurationIsNotSpecial() {
		offer.duration = prepareDuration();
		assertFalse(offer.isSpecial());
	}
	
	public void testImplementsOfferInterface() {
		assertTrue(new OfferImpl() instanceof Offer);
	}
	
	public void testEquals() {
		assertEquals(prepareOffer(), prepareOffer());
	}
	
	private static interface ArgumentChanger<T> {
		void change(T arg);
	}
	
	private static void assertNotEqualsIfDifferent(ArgumentChanger<OfferImpl> changer) {
		OfferImpl one = prepareOffer();
		OfferImpl two = prepareOffer();
		changer.change(two);
		assertFalse(one.equals(two));
	}
	
	public void testNotEqualsIfDifferent_Id() {
		assertNotEqualsIfDifferent(new ArgumentChanger<OfferImpl>() {
			@Override
			public void change(OfferImpl arg) {
				arg.id = differentId;
			}
		});
	}
	
	public void testNotEqualsIfDifferent_PartnerId() {
		assertNotEqualsIfDifferent(new ArgumentChanger<OfferImpl>() {
			@Override
			public void change(OfferImpl arg) {
				arg.partnerId = differentPartnerId;
			}
		});
	}
	
	public void testNotEqualsIfDifferent_Duration() {
		assertNotEqualsIfDifferent(new ArgumentChanger<OfferImpl>() {
			@Override
			public void change(OfferImpl arg) {
				arg.duration = differentDuration;
			}
		});
	}
	
	public void testNotEqualsIfDifferent_ShortDescription() {
		assertNotEqualsIfDifferent(new ArgumentChanger<OfferImpl>() {
			@Override
			public void change(OfferImpl arg) {
				arg.shortDescription = differentShortDescription;
			}
		});
	}
	
	public void testNotEqualsIfDifferent_FullDescription() {
		assertNotEqualsIfDifferent(new ArgumentChanger<OfferImpl>() {
			@Override
			public void change(OfferImpl arg) {
				arg.fullDescription = differentFullDescription;
			}
		});
	}
	
	public void testNotEqualsIfDifferent_ImageUrl() {
		assertNotEqualsIfDifferent(new ArgumentChanger<OfferImpl>() {
			@Override
			public void change(OfferImpl arg) {
				arg.imageUrl = differentImageUrl;
			}
		});
	}
	
	public void testHashCodeOfTwoEqualObjectsAreEqual() {
		assertEquals(prepareOffer().hashCode(), prepareOffer().hashCode());
	}
}
