package SoftEng_2024.Model.Enums;

public enum GameState {
    CONNECTION,
    STARTER,
    SETCOLOR,
    CHOOSEGOAL,
    PLAY,
    NOTPLAYING,
    ENDGAME;
    public GameState nextState(){
        return values()[this.ordinal()+1];
    }

}
