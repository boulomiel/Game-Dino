package com.rubenmimoun.rubenmimoungame.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class GameInfo  {



    public static final Float WIDTH = 540f ;
    public static final Float HEIGHT = 360f ;

    public static  Float PPM = 100f ;

    public static final short DESTROYED = 0;
    public static final short FINALWALLS = 1;
    public static final short GROUND = 2 ;
    public static final short FLYING_ENEMY = 4 ;
    public static final short DINOHEAD = 8 ;
    public static final short BRICK = 16 ;
    public static final short DINOFEET = 32;
    public static final short ENEMY_HEAD = 64;
    public static final short DINO = 128 ;
    public static final short WATER_BUMPER = 256  ;
    public static final short DINO_LIFE = 512 ;
    public static final short END_MEAT_1 =1024 ;
    public static final short GROUND_ENEMY = 2048 ;
    public static final short ENEMYWALLS = 8192 ;
    public static final short ENEMY_BOSS = 16384;



    public static boolean FINISHED_GAME =  false ;
    public static  boolean REACHED_SECOND_END = false ;
    public static  boolean REACHED_FIRST_END = false ;

    public static boolean FIRST_LEVEL = false ;
    public static boolean SECOND_LEVEL = false ;
    public static boolean THIRD_LEVEL = false ;



    public static boolean WORLD2_PLAYING = false ;
    public static boolean TOUCHABLE = true ;


    public static void cleanScreen(Color color){
        Gdx.gl.glClearColor(color.r, color.g, color.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    public static  void cleanscreen(){
        cleanScreen(Color.BLACK);
    }


}
