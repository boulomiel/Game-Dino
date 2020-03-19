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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.Sprites.Dino;
import com.rubenmimoun.rubenmimoungame.Sprites.Enemy;
import com.rubenmimoun.rubenmimoungame.Sprites.FlyingEnemy;
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.huds.UIhuds;
import com.rubenmimoun.rubenmimoungame.items.Item;
import com.rubenmimoun.rubenmimoungame.items.ItemDef;
import com.rubenmimoun.rubenmimoungame.items.LifeItem;
import com.rubenmimoun.rubenmimoungame.map.WorldMap;
import com.rubenmimoun.rubenmimoungame.tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class GameScreenWorld1 extends GameScreenInterface {


    private World world;
    private WorldMap worldMap;
    private Dino dino;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera box2dcamera;
    private OrthographicCamera mainCamera;
    private Viewport gameViewPort;
    private MainGame gameMain;
    private TextureAtlas dinoAtlas;
    private Texture texture;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private UIhuds huds;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;
    private Array<Item> items;
    private Music music;

    private boolean left ;
    private boolean right ;
    private boolean jump ;
    private boolean canJump;

    private AssetsManager manager =  new AssetsManager();

    private Vector2 touchLocation = new Vector2() ;

    public GameScreenWorld1(MainGame gameMain) {


        dinoAtlas = new TextureAtlas("dinoTilesPack/dinoPack.atlas");
        texture = new Texture("dinoTilesPack/dinoPack.png");


        this.gameMain = gameMain;


        // map settings
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("dinoTileLand/dinoMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f / GameInfo.PPM);
        // camera settings
        mainCamera = new OrthographicCamera();

        gameViewPort = new FitViewport(GameInfo.WIDTH / GameInfo.PPM, GameInfo.HEIGHT / GameInfo.PPM, mainCamera);
        mainCamera.position.set(GameInfo.WIDTH / 2f / GameInfo.PPM, GameInfo.HEIGHT / 2f / GameInfo.PPM, 0);
        // gravity
        world = new World(new Vector2(0.0f, -9.8f), true);

        huds = new UIhuds(gameMain);


        // contactListener
        world.setContactListener(new WorldContactListener(huds, this));

        //box2debug
        box2dcamera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer();

        // instantiated objects
        this.worldMap = new WorldMap(this);

        dino = new Dino(this, 120f/GameInfo.PPM , 50f / GameInfo.PPM);


        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
        items = new Array<Item>();



        if(GameManager.getInstance().getMusic() != null ){
            GameManager.getInstance().stopMusic();
        }


           music =  FileManager.getInstanceOf().getTheme("theme-1.ogg") ;
           GameManager.getInstance().playMusic(music);





        GameInfo.FIRST_LEVEL = true ;

        huds.setWorldNum();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(huds.getStage());
        inputMultiplexer.addProcessor(new GestureDetector(this));
        inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(inputMultiplexer);



    }


    public void spawnItem(ItemDef itemDef) {

        itemsToSpawn.add(itemDef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            // poll = pop for queue
            ItemDef itemDef = itemsToSpawn.poll();
            if (itemDef.type == LifeItem.class) {
                items.add(new LifeItem(this, itemDef.position.x, itemDef.position.y, "items/life.png"));
            }

        }
    }

    public void update(float delta) {


        if(!GameManager.getInstance().paused){

            handleInput(delta);
            handleSpawningItems();
            world.step(1 / 60f, 6, 2);

            dino.update(delta);


            for (Enemy enemy : worldMap.getPterodactyl()) {
                enemy.update(delta);

            }

            for (Item item : items) {
                item.update(delta);
            }

            worldMap.getMeatEnd().update(delta);

            // follows the dino if it passed passed half of the screen
            if (dino.getBody().getPosition().x >= GameInfo.WIDTH / 2 / GameInfo.PPM)
                mainCamera.position.x = dino.getBody().getPosition().x;


            huds.update(delta);

            mainCamera.update();

            mapRenderer.setView(mainCamera);

            GameInfo.FIRST_LEVEL = true  ;
            huds.setWorldNum();


        }

    }


    public void handleInput(float delta) {

        if (dino.getCurrentState() != Dino.State.DEAD) {

            if (left && dino.getBody().getLinearVelocity().x >= - 1){
                dino.getBody().applyLinearImpulse(new Vector2(-1.0f, 0), dino.getBody().getWorldCenter(), true);

            }else if( right && dino.getBody().getLinearVelocity().x <=1){
                dino.getBody().applyLinearImpulse(new Vector2(1.0f, 0), dino.getBody().getWorldCenter(), true);

            }

            if (jump && dino.getBody().getLinearVelocity().y == 0){
                dino.getBody().applyLinearImpulse(new Vector2(0, 5.0f), dino.getBody().getWorldCenter(), true);
                jump = false ;
            }




        }

    }


    @Override
    public void show() {
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render(float delta) {

        GameInfo.cleanScreen(Color.BLACK);
        update(delta);

        // always set before the gamemain.batch begin  !!
        mapRenderer.render();


        gameMain.getBatch().setProjectionMatrix(mainCamera.combined);

        gameMain.getBatch().begin();

        dino.draw(gameMain.getBatch());


        for (Enemy enemy : worldMap.getPterodactyl()) {
            enemy.draw(gameMain.getBatch());

        }

        worldMap.getMeatEnd().draw(gameMain.getBatch());


        for (Item item : items) {
            item.draw(gameMain.getBatch());
        }
        gameMain.getBatch().end();

        gameMain.getBatch().setProjectionMatrix(huds.getStage().getCamera().combined);
        huds.getStage().draw();
        // since we incorporated actions, we have to call this method if we want to see them
        huds.getStage().act();


//        debugRenderer.render(world, mainCamera.combined);


    }

    public void setScores() {

    huds.decrementLife();

    huds.decrementScore(200);
    }


    public void rebootScreen() {

        setScores();

        RunnableAction run = new RunnableAction();
        run.setRunnable(new Runnable() {
            @Override
            public void run() {

                if (GameManager.getInstance().lifeScore > 0) {

                    if (dino.getDeath().equals("drown")) {

                        gameMain.setScreen(new LoadingScreen(gameMain, new GameScreenWorld1(gameMain), "inWater.jpg"));

                    } else if (dino.getDeath().equals("touched")) {

                        gameMain.setScreen(new LoadingScreen(gameMain, new GameScreenWorld1(gameMain), "hurt.jpg"));


                    }

                    GameInfo.TOUCHABLE =true ;

                } else {

                    GameManager.getInstance().checkForNewHighScores();
                    GameManager.getInstance().stopMusic();
                    gameMain.setScreen(new GameOverScreen(gameMain));

                    GameManager.getInstance().lifeScore = 3;
                    GameManager.getInstance().score = 0 ;
                }


            }
        });

        // actions are built in libgdx to give an effect
        SequenceAction sa = new SequenceAction();
//        sa.addAction(Actions.fadeOut(0f));
//        sa.addAction(Actions.delay(3f));

        sa.addAction(Actions.sequence(Actions.delay(1f), Actions.fadeOut(1f), run));
        // then we run the previous runnable action, to come back on th gamescreen
        // the stage is the one taking care of the ations
        huds.getStage().addAction(sa);

        dino.setDead(false);



    }


    public  void quitting() {
        dispose();
        gameMain.setScreen(new GameOverScreen(gameMain));
    }

    @Override
    public void finishedLevel() {
            gameMain.setScreen(new LoadingScreen(gameMain,new GameLevelScreen(gameMain),"victory.jpg"));
           GameInfo.REACHED_FIRST_END = true ;

    }

    @Override
    public void resize(int width, int height) {
        gameViewPort.update(width, height);

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

    public Music getMusic() {
        return music;
    }

    @Override
    public void dispose() {

            dinoAtlas.dispose();

            dino.getTexture().dispose();

            world.dispose();

           // worldMap.disposePterodactylTextures();


            tiledMap.dispose();
            mapLoader = null ;

            dinoAtlas.dispose();
            texture.dispose();

            worldMap.getMeatEnd().getTexture().dispose();

            mapRenderer.dispose();
            debugRenderer.dispose();

            for (Item item : items) {
                item.getTexture().dispose();
            }

        for (Enemy enemy : worldMap.getPterodactyl()) {
            ((FlyingEnemy)enemy).dispose();
        }

            huds.disposeUiHuds();
            manager.dispose();



            GameManager.getInstance().getMusic().dispose();
        }




    public World getWorld() {
        return world;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public TextureAtlas getDinoAtlas() {
        return dinoAtlas;
    }

    public Texture getTexture() {
        return texture;
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

            if( x < gameViewPort.getScreenWidth() / 2f && y < gameViewPort.getScreenHeight() - gameViewPort.getScreenHeight() /8f   ){
                left = true ;


            }

            if( x > gameViewPort.getScreenWidth() / 2f  && y < gameViewPort.getScreenHeight() - gameViewPort.getScreenHeight() /8f){
                right = true ;





        }


        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        System.out.println(count == 2 && y>gameViewPort.getScreenHeight() - gameViewPort.getScreenHeight() /8f);

     if( count ==  1 && y > gameViewPort.getScreenHeight() - gameViewPort.getScreenHeight() /8f){

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

            if( deltaY >= 0 ){
                canJump = false  ;
            }else {
                canJump = true ;
            }



        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        if(canJump){
            jump = true ;
            canJump = false ;
        }
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

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public AssetsManager getManager() {
        return manager;
    }
}
