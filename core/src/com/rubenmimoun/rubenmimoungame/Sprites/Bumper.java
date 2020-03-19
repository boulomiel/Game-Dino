package com.rubenmimoun.rubenmimoungame.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public class Bumper extends Sprite {

    public enum State{ NORMAL, BUMPED}
    private State currentState;
    private State previousState ;
    private World world ;
    private GameScreenInterface screen ;
    private TextureAtlas bumpatlas ;
    private Animation bumpAnim ;
    private boolean bumped ;
    private Body body;
    private TextureRegion basicBumper ;
    private float stateTime ;



    public Bumper(GameScreenInterface screen , float x , float y){
        this.screen = screen ;
        this.world =  screen.getWorld() ;
        bumpatlas = new TextureAtlas("object/bumper.atlas");

        bumpAnim = new Animation(0.1f,bumpatlas.getRegions());

        basicBumper = new TextureRegion() ;
        basicBumper = bumpatlas.findRegion("bump");

        bumped = false ;
        //setBounds(getX(),getY(),64,25);
        setPosition(x, y);
        createBody();

        previousState = currentState = State.NORMAL ;
    }


    public void  update(float delta){

        setRegion(getFrame(delta));
        fixPosition(delta);


    }

    private void fixPosition(float delta){

        setPosition((body.getPosition().x - getWidth()/2f - 0.3f)* com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,
                (body.getPosition().y - getHeight()/2f)* com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM
        );

    }

    public TextureRegion getFrame(float delta) {

        TextureRegion currentRegion ;
        currentState = getState() ;

        switch (currentState){
            case BUMPED:
                  currentRegion = (TextureRegion) bumpAnim.getKeyFrame(stateTime,false);
                  if(!bumpAnim.isAnimationFinished(stateTime)){
                      setBumped(true);
                  }else {
                      setBumped(false);
                  }

                break ;
           case  NORMAL :
               default:
             currentRegion = basicBumper ;
             break ;

        }

        stateTime =  ( currentState ==  previousState ?  stateTime + delta  : 0  );

        // if not the previous state is equals to the current one
        previousState = currentState ;


        return  currentRegion ;

    }

    private State getState(){

        if( isBumped()){
            return  currentState =  State.BUMPED ;
        }else if( !isBumped()){
          return   currentState = State.NORMAL ;
        }

        return currentState ;

    }

    private void createBody() {

        BodyDef bodyDef = new BodyDef() ;
        bodyDef.type = BodyDef.BodyType.StaticBody ;
        bodyDef.position.set(getX(),getY()) ;
        body = world.createBody(bodyDef);

        CircleShape shape =  new CircleShape() ;
        shape.setRadius(22/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

        FixtureDef fixtureDef =  new FixtureDef() ;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.WATER_BUMPER ;
        fixtureDef.filter.maskBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.GROUND | com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINOFEET | GameInfo.DINO;
        body.createFixture(fixtureDef).setUserData(this);



        shape.dispose();

    }

    public boolean isBumped() {
        return bumped;
    }

    public void setBumped(boolean bumped) {
        this.bumped = bumped;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
