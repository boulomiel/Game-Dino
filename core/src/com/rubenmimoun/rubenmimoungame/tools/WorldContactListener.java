package com.rubenmimoun.rubenmimoungame.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.rubenmimoun.rubenmimoungame.Sprites.BossEnemy;
import com.rubenmimoun.rubenmimoungame.Sprites.Bumper;
import com.rubenmimoun.rubenmimoungame.Sprites.Dino;
import com.rubenmimoun.rubenmimoungame.Sprites.Enemy;
import com.rubenmimoun.rubenmimoungame.Sprites.GroundEnemy;
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameData;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.helper.GameManager;
import com.rubenmimoun.rubenmimoungame.huds.UIhuds;
import com.rubenmimoun.rubenmimoungame.items.LifeItem;
import com.rubenmimoun.rubenmimoungame.map.WorldObject;
import com.rubenmimoun.rubenmimoungame.screens.BossScreen;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

import java.util.Timer;
import java.util.TimerTask;


public class WorldContactListener implements ContactListener {

    private UIhuds huds ;
    private GameScreenInterface screen;
    private GameData gameData = new GameData();


    public WorldContactListener(UIhuds huds, GameScreenInterface game){
        this.huds = huds ;
        this.screen = game ;
    }


    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA() ;
        Fixture fixtureB =  contact.getFixtureB() ;

        int collisionDef =  fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits ;

