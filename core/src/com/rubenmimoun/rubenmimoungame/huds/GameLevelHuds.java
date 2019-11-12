package com.rubenmimoun.rubenmimoungame.huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rubenmimoun.rubenmimoungame.MainGame;
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameData;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.BossScreen;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenWorld1;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenWorld2;
import com.rubenmimoun.rubenmimoungame.screens.LoadingScreen;
import com.rubenmimoun.rubenmimoungame.screens.MenuScreen;

public class GameLevelHuds extends Huds {



    private MainGame mainGame ;
    private Stage stage ;
    private Viewport viewport ;
    private ImageButton lvl1_button , lvl2_button , boss_button ,back_btn;
    private GameData gameData ;
    private AssetsManager manager = new AssetsManager() ;

    public GameLevelHuds(MainGame mainGame) {
        super(mainGame);

        this.mainGame = mainGame ;

        viewport =  new FitViewport(GameInfo.WIDTH ,
                GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport, mainGame.getBatch());

        stage.addActor(lvl1_button);
        stage.addActor(lvl2_button);
        stage.addActor(boss_button);
        stage.addActor(back_btn);

        gameData = new GameData();

        Gdx.input.setInputProcessor(stage);


    }

    @Override
    protected void createButton() {
        super.createButton();

        lvl1_button = new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/lvl1.png"))));
        lvl2_button = new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/lvl2.png"))));
        boss_button = new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/boss.png"))));
        back_btn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/back.png"))));

        lvl1_button.setSize(90f, 90f);
        lvl2_button.setSize(90f,90f);
        boss_button.setSize(90f,90f);
        back_btn.setSize(90, 50);

        lvl1_button.setPosition( GameInfo.WIDTH / 3 - (GameInfo.WIDTH / 3 / 2 ) -lvl1_button.getWidth() / 2 ,
                GameInfo.HEIGHT / 2f - lvl1_button.getHeight() );


        lvl2_button.setPosition(GameInfo.WIDTH /2f , GameInfo.HEIGHT /2f , Align.center );

        boss_button.setPosition( GameInfo.WIDTH - (GameInfo.WIDTH / 3 -lvl1_button.getWidth() / 2 ) ,
                GameInfo.HEIGHT / 2f - lvl1_button.getHeight() );

        back_btn.setPosition(GameInfo.WIDTH - back_btn.getWidth()  ,0, Align.bottomLeft);



        //TODO SET ALL LISTENERS // listeners
        back_btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileManager.getInstanceOf().getSound("picked.mp3").play();
                mainGame.setScreen(new MenuScreen(mainGame));
            }
        });

        lvl1_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileManager.getInstanceOf().getSound("picked.mp3").play();
                lvl1_button.setChecked(true);
                mainGame.setScreen(new LoadingScreen(mainGame,new GameScreenWorld1(mainGame),"loading.jpg"));
            }
        });

        boss_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileManager.getInstanceOf().getSound("picked.mp3").play();
                boss_button.setChecked(true);
                mainGame.setScreen(new LoadingScreen(mainGame,new BossScreen(mainGame),"loading.jpg"));
            }
        });


        lvl2_button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileManager.getInstanceOf().getSound("picked.mp3").play();
                lvl2_button.setChecked(true);
                mainGame.setScreen(new LoadingScreen(mainGame,new GameScreenWorld2(mainGame),"loading.jpg"));
            }
        });



        if( GameInfo.REACHED_FIRST_END){

            lvl2_button.setVisible(true);
            boss_button.setVisible(false);

        }else if( GameInfo.REACHED_SECOND_END){

            boss_button.setVisible(true);

        }else{
            lvl2_button.setVisible(false);
            boss_button.setVisible(false);
        }

    }


    public Stage getStage() {
        return stage;
    }
}
