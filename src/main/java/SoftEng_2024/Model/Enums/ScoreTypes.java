package SoftEng_2024.Model.Enums;

/**
 * Enumeration representing different types of scoring mechanisms in the game.
 * <p>
 * The scoring types included are:
 * - {@code STATIC}: Points earned immediately upon placement.
 * - {@code INK}: Points earned for each ink in the player's board.
 * - {@code SCROLL}: Points earned for each scroll in the player's board.
 * - {@code FEATHER}: Points earned for each feather in the player's board.
 * - {@code ANGLES}: Points earned for each angle covered by a card.
 */
public enum ScoreTypes {
    /**
     * Points earned immediately upon placement.
     */
    STATIC,

    /**
     * Points earned for each ink in the player's board.
     */
    INK,

    /**
     * Points earned for each scroll in the player's board.
     */
    SCROLL,

    /**
     * Points earned for each feather in the player's board.
     */
    FEATHER,

    /**
     * Points earned for each angle covered by a card.
     */
    ANGLES;

    /**
     * Returns the index associated with the {@code ScoreTypes} enum value.
     *
     * @param scoreType the {@code ScoreTypes} enum value
     * @return the index associated with the score type; returns {@code -1} if not found
     */
    public static int getIndex(ScoreTypes scoreType){
        switch (scoreType) {
            case STATIC:
                return 8;
            case INK:
                return 4;
            case SCROLL:
                return 5;
            case FEATHER:
                return 6;
            case ANGLES:
                return 7;
        }
        return -1;
    }

    /**
     * Returns a descriptive string representing the scoring mechanism of the {@code ScoreTypes} enum value.
     *
     * @param scoreTypes the {@code ScoreTypes} enum value
     * @return a string describing the scoring mechanism; returns an empty string if not found
     */
    public static String getScoreType(ScoreTypes scoreTypes){
        switch (scoreTypes){
            case STATIC:
                return "when placed";
            case FEATHER:
                return "for each feather in your board";
            case SCROLL:
                return "for each scroll in your board";
            case INK:
                return "for each ink in your board";
            case ANGLES:
                return "for each angle you'll cover with this card";
        }
        return "";
    }
}
