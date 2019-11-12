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
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameData;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.huds.UIhuds;
import com.rubenmimoun.rubenmimoungame.items.Item;
import com.rubenmimoun.rubenmimoungame.items.ItemDef;
import com.rubenmimoun.rubenmimoungame.items.LifeItem;
import com.rubenmimoun.rubenmimoungame.map.MeatEnd;
import com.rubenmimoun.rubenmimoungame.map.WorldMap2;
import com.rubenmimoun.rubenmimoungame.tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class GameScreenWorld2 extends GameScreenInterface {


    private  Music music;
    private World world;
    private WorldMap2 worldMap;
    private com.rubenmimoun.rubenmimoungame.Sprites.Dino dino;
    private MeatEnd meatEnd;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera box2dcamera;
    private OrthographicCamera mainCamera;
    private Viewport gameViewPort;
    private com.rubenmimoun.rubenmimoungame.MainGame gameMain;
    private TextureAtlas dinoAtlas;
    private Texture texture;
    private TmxMapLoader mapLoader;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMap tiledMap;
    private com.rubenmimoun.rubenmimoungame.huds.UIhuds huds;
    private LinkedBlockingQueue<com.rubenmimoun.rubenmimoungame.items.ItemDef> itemsToSpawn;
    private Array<com.rubenmimoun.rubenmimoungame.items.Item> items;
    private Music theme;
    private GameData gameData ;

    private AssetsManager manager = new AssetsManager() ;


    private boolean left ;
    private boolean right ;
    private boolean jump ;
    private boolean canJump ;

    public Vector2 touchLocation = new Vector2() ;

    public GameScreenWorld2(MainGame gameMain) {

        com.rubenmimoun.rubenmimoungame.helper.GameInfo.WORLD2_PLAYING = true ;

        com.rubenmimoun.rubenmimoungame.helper.GameInfo.SECOND_LEVEL = true ;

        dinoAtlas = new TextureAtlas("dinoTilesPack/dinoPack.atlas");
        texture = new Texture("dinoTilesPack/dinoPack.png");


        this.gameMain = gameMain;

        // map settings
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("dinoTileLand/world2/dinoMap2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);
        // camera settings
        mainCamera = new OrthographicCamera();
        gameViewPort = new FitViewport(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, mainCamera);
        mainCamera.position.set(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH / 2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT / 2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, 0);
        // gravity
        world = new World(new Vector2(0.0f, -9.8f), true);

        huds = new UIhuds(gameMain);



        // contactListener
        world.setContactListener(new WorldContactListener(huds, this));

        //box2debug
        box2dcamera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer();

        // instantiated objects
        worldMap = new WorldMap2(this);

        dino = new Dino(this, 120f / GameInfo.PPM, 50f / GameInfo.PPM);


        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
        items = new Array<Item>();

        if(GameManager.getInstance().getMusic() != null)
       GameManager.getInstance().stopMusic();

        music =   FileManager.getInstanceOf().getTheme("theme-2.ogg") ;
      GameManager.getInstance().playMusic(music);



        huds.setWorldNum();

        huds.setWorldNum();
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(huds.getStage());
        inputMultiplexer.addProcessor(new GestureDetector(this));
        inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(inputMultiplexer);


    }

    public void spawnItem(com.rubenmimoun.rubenmimoungame.items.ItemDef itemDef) {

        itemsToSpawn.add(itemDef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            // poll = pop for queue
            ItemDef itemDef = itemsToSpawn.poll();
            if (itemDef.type == com.rubenmimoun.rubenmimoungame.items.LifeItem.class) {
                items.add(new LifeItem(this, itemDef.position.x, itemDef.position.y, "items/life.png"));
            }

        }
    }

    public void update(float delta) {

        if(!com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().paused){

            handleInput(delta);
            handleSpawningItems();
            world.step(1 / 60f, 6, 2);

            dino.update(delta);


            for (com.rubenmimoun.rubenmimoungame.Sprites.Enemy enemy : worldMap.getFlyingDino()) {
                enemy.update(delta);
            }

            for (com.rubenmimoun.rubenmimoungame.Sprites.Enemy enemy : worldMap.getGroundEnemies()) {
                enemy.update(delta);

            }

            for (com.rubenmimoun.rubenmimoungame.items.Item item : items) {
                item.update(delta);
            }

            worldMap.getMeatEnd().update(delta);

            // follows the dino if it passed passed half of the screen
            if (dino.getBody().getPosition().x >= com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH / 2 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM )
                mainCamera.position.x = dino.getBody().getPosition().x;

            if( dino.getBody().getPosition().y >= com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT / 2 / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM )
                mainCamera.position.y = dino.getBody().getPosition().y;



            huds.update(delta);

            mainCamera.update();

            mapRenderer.setView(mainCamera);



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
                dino.getBody().applyLinearImpulse(new Vector2(0, 5.2f), dino.getBody().getWorldCenter(), true);
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

        com.rubenmimoun.rubenmimoungame.helper.GameInfo.cleanScreen(Color.BLACK);
        update(delta);

        // always set before the gamemain.batch begin  !!
        mapRenderer.render();


        gameMain.getBatch().setProjectionMatrix(mainCamera.combined);

        gameMain.getBatch().begin();

        dino.draw(gameMain.getBatch());

        for (com.rubenmimoun.rubenmimoungame.Sprites.Enemy enemy : worldMap.getFlyingDino()) {
            enemy.draw(gameMain.getBatch());
        }

        for (com.rubenmimoun.rubenmimoungame.Sprites.Enemy enemy : worldMap.getGroundEnemies()) {
            enemy.draw(gameMain.getBatch());
        }

        worldMap.getMeatEnd().draw(gameMain.getBatch());


        for (com.rubenmimoun.rubenmimoungame.items.Item item : items) {
            item.draw(gameMain.getBatch());
        }
        gameMain.getBatch().end();

        gameMain.getBatch().setProjectionMatrix(huds.getStage().getCamera().combined);
        huds.getStage().draw();
        // since we incorporated actions, we have to call this method if we want to see them
        huds.getStage().act();


      //  debugRenderer.render(world, mainCamera.combined);


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

                if (com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().lifeScore > 0) {

                    if (dino.getDeath().equals("drown")) {

                        gameMain.setScreen(new LoadingScreen(gameMain, new GameScreenWorld2(gameMain), "inWater.jpg"));

                    } else if (dino.getDeath().equals("touched")) {

                        gameMain.setScreen(new LoadingScreen(gameMain, new GameScreenWorld2(gameMain), "hurt.jpg"));


                    }

                    com.rubenmimoun.rubenmimoungame.helper.GameInfo.TOUCHABLE =true ;

                } else {
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().checkForNewHighScores();
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().savedata();
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().stopMusic();
                    FileManager.getInstanceOf().getSound("gameover.ogg").play();
                    gameMain.setScreen(new GameOverScreen(gameMain));
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().lifeScore = 3;
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

    public Music getMusic() {
        return music;
    }

    @Override
    public void finishedLevel() {
            gameMain.setScreen(new LoadingScreen(gameMain,new GameLevelScreen(gameMain),"victory.jpg"));
             com.rubenmimoun.rubenmimoungame.helper.GameInfo.REACHED_SECOND_END = true ;

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

    public WorldMap2 getWorldMap() {
        return worldMap;
    }

    @Override
    public void dispose() {


        GameInfo.SECOND_LEVEL = false ;

        dinoAtlas.dispose();

        dino.getTexture().dispose();

        world.dispose();
        gameMain.getBatch().dispose();
        debugRenderer.dispose();
        mapRenderer.dispose();


        tiledMap.dispose();


        for (com.rubenmimoun.rubenmimoungame.Sprites.Enemy enemy : worldMap.getFlyingDino()) {
            enemy.getTexture().dispose();
        }

        for (Enemy enemy : worldMap.getGroundEnemies()) {
            enemy.getTexture().dispose();
        }

        worldMap.getMeatEnd().getTexture().dispose();

        mapRenderer.dispose();

        for (Item item : items) {
            item.getTexture().dispose();
        }

        huds.disposeUiHuds();
        manager.dispose();

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
}
