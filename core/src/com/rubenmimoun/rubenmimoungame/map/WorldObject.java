package com.rubenmimoun.rubenmimoungame.map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rubenmimoun.rubenmimoungame.Sprites.Dino;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public abstract class WorldObject extends Sprite {
    protected World world ;
    protected Rectangle bounds ;
    protected TiledMap map  ;
    protected com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface screen ;
    protected MapObject object ;

    Body body ;
    Fixture fixture ;

    public  WorldObject(GameScreenInterface screen , MapObject object){
        this.object =object ;
        this.world = screen.getWorld() ;
        this.screen =screen ;
        this.bounds = ((RectangleMapObject)object).getRectangle();

        BodyDef bodyDef = new BodyDef() ;
        FixtureDef fixtureDef = new FixtureDef() ;
        PolygonShape shape = new PolygonShape() ;
        map = screen.getTiledMap();

        bodyDef.type =  BodyDef.BodyType.StaticBody ;

        bodyDef.position.set(
                (bounds.getX()  + bounds.getWidth() /2f) / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,
                (bounds.getY() + bounds.getHeight()/2f) / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);



        body = world.createBody(bodyDef) ;



        shape.setAsBox(
                bounds.getWidth()/2f  / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM ,
                bounds.getHeight()/2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

        fixtureDef.shape = shape ;
        fixture =   body.createFixture(fixtureDef);

    }



    public  void onHeadHit(Dino Dino){

    }

    public void setCategoryFilter(Short filterBit){

        Filter filter = new Filter();

        filter.categoryBits = filterBit ;

        fixture.setFilterData(filter);

    }

    public void destroy(){}

    public TiledMapTileLayer.Cell getCell(){

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);

        return  layer.getCell(  (int)(body.getPosition().x * com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM  / 32),
                (int) (body.getPosition().y * GameInfo.PPM  / 32 ) ) ;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void dispose(){
        getTexture().dispose();
    }
}
