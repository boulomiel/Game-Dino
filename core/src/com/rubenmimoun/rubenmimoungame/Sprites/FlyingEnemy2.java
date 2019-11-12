package com.rubenmimoun.rubenmimoungame.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public class FlyingEnemy2 extends Enemy {

    // state
    private enum State { ALIVE , DEAD}
    private State currentState ;
    private State previousState ;

    //private Body body ;
    private TextureAtlas atlas ;
    private TextureAtlas dieAtlas ;
    private Animation fly ;
    private Animation die ;
    // states boolean
    private boolean flyingRight ;
    private boolean destroyed ;
    boolean setToDestroy;

    float stateTime = 0 ;
    private Array<TextureRegion> frames = new Array<TextureRegion>();
    int position   = 0  ;
    int previousPosition = 0  ;

    public int height =  0 ;
    private int maxHeight = 0 ;

    private  TextureRegion currentFrame ;


    public FlyingEnemy2(GameScreenInterface gameScreen, float x, float y) {
        super(gameScreen, x, y);




        atlas = new TextureAtlas("dinoTilesPack/enemies/flyDino.atlas");
        dieAtlas = new TextureAtlas("dinoTilesPack/enemies/flyDinoDie.atlas");

        currentFrame = atlas.findRegion("flyDinoA") ;
      //  setTexture(currentFrame.getTexture());


        fly = new Animation(0.1f, atlas.getRegions()) ;
        die = new Animation(0.1f, dieAtlas.getRegions());

        setBounds(getX()  , getY() , 64/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM , 35 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

        flyingRight = true ;
        destroyed = false;

        currentState = State.ALIVE ;
        previousState = State.ALIVE ;

        maxHeight = (int)y;
    }

    @Override
    protected void createBody() {

        BodyDef bodyDef = new BodyDef() ;
        bodyDef.type = BodyDef.BodyType.DynamicBody ;
        bodyDef.position.set( getX() , getY() ) ;
        body = world.createBody(bodyDef) ;

        CircleShape shape = new CircleShape() ;
        shape.setRadius( 15f/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);
        FixtureDef fixtureDef  = new FixtureDef() ;

        fixtureDef.shape = shape  ;

        fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.FLYING_ENEMY;
        fixtureDef.filter.maskBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINO | com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINOFEET| com.rubenmimoun.rubenmimoungame.helper.GameInfo.DINOHEAD ;

        body.createFixture(fixtureDef).setUserData(this); ;

        // Create head :
        PolygonShape head = new PolygonShape() ;

        Vector2[] vertice =  new Vector2[4];
        // we create a trapeze each vertice is a coordinate set for a point
        vertice[0] = new Vector2(-17,21).scl( 1 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM) ;
        vertice[1] = new Vector2( 17,21).scl( 1 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM) ;
        vertice[2] = new Vector2(-3,15).scl( 1 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM) ;
        vertice[3] = new Vector2( 3,15).scl( 1 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM) ;
        // giving the attributes array to the shape
        head.set(vertice);

        fixtureDef.shape = head ;
        // bounce : if you just 10 px, you bounce 5 from the contact with the head
        fixtureDef.restitution = 0.5f ;

        fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.ENEMY_HEAD ;

        // we set user data so we can access it through the contact listener
        body.createFixture(fixtureDef).setUserData(this);


    }

    @Override
    public void update(Float delta) {

        stateTime += delta ;

        if( setToDestroy && !destroyed){

            // get rids of the b2d body
            world.destroyBody(body);


            setBounds(getX(), getY(), 64  / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, 4 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

            destroyed = true ;

            stateTime = 0 ;


        }else if( !destroyed)


        body.setLinearVelocity(forth);
        setRegion(getFrame(delta));

        setPosition(body.getPosition().x  - getWidth() /2f ,
                body.getPosition().y - getHeight() /2f);


    }

    public Vector2 reversedirection(){

        movement.x = - movement.x ;
        return  movement ;
    }

    public   Vector2 reverseHeight(){

        if( maxHeight >= height + 2 ){
            movement.y = 2 ;
        }else if( maxHeight < height   ){
            movement.y = 0;
        }



        return  movement ;

    }

    private State getState(){

        if(!destroyed){
            return State.ALIVE ;
        }else{
            return State.DEAD ;
        }

    }




    private TextureRegion getFrame(float delta){

        position =  previousPosition ++;
        height = (int)getY() ;

        currentState = getState();



        switch (currentState){
            case ALIVE:
              currentFrame = (TextureRegion) fly.getKeyFrame(stateTime,true);
              break;
            case DEAD:
                currentFrame = (TextureRegion) die.getKeyFrame(stateTime,false);
                break ;
        }


// not facing the left side
        if( (body.getLinearVelocity().x < 0  )   && !currentFrame.isFlipX()){
            // then flip the region on the x axis
            currentFrame.flip(true, false);
            // and he is not going right nanymore
            previousPosition =  (int)body.getPosition().x;


            // if he s going right and the region is facing left
        }else if( (body.getLinearVelocity().x > 0  ) && currentFrame.isFlipX()){
            currentFrame.flip(true, false);

            previousPosition =  (int)body.getPosition().x;

        }


        if( position % 150 ==  0  ){
            reversedirection() ;
        }

        reverseHeight();



        //is our current State  equals the previous one ? if it does , stateTime + deltatime, if not reset it
        // because the player is in another state
        stateTime =  (currentState ==  previousState ?  stateTime + delta : 0  );

        // if not the previous state is equals to the current one
        previousState = currentState ;


        return    currentFrame ;

    }


    @Override
    public void crushedByDinosStep(Dino dino) {
        currentState = State.DEAD ;
        body.setLinearVelocity(new Vector2(0,-1));
        setToDestroy = true;
        body.setUserData(GameInfo.DESTROYED);
        FileManager.getInstanceOf().getSound("fly_enemyhit.wav").play();

    }



    @Override
    public void draw(Batch batch) {

        if( !destroyed)
            super.draw(batch);
    }

    @Override
    public Body getBody() {
        return super.getBody();
    }
}
