package com.rubenmimoun.rubenmimoungame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;


public class EndAndCreditScreen implements Screen {

    private MainGame mainGame ;
    private Label end_game ;
    private Label signature;
    private Label credits ;
    private Label loadingArtist ; //3D_Maennchen
    private Viewport viewport ;
    private OrthographicCamera camera ;
    private Stage stage ;
    private Music music ;

    private AssetsManager manager = new AssetsManager();


    public EndAndCreditScreen(final MainGame mainGame){

        this.mainGame = mainGame ;
        camera = new OrthographicCamera() ;
        viewport =  new FitViewport(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT , camera);
        camera.position.set( com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT/2f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM,0);
        music =  FileManager.getInstanceOf().getTheme("finalVictory.ogg");
        music.play();

        createLabels();

        stage = new Stage(viewport,mainGame.getBatch()) ;
        stage.addActor(end_game);
        stage.addActor(credits);
        stage.addActor(signature);
        stage.addActor(loadingArtist);
        stage.addAction(Actions.sequence(Actions.alpha(0.1f), Actions.fadeIn(1.0f),
                Actions.delay(8f),Actions.fadeOut(1.0f),Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        dispose();
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameLevelScreen(mainGame));
                    }
                })));
    }


    protected void createLabels() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter bigParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter smallP = new FreeTypeFontGenerator.FreeTypeFontParameter();

        bigParameter.size = 30 ;
        smallP.size = 10 ;


        BitmapFont font = generator.generateFont(bigParameter) ;
        BitmapFont font1 = generator.generateFont(smallP) ;

        end_game = new Label("END",new Label.LabelStyle(font, Color.WHITE)) ;
        end_game.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2- end_game.getWidth()/2f  , com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT- end_game.getHeight() -30 );


        signature = new Label("Developed by R.Meimoun" , new Label.LabelStyle(font1, Color.WHITE)) ;
        signature.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH /2 - signature.getWidth()/2  , com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT -140);

        credits = new Label(" Credits : "+"\n" ,new Label.LabelStyle(font1, Color.WHITE)) ;
        credits.setPosition(0 ,  60);

        loadingArtist = new Label( "   3D_Maennchen"+"\n"+ "   GameArt2d"
                ,new Label.LabelStyle(font1, Color.WHITE)) ;
        loadingArtist.setPosition(0, 30);

    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        GameInfo.cleanScreen(Color.BLACK);

        mainGame.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
        stage.act();

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

    @Override
    public void dispose() {

        stage.dispose();


    }

    public Music getMusic() {
        return music;
    }
}

