package com.rubenmimoun.rubenmimoungame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import  com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.Sprites.BossEnemy;
import com.rubenmimoun.rubenmimoungame.Sprites.BossHead;
import com.rubenmimoun.rubenmimoungame.Sprites.Bumper;
import com.rubenmimoun.rubenmimoungame.Sprites.Dino;
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.huds.UIhuds;
import com.rubenmimoun.rubenmimoungame.tools.WorldContactListener;

import java.util.Stack;

public class BossScreen extends GameScreenInterface{

    private MainGame mainGame ;
    private World world ;
    private OrthographicCamera camera ;
    private Viewport viewport ;
    private UIhuds huds ;
    private Texture bg ;
    private TextureAtlas dinoAtlas ;
    private Dino dino ;
    private Fixture fixture ;
    private Bumper bumperRight ;
    private Bumper bumperLeft ;
    private Box2DDebugRenderer debugRenderer;
    private BossEnemy boss ;
    private Stack<BossHead> headsStackArray;

    private AssetsManager manager = new AssetsManager() ;



    private boolean left ;
    private boolean right ;
    private boolean jump ;
    private boolean canJump ;
    private Music music ;

    public Vector2 touchLocation = new Vector2() ;
    public BossScreen(MainGame mainGame){
        this.mainGame =mainGame ;


        com.rubenmimoun.rubenmimoungame.helper.GameInfo.THIRD_LEVEL = true  ;
        music =  FileManager.getInstanceOf().getTheme("theme-3.ogg");
        GameManager.getInstance().playMusic(music);

        // setting
        camera =  new OrthographicCamera();
        viewport = new FitViewport(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, camera);
        camera.position.set(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH / 2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT / 2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, 0);
        world = new World(new Vector2(0.0f, -9.8f), true);

        // huds ui
        huds = new UIhuds(mainGame);
        huds.setWorldNum();



        // contactListener
        world.setContactListener(new WorldContactListener(huds, this));

        //box2debug
        debugRenderer = new Box2DDebugRenderer();

        // characters
        dinoAtlas = new TextureAtlas("dinoTilesPack/dinoPack.atlas");
        dino = new Dino(this, (GameInfo.WIDTH/2 +70f) / GameInfo.PPM , GameInfo.HEIGHT/ GameInfo.PPM);
        System.out.println(dino.getBody().getPosition().x);
        boss= new BossEnemy(this, com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2  / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, 20/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);
        // bumpers
        bumperLeft = new Bumper(this, 30/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM , 23/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);
        bumperRight = new Bumper(this, 500/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM , 23/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);


        // backgrounds
        bg  = new Texture("dinoTileLand/graveyard.png");


        headsStackArray =  new Stack<BossHead>();
        // methods
        createGround();
        creatWall();
        createBossLife();

        huds.setWorldNum();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(huds.getStage());
        inputMultiplexer.addProcessor(new GestureDetector(this));
        inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(inputMultiplexer);



    }



    private void createBossLife(){

        for (int i = 0; i < com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().bossLife; i++) {
            headsStackArray.add(new BossHead(this, (com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH -100f +i *( -25f))/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,
                    (com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT -40f)/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM));
        }

        for ( BossHead b : headsStackArray) {

            System.out.println(b.getBody().getPosition().x);
    }


    }

    public void decrementBossLife(){
        com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().bossLife -- ;
        boss.raiseSpeed();
        headsStackArray.pop().dispose();
        victory();


    }

    @Override
    public void victory() {
        if(headsStackArray.size() == 0 ){
            boss.getBody().setLinearVelocity(0,0f);
            if(com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().getMusic() != null) com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().stopMusic();

            mainGame.setScreen(new EndAndCreditScreen(mainGame));
        }


    }

    public void incrementBossLife(){
        com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().bossLife ++ ;

        if( headsStackArray.size() <= 5 ){
            headsStackArray.push(new BossHead(this, (com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH -100f + (headsStackArray.size()+1)*( -25f))/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,
                    (com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT -40f)/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM));
        }

    }

