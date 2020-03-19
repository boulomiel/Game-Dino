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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
import com.rubenmimoun.rubenmimoungame.screens.GameOverScreen;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenWorld1;
import com.rubenmimoun.rubenmimoungame.screens.MenuScreen;


public class UIhuds  extends Huds {


    private MainGame mainGame ;
    private Stage stage ;
    private Viewport viewport ;
    private Image life_image , score_img, worldImg ;
    private Label lifeLabel , worlLabel, timeLabel , scoreLabel;
    private ImageButton pauseButton ,resume_btn, music_btn, quit_btn ;
    private Table life_score ;
    private Image pause_panel  ;
    private GameData gameData ;

    private AssetsManager manager = new AssetsManager() ;

    private boolean isOnScreen = false  ;

    public UIhuds(MainGame mainGame){
        super(mainGame);
        this.mainGame = mainGame ;

        viewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT , new OrthographicCamera());

        stage = new Stage(viewport,mainGame.getBatch());


        Gdx.input.setInputProcessor(getStage());

        createLabels();
        createButton();
        createUiHuds() ;
        createTable() ;



        stage.addActor(life_score);
        stage.addActor(pauseButton);
        gameData =  new GameData() ;

    }

    public void update(float delta){

        incrementLife();



    }



    @Override
    protected void createTable(){
        life_score = new Table() ;
        life_score.setFillParent(true);
        life_score.top().left() ;
        life_score.add(life_image).padTop(10).padLeft(10);

        life_score.add(worldImg).padTop(10).padLeft(GameInfo.WIDTH/3 + 20).padRight(GameInfo.WIDTH/3);

        life_score.top().right() ;
        life_score.add(score_img).padRight(10).padTop(10);

        life_score.row();

        life_score.top().left() ;
        life_score.add(lifeLabel).padLeft(10) ;

        life_score.top().center() ;
        life_score.add(worlLabel).center().padLeft(20);

        life_score.top().right() ;
        life_score.add(scoreLabel).padRight(10) ;
    }

    @Override
    protected void createUiHuds() {

        life_image = new Image(new Texture("huds/lifeHead.png"));
        score_img = new Image(new Texture("huds/scoreImg.png"));
        worldImg = new Image(new Texture("huds/worldImg.png"));

    }
    @Override
    protected void createButton() {

        pauseButton = new ImageButton(new SpriteDrawable(new Sprite(new Texture("huds/buttonPause.png"))));

        pauseButton.setPosition(GameInfo.WIDTH - pauseButton.getWidth() - 20 , -20);

        pauseButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("pause");

                if( !GameManager.getInstance().paused)

                   GameManager.getInstance().paused = true ;
                createpausePanel();
            }
        });

    }

    public void createpausePanel(){

        if(!isOnScreen){
            isOnScreen = true ;
            pause_panel = new Image(new Texture("screens/pause_panel.png"));

            resume_btn =
                    new ImageButton(new SpriteDrawable(new Sprite(new Texture("huds/resume.png"))));

         //         quit_btn =
            new ImageButton(new SpriteDrawable(new Sprite(new Texture("huds/quit.png"))));

            music_btn =
                    new ImageButton(new SpriteDrawable(new Sprite(new Texture("buttons/music.png"))));
            music_btn.setSize(90,50);

            pause_panel.setPosition(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, Align.center);

            resume_btn.setPosition(GameInfo.WIDTH/2f , GameInfo.HEIGHT /2f + 20  ,Align.center );

       // quit_btn.setPosition(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f - 80,Align.center);

            music_btn.setPosition(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f - 100,Align.center);

            resume_btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    removePausePanel();

                    if (com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().paused){
                        manager.getSound("gameover.ogg").play();
                        com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().paused = false ;

                    }

                }
            });

            music_btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    if( com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().getMusic() != null
                            && com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().getMusic().getVolume() > 0 ){
                        com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().getMusic().setVolume(0);
                    }else{
                        com.rubenmimoun.rubenmimoungame.helper.GameManager.getInstance().getMusic().setVolume(1);
                    }

                }
            });

//        quit_btn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                RunnableAction runnable = new RunnableAction() {
//                    @Override
//                    public void run() {
//
//                        GameManager.getInstance().stopMusic();
//                        FileManager.getInstanceOf().getSound("gameover.ogg").play() ;
//                        mainGame.getScreen().dispose();
//                        mainGame.setScreen(new MenuScreen(mainGame));
//
//                    }
//                };
//                SequenceAction sa = new SequenceAction();
//                sa.addAction(Actions.sequence(Actions.delay(0.5f), Actions.fadeOut(1f), runnable));
//                stage.addAction(sa);
//
//            }
//        });


            stage.addActor(pause_panel);
            stage.addActor(resume_btn);
            stage.addActor(music_btn);
        //    stage.addActor(quit_btn);



        }

    }

    public void removePausePanel(){
        isOnScreen = false ;
        pause_panel.remove();
        resume_btn.remove();
        music_btn.remove() ;
      // quit_btn.remove();
    }




    @Override
    protected void createLabels() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 15 ;


        BitmapFont font = generator.generateFont(parameter) ;

        lifeLabel = new Label("x" +GameManager.getInstance().lifeScore,new Label.LabelStyle(font, Color.WHITE)) ;

        scoreLabel = new Label("x" + GameManager.getInstance().score ,new Label.LabelStyle(font, Color.WHITE)) ;

        worlLabel = new Label("x" +  GameManager.getInstance().world  ,new Label.LabelStyle(font, Color.WHITE)) ;
    }

    public void incrementscore(int score){

        GameManager.getInstance().score += score ;
        gameData.setHighscore(GameManager.getInstance().score);
        scoreLabel.setText("x" + GameManager.getInstance().score) ;

    }

    public void decrementScore(int minus){

     GameManager.getInstance().score -= minus ;

        if( GameManager.getInstance().score >= 0 ){
            scoreLabel.setText("x" + GameManager.getInstance().score) ;
            gameData.setHighscore(GameManager.getInstance().score);
        }else if ( GameManager.getInstance().score < 0 ){
            GameManager.getInstance().score = 0;
            scoreLabel.setText("x" + 0) ;
        }


    }

    public void incrementLife(){
        int life  =   GameManager.getInstance().lifeScore ;

        if( life <  GameManager.getInstance().lifeScore){
            life = GameManager.getInstance().lifeScore ;
        }

        lifeLabel.setText("x" +GameManager.getInstance().lifeScore) ;
    }

    public void decrementLife(){

        GameManager.getInstance().lifeScore --;
       GameManager.getInstance().checkForNewHighScores();

        if(GameManager.getInstance().lifeScore >= 0){
            lifeLabel.setText("x" + GameManager.getInstance().lifeScore);
        }else if( GameManager.getInstance().lifeScore < 0){
            GameManager.getInstance().lifeScore = 0 ;
            lifeLabel.setText("x" + 0);
        }

    }





    public void setWorldNum(){

        if( GameInfo.FIRST_LEVEL) {
            worlLabel.setText(1);
        }

        if( GameInfo.SECOND_LEVEL)
            worlLabel.setText(2);

        if(GameInfo.THIRD_LEVEL){
            worlLabel.setText("Boss");
        }



    }


    public Stage getStage() {
        return stage;
    }


    public void disposeUiHuds(){
        stage.dispose();

    }
}
