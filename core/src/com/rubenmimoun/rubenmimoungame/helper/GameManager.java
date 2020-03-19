package com.rubenmimoun.rubenmimoungame.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;


public class GameManager {

    public GameData gameData ;
    // the object that will save the data
    private Json json  = new Json();
    // the path to the file
    private FileHandle fileHandle = Gdx.files.local("bin/GameSaveData.json");

    private   Music music;

    public boolean paused = false ;




    private static final GameManager ourInstance = new GameManager();

    public static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
    }

    public int lifeScore = 3 , score = 0 , world ;
    public int bossLife = 5 ;

    public void initializeGameData(){
        // if the filehandle  exist, it means we already created our game data
        // otherwise its the first time we run the game, so create initialize the data
        if(!fileHandle.exists()){

            gameData = new GameData();

            gameData.setHighscore(score);
            gameData.setLvl2open(false);
            gameData.setBoss_lvlopen(false);

            gameData.setMusicOn(true);

            savedata();
        }else {

            loadData();
        }
    }


    public void savedata(){

        if(gameData != null){
            // if our game data is not empty override it, like this
            // crypting it with basecode64
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false);

        }

    }

    public void loadData(){
         /*
         the object is equals to the json from the gamedata class,decode the file from this path
          */
        gameData= json.fromJson(GameData.class,
                Base64Coder.decodeString( fileHandle.readString()));
    }

    public void checkForNewHighScores(){

        int oldHighScore = gameData.getHighscore() ;

        if(oldHighScore < score){
            gameData.setHighscore(score);
            savedata();
        }

    }


    public void playMusic( Music music){

        this.music = music ;

        music.setLooping(true);

        if(!music.isPlaying()){
            music.play();

        }


    }

    public void stopMusic(){

        if(music.isPlaying()){
            music.stop();
        }

    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }


}
