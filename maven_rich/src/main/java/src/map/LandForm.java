package src.map;

import src.tools.Tool;

import java.awt.*;

public abstract class LandForm implements BehaviorToPlayer{
    String name;
    String displayName;
    protected int landIndex;
    protected boolean bombFlag=false;
    protected boolean blockFlag=false;
    protected Color color=Color.WHITE;
    public LandForm(String name, int index) {
            this.name=name;
            this.displayName=name;
            this.landIndex =index;
    }

    public  void setBomb(){
        bombFlag=true;
        displayName= Tool.Bomb.getDisplayName();
    }

    public void setBlock(){
        blockFlag=true;
        displayName= Tool.Blockade.getDisplayName();
    }

    public void clearBombAndBlock(){
        bombFlag=false;
        blockFlag=false;
    }

    public boolean isBlocked(){
        if(blockFlag) {
        System.out.print("该处有路障");
        }
        return blockFlag;

    }
    public int getLandIndex(){
        return landIndex;
    }
    public  boolean isBombed(){
        if(bombFlag){
        System.out.print("该处有炸弹");
        }
        return bombFlag;
    }
    public String getName(){
        return name;
    }
    public void setDisplayName(String name) {
        this.displayName=name;
    }


    public String getDisplayName() {
        return displayName;
    }

    public Color getColor(){
        return this.color;
    }
}
