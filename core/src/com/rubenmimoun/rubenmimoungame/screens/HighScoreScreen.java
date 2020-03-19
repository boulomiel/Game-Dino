package com.rubenmimoun.rubenmimoungame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.huds.HighScoreHuds;

public class HighScoreScreen implements Screen {

    private MainGame game ;

    private OrthographicCamera camera ;
    private Viewport gameViewport ;

    private Texture scoreBg;

    private HighScoreHuds btns ;

    public  HighScoreScreen(MainGame game){

        this.game = game ;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameInfo.WIDTH,GameInfo.HEIGHT);
        camera.position.set(GameInfo.WIDTH/2f , GameInfo.HEIGHT/2f , 0);

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT,camera);


        scoreBg =  new Texture("screens/score.png");

        btns = new HighScoreHuds(game);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();

        game.getBatch().draw(scoreBg,0,0);

        game.getBatch().end();

        game.getBatch().setProjectionMatrix(btns.getStage().getCamera().combined);
        btns.getStage().draw();

    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width,height);
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
        scoreBg.dispose();
    }

}
