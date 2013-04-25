package src.map;


import src.Administration.ABHL;
import src.Game.Game;
import src.NUMS.SpecialHouseIndex;
import src.player.Player;

import java.awt.*;

public class BareLand extends LandForm {

    private static final int PRICE_ONE = 200;
    private static final int PRICE_TWO = 500;
    private static final int PRICE_THREE = 300;
    private int level;
    private Player owner=null;


    BareLand(int index){
        super("0",index);
        level=0;

    }

    public  int getPrice() {
        if(landIndex >0&& landIndex <29){
            return PRICE_ONE;
        }else if(landIndex >28&& landIndex <35){
            return PRICE_TWO;
        }else{
            return PRICE_THREE;
        }
    }

    public void upGrade() {
        if(level<3) {
        level=level+1;
        displayName=String.valueOf(level);
        }

    }

    public int getHouseLevel() {
        return level;
    }

    public Color getColor() {
            return color;
    }

    public void setColor(Color color) {
        this.color=color;
    }


    public void setOwner(Player player) {
        owner=player;
    }

    public int getOwnerIndex() {
        if(owner!=null){
        return owner.getPlayerIndex();
        }else {
            return 0;
        }
    }

    public void setHouseLevel(int level) {
        this.level=level;
    }

    @Override
    public void PassByImpact(Player player) {
        if(IsOwner(player)){
            System.out.println(this.name+">是否升级该处地产"+ getPrice()+"元（Y/N）？");
            String upGradeLandOrNot= Game.getPlayerCommand(player);
            if(upGradeLandOrNot.equalsIgnoreCase("Y")) {
                upGradeLand(player);
            }
        }
        else{
            if(ABHL.checkIfSold(landIndex)){
                if(player.hasMascot()) {
                    player.useMascot();
                    return;
                }
                if(owner.getLandIndex()== SpecialHouseIndex.HOSPITAL_INDEX.getHouseIndex()||owner.getLandIndex()==SpecialHouseIndex.PRISON_INDEX.getHouseIndex())
                {
                    return;
                }
               player.payPassingFee(this,owner, null);
        }
            else {
                System.out.println(this.name+">是否购买该处空地"+getPrice()+"元（Y/N）？");
                String buyLandOrNot=Game.getPlayerCommand(player);
                while (!(buyLandOrNot.equalsIgnoreCase("Y")||buyLandOrNot.equalsIgnoreCase("N"))){
                    System.out.println(this.name+">请输入正确的命令（Y/N）");
                    buyLandOrNot=Game.getPlayerCommand(player);
                }
                if(buyLandOrNot.equalsIgnoreCase("Y")) {
                    player.buyLand(landIndex);
                }
                else if(buyLandOrNot.equalsIgnoreCase("N")) {
                       return;
                }

            }

        }


    }

    private void upGradeLand(Player player) {
        if(getHouseLevel()<3){
            if(player.getMoney()>= getPrice()){
                player.deductMoney(getPrice());
                upGrade();
            }else {
                System.out.println(player.getName() + ">您当前剩余的资金为" + player.getMoney() + "元，不足以进行升级！");
            }
        }else if(getHouseLevel()==3){
            System.out.println(player.getName()+">编号为"+landIndex+"的房产已经是摩天楼，不用进行升级");
        }
    }

    private boolean IsOwner(Player player) {
        if(owner!=null){
        return owner.getPlayerIndex()==player.getPlayerIndex();
        }
        else return false;
    }
}
