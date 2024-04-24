package SoftEng_2024.Model.Enums;
public enum AngleIndexes {
    UPLEFT,
    UPRIGHT,
    DOWNLEFT,
    DOWNRIGHT;

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

