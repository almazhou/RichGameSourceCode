package src.map;

import src.tools.OwnedTools;

/**
 * Created with IntelliJ IDEA.
 * User: dell
 * Date: 13-1-25
 * Time: 下午10:40
 * To change this template use File | Settings | File Templates.
 */
public class ToolHouse extends LandForm{
    OwnedTools block=OwnedTools.Blockade;
    OwnedTools bomb=OwnedTools.Bomb;
    OwnedTools robot=OwnedTools.Robot;
    public ToolHouse(int index){
        super("T",index);
    }

    @Override
    public int getPrice() {
        return 0;
    }

    public  void displayTools() {
        System.out.println("道具      编号  价值（点数）  显示方式");
        System.out.println(block.getName() + "      " + block.getToolIndex() + "      " + block.getPoint() + "             " + block.getDisplayName());
        System.out.println(robot.getName() + "  " + robot.getToolIndex() + "      " + robot.getPoint() + "    " +robot.getDisplayName());
       System.out.println(bomb.getName() + "      " + bomb.getToolIndex() + "      " + bomb.getPoint() + "             " + bomb.getDisplayName());
    }
}
