package SoftEng_2024.Model.Enums;


/**
 * Enumeration representing different types of angles on a card.
 * Provides methods to get the index and symbol of each angle.
 */
public enum Angles {
    /**
     * Represents the angle containing insects.
     */
    INSECTS,

    /**
     * Represents the angle containing fungi.
     */
    FUNGI,

    /**
     * Represents the angle containing animals.
     */
    ANIMALS,

    /**
     * Represents the angle containing plants.
     */
    PLANTS,

    /**
     * Represents the angle containing ink.
     */
    INK,

    /**
     * Represents the angle containing a scroll.
     */
    SCROLL,

    /**
     * Represents the angle containing a feather.
     */
    FEATHER,

    /**
     * Represents an empty angle.
     */
    EMPTY,

    /**
     * Represents an invisible angle.
     */
    INVISIBLE;

    /**
     * Retrieves the index of the specified angle.
     *
     * @param angle the angle whose index is to be retrieved
     * @return the index of the angle (0 for INSECTS, 1 for FUNGI, ..., 7 for EMPTY, 8 for INVISIBLE)
     */
    public static int getIndex(Angles angle) {
        switch (angle) {
            case INSECTS:
                return 0;
            case FUNGI:
                return 1;
            case ANIMALS:
                return 2;
            case PLANTS:
                return 3;
            case  INK:
                return 4;
            case SCROLL:
                return 5;
            case FEATHER:
                return 6;
            case EMPTY:
                return 7;
            case INVISIBLE:
                return 8;
        }
        return 0;
    }

    /**
     * Retrieves the symbol representing the specified angle.
     *
     * @param angle the angle whose symbol is to be retrieved
     * @return the symbol representing the angle ('I' for INSECTS, 'F' for FUNGI, ..., '-' for INVISIBLE)
     */
    public static char getAngleSymbol(Angles angle) {
        char symbol = 0;
        switch (angle) {
            case INSECTS:
                symbol = 'I';
                break;
            case FUNGI:
                symbol = 'F';
                break;
            case PLANTS:
                symbol = 'P';
                break;
            case ANIMALS:
                symbol = 'A';
                break;
            case INK:
                symbol= 'K';
                break;
            case SCROLL:
                symbol = 'S';
                break;
            case FEATHER:
                symbol = 'H';
                break;
            case EMPTY:
                symbol = 'Ø';
                break;
            case INVISIBLE:
                symbol = '-';
                break;

        }
        return symbol;
    }
}