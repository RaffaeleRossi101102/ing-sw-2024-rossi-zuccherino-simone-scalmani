package SoftEng_2024.Model.Enums;

public enum Angles {
    INSECTS, FUNGI, ANIMALS, PLANTS, INK, SCROLL, FEATHER, EMPTY,INVISIBLE;
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