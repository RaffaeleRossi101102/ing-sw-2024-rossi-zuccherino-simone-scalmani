package SoftEng_2024.Model.Enums;

/**
 * Enumeration representing the states of a cell in a game scenario.
 * <p>
 * The states include:
 * - {@code PLACEABLE}: Indicates that the cell can be placed.
 * - {@code NOTPLACEABLE}: Indicates that the cell cannot be placed.
 * - {@code BANNED}: Indicates that the cell is banned from being placed.
 */
public enum CellState {
    /**
     * Indicates that the cell is placeable.
     */
    PLACEABLE,

    /**
     * Indicates that the cell is not placeable.
     */
    NOTPLACEABLE,

    /**
     * Indicates that the cell is banned from being placed.
     */
    BANNED;
}
