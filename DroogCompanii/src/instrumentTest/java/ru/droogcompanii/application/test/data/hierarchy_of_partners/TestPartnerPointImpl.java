package ru.droogcompanii.application.test.data.hierarchy_of_partners;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPointImpl;
import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnHoliday;
import ru.droogcompanii.application.test.TestingUtils;
import ru.droogcompanii.application.util.Objects;

/**
 * Created by ls on 08.01.14.
 */
public class TestPartnerPointImpl extends TestCase {
    private static final double latitude = 23.24455;
    private static final double longitude = 21.35354;
    private static final int partnerId = 123;
    private static final List<String> phones = Arrays.asList("9196673255", "9171098777", "6445311");
    private static final String address = "Kazan, ul. Lenina, 23";
    private static final String paymentMethods = "Master Card";
    private static final String title = "Tatneft-Kazan";
    private static final WeekWorkingHours workingHours = new WeekWorkingHours(prepareWorkingHoursOnEachDayOfWeek());


    private PartnerPointImpl partnerPoint;


    private static WorkingHoursForEachDayOfWeek prepareWorkingHoursOnEachDayOfWeek() {
        TimeOfDay fromOfUsualDay = new TimeOfDay(9, 0);
        TimeOfDay toOfUsualDay = new TimeOfDay(19, 0);
        WorkingHours workingHoursOfUsualDay = new WorkingHoursOnBusinessDay()
                .include(new TimeRangeIncludedExcluded(fromOfUsualDay, toOfUsualDay));
        WorkingHours workingHoursOfHoliday = new WorkingHoursOnHoliday("Holiday");
        WorkingHoursForEachDayOfWeek workingHours = new WorkingHoursForEachDayOfWeek();
        workingHours.onMonday = workingHoursOfUsualDay;
        workingHours.onTuesday = workingHoursOfUsualDay;
        workingHours.onWednesday = workingHoursOfUsualDay;
        workingHours.onThursday = workingHoursOfUsualDay;
        workingHours.onFriday = new WorkingHoursOnBusinessDay()
                .include(new TimeRangeIncludedExcluded(new TimeOfDay(10, 0), new TimeOfDay(18, 0)));
        workingHours.onSaturday = workingHoursOfHoliday;
        workingHours.onSunday = workingHoursOfHoliday;
        return workingHours;
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();

        partnerPoint = createPartnerPointByConstants();
    }

    private static PartnerPointImpl createPartnerPointByConstants() {
        PartnerPointImpl partnerPoint = new PartnerPointImpl();
        partnerPoint.title = title;
        partnerPoint.address = address;
        partnerPoint.phones = phones;
        partnerPoint.workingHours = workingHours;
        partnerPoint.paymentMethods = paymentMethods;
        partnerPoint.longitude = longitude;
        partnerPoint.latitude = latitude;
        partnerPoint.partnerId = partnerId;
        return partnerPoint;
    }


    public void testIsSerializable() {
        assertEquals(partnerPoint, TestingUtils.serializeAndDeserialize(partnerPoint));
    }


    public void testAccessors() {
        assertEquals(title, partnerPoint.getTitle());
        assertEquals(address, partnerPoint.getAddress());
        assertEquals(phones, partnerPoint.getPhones());
        assertEquals(workingHours, partnerPoint.getWorkingHours());
        assertEquals(paymentMethods, partnerPoint.getPaymentMethods());
        assertEquals(longitude, partnerPoint.getLongitude());
        assertEquals(latitude, partnerPoint.getLatitude());
        assertEquals(partnerId, partnerPoint.getPartnerId());
    }

    public void testDefaultConstructor() {
        PartnerPointImpl def = new PartnerPointImpl();
        assertTrue(def.getTitle().isEmpty());
        assertTrue(def.getAddress().isEmpty());
        assertTrue(def.getLatitude() == 0.0);
        assertTrue(def.getLongitude() == 0.0);
        assertTrue(def.getPhones().isEmpty());
        assertTrue(def.getPaymentMethods().isEmpty());
        assertTrue(def.getWorkingHours() == null);
        assertTrue(def.getPartnerId() == 0);
    }


    public void testEquals() {
        assertEquals(createPartnerPointByConstants(),
                     createPartnerPointByConstants());
    }


    public void testEqualsToItself() {
        assertEquals(partnerPoint, partnerPoint);
    }


    public void testNotEqualsToNull() {
        assertFalse(partnerPoint.equals(null));
    }


    public void testNotEqualsWithDifferent_Title() {
        assertNotEqualsAfterChanging(new Changer() {
            @Override
            public void change(PartnerPointImpl partnerPointToChange) {
                partnerPointToChange.title = partnerPointToChange.title + " other address";
            }
        });
    }

    private static interface Changer {
        void change(PartnerPointImpl partnerPointToChange);
    }

    public void assertNotEqualsAfterChanging(Changer changer) {
        PartnerPointImpl one = createPartnerPointByConstants();
        PartnerPointImpl two = createPartnerPointByConstants();
        changer.change(two);
        assertNotEquals(one, two);
    }

    private void assertNotEquals(Object obj1, Object obj2) {
        assertFalse(Objects.equals(obj1, obj2));
    }

    public void testNotEqualsWithDifferent_Address() {
        assertNotEqualsAfterChanging(new Changer() {
            @Override
            public void change(PartnerPointImpl partnerPointToChange) {
                partnerPointToChange.address = partnerPointToChange.address + " other address";
            }
        });
    }


    public void testNotEqualsWithDifferent_Phones() {
        assertNotEqualsAfterChanging(new Changer() {
            @Override
            public void change(PartnerPointImpl partnerPointToChange) {
                partnerPointToChange.phones = Arrays.asList("111");
            }
        });
    }


