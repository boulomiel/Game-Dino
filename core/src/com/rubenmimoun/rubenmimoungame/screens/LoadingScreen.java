package com.rubenmimoun.rubenmimoungame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;

public class LoadingScreen implements Screen {

    private Stage stage;
    private MainGame gameMain ;
    private Screen s ;

    private Texture texture;
    private Image image;
    private Label loadingLabel ;
    private String textureName  ;

    public LoadingScreen(MainGame game, Screen s, String textureName ){
        this.gameMain = game;
        this.s = s ;
        this.textureName = textureName ;


    }

    @Override
    public void show() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 40 ;



        BitmapFont font = generator.generateFont(parameter) ;

        loadingLabel = new Label("Loading",new Label.LabelStyle(font, Color.WHITE)) ;
        loadingLabel.setPosition( com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/ 2  - loadingLabel.getWidth()/2 ,
                com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT - loadingLabel.getHeight() -20 ,
                Align.center );

        texture = new Texture("screens/" + textureName);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);


        image = new Image(texture);
        image.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2f, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT/2f, Align.center);

        stage = new Stage(new FitViewport(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT,
                new OrthographicCamera(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH, GameInfo.HEIGHT)));

        stage.addActor(image);
        stage.addActor(loadingLabel);

        image.addAction(Actions.sequence(Actions.alpha(0.5f), Actions.fadeIn(1.0f),
                Actions.delay(0.5f),Actions.fadeOut(1.0f),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                            dispose();
                        ((Game) Gdx.app.getApplicationListener()).setScreen(s);


                    }
                })));


    }




    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
        texture.dispose();



    }


}
