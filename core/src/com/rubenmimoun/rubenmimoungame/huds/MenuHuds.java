package com.rubenmimoun.rubenmimoungame.huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.screens.GameLevelScreen;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenWorld1;
import com.rubenmimoun.rubenmimoungame.screens.HighScoreScreen;
import com.rubenmimoun.rubenmimoungame.screens.LoadingScreen;
import com.rubenmimoun.rubenmimoungame.screens.MenuScreen;

public class MenuHuds extends Huds {


    private MainGame mainGame ;
    private Stage stage ;
    private Viewport viewport ;
    private ImageButton playButton , scoreButton , quitButton, stageButtons, musicButton ;
    private TextureAtlas buttonAtlas ;



    public MenuHuds(MainGame mainGame) {

        super(mainGame);
        this.mainGame = mainGame ;

        viewport = new FitViewport(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH , com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT , new OrthographicCamera());

        stage = new Stage(viewport, mainGame.getBatch()) ;

        buttonAtlas = new TextureAtlas("buttons/buttonAtlas.atlas");

        Gdx.input.setInputProcessor(stage);




        stage.addActor(playButton);
        stage.addActor(scoreButton);
        stage.addActor(stageButtons);
        stage.addActor(musicButton);
        stage.addActor(quitButton);


    }


    @Override
    protected void createButton() {


     playButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/play.png"))));
     scoreButton = new ImageButton( new SpriteDrawable(new Sprite(new Texture("buttons/score.png"))));
     stageButtons = new ImageButton( new SpriteDrawable( new Sprite( new Texture("buttons/stage.png"))));
     musicButton = new ImageButton( new SpriteDrawable(new Sprite( new Texture("buttons/music.png"))));
     quitButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/quit.png"))));

     playButton.setPosition( com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2f -100  ,  com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT/2f + 125 , Align.center );
     scoreButton.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2f -100  ,  com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT/2f + 50 , Align.center );
     stageButtons.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2f -100  ,  com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT/2f - 25, Align.center );
     musicButton.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH - 100,  20);
     quitButton.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2f - 100  ,  GameInfo.HEIGHT/2f - 100 , Align.center );

        scoreButton.setSize(90,50);
        stageButtons.setSize(90,50);
        quitButton.setSize(90,50);
        playButton.setSize(90,50);
        musicButton.setSize(90,90);

        Button.ButtonStyle style = new Button.ButtonStyle();


        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileManager.getInstanceOf().getSound("picked.mp3");
              //  manager.getSound("picked.mp3").play();
              //  manager.getSound("picked.mp3").play() ;
                GameManager.getInstance().stopMusic();
                ((MenuScreen)mainGame.getScreen()).dispose();
                disposeMenuHuds();
                mainGame.setScreen(new LoadingScreen(mainGame,new GameScreenWorld1(mainGame),"loading.jpg"));



            }
        });

        scoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               FileManager.getInstanceOf().getSound("picked.mp3").play();
                    mainGame.setScreen(new HighScoreScreen(mainGame));
            }
        });

        stageButtons.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileManager.getInstanceOf().getSound("picked.mp3").play();
                mainGame.setScreen(new GameLevelScreen(mainGame));
            }
        });

       quitButton.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
               Gdx.app.exit();
           }
       });

       musicButton.addListener(new ChangeListener() {

           @Override
           public void changed(ChangeEvent event, Actor actor) {
                if( com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().getMusic() != null
                        && com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().getMusic().getVolume() > 0 ){
                    com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().getMusic().setVolume(0);
                }else{
                    GameManager.getInstance().getMusic().setVolume(1);
                }
           }
       });







    }


    public Stage getStage() {
        return stage;
    }

    public void disposeMenuHuds(){
        stage.dispose();


    }
}
