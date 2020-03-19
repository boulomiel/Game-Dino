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
import com.rubenmimoun.rubenmimoungame.Sprites.Enemy;
import com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy;
import com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy2;
import com.rubenmimoun.rubenmimoungame.Sprites.GroundEnemy;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

import java.util.ArrayList;
import java.util.List;

public class WorldMap2 {

    private com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface gameScreen ;
    private Rectangle bounds ;
    private Array<com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy> pterodactyl ;
    private Array<com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy2> flyingDino ;
    private Array<GroundEnemy> groundEnemies ;

    private Array<com.rubenmimoun.rubenmimoungame.Sprites.Enemy>allEnemies =  new Array<com.rubenmimoun.rubenmimoungame.Sprites.Enemy>();
    private MeatEnd meatEnd ;



    public WorldMap2(GameScreenInterface gameScreen){

        this.gameScreen = gameScreen ;

        World world = gameScreen.getWorld() ;



         pterodactyl = new Array<com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy>();
        flyingDino = new Array<com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy2>();
     groundEnemies = new Array<GroundEnemy>();

        createMap(world) ;
    }

    private void createMap(World world) {

        BodyDef bodyDef = new BodyDef() ;
        PolygonShape shape = new PolygonShape() ;
        FixtureDef fixtureDef = new FixtureDef() ;
        Body body ;

        // ground
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            bounds= ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody ;

            bodyDef.position.set(
                    (bounds.getX() + bounds.getWidth() /2f )  / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,
                    (bounds.getY() + bounds.getHeight() /2f) / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM
            );

            body = gameScreen.getWorld().createBody(bodyDef) ;

            shape.setAsBox(bounds.getWidth()/2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM , bounds.getHeight() /2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

            fixtureDef.shape = shape ;

            fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.GROUND ;
            fixtureDef.filter.maskBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINO | com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINO_LIFE | com.rubenmimoun.rubenmimoungame.helper.GameInfo.GROUND_ENEMY ;

            body.createFixture(fixtureDef);

        }

        // blocks
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            bounds= ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody ;

            bodyDef.position.set(
                    (bounds.getX() + bounds.getWidth() /2f )  / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,
                    (bounds.getY() + bounds.getHeight() /2f) / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM
            );

            body = gameScreen.getWorld().createBody(bodyDef) ;

            shape.setAsBox(bounds.getWidth()/2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM , bounds.getHeight() /2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

            fixtureDef.shape = shape ;

            body.createFixture(fixtureDef);

        }

        // fragileBricks
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            bounds= ((RectangleMapObject)object).getRectangle();

            new FragileBricks(gameScreen, object);

        }

        // flyingEnemies
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            bounds= ((RectangleMapObject)object).getRectangle();

                flyingDino.add(new FlyingEnemy2(gameScreen, bounds.getX() / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, bounds.getY()/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM));


        }

        // water
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            bounds= ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody ;

            bodyDef.position.set(
                    (bounds.getX() + bounds.getWidth() /2f )  / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,
                    (bounds.getY() + bounds.getHeight() /2f - 50f) / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM
            );

            body = gameScreen.getWorld().createBody(bodyDef) ;

            shape.setAsBox(bounds.getWidth()/2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM , bounds.getHeight() /2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

            fixtureDef.shape = shape ;
            //fixtureDef.isSensor = true ;
            fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.WATER_BUMPER;


            body.createFixture(fixtureDef);




        }

        //meat_end
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            bounds= ((RectangleMapObject)object).getRectangle();
            meatEnd = new MeatEnd(gameScreen, bounds.getX() / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, bounds.getY() / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM );

        }

        // dinoFoes
        for (MapObject object : gameScreen.getTiledMap().getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            bounds= ((RectangleMapObject)object).getRectangle();
            if( com.rubenmimoun.rubenmimoungame.helper.GameInfo.WORLD2_PLAYING)
                groundEnemies.add(new GroundEnemy(gameScreen, bounds.getX() / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, bounds.getY()/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM));

        }

        //dinoWalls

        for (MapObject object : gameScreen.getTiledMap().getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            bounds= ((RectangleMapObject)object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody ;

            bodyDef.position.set(
                    (bounds.getX() + bounds.getWidth() /2f )  / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,
                    (bounds.getY() + bounds.getHeight() /2f) / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM
            );

            body = gameScreen.getWorld().createBody(bodyDef) ;

            shape.setAsBox(bounds.getWidth()/2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM , bounds.getHeight() /2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

            fixtureDef.shape = shape ;

            fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.ENEMYWALLS;
            fixtureDef.filter.maskBits = GameInfo.GROUND_ENEMY;

            body.createFixture(fixtureDef);




        }
    }

    public  void disposeAll(){

    }



    public Array<com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy2> getFlyingDino() {
        return flyingDino;
    }

    public Array<GroundEnemy> getGroundEnemies() {
        return groundEnemies;
    }

    public GroundEnemy getGroundEnemy(int pos) {
        return groundEnemies.get(pos);
    }

    public Array<Enemy>getAllEnemies(){
        allEnemies.addAll(flyingDino);
        allEnemies.addAll(groundEnemies);
        return  allEnemies ;
    }

    public void disposeAllEnemiesTextures(){

        for ( Texture t: getAllEnemiesTexture()) {
            t.dispose();
        }
    }

    public List<Texture> getAllEnemiesTexture (){

        List<Texture>textures = new ArrayList<Texture>();

        for (int i = 0; i <flyingDino.size ; i++) {
            textures.add(flyingDino.get(i).getTexture());
        }
        return textures ;
    }

    public MeatEnd getMeatEnd() {
        return meatEnd;
    }

    public FlyingEnemy getFlyingEnemy(int pos){
        return  pterodactyl.get(pos);
    }

    public FlyingEnemy2 getFlyingDino(int pos){
        return  flyingDino.get(pos);
    }


}

