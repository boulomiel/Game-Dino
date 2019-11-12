package com.rubenmimoun.rubenmimoungame.items;

import com.badlogic.gdx.math.Vector2;

public class ItemDef {

    public Vector2 position ;
    public  Class<?> type  ;

    public ItemDef(Vector2 position, Class<?> type){

        // item def is class to shortcut the use of item cause the constructor has a parameter
        // class <?> allowing it to transform itself into what we need

        this.position =position ;
        this.type = type;

    }
}
