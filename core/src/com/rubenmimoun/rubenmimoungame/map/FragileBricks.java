package com.rubenmimoun.rubenmimoungame.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.rubenmimoun.rubenmimoungame.Sprites.Dino;
import com.rubenmimoun.rubenmimoungame.helper.AssetsManager;
import com.rubenmimoun.rubenmimoungame.helper.FileManager;
import com.rubenmimoun.rubenmimoungame.helper.GameInfo;
import com.rubenmimoun.rubenmimoungame.items.ItemDef;
import com.rubenmimoun.rubenmimoungame.items.LifeItem;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenInterface;
import com.rubenmimoun.rubenmimoungame.screens.GameScreenWorld1;

import java.util.ArrayList;
import java.util.List;

public class FragileBricks extends WorldObject {


    private GameScreenInterface screen ;
    private static TiledMapTileSet tile;
    private ItemDef item  ;


    /*
    even the tile id is 27 ,  libgdx counts starts from 0, therefore we add one
     */

    public final int LIFE1 = 28 ;
    public final int LIFE2 = 82 ;
    public final int LIFE3 = 93 ;

    List<Integer> lifeTile = new ArrayList<Integer>();

    AssetsManager manager ;


    public FragileBricks(GameScreenInterface screen, MapObject object) {
        super(screen, object);

        setCategoryFilter(com.rubenmimoun.rubenmimoungame.helper.GameInfo.BRICK);

        item =  new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 35 /GameInfo.PPM),
                LifeItem.class);

        lifeTile.add(LIFE1) ;
        lifeTile.add(LIFE2) ;
        lifeTile.add(LIFE3 ) ;


        fixture.setUserData(this);

        this.screen = screen ;
    }

    @Override
    public void onHeadHit(Dino dino) {



        //Gdx.app.log("head", "brick");

            // if the tile cell object contains the mushroom key
            if(object.getProperties().containsKey("life") && object != null){
               // GameManager.getInstance().getSound("audio/powerup_appears.ogg", Sound.class).play() ;
                // spawn a mushroom at the vector2 position, upon the coin box
                screen.spawnItem(item);

                FileManager.getInstanceOf().getSound("coin.ogg").play();
                FileManager.getInstanceOf().getSound("breakblock.ogg").play();


            }else{
                FileManager.getInstanceOf().getSound("breakblock.ogg").play();
            }


        //getCell().setTile(tile.getTile(BLANK_COIN));


        }




    @Override
    public void destroy() {
        System.out.println("destroyed brick");
      getCell().setTile(null);

        setCategoryFilter(GameInfo.DESTROYED);


    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
