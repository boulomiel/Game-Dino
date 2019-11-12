package com.rubenmimoun.rubenmimoungame.huds;

import com.rubenmimoun.rubenmimoungame.MainGame;

public abstract class Huds {

    private com.rubenmimoun.rubenmimoungame.MainGame mainGame ;
//

    public Huds(MainGame mainGame){
        this.mainGame = mainGame ;


        createLabels();
        createButton();
        createUiHuds() ;
        createTable() ;



    }

    protected   void createLabels(){}

    protected   void createButton(){}

    protected void createUiHuds(){}

    protected void createTable(){}

}
