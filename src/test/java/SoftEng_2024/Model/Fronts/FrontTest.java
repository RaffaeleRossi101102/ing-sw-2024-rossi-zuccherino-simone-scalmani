package SoftEng_2024.Model.Fronts;

import SoftEng_2024.Model.Enums.Angles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrontTest {

    Front testFront;

    @Test
    void getFrontAngles() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI}, 0, new boolean[]{false, false, false, false});
        Angles[] expected = new Angles[]{Angles.FUNGI, Angles.FUNGI};

        assertArrayEquals(expected, testFront.getFrontAngles());
    }

    @Test
    void getPoints() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI}, 0, new boolean[]{false, false, false, false});

        assertEquals(0, testFront.getPoints());
    }

    @Test
    void getCovered() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI}, 0, new boolean[]{false, false, false, false});
        boolean[] expected = new boolean[]{false, false, false, false};

        assertArrayEquals(expected, testFront.getCovered());
    }

    @Test
    void hideFrontAngles() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI}, 0, new boolean[]{false, false, false, false});
        Angles[] expected = new Angles[]{Angles.EMPTY, Angles.EMPTY};

        testFront.hideFrontAngles();
        assertArrayEquals(expected, testFront.getFrontAngles());
    }

    @Test
    void getHidden() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI}, 0, new boolean[]{false, false, false, false});

        assertFalse(testFront.getHidden());
    }

    @Test
    void setHidden() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI}, 0, new boolean[]{false, false, false, false});
        testFront.setHidden(true);

        assertTrue(testFront.getHidden());
    }

    @Test
    void setCovered() {
        testFront = new ResourceFront(new Angles[]{Angles.FUNGI, Angles.FUNGI}, 0, new boolean[]{false, false, false, false});
        testFront.setCovered(0, true);

        assertTrue(testFront.getCovered()[0]);
    }
}