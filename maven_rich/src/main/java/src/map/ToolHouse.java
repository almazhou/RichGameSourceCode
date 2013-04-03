package src.map;

import src.Game.Game;
import src.player.Player;
import src.tools.Tool;

/**
 * Created with IntelliJ IDEA.
 * User: dell
 * Date: 13-1-25
 * Time: 下午10:40
 * To change this template use File | Settings | File Templates.
 */
public class ToolHouse extends LandForm implements BehaviorToPlayer {
    public ToolHouse(int index){
        super("T",index);
    }

    public  void displayTools() {
        System.out.println("道具      编号  价值（点数）  显示方式");
        System.out.println(Tool.Blockade.getName() + "      " + Tool.Blockade.getToolIndex() + "      " + Tool.Blockade.getPoint() + "             " + Tool.Blockade.getDisplayName());
        System.out.println(Tool.Robot.getName() + "  " + Tool.Robot.getToolIndex() + "      " + Tool.Robot.getPoint() + "    " +Tool.Robot.getDisplayName());
       System.out.println(Tool.Bomb.getName() + "      " +Tool.Bomb.getToolIndex() + "      " + Tool.Bomb.getPoint() + "             " + Tool.Bomb.getDisplayName());
    }

    @Override
    public void PassByImpact(Player player) {
        System.out.println(this.name+">欢迎光临道具屋，请选择您所需要的道具(1-3)：");
       displayTools();
       String toolIndexInString= Game.getPlayerCommand(player);

        player.chooseTools(toolIndexInString);

    }
}