    @Override
    public void update(float delta) {

        if( !com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().paused){
            handleInput(delta);
            world.step(1 / 60f, 6, 2);


            bumperRight.update(delta);
            bumperLeft.update(delta);
            dino.update(delta);
            fixDinoPosition(delta);

            boss.update(delta);

            for ( BossHead b: headsStackArray) {
                b.update();
            }
            huds.update(delta);
        }


    }

    private void fixDinoPosition(float delta){

        if(dino.isRunninRight()){
            dino.setPosition
                    ((dino.getBody().getPosition().x -dino.getWidth()/3)* com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM ,
                            (dino.getBody().getPosition().y-dino.getHeight()/2f)* com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM );

        }else{
            dino.setPosition
                    ((dino.getBody().getPosition().x - dino.getWidth()/2 -0.2f)* com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM ,
                            (dino.getBody().getPosition().y-dino.getHeight()/2f)* com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM );

        }


    }

    public void handleInput(float delta) {

        if (dino.getCurrentState() != Dino.State.DEAD) {

            if (left && dino.getBody().getLinearVelocity().x >= - 1){
                dino.getBody().applyLinearImpulse(new Vector2(-1.0f, 0), dino.getBody().getWorldCenter(), true);

            }else if( right && dino.getBody().getLinearVelocity().x <=1){
                dino.getBody().applyLinearImpulse(new Vector2(1.0f, 0), dino.getBody().getWorldCenter(), true);

            }

            if (jump){
                dino.getBody().applyLinearImpulse(new Vector2(dino.getBody().getLinearVelocity().x,
                        3.5f), dino.getBody().getWorldCenter(), true);
                jump = false ;
            }
        }

    }





    public void rebootAllGame() {
        com.rubenmimoun.rubenmimoungame.helper.GameInfo.THIRD_LEVEL = false ;
        setScores();



                    RunnableAction run = new RunnableAction();
                    run.setRunnable(new Runnable() {
                        @Override
                        public void run() {
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().checkForNewHighScores();
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().stopMusic();
                            FileManager.getInstanceOf().getSound("gameover.ogg").play();
                    mainGame.setScreen(new GameOverScreen(mainGame));
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().savedata();
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().lifeScore = 3;
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().score = 0 ;
                }

                    });

                    SequenceAction sa = new SequenceAction();
                    sa.addAction(Actions.sequence(Actions.delay(1f), Actions.fadeOut(1f), run));

                    huds.getStage().addAction(sa);


        dino.setDead(false);



    }

    @Override
    public void finishedLevel() {

    }

    @Override
    public World getWorld() {
        return this.world;
    }


