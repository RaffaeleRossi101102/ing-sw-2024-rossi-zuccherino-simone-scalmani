package SoftEng_2024.Model.Enums;

/**
 * Enumeration representing indexes for angles on a card.
 * This enum provides a method to retrieve the index of each angle.
 */
public enum AngleIndexes {
    /**
     * Index for the upper-left angle.
     */
    UPLEFT,

    /**
     * Index for the upper-right angle.
     */
    UPRIGHT,

    /**
     * Index for the down-left angle.
     */
    DOWNLEFT,

    /**
     * Index for the down-right angle.
     */
    DOWNRIGHT;

    /**
     * Retrieves the index of the specified angle.
     *
     * @param angle the angle whose index is to be retrieved
     * @return the index of the angle (0 for UPLEFT, 1 for UPRIGHT, 2 for DOWNLEFT, 3 for DOWNRIGHT)
     */
    public static int getIndex(AngleIndexes angle) {
        switch (angle) {
            case UPLEFT:
                return 0;
            case UPRIGHT:
                return 1;
            case DOWNLEFT:
                return 2;
            case DOWNRIGHT:
                return 3;
        }
        return 0;
    }
}

