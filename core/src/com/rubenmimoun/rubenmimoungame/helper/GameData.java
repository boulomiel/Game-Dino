package com.rubenmimoun.rubenmimoungame.helper;

public class GameData {

    private int highscore;
    private boolean lvl2open ;
    private  boolean boss_lvlopen ;

    private boolean isMusicOn ;

    public GameData(){

        lvl2open = false ;
        boss_lvlopen = false ;
        this.highscore =  GameManager.getInstance().score ;
    }



    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }


    public void setMusicOn(boolean musicOn) {
        isMusicOn = musicOn;
    }

    public void setLvl2open(boolean lvl2open) {
        this.lvl2open = lvl2open;
    }

    public boolean isLvl2open() {
        return lvl2open;
    }

    public void setBoss_lvlopen(boolean boss_lvlopen) {
        this.boss_lvlopen = boss_lvlopen;
    }

    public boolean isBoss_lvlopen() {
        return boss_lvlopen;
    }
}