    @Override
    public TextureAtlas getDinoAtlas() {
        return dinoAtlas;
    }


    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
    }


    private void createGround(){

        BodyDef bodyDef =  new BodyDef() ;
        bodyDef.type = BodyDef.BodyType.StaticBody ;
        bodyDef.position.set( 0f , 0f);
        Body body =  getWorld().createBody(bodyDef) ;

        PolygonShape shape =  new PolygonShape() ;
        shape.setAsBox(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH /  com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM , 22/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM );

        FixtureDef fixtureDef =  new FixtureDef() ;
        fixtureDef.shape = shape ;
        fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.GROUND ;

        fixture = body.createFixture(fixtureDef);


        BodyDef bodyDefCeiling =  new BodyDef() ;
        bodyDefCeiling.type = BodyDef.BodyType.StaticBody ;
        bodyDefCeiling.position.set( 0,4f);
        Body body2 =  world.createBody(bodyDefCeiling);


        FixtureDef fixtureDef2 =  new FixtureDef() ;
        fixtureDef2.shape = shape ;
        fixtureDef2.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.FINALWALLS;

        fixture = body2.createFixture(fixtureDef2);
        fixture.setUserData(this);

        shape.dispose();

    }
    private void creatWall(){

        BodyDef bodyDefWall =  new BodyDef() ;
        bodyDefWall.type = BodyDef.BodyType.StaticBody ;
        bodyDefWall.position.set( 0,0);
        Body body =  getWorld().createBody(bodyDefWall);

        PolygonShape shape =  new PolygonShape() ;
        shape.setAsBox(1/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT/ com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

        FixtureDef fixtureDef =  new FixtureDef() ;
        fixtureDef.shape = shape ;
        fixtureDef.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.FINALWALLS;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        BodyDef bodyDefWall1 =  new BodyDef() ;
        bodyDefWall1.type = BodyDef.BodyType.StaticBody ;
        bodyDefWall1.position.set( 5.4f,0);
        Body body1 =  world.createBody(bodyDefWall1);


        FixtureDef fixtureDef1 =  new FixtureDef() ;
        fixtureDef1.shape = shape ;
        fixtureDef1.filter.categoryBits = com.rubenmimoun.rubenmimoungame.helper.GameInfo.FINALWALLS;

        fixture = body1.createFixture(fixtureDef1);
        fixture.setUserData(this);




        shape.dispose();


    }




    @Override
    public void render(float delta) {

        update(delta);

        com.rubenmimoun.rubenmimoungame.helper.GameInfo.cleanScreen(Color.BLACK);

        mainGame.getBatch().begin();

        mainGame.getBatch().draw(bg,-50f,-10);
        mainGame.getBatch().draw(dino, dino.getX(), dino.getY());
        mainGame.getBatch().draw(boss, boss.getX(), boss.getY());
        mainGame.getBatch().draw(bumperLeft, bumperLeft.getX(), bumperLeft.getY());
        mainGame.getBatch().draw(bumperRight,bumperRight.getX(), bumperRight.getY());
     //   boss.draw(mainGame.getBatch());

        for ( BossHead b: headsStackArray) {
        mainGame.getBatch().draw(b,b.getX(), b.getY());
        }

        mainGame.getBatch().end();

        mainGame.getBatch().setProjectionMatrix(huds.getStage().getCamera().combined);
        huds.getStage().getViewport().apply();
        huds.getStage().draw();
        // since we incorporated actions, we have to call this method if we want to see them
        huds.getStage().act();




//        debugRenderer.render(world, camera.combined);

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public Stack<BossHead> getHeadsStackArray() {
        return headsStackArray;
    }

    public Music getMusic() {
        return music;
    }

    @Override
    public void dispose() {
        GameInfo.THIRD_LEVEL = false  ;
        dinoAtlas.dispose();
        bg.dispose();
        world.dispose();
        huds.disposeUiHuds();
        for ( BossHead b: headsStackArray) {
            b.dispose();
        }
        GameManager.getInstance().stopMusic();
        dinoAtlas.dispose();
        debugRenderer.dispose();
        world.dispose();
        bumperLeft.getTexture().dispose();
        bumperRight.getTexture().dispose();
        boss.getTexture().dispose();

    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.RIGHT :
                return right = true ;
            case Input.Keys.LEFT :
                return left = true ;
            case Input.Keys.UP :
                return jump = true ;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.RIGHT :
                return right = false ;
            case Input.Keys.LEFT :
                return left = false;
            case Input.Keys.UP :
                return jump = false ;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        left = false ;
        right = false ;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        if( x < viewport.getScreenWidth() / 2f && y < viewport.getScreenHeight() - viewport.getScreenHeight() /8f   ){
            left = true ;
        }

        if( x > viewport.getScreenWidth() / 2f  && y < viewport.getScreenHeight() - viewport.getScreenHeight() /8f){
            right = true ;

        }


        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        System.out.println(count == 2 && y>viewport.getScreenHeight() - viewport.getScreenHeight() /8f);

        if( count ==  1 && y > viewport.getScreenHeight() - viewport.getScreenHeight() /8f){

            jump = true ;
        }




        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
       if(deltaX < 0 ){
           left =true ;
           right = false;
       }else {
           right = true ;
           left= false ;
       }

       if( deltaY >= 0) {
           canJump = false;
       }else {
           canJump = true ;
       }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if( canJump)
        jump = true ;
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
