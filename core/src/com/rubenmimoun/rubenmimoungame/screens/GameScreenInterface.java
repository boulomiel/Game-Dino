package com.rubenmimoun.rubenmimoungame.screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.rubenmimoun.rubenmimoungame.items.ItemDef;

public abstract class GameScreenInterface implements Screen, InputProcessor, GestureDetector.GestureListener {


    private World world;
    private TextureAtlas dinoAtlas;
    private Texture texture;
    private TiledMap tiledMap;


     public void spawnItem(ItemDef itemDef){}

     protected void handleSpawningItems(){};

    protected void update(float delta){};


   protected void handleInput(float delta){}


    protected void setScores(){};


    public void rebootScreen(){};

    public void finishedLevel(){};

    public void victory(){}



   public   World getWorld(){return this.world;}

   public TiledMap getTiledMap(){return this.tiledMap;}

    public TextureAtlas getDinoAtlas(){return this.dinoAtlas;}

    public Texture getTexture(){return this.texture;}




}
