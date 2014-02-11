package ru.droogcompanii.application.test.data.hierarchy_of_partners;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.data.time.TimeOfDay;
import ru.droogcompanii.application.data.time.TimeRangeIncludedExcluded;
import ru.droogcompanii.application.data.working_hours.WeekWorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHours;
import ru.droogcompanii.application.data.working_hours.WorkingHoursForEachDayOfWeek;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnBusinessDay;
import ru.droogcompanii.application.data.working_hours.working_hours_impl.WorkingHoursOnHoliday;
import ru.droogcompanii.application.test.TestingUtils;

/**
 * Created by ls on 08.01.14.
 */
public class TestPartnerPoint extends TestCase {
    private static final double latitude = 23.24455;
    private static final double longitude = 21.35354;
    private static final int partnerId = 123;
    private static final List<String> phones = Arrays.asList("9196673255", "9171098777", "6445311");
    private static final String address = "Kazan, ul. Lenina, 23";
    private static final String paymentMethods = "Master Card";
    private static final String title = "Tatneft-Kazan";
    private static final WeekWorkingHours workingHours = new WeekWorkingHours(prepareWorkingHoursOnEachDayOfWeek());


    private PartnerPoint partnerPoint;


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

    private PartnerPoint createPartnerPointByConstants() {
        return new PartnerPoint(
            title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId
        );
    }


    public void testIsSerializable() {
        assertEquals(partnerPoint, TestingUtils.serializeAndDeserialize(partnerPoint));
    }


    public void testConstructor() {
        assertEquals(title, partnerPoint.title);
        assertEquals(address, partnerPoint.address);
        assertEquals(phones, partnerPoint.phones);
        assertEquals(workingHours, partnerPoint.workingHours);
        assertEquals(paymentMethods, partnerPoint.paymentMethods);
        assertEquals(longitude, partnerPoint.longitude);
        assertEquals(latitude, partnerPoint.latitude);
        assertEquals(partnerId, partnerPoint.partnerId);
    }


    public void testEquals() {
        PartnerPoint one = createPartnerPointByConstants();
        PartnerPoint two = createPartnerPointByConstants();
        assertEquals(one, two);
    }


    public void testEqualsToItself() {
        assertEquals(partnerPoint, partnerPoint);
    }


    public void testNotEqualsToNull() {
        assertFalse(partnerPoint.equals(null));
    }


    public void testNotEqualsWithDifferent_Title() {
        assertNotEquals(
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId),
            new PartnerPoint(title + "a", address, phones, workingHours, paymentMethods, longitude, latitude, partnerId)
        );
    }

    private void assertNotEquals(Object obj1, Object obj2) {
        assertFalse(areEqual(obj1, obj2));
    }

    private boolean areEqual(Object obj1, Object obj2) {
        return (obj1 == null) ? (obj2 == null) : obj1.equals(obj2);
    }


    public void testNotEqualsWithDifferent_Address() {
        assertNotEquals(
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId),
            new PartnerPoint(title, address + "a", phones, workingHours, paymentMethods, longitude, latitude, partnerId)
        );
    }


    public void testNotEqualsWithDifferent_Phones() {
        assertNotEquals(
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId),
            new PartnerPoint(title, address, Arrays.asList("111"), workingHours, paymentMethods, longitude, latitude, partnerId)
        );
    }


    public void testNotEqualsWithDifferent_WorkingHours() {
        WorkingHoursForEachDayOfWeek workingHoursForEachDayOfWeek = prepareWorkingHoursOnEachDayOfWeek();
        workingHoursForEachDayOfWeek.onMonday = new WorkingHoursOnHoliday("Monday - Holiday");
        WeekWorkingHours otherWorkingHours = new WeekWorkingHours(workingHoursForEachDayOfWeek);

        assertNotEquals(
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId),
            new PartnerPoint(title, address, phones, otherWorkingHours, paymentMethods, longitude, latitude, partnerId)
        );
    }


    public void testNotEqualsWithDifferent_PaymentMethods() {
        assertNotEquals(
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId),
            new PartnerPoint(title, address, phones, workingHours, paymentMethods + " Visa", longitude, latitude, partnerId)
        );
    }


    public void testNotEqualsWithDifferent_Longitude() {
        assertNotEquals(
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId),
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude + 1.1, latitude, partnerId)
        );
    }


    public void testNotEqualsWithDifferent_Latitude() {
        assertNotEquals(
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId),
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude + 2.31, partnerId)
        );
    }


    public void testNotEqualsWithDifferent_PartnerId() {
        assertNotEquals(
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId),
            new PartnerPoint(title, address, phones, workingHours, paymentMethods, longitude, latitude, partnerId + 2)
        );
    }


    public void testHashCodesAreEqual_OfEqualObjects() {
        PartnerPoint copy = createPartnerPointByConstants();
        assertEquals(partnerPoint.hashCode(), copy.hashCode());
    }

    public void testHashCodeDoesNotThrowExceptionIfSomeOfFieldsIsNull() {
        new PartnerPoint(null, null, null, null, null, 0.0, 0.0, 0)
                .hashCode();
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
        public final PartnerPoint partnerPoint;

        public LocationAndPartnerPoint(android.location.Location location, PartnerPoint partnerPoint) {
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

        public PartnerPoint getPartnerPoint1() {
            return first.partnerPoint;
        }

        public PartnerPoint getPartnerPoint2() {
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

    private static PartnerPoint partnerPointByLocation(Location location) {
        return new PartnerPoint(title, address, phones, workingHours, paymentMethods,
                location.getLongitude(), location.getLatitude(), partnerId);
    }

    public void testDistanceToPartnerPointThrowsExceptionIfArgumentIsNull() {
        TestingUtils.assertExpectedException(IllegalArgumentException.class, new Runnable() {
            @Override
            public void run() {
                partnerPoint.distanceTo((PartnerPoint) null);
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
}

