package com.rubenmimoun.rubenmimoungame.helper;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetsManager  {

    private  AssetManager manager ;


    public AssetManager getInstanceOf(){
        return  manager ;
    }

    public AssetsManager(){

        manager = new AssetManager();
        loadingAssets();
        manager.update();


        manager.finishLoading();

    }


    private void loadingAssets(){

        manager.load("audio/music/theme-1.ogg", Music.class);
        manager.load("audio/music/theme-3.ogg", Music.class);
        manager.load("audio/music/menu.ogg", Music.class);
        manager.load("audio/sound/breakblock.ogg", Sound.class);
        manager.load("audio/sound/coin.ogg", Sound.class);
        manager.load("audio/sound/bump.ogg", Sound.class);
        manager.load("audio/sound/dino_got_hit.wav", Sound.class);
        manager.load("audio/sound/fly_enemyhit.wav", Sound.class);
        manager.load("audio/sound/picked.mp3", Sound.class);
        manager.load("audio/sound/powerup.wav", Sound.class);
        manager.load("audio/sound/water.wav", Sound.class);
        manager.load("audio/sound/gameover.ogg", Sound.class);
        manager.load("audio/sound/victory.ogg", Sound.class);
        manager.load("audio/sound/dinosaur-4.wav", Sound.class);
        manager.load("audio/music/theme-2.ogg", Music.class);
        manager.load("audio/sound/dinosaur-1.wav", Sound.class);
        manager.load("audio/music/finalVictory.ogg", Music.class);
        manager.load("dinoTilesPack/enemies/fylPack.atlas", TextureAtlas.class);

        // box all the loading and then move on

    }

    public Music getTheme(String theme){

        return (Music) manager.get("audio/music/" +  theme, Music.class);

    }

    public Sound getSound(String sound ){
        return (Sound)manager.get("audio/sound/" + sound ,Sound.class);
    }

    public AssetManager getManager() {
        return manager;
    }

    public void dispose(){
        manager.dispose();
    }
}
