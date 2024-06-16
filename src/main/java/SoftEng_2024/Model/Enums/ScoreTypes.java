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
