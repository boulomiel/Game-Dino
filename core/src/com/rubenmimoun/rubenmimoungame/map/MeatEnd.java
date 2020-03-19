package com.rubenmimoun.rubenmimoungame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public class MeatEnd extends Sprite {

    private com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface gameScreen ;
    private Animation animation_grill ;
    private TextureAtlas atlas ;
    private TextureRegion currentregion;
    private Body body ;
    private World world ;
    private float stateTime  ;


    public MeatEnd(GameScreenInterface gameScreen , float x , float y){

        stateTime = 0;


        this.gameScreen = gameScreen;
        atlas = new TextureAtlas(Gdx.files.internal("meat_end.atlas"));

        currentregion = atlas.findRegion("meat");
        //setTexture(currentregion.getTexture());


        world = gameScreen.getWorld() ;

        setPosition(x,y);

        setBounds(getX(),getY(), 83f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, 55f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

        createBody();

        animation_grill = new Animation( 0.1f, atlas.getRegions()) ;

    }


    public void update(Float delta) {

        stateTime += delta ;

        setRegion(getFrame(delta));
        setPosition(body.getPosition().x  - getWidth() /2f ,
                body.getPosition().y - getHeight()/2f);



    }
    public TextureRegion getFrame(float delta){

            return currentregion =(TextureRegion)animation_grill.getKeyFrame(stateTime,true);

        }




    private void createBody() {

        BodyDef bodyDef =  new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody ;
        bodyDef.position.set(getX() + getWidth() /2f , getY() +getHeight() /2f) ;
        body = world.createBody(bodyDef) ;

        PolygonShape shape = new PolygonShape() ;
        shape.setAsBox(getWidth() - getWidth() /2  , getHeight() -getHeight()/2 );

        FixtureDef fixtureDef = new FixtureDef() ;
        fixtureDef.shape = shape ;
        fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.END_MEAT_1;
        fixtureDef.filter.maskBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINO | com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINOHEAD | com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINOFEET | GameInfo.GROUND;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public Body getBody() {
        return body;
    }

        @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
