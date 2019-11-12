package com.rubenmimoun.rubenmimoungame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;


public class Dino  extends Sprite {




    private Body body ;
    private GameScreenInterface gameScreen ;
    private World world ;

    public enum  State { STANDING , WALKING, JUMPING, DEAD}
    private State currentState ;
    private State previousState ;

    private TextureRegion standingRegion ;
    private TextureAtlas walkAtlas ;
    private TextureAtlas jumpAtlas ;
    private TextureAtlas deadAtlas ;

    private Animation animationWalk ;
    private Animation animationJump ;
    private Animation animationDead ;
    private TextureRegion deadDinoRegion ;
    private Array<TextureRegion> frames;


   private float stateTimer ;

    private boolean isDead ;
    private boolean isRunninRight ;

    private boolean hasDrown ;
    private boolean beenTouched ;



    public Dino(GameScreenInterface gameScreen, float x, float y){

        super(new Texture("dinoTilesPack/Idle.png"));

        // initializing
        this.gameScreen = gameScreen ;
        this.world = gameScreen.getWorld();


        frames = new Array<TextureRegion>();

        standingRegion = new TextureRegion(gameScreen.getDinoAtlas().findRegion("Idle"), 0 , 0 , 100,69);
        isRunninRight = true ;
        isDead = false ;
        stateTimer = 0 ;

        // atlas
        walkAtlas = new TextureAtlas("dinoTilesPack/walkDino.atlas");
        deadAtlas = new TextureAtlas("dinoTilesPack/deadDino.atlas");
        jumpAtlas = new TextureAtlas("dinoTilesPack/jumpDino.atlas");

        deadDinoRegion = new TextureRegion(deadAtlas.findRegion("Dead 8"),700,0,100,69);

        // animation
        animationWalk = new Animation(0.1f,walkAtlas.getRegions());
        animationJump = new Animation(0.08f, jumpAtlas.getRegions());
        animationDead = new Animation(0.1f,deadAtlas.getRegions());





        currentState = State.STANDING ;
        previousState = State.STANDING ;

        setPosition(x, y);

        createBody();

        setBounds(x,y,100 / GameInfo.PPM,69 / GameInfo.PPM );



    }


    public  void update(float delta){

            correctB2dPosition();
            //setPosition( body.getPosition().x  - getWidth()/2f + 0.2f + getWidth()    / GameInfo.PPM, body.getPosition().y - getHeight() /2f  -getHeight()/2f /GameInfo.PPM);
            setRegion(getFrame(delta));


    }



    private void correctB2dPosition(){

        if( isRunninRight){

            setPosition( body.getPosition().x  - getWidth()/2f + 0.2f + getWidth()    / GameInfo.PPM,
                    body.getPosition().y - getHeight() /2f  -0.4f /GameInfo.PPM);

        }else {
            setPosition( body.getPosition().x  - getWidth()/2f - 0.2f + getWidth()    / GameInfo.PPM,
                    body.getPosition().y - getHeight()/2f - 0.5f  /GameInfo.PPM);

        }

    }



    private TextureRegion getFrame(float delta){

        currentState = getState() ;

        TextureRegion currentFrame ;


        switch (currentState){
            case WALKING:

                 currentFrame = (TextureRegion) animationWalk.getKeyFrame(stateTimer,true);
                 break;
            case JUMPING:

                currentFrame = (TextureRegion)animationJump.getKeyFrame(stateTimer, false);
                break;

            case DEAD :
                currentFrame =(TextureRegion)animationDead.getKeyFrame(stateTimer,false);

                break ;

            case STANDING:
                default:
                    currentFrame =standingRegion;
                    break ;
        }

        // if his body x position is decrementing or he is not going right and the regions is
        // not facing the left side
        if( (body.getLinearVelocity().x < 0  || !isRunninRight )   && !currentFrame.isFlipX()){
            // then flip the region on the x axis
            currentFrame.flip(true, false);
            // and he is not going right anymore
            isRunninRight = false ;

            // if he s going right and the region is facing left
        }else if( (body.getLinearVelocity().x > 0 || isRunninRight ) && currentFrame.isFlipX()){
            currentFrame.flip(true, false);
            isRunninRight = true ;

        }

        // check if the player is in another state
        stateTimer =  ( currentState ==  previousState ?  stateTimer + delta : 0  );

        // if not the previous state is equals to the current one
        previousState = currentState ;

        return  currentFrame ;



    }


