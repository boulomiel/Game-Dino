package com.rubenmimoun.rubenmimoungame.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.rubenmimoun.rubenmimoungame.Sprites.Dino;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public abstract class Item extends Sprite {

    Body body ;
    World world ;
    Vector2 velocity  ;
    //TODO :  when we'll have another world, create an abstract class of playscreens !!
    com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface screen;
    boolean isDestroyed ;
    boolean toDestroy ;



    Item(GameScreenInterface screen, float x , float y, String pathTotexture){
        super(new Texture(pathTotexture));
        this.world = screen.getWorld() ;



        setPosition(x, y);
        setBounds(getX(),getY(),35f/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, 29f/ GameInfo.PPM);

        createItem();

        isDestroyed = false ;
        toDestroy = false ;


    }

    protected abstract void createItem();

    protected abstract void itemUsed(Dino dino);

    public  void update(float delta){
        if( toDestroy && !isDestroyed ){
            world.destroyBody(body);
            isDestroyed = true ;
        }

    }

    @Override
    public void draw(Batch batch) {
        if( !isDestroyed)
        super.draw(batch);

    }

    public boolean toDestroy(){
    return  this.toDestroy = true;
    }

    public  void reverseVelocity(boolean x , boolean y ){
        if(x){
            velocity.x = -velocity.x;
        }
        if(y){
            velocity.y = -velocity.y;

        }
    }
}
