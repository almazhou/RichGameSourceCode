package src.map;

import src.player.Player;
import src.tools.Tool;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class LandForm implements BehaviorToPlayer{
    String name;
    String displayName;
    protected int landIndex;
    protected Color color=Color.WHITE;
    private List<Player> playerOnThisLand=new ArrayList<Player>();
    private List<Tool> toolList = new ArrayList<Tool>();

    public LandForm(String name, int index) {
            this.name=name;
            this.displayName=name;
            this.landIndex =index;
    }

    public  void setTool(Tool tool){
        toolList.add(tool);
        displayName= tool.getDisplayName();
    }

    public boolean isBlocked(){
        return toolList.contains(Tool.Blockade);
    }
    public int getLandIndex(){
        return landIndex;
    }
    public  boolean isBombed(){
        return toolList.contains(Tool.Bomb);
    }
    public String getName(){
        return name;
    }
    public void setDisplayName(String name) {
        this.displayName=name;
    }
    public void addPlayer(Player player){
        playerOnThisLand.add(player);
    }

    public List getPlayerOnLand(){
        return playerOnThisLand;
    }

    public  void clearBomb(){
        if(isBombed()){
        toolList.remove(Tool.Bomb);
        }
    }

    public void clearBlock(){
        if(isBlocked()){
        toolList.remove(Tool.Blockade);
        }
    }
}