    private  State getState(){

        if( hasDrown || beenTouched ){

            return State.DEAD ;

        } else {

            if(body.getLinearVelocity().x != 0 && body.getLinearVelocity().y <= 0){
                return  State.WALKING ;
            }
            else if(body.getLinearVelocity().y > 0  ){
                return State.JUMPING ;
            }else {
                return State.STANDING ;
            }

        }

    }

    public  void bumperJump(){
        if(body.getLinearVelocity().y <= 0 ){
            body.setLinearVelocity(0,6.0f);


        }


    }





    private void createBody() {

        BodyDef bodyDef = new BodyDef() ;
        bodyDef.type = BodyDef.BodyType.DynamicBody ;
        bodyDef.position.set( getX() , getY() ) ;
        body = world.createBody(bodyDef) ;
        body.setFixedRotation(true);
        FixtureDef fixtureDef  = new FixtureDef() ;

        // dinos body
        PolygonShape shape = new PolygonShape() ;
        shape.setAsBox((getWidth() /2 -35f ) / GameInfo.PPM , (getHeight() /2f -8f) / GameInfo.PPM);
        fixtureDef.shape = shape  ;
        fixtureDef.friction = 2 ;

        fixtureDef.filter.categoryBits = GameInfo.DINO ;
        fixtureDef.filter.maskBits = GameInfo.GROUND | GameInfo.BRICK | GameInfo.FLYING_ENEMY
                | GameInfo.ENEMY_HEAD  | GameInfo.WATER_BUMPER | GameInfo.DINO_LIFE | GameInfo.END_MEAT_1
                |GameInfo.GROUND_ENEMY| GameInfo.FINALWALLS| GameInfo.ENEMY_BOSS ;

      body.createFixture(fixtureDef).setUserData(this);

        // dinos head sensor
        // dinos feet sensor
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-10f / GameInfo.PPM, (getHeight() / 2f - 6f )/ GameInfo.PPM ),
                new Vector2(10f / GameInfo.PPM, (getHeight() / 2f   -6f)/ GameInfo.PPM));
        fixtureDef.shape = head ;
       // fixtureDef.isSensor = true ;
        fixtureDef.filter.categoryBits = GameInfo.DINOHEAD ;


        body.createFixture(fixtureDef).setUserData(this);

        // dinos feet sensor
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-20f / GameInfo.PPM, (-getHeight() / 2f + 5 )/ GameInfo.PPM ),
                new Vector2(20f / GameInfo.PPM, (-getHeight() / 2f   + 5)/ GameInfo.PPM));

        fixtureDef.shape = feet ;
        fixtureDef.isSensor= true ;
        fixtureDef.filter.categoryBits = GameInfo.DINOFEET ;
        body.createFixture(fixtureDef).setUserData(this);


        shape.dispose();

    }


    public void hurtdino(){

        isDead = true ;
        beenTouched = true ;
        gameScreen.rebootScreen();

    }

    public  void dinoFell(){
        isDead = true ;
        hasDrown = true ;
        gameScreen.rebootScreen();
    }

    public String getDeath(){

        if( isDead){
            if(hasDrown){
                return "drown" ;
            }else{
                return "touched" ;
            }
        }
        return "alive" ;
    }

    public void touchedByBoss(){
        if( isRunninRight){
            body.applyLinearImpulse(new Vector2(-8f,0),getBody().getLocalCenter(),true);
        }else {
            body.applyLinearImpulse(new Vector2(8f,0),getBody().getLocalCenter(),true);
        }
    }

    public void hitBossHead(){
        if( isRunninRight){
            body.applyLinearImpulse(new Vector2(2f,2),getBody().getLocalCenter(),true);
        }else {
            body.applyLinearImpulse(new Vector2(-2f,2),getBody().getLocalCenter(),true);
        }
    }




    public Body getBody() {
        return body;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public boolean setDead(boolean dead){
        return dead ;
    }

    public boolean isRunninRight() {
        return isRunninRight;
    }

    public void setRunninRight(boolean runninRight) {
        isRunninRight = runninRight;
    }

    public State getCurrentState() {
        return currentState;
    }



    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
}