        switch (collisionDef){

            case GameInfo.DINOHEAD | GameInfo.BRICK :

                    if(fixtureA.getFilterData().categoryBits == GameInfo.DINOHEAD){
                        ((WorldObject)fixtureB.getUserData()).onHeadHit((Dino) fixtureA.getUserData());
                        ((WorldObject)fixtureB.getUserData()).destroy();
                        huds.incrementscore(50);
                    }else{
                        ((WorldObject)fixtureA.getUserData()).onHeadHit((Dino) fixtureB.getUserData());
                        ((WorldObject)fixtureB.getUserData()).destroy();
                        huds.incrementscore(50);


                }


                break;

            case GameInfo.DINOHEAD |GameInfo.GROUND :

            case GameInfo.DINOFEET |GameInfo.GROUND :

                FileManager.getInstanceOf().getSound("bump.ogg").play();

                break;

            case GameInfo.ENEMY_HEAD | GameInfo.DINOFEET:

                if( !GameInfo.THIRD_LEVEL){

                    if(fixtureA.getFilterData().categoryBits == GameInfo.ENEMY_HEAD){

                        if( fixtureB.getBody().getLinearVelocity().y < 0){
                            ((Enemy)fixtureA.getUserData()).crushedByDinosStep((Dino)fixtureB.getUserData());
                            huds.incrementscore(100);
                        }

                    }else{
                        if( fixtureA.getBody().getLinearVelocity().y < 0){
                            ((Enemy)fixtureB.getUserData()).crushedByDinosStep((Dino)fixtureA.getUserData());
                            huds.incrementscore(100);
                        }
                    }
                }else {


                        if(fixtureA.getFilterData().categoryBits ==GameInfo.ENEMY_HEAD){

                            if(GameInfo.TOUCHABLE){
                               GameInfo.TOUCHABLE = false ;

                                ((Enemy)fixtureA.getUserData()).crushedByDinosStep((Dino)fixtureB.getUserData());
                                huds.incrementscore(500);
                                ((BossScreen)screen).decrementBossLife();
                                ((Dino)fixtureB.getUserData()).hitBossHead();
                                FileManager.getInstanceOf().getSound("dinosaur-1.wav").play();



                            }
                            timer();


                    }else{
                            if( GameInfo.TOUCHABLE){
                               GameInfo.TOUCHABLE = false ;

                                ((Enemy)fixtureB.getUserData()).crushedByDinosStep((Dino)fixtureA.getUserData());
                                ((BossScreen)screen).decrementBossLife();
                                ((Dino)fixtureA.getUserData()).hitBossHead();
                                huds.incrementscore(500);
                                FileManager.getInstanceOf().getSound("dinosaur-1.wav").play();
                            }

                            timer();


                    }
                }

                break;



            case GameInfo.GROUND_ENEMY |GameInfo.DINO :

            case GameInfo.FLYING_ENEMY |GameInfo.DINO :


                if(GameInfo.TOUCHABLE){

                   GameInfo.TOUCHABLE = false ;

                    if( fixtureA.getFilterData().categoryBits ==GameInfo.FLYING_ENEMY
                            || fixtureA.getFilterData().categoryBits ==GameInfo.GROUND_ENEMY ) {
                        System.out.println(fixtureA.getFilterData().categoryBits);
                        ((Dino)fixtureB.getUserData()).hurtdino();
                        FileManager.getInstanceOf().getSound("dino_got_hit.wav").play();
                    }else{
                        ((Dino)fixtureA.getUserData()).hurtdino();
                        FileManager.getInstanceOf().getSound("dino_got_hit.wav").play();
                    }
                }



                break;


            case GameInfo.DINO | GameInfo.WATER_BUMPER:

                if( !GameInfo.THIRD_LEVEL){
                    if( fixtureA.getFilterData().categoryBits == GameInfo.DINO ) {
                        ((Dino)fixtureA.getUserData()).dinoFell();
                        FileManager.getInstanceOf().getSound("water.wav").play();
                    }else{
                        ((Dino)fixtureB.getUserData()).dinoFell();
                        FileManager.getInstanceOf().getSound("water.wav").play();

                    }
                }else {
                    if( fixtureA.getFilterData().categoryBits == GameInfo.DINO ) {
                        ((Bumper)fixtureB.getUserData()).setBumped(true);
                        ((Dino)fixtureA.getUserData()).bumperJump();



                    }
                }

                break;

            case GameInfo.DINO | GameInfo.DINO_LIFE :
                if( fixtureA.getFilterData().categoryBits == GameInfo.DINO ) {
                    ((LifeItem)fixtureB.getUserData()).itemUsed((Dino)fixtureA.getUserData());
                  GameManager.getInstance().lifeScore += 1 ;
                    FileManager.getInstanceOf().getSound("powerup.wav").play();
                }else{
                    ((LifeItem)fixtureA.getUserData()).itemUsed((Dino)fixtureB.getUserData());
                   GameManager.getInstance().lifeScore += 1 ;
                    FileManager.getInstanceOf().getSound("powerup.wav").play();
                }
                break;

            case GameInfo.GROUND_ENEMY| GameInfo.ENEMYWALLS:
                if( fixtureA.getFilterData().categoryBits == GameInfo.DINO ) {
                    ((GroundEnemy)fixtureA.getUserData()).reverseVelocity(true,false);

                }else{
                    ((GroundEnemy)fixtureB.getUserData()).reverseVelocity(true,false); ;
                }

                break;



            case GameInfo.DINOFEET| GameInfo.END_MEAT_1 :
            case GameInfo.DINO | GameInfo.END_MEAT_1 :
                System.out.println("finished");

                checkLevels();

                GameManager.getInstance().savedata();
               GameManager.getInstance().stopMusic();
                FileManager.getInstanceOf().getSound("victory.ogg").play();

                screen.finishedLevel();
                break;

            case GameInfo.ENEMY_BOSS | GameInfo.FINALWALLS:
                if(fixtureA.getFilterData().categoryBits ==  GameInfo.ENEMY_BOSS){
                    ((BossEnemy)fixtureA.getUserData()).reverseVelocity(true, false);


                }else{
                    ((BossEnemy)fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break ;

            case GameInfo.DINO | GameInfo.ENEMY_BOSS:
                if(fixtureA.getFilterData().categoryBits ==  GameInfo.DINO){
                    ((BossEnemy)fixtureB.getUserData()).reverseVelocity(true, false);

                  //  ((BossScreen)screen).incrementBossLife();
                    if(GameInfo.TOUCHABLE){
                        huds.decrementLife();
                        ((Dino)fixtureA.getUserData()).touchedByBoss();
                        FileManager.getInstanceOf().getSound("dino_got_hit.wav").play();
                        GameInfo.TOUCHABLE = false;
                    }

                    timer();

                    if (GameManager.getInstance().lifeScore == 0)  {
                        ((BossScreen)screen).rebootAllGame();
                    }



                }else{

                    if( GameInfo.TOUCHABLE){
                        ((BossEnemy)fixtureA.getUserData()).reverseVelocity(true, false);
                        huds.decrementLife();
                        ((Dino)fixtureA.getUserData()).touchedByBoss();
                        FileManager.getInstanceOf().getSound("dino_got_hit.wav").play();
                    }


                    timer();

                    if (GameManager.getInstance().lifeScore == 0)  {
                        ((BossScreen)screen).rebootAllGame();
                    }
                }
                break ;


        }




    }




    private boolean checkLevels(){

        boolean lvl1Checked = false ;
        boolean lvl2checked =  false ;


            if( GameInfo.FIRST_LEVEL && !GameInfo.REACHED_FIRST_END){
             GameInfo.REACHED_FIRST_END = true ;
                gameData.setLvl2open(true);


                lvl1Checked = true ;
            }

            if(GameInfo.SECOND_LEVEL && !GameInfo.REACHED_SECOND_END){
                GameInfo.REACHED_SECOND_END = true ;
                gameData.setBoss_lvlopen(true);
                lvl2checked = true ;

            }



        return false ;

    }

    public void timer(){

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!GameInfo.TOUCHABLE)
                GameInfo.TOUCHABLE = true ;
            }
        },1500);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }


}
