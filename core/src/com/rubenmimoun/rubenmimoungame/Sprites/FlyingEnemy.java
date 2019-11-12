package com.rubenmimoun.rubenmimoungame.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;


public class FlyingEnemy extends Enemy {

    //private Body body ;
    private TextureAtlas atlas ;
    private TextureRegion currentFrame  ;
    private Animation fly ;
    // states boolean
    private boolean flyingRight ;
    private boolean destroyed ;
    boolean setToDestroy;

    private float stateTime = 0 ;
    private int position   = 0  ;
    private int previousPosition = 0  ;

    private int height =  0 ;





    public FlyingEnemy(GameScreenInterface gameScreen, float x , float y) {

        super(gameScreen,x ,y);

        currentFrame = new TextureRegion();


        atlas = new TextureAtlas("dinoTilesPack/enemies/fylPack.atlas");
        currentFrame = atlas.findRegion("fly1_");
       // setTexture(currentFrame.getTexture());

        fly = new Animation(0.1f, atlas.getRegions()) ;

        setBounds(getX()  , getY() , 64/ GameInfo.PPM , 35 /GameInfo.PPM);

        flyingRight = true ;
        destroyed = false;

    }

    @Override
    protected void createBody() {

        BodyDef bodyDef = new BodyDef() ;
        bodyDef.type = BodyDef.BodyType.DynamicBody ;
        bodyDef.position.set( getX() , getY() ) ;
        body = world.createBody(bodyDef) ;

        CircleShape shape = new CircleShape() ;
        shape.setRadius( 15f/GameInfo.PPM);
        FixtureDef fixtureDef  = new FixtureDef() ;

        fixtureDef.shape = shape  ;

        fixtureDef.filter.categoryBits = GameInfo.FLYING_ENEMY;
        fixtureDef.filter.maskBits = GameInfo.DINO | GameInfo.DINOFEET| GameInfo.DINOHEAD ;

        body.createFixture(fixtureDef).setUserData(this); ;

        // Create head :
        PolygonShape head = new PolygonShape() ;

        Vector2[] vertice =  new Vector2[4];
        // we create a trapeze each vertice is a coordinate set for a point
        vertice[0] = new Vector2(-17,21).scl( 1 / GameInfo.PPM) ;
        vertice[1] = new Vector2( 17,21).scl( 1 / GameInfo.PPM) ;
        vertice[2] = new Vector2(-3,15).scl( 1 / GameInfo.PPM) ;
        vertice[3] = new Vector2( 3,15).scl( 1 / GameInfo.PPM) ;
        // giving the attributes array to the shape
        head.set(vertice);

        fixtureDef.shape = head ;
        // bounce : if you just 10 px, you bounce 5 from the contact with the head
        fixtureDef.restitution = 0.5f ;

        fixtureDef.filter.categoryBits = GameInfo.ENEMY_HEAD ;

        // we set user data so we can access it through the contact listener
        body.createFixture(fixtureDef).setUserData(this);


    }

    @Override
    public void update(Float delta) {

        stateTime += delta ;

        if( setToDestroy && !destroyed){

            // get rids of the b2d body
            world.destroyBody(body)
            ;
            // setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 0, 0,16,16));
            // crashes th goomba by shrinking the bounds
            setBounds(getX(), getY(), 64  / GameInfo.PPM, 4 / GameInfo.PPM);

            destroyed = true ;

            stateTime = 0 ;


        }else if( !destroyed)

        if( position % 150 ==  0  ){
            reversedirection() ;
        }

        reverseHeight() ;

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

        if( height == 0){
            movement.y = 2 ;
        }else if( height == 3  ){
            movement.y = 0;
        }

        return  movement ;
    }




    private TextureRegion getFrame(float delta){

        position =  previousPosition ++;
        height = (int) body.getPosition().y ;



        // not facing the left side
        if( (body.getLinearVelocity().x < 0  || !flyingRight )   && !currentFrame.isFlipX()){
            // then flip the region on the x axis
            currentFrame.flip(true, false);
            // and he is not going right nanymore
            flyingRight = false ;

            previousPosition =  (int)body.getPosition().x;


            // if he s going right and the region is facing left
        }else if( (body.getLinearVelocity().x > 0 || flyingRight ) && currentFrame.isFlipX()){
            currentFrame.flip(true, false);
            flyingRight = true ;
            previousPosition =  (int)body.getPosition().x;
        }


        return    currentFrame = (TextureRegion) fly.getKeyFrame(stateTime,true);

        }


    @Override
    public void crushedByDinosStep(Dino dino) {
            setToDestroy = true;
            body.setUserData(GameInfo.DESTROYED);
        FileManager.getInstanceOf().getSound("fly_enemyhit.wav").play();

    }



    @Override
    public void draw(Batch batch) {
        if( !destroyed ){
            super.draw(batch);
        }


    }

    @Override
    public void dispose() {
        super.dispose();
        atlas.dispose();

    }
}
