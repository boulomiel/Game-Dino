package com.rubenmimoun.rubenmimoungame.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy;
import com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy2;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

import java.util.ArrayList;
import java.util.List;

public class WorldMap {


    private GameScreenInterface gameScreen ;
    private Array<FlyingEnemy> pterodactyl ;
    private Array<FlyingEnemy2> flyingDino ;
    private MeatEnd meatEnd ;


    
    public WorldMap(GameScreenInterface gameScreen){

        this.gameScreen = gameScreen ;

        World world = gameScreen.getWorld() ;

        pterodactyl = new Array<FlyingEnemy>();
        
        createMap(world) ;



    }

    private void createMap(World world) {

        BodyDef bodyDef = new BodyDef() ;
        PolygonShape shape = new PolygonShape() ;
        FixtureDef fixtureDef = new FixtureDef() ;
        Body body ;

        // ground
        Rectangle bounds;
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            bounds = ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody ;

            bodyDef.position.set(
                    (bounds.getX() + bounds.getWidth() /2f )  / GameInfo.PPM,
                    (bounds.getY() + bounds.getHeight() /2f) / GameInfo.PPM
            );

            body = gameScreen.getWorld().createBody(bodyDef) ;

            shape.setAsBox(bounds.getWidth()/2f / GameInfo.PPM , bounds.getHeight() /2f / GameInfo.PPM);

            fixtureDef.shape = shape ;

            fixtureDef.filter.categoryBits = GameInfo.GROUND ;
            fixtureDef.filter.maskBits = GameInfo.DINO | GameInfo.DINO_LIFE |GameInfo.GROUND_ENEMY | GameInfo.ENEMY_BOSS ;

             body.createFixture(fixtureDef);

        }

        // blocks
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            bounds = ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody ;

            bodyDef.position.set(
                    (bounds.getX() + bounds.getWidth() /2f )  / GameInfo.PPM,
                    (bounds.getY() + bounds.getHeight() /2f) / GameInfo.PPM
            );

            body = gameScreen.getWorld().createBody(bodyDef) ;

            shape.setAsBox(bounds.getWidth()/2f / GameInfo.PPM , bounds.getHeight() /2f / GameInfo.PPM);

            fixtureDef.shape = shape ;

            body.createFixture(fixtureDef);

        }

        // fragileBricks
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            bounds = ((RectangleMapObject)object).getRectangle();

            new FragileBricks(gameScreen, object);

        }

        // flyingEnemies
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            bounds = ((RectangleMapObject)object).getRectangle();


                pterodactyl.add(new FlyingEnemy(gameScreen, bounds.getX() / GameInfo.PPM, bounds.getY()/GameInfo.PPM ));


        }

        // water
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            bounds = ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody ;

            bodyDef.position.set(
                    (bounds.getX() + bounds.getWidth() /2f )  / GameInfo.PPM,
                    (bounds.getY() + bounds.getHeight() /2f - 50f) / GameInfo.PPM
            );

            body = gameScreen.getWorld().createBody(bodyDef) ;

            shape.setAsBox(bounds.getWidth()/2f / GameInfo.PPM , bounds.getHeight() /2f / GameInfo.PPM);

            fixtureDef.shape = shape ;
            //fixtureDef.isSensor = true ;
            fixtureDef.filter.categoryBits = GameInfo.WATER_BUMPER;


            body.createFixture(fixtureDef);




        }

        //meat_end
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            bounds = ((RectangleMapObject)object).getRectangle();
             meatEnd = new MeatEnd(gameScreen, bounds.getX() / GameInfo.PPM, bounds.getY() / GameInfo.PPM );

        }

        }


    public Array<FlyingEnemy> getPterodactyl() {

        return pterodactyl;
    }
    public Array<FlyingEnemy2> getFlyingDino() {
        return flyingDino;
    }

    public List<Texture> getPterodactylTextures (){
        List<Texture>textures = new ArrayList<Texture>();

        for (int i = 0; i <pterodactyl.size ; i++) {
            textures.add(pterodactyl.get(i).getTexture());
        }

        return textures ;
    }


    public void disposePterodactylTextures(){

        for ( Texture t: getPterodactylTextures()) {
            t.dispose();
        }

        getMeatEnd().getTexture().dispose();

        pterodactyl = null ;
    }



    public MeatEnd getMeatEnd() {
        return meatEnd;
    }


    public FlyingEnemy2 getFlyingDino(int pos){
        return  flyingDino.get(pos);
    }


}
