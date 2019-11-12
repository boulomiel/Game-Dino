package com.rubenmimoun.rubenmimoungame.huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.screens.MenuScreen;

public class HighScoreHuds extends Huds {



    private MainGame game;
    private Stage stage ;
    private Viewport viewport ;
    private GameData gameData ;
    private AssetsManager manager = new AssetsManager() ;

    private ImageButton backbtn ;
    private Label score_label;

    public HighScoreHuds(MainGame mainGame) {
        super(mainGame);

        this.game = mainGame ;

        viewport = new FitViewport(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH, com.rubenmimoun.rubenmimoungame.helper.GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport,mainGame.getBatch());


        Gdx.input.setInputProcessor(stage);

        gameData = new GameData();
        /*
        dont forget it ! if we dont put the input processor as a stage we cannot interact
         */



        stage.addActor(backbtn);
        stage.addActor(score_label);
    }

    @Override
    protected void createButton() {
        super.createButton();



        backbtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/back.png"))));

        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        /*
        generates our font
         */

        FreeTypeFontGenerator.FreeTypeFontParameter  parameter =
                new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 40 ;

            /*/
            resize the font (other functions are available
             */
        BitmapFont scoreFont = generator.generateFont(parameter);


        // returns the font as two different bitmaps

        score_label = new Label(String.valueOf(com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().score),
                new Label.LabelStyle(scoreFont, Color.WHITE));


        // set the fonts and colors to the labell


        backbtn.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH - backbtn.getWidth()  ,0, Align.bottomLeft);
        backbtn.setSize(90, 50);

        score_label.setPosition(com.rubenmimoun.rubenmimoungame.helper.GameInfo.WIDTH/2f - 100, GameInfo.HEIGHT/2f + 50 , Align.center);

        score_label.setText(GameManager.getInstance().score);

        backbtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileManager.getInstanceOf().getSound("picked.mp3").play();
                game.setScreen(new MenuScreen(game));
            }
        });

    }

    public Stage getStage() {
        return stage;
    }
}
