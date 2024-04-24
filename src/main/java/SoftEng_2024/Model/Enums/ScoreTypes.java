package SoftEng_2024.Model.Enums;
public enum ScoreTypes {
    STATIC,
    INK,
    SCROLL,
    FEATHER,
    ANGLES;
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
}
