package com.rubenmimoun.rubenmimoungame.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public class BossHead extends Sprite {

    private World world ;
    private GameScreenInterface screen ;
    private Body body ;


    public BossHead(GameScreenInterface screen , float x , float y){
        super(new Texture("dinoTilesPack/enemies/bossHead2.png"));
        this.screen = screen ;
        world= screen.getWorld() ;

        setPosition(x, y);


        createBody();
    }

    public void update(){
        setPosition((body.getPosition().x - 0.1f) * com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM ,
                (body.getPosition().y-0.1f) * com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM

        );
    }

    private void createBody() {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody ;
        bodyDef.position.set(getX(), getY());
        body = world.createBody(bodyDef);

        CircleShape shape =  new CircleShape() ;
        shape.setRadius(getWidth()/2 / GameInfo.PPM);

        FixtureDef fixtureDef =  new FixtureDef();
        fixtureDef.isSensor = true ;
        fixtureDef.shape =  shape ;
        body.createFixture(fixtureDef);

        shape.dispose();
    }


    public void dispose(){

        getTexture().dispose();

    }

    public Body getBody() {
        return body;
    }
}
