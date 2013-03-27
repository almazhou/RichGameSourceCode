package src.map;

import src.tools.Blockade;
import src.tools.Bomb;
import src.tools.OwnedTools;
import src.tools.Robot;

/**
 * Created with IntelliJ IDEA.
 * User: dell
 * Date: 13-1-25
 * Time: 下午10:40
 * To change this template use File | Settings | File Templates.
 */
public class ToolHouse extends LandForm{
    Blockade block=new Blockade();
    Bomb bomb=new Bomb();
    Robot robot=new Robot();
    public ToolHouse(int index){
        super("T",index);
    }

    @Override
    public int getPrice() {
        return 0;
    }

    public static void displayTools() {
        System.out.println("道具      编号  价值（点数）  显示方式");
        System.out.println(Blockade.getName() + "      " + Blockade.getToolIndex() + "      " + Blockade.getPoint() + "             " + Blockade.getDisplayName());
        System.out.println(Robot.getName() + "  " + Robot.getToolIndex() + "      " + Robot.getPoint() + "    " + Robot.getDisplayName());
       System.out.println(Bomb.getName() + "      " + Bomb.getToolIndex() + "      " + Bomb.getPoint() + "             " + Bomb.getDisplayName());
    }
}
