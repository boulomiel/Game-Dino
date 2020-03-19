package com.rubenmimoun.rubenmimoungame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.huds.MenuHuds;

public class MenuScreen implements Screen {


       private MainGame game ;
       private MenuHuds huds ;
       private OrthographicCamera mainCamera ;
       private Viewport gameViewPort;
       private Texture bg ;

       private  AssetsManager manager = new AssetsManager() ;

       private Music music ;
     public  MenuScreen(MainGame game){



        this.game = game ;
        huds = new MenuHuds(game);

        mainCamera = new OrthographicCamera() ;
        mainCamera.setToOrtho(false,GameInfo.WIDTH ,GameInfo.HEIGHT);

        mainCamera.position.set( GameInfo.WIDTH /2,  GameInfo.HEIGHT /2f , 0 );

        gameViewPort = new FitViewport(GameInfo.WIDTH , GameInfo.HEIGHT );

        bg =  new Texture("screens/homeS.jpg");


        music =  FileManager.getInstanceOf().getTheme("menu.ogg") ;
        GameManager.getInstance().playMusic(music);
        GameManager.getInstance().initializeGameData();




    }


    @Override
    public void show() {
        Gdx.input.setCatchBackKey(false);
    }

    @Override
    public void render(float delta) {

        GameInfo.cleanScreen(Color.BLACK);


        game.getBatch().begin();

        game.getBatch().draw(bg,0,0);

        game.getBatch().end();

        game.getBatch().setProjectionMatrix(huds.getStage().getCamera().combined);

        huds.getStage().draw();
        huds.getStage().act();





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



    @Override
    public void dispose() {

        bg.dispose();

        huds.disposeMenuHuds();




    }
}
