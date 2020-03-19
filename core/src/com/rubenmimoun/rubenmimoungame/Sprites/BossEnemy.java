package com.rubenmimoun.rubenmimoungame.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public class BossEnemy extends Enemy {


    public enum State { ALIVE, HIT, DEAD}
    private boolean dead ;

    private TextureAtlas bossAlive;
    private TextureAtlas bossHit ;
    private TextureAtlas bossHitAndDead ;

    private Animation dying ;
    private Animation hit ;
    private Animation walking ;

    private State currentState ;
    private State previousState ;

    private Vector2 walkingMovement ;
    private Vector2 jumpingMovement ;

    private boolean walkingRight ;
    private float stateTime ;

    private boolean touched = false ;
    private boolean runHitAnim = false ;




    public BossEnemy(GameScreenInterface gameScreen, float x, float y) {
        super(gameScreen, x, y);
        this.world = gameScreen.getWorld() ;
        this.gameScreen = gameScreen ;

        bossAlive = new TextureAtlas("dinoTilesPack/enemies/bossAlive.atlas");
        bossHit = new TextureAtlas("dinoTilesPack/enemies/bosshit.atlas");
        bossHitAndDead = new TextureAtlas("dinoTilesPack/enemies/bossdying.atlas");

        dying = new Animation(0.1f, bossHitAndDead.getRegions()) ;
        hit = new Animation(0.4f, bossHit.getRegions());
        walking = new Animation(0.1f, bossAlive.getRegions());


       currentState = State.ALIVE ;
       previousState = State.ALIVE ;

       dead = false ;

       walkingRight = true ;

       walkingMovement = new Vector2(-1f,0);
       jumpingMovement = new Vector2(0,1);

        // we set bounds to set how large it is on the screen and scaled
        setBounds(0,0,60 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,80 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM );

    }

    @Override
    public void update(Float delta) {

        fixPosition(delta);
        setRegion(getFrame(delta));
        setMovement(true, false);


    }


    private TextureRegion getFrame(float delta){

        currentState = getState() ;

        TextureRegion currentRegion;

        switch (currentState){

            case HIT:
                 currentRegion = (TextureRegion) hit.getKeyFrame(stateTime,false);
                 if (hit.isAnimationFinished(stateTime)){
                     runHitAnim = false;
                 }
                 break ;
            case DEAD:
                 currentRegion =(TextureRegion)dying.getKeyFrame(stateTime,false);
                 break ;

                 case ALIVE:
                    default:
                currentRegion = (TextureRegion) walking.getKeyFrame(stateTime, true);
                break;

        }


        if( (body.getLinearVelocity().x < 0  || !walkingRight )   && currentRegion.isFlipX()){

            currentRegion.flip(true, false);

            walkingRight = false ;


        }else if( (body.getLinearVelocity().x > 0 || walkingRight ) && !currentRegion.isFlipX()){
            currentRegion.flip(true, false);
            walkingRight = true ;

        }

        stateTime =  ( currentState ==  previousState ?  stateTime + delta  : 0  );

        // if not the previous state is equals to the current one
        previousState = currentState ;


        return currentRegion;



    }

    private void setMovement(boolean walk ,  boolean jump){
        if(walk){
            body.setLinearVelocity(walkingMovement);
        }
        if(jump){
            body.setLinearVelocity(jumpingMovement);
        }

    }


    @Override
    public void reverseVelocity(boolean x, boolean y) {
        if(x){
            walkingMovement.x = - walkingMovement.x ;
        }
    }

    public void raiseSpeed(){
        walkingMovement.x = walkingMovement.x * 1.5f ;
    }



    private State getState(){



        if(!runHitAnim){
            currentState= State.ALIVE ;
        }else if( runHitAnim){
            currentState = State.HIT ;
            if(!hit.isAnimationFinished(stateTime)){
                currentState = State.HIT ;

                }

        }else if(runHitAnim && isDead()){
            currentState = State.DEAD ;
        }

        return currentState ;

    }


    private void fixPosition(float delta){

        if(!walkingRight){
            setPosition((body.getPosition().x - 0.7f) * com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM ,
                    (body.getPosition().y - 0.8f) * com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);
        }else{
            setPosition((body.getPosition().x - 1.7f) * com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM ,
                    (body.getPosition().y - 0.8f) * com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);
        }


    }

    @Override
    public void crushedByDinosStep(Dino dino) {
        super.crushedByDinosStep(dino);
        runHitAnim = true ;


    }

    @Override
    protected void createBody() {

        BodyDef bodyDef = new BodyDef() ;
        bodyDef.position.set(getX()  , getY() );
        bodyDef.type = BodyDef.BodyType.DynamicBody ;
        body =  world.createBody(bodyDef) ;


        CircleShape shape =  new CircleShape() ;
       shape.setRadius(80/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef() ;
        fixtureDef.shape =  shape ;
        fixtureDef.restitution = 1f ;
        fixtureDef.filter.categoryBits =  com.rubenmimoun.rubenmimoungame.helper.GameInfo.ENEMY_BOSS;
        fixtureDef.filter.maskBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.GROUND | com.rubenmimoun.rubenmimoungame.helper.GameInfo.FINALWALLS | com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINO | com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINOFEET;
         body.createFixture(fixtureDef).setUserData(this);

         // head
        CircleShape head =  new CircleShape();

        head.setRadius(40/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);
        head.setPosition(new Vector2(0f/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, 70f/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM));
        fixtureDef.shape =head ;
        fixtureDef.restitution= 1f;
        fixtureDef.filter.categoryBits = GameInfo.ENEMY_HEAD ;
        body.createFixture(fixtureDef).setUserData(this);



        shape.dispose();
        head.dispose();


    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public State getCurrentState() {
        return currentState;
    }

    public Animation getDying() {
        return dying;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
