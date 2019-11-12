package com.rubenmimoun.rubenmimoungame.helper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class FileManager extends AssetManager {


    private static AssetManager manager;

    private static final FileManager fileManager = new FileManager() ;

    public static FileManager getInstanceOf(){ return fileManager ; }


     private FileManager(){

        loadingAssets();

        finishLoading();



    }


    private void loadingAssets(){

       load("audio/music/theme-1.ogg", Music.class);
        load("audio/music/theme-3.ogg", Music.class);
        load("audio/music/menu.ogg", Music.class);
        load("audio/sound/breakblock.ogg", Sound.class);
        load("audio/sound/coin.ogg", Sound.class);
        load("audio/sound/bump.ogg", Sound.class);
        load("audio/sound/dino_got_hit.wav", Sound.class);
        load("audio/sound/fly_enemyhit.wav", Sound.class);
        load("audio/sound/picked.mp3", Sound.class);
        load("audio/sound/powerup.wav", Sound.class);
        load("audio/sound/water.wav", Sound.class);
        load("audio/sound/gameover.ogg", Sound.class);
        load("audio/sound/victory.ogg", Sound.class);
        load("audio/sound/dinosaur-4.wav", Sound.class);
        load("audio/music/theme-2.ogg", Music.class);
        load("audio/sound/dinosaur-1.wav", Sound.class);
        load("audio/music/finalVictory.ogg", Music.class);
        load("dinoTilesPack/enemies/fylPack.atlas", TextureAtlas.class);

        // box all the loading and then move on

    }

    public Music getTheme(String theme){

        return (Music) get("audio/music/" +  theme, Music.class);

    }

    public Sound getSound(String sound ){
        return (Sound)get("audio/sound/" + sound ,Sound.class);
    }

    public AssetManager getManager() {
        return this;
    }

    public void dispose(){
        dispose();
    }
}
