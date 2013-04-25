package src.map;

import src.player.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-27
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
 */
public class Prison extends LandForm{
    Prison(int index){
        super("P",index);
    }


    @Override
    public void PassByImpact(Player player) {
        if(player.getTimeInPrison()>0){
            System.out.println(this.name+">玩家在监狱中！还有"+player.getTimeInPrison()+"天出狱！");
            return;

        }
    }


}

