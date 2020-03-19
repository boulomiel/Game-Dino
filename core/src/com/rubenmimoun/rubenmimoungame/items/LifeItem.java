package com.rubenmimoun.rubenmimoungame.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.rubenmimoun.rubenmimoungame.Sprites.Dino;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;

public class LifeItem extends  Item {




    public LifeItem(GameScreenInterface screen, float x, float y, String pathTotexture) {
        super(screen ,x, y,pathTotexture);


        velocity = new Vector2(0.7f, 0) ;

    }

    @Override
    protected void createItem() {

        BodyDef bodyDef = new BodyDef() ;
        bodyDef.type = BodyDef.BodyType.DynamicBody ;
        bodyDef.position.set(getX(), getY()) ;

        body = world.createBody(bodyDef) ;

        CircleShape shape = new CircleShape() ;
        shape.setRadius(10f / com.rubenmimoun.rubenmimoungame.helper.GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef() ;
        fixtureDef.shape = shape  ;
        fixtureDef.density = 1 ;
        fixtureDef.filter.categoryBits = GameInfo.DINO_LIFE ;

        body.createFixture(fixtureDef).setUserData(this);



    }

    @Override
    public void update(float delta) {
        super.update(delta);

        setPosition(body.getPosition().x - getWidth()/2 , body.getPosition().y - getHeight() /2);

        // meaning the y velocity doesn't change , its moving on the x axis only
        velocity.y = body.getLinearVelocity().y;

        body.setLinearVelocity(velocity);
    }

    @Override
    public void itemUsed(Dino dino) {
       toDestroy();

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);



    }
}