    public void testNotEqualsWithDifferent_WorkingHours() {
        WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek = prepareWorkingHoursOnEachDayOfWeek();
        workingHoursForEachDayOfWeek.onMonday = new WorkingHoursOnHoliday("Monday - Holiday");
        final WeekWorkingHours otherWorkingHours = new WeekWorkingHours(workingHoursForEachDayOfWeek);

        assertNotEqualsAfterChanging(new Changer() {
            @Override
            public void change(PartnerPointImpl partnerPointToChange) {
                partnerPointToChange.workingHours = otherWorkingHours;
            }
        });
    }


    public void testNotEqualsWithDifferent_PaymentMethods() {
        assertNotEqualsAfterChanging(new Changer() {
            @Override
            public void change(PartnerPointImpl partnerPointToChange) {
                partnerPointToChange.paymentMethods = partnerPointToChange.paymentMethods + " Visa";
            }
        });
    }


    public void testNotEqualsWithDifferent_Longitude() {
        assertNotEqualsAfterChanging(new Changer() {
            @Override
            public void change(PartnerPointImpl partnerPointToChange) {
                partnerPointToChange.longitude += 1.1;
            }
        });
    }


    public void testNotEqualsWithDifferent_Latitude() {
        assertNotEqualsAfterChanging(new Changer() {
            @Override
            public void change(PartnerPointImpl partnerPointToChange) {
                partnerPointToChange.latitude += 2.31;
            }
        });
    }


    public void testNotEqualsWithDifferent_PartnerId() {
        assertNotEqualsAfterChanging(new Changer() {
            @Override
            public void change(PartnerPointImpl partnerPointToChange) {
                partnerPointToChange.partnerId += 1;
            }
        });
    }


    public void testEqualsDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        PartnerPointImpl one = createPartnerPointByConstants();
        PartnerPointImpl two = createPartnerPointByConstants();
        one.title = null;
        two.title = null;
        one.address = null;
        two.phones = null;
        one.equals(two);
    }


    public void testHashCodesAreEqual_OfEqualObjects() {
        assertEquals(createPartnerPointByConstants().hashCode(),
                     createPartnerPointByConstants().hashCode());
    }

    public void testHashCodeDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        new PartnerPointImpl().hashCode();
    }

    public void testGetPosition() {
        LatLng expected = new LatLng(latitude, longitude);
        assertEquals(expected, partnerPoint.getPosition());
    }

    public void testDistanceToItselfIsZero() {
        assertEquals(0.0f, partnerPoint.distanceTo(partnerPoint));
    }

    private static class LocationAndPartnerPoint {
        public final Location location;
        public final PartnerPointImpl partnerPoint;

        public LocationAndPartnerPoint(android.location.Location location, PartnerPointImpl partnerPoint) {
            this.location = location;
            this.partnerPoint = partnerPoint;
        }
    }

    private static class PairOfLocationAndPartnerPoint {
        private final LocationAndPartnerPoint first;
        private final LocationAndPartnerPoint second;

        public PairOfLocationAndPartnerPoint(LocationAndPartnerPoint first, LocationAndPartnerPoint second) {
            this.first = first;
            this.second = second;
        }

        public float expectedDistance() {
            return getLocation1().distanceTo(getLocation2());
        }

        public Location getLocation1() {
            return first.location;
        }

        public Location getLocation2() {
            return second.location;
        }

        public PartnerPointImpl getPartnerPoint1() {
            return first.partnerPoint;
        }

        public PartnerPointImpl getPartnerPoint2() {
            return second.partnerPoint;
        }
    }

    public void testDistanceToPartnerPoint() {
        PairOfLocationAndPartnerPoint testData = getPairOfLocationAndPartnerPoint();
        float actualDistance = testData.getPartnerPoint1().distanceTo(testData.getPartnerPoint2());
        assertEquals(testData.expectedDistance(), actualDistance);
    }

    private static PairOfLocationAndPartnerPoint getPairOfLocationAndPartnerPoint() {
        Location location1 = createLocation("1", 53.5462, 55.2142);
        Location location2 = createLocation("2", 51.5368, 52.4258);
        LocationAndPartnerPoint first = new LocationAndPartnerPoint(location1, partnerPointByLocation(location1));
        LocationAndPartnerPoint second = new LocationAndPartnerPoint(location2, partnerPointByLocation(location2));
        return new PairOfLocationAndPartnerPoint(first, second);
    }

    private static Location createLocation(String provider, double latitude, double longitude) {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }

    private static PartnerPointImpl partnerPointByLocation(Location location) {
        PartnerPointImpl partnerPoint = createPartnerPointByConstants();
        partnerPoint.latitude = location.getLatitude();
        partnerPoint.longitude = location.getLongitude();
        return partnerPoint;
    }

    public void testDistanceToPartnerPointThrowsExceptionIfArgumentIsNull() {
        TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                partnerPoint.distanceTo((PartnerPointImpl) null);
            }
        });
    }

    public void testDistanceToLocation() {
        PairOfLocationAndPartnerPoint testData = getPairOfLocationAndPartnerPoint();
        float actualDistance = testData.getPartnerPoint1().distanceTo(testData.getLocation2());
        assertEquals(testData.expectedDistance(), actualDistance);
    }

    public void testDistanceToLocationThrowsExceptionIfArgumentIsNull() {
        TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                partnerPoint.distanceTo((Location) null);
            }
        });
    }

    public void testToLocation() {
        Location actual = partnerPoint.toLocation();
        assertEquals(title, actual.getProvider());
        assertEquals(latitude, actual.getLatitude());
        assertEquals(longitude, actual.getLongitude());
    }

    public void testToStringDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        PartnerPointImpl partnerPoint = new PartnerPointImpl();
        partnerPoint.title = "some title";
        String str = partnerPoint.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());
    }
}

