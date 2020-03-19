package com.rubenmimoun.rubenmimoungame.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public abstract class Enemy extends Sprite {

    protected World world;
    protected GameScreenInterface gameScreen ;
    protected Body body  ;

    protected Vector2 movement ;

    protected Vector2 forth ;

    protected Vector2 back ;


    public Enemy(GameScreenInterface gameScreen , float x , float y  ){

        this.gameScreen = gameScreen ;

        this.world = gameScreen.getWorld() ;

        movement = new Vector2(1 ,0);

        forth =new Vector2() ;
        forth = movement ;

        back = new Vector2(-1 , 0) ;

        setPosition(x , y);

        createBody();

    }


    protected void createBody(){
    }

    public void update(Float delta){}

    public  void crushedByDinosStep(Dino dino){}

    public  Vector2 reverseHeight(){return  movement ; }


    public   void reverseVelocity(boolean x, boolean y){
        if (x){
            movement.x = -movement.x ;
        }

        if(y){
            movement.y = -movement.y;
        }

    }

    public Body getBody() {
        return body;
    }

    public void dispose(){
        getTexture().dispose();
    }
}
