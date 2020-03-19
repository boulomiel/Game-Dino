package com.rubenmimoun.rubenmimoungame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.huds.GameLevelHuds;

public class GameLevelScreen implements Screen {


    private MainGame mainGame ;
    private OrthographicCamera camera ;
    private Viewport viewport ;
    private Texture lvl_screen_BG ;
    private GameLevelHuds levelHuds  ;


    public GameLevelScreen(MainGame mainGame){

        this.mainGame =  mainGame ;
        // setto ortho =  doesnt move
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        camera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f , 0);
        viewport = new FitViewport(GameInfo.WIDTH,GameInfo.HEIGHT,camera);

        levelHuds = new GameLevelHuds(mainGame);

        lvl_screen_BG = new Texture("screens/gameLevelMap.png");

        if(GameManager.getInstance().getMusic() != null)
        GameManager.getInstance().stopMusic();

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        GameInfo.cleanScreen(Color.BLACK);

        mainGame.getBatch().begin();

        mainGame.getBatch().draw(lvl_screen_BG,0,0);

        mainGame.getBatch().end();

        mainGame.getBatch().setProjectionMatrix(levelHuds.getStage().getCamera().combined);
        levelHuds.getStage().draw();
        levelHuds.getStage().act();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

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

    @Override
    public void dispose() {

        mainGame.getBatch().dispose();
        mainGame.dispose();
        lvl_screen_BG.dispose();
        levelHuds.getStage().dispose();

    }
}
