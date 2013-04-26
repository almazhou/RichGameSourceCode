package src.map;

import src.player.Player;

public class Hospital extends LandForm {
    Hospital(int index){
        super("H",index);
    }

    @Override
    public void PassByImpact(Player player) {
        if(player.getTimeInHospital()>0) {
            System.out.println(this.name+">玩家仍然在医院养病！还有"+player.getTimeInHospital()+"天出院！");
            return;
        }

    }


}
