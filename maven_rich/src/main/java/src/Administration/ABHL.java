package src.Administration;

import src.map.BareLand;
import src.map.LandForm;
import src.map.RichGameMap;
import src.NUMS.SpecialNum;
import src.player.Player;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-28
 * Time: 下午9:45
 * To change this template use File | Settings | File Templates.
 */
public class ABHL {
    String name;
    public static Map landTable;
    Map houseTable;

    public ABHL(RichGameMap map){
        name="Administration Bureau of House and Land";
        landTable=new Hashtable();
        for(int i=0;i< SpecialNum.LANDNUM.getNum();i++)
        {
            LandForm tempBareLand=(LandForm)map.landList.get(i);
            if(tempBareLand.getName().equals("0")) {
                BareLand tempLand=(BareLand)map.landList.get(i);
                landTable.put(tempLand.getLandIndex(), tempLand);

            }
        }

    }



    public static boolean checkIfSold(int landIndex) {
        if(landTable.containsKey(landIndex)){
            BareLand tempLand=(BareLand)landTable.get(landIndex);
             if(tempLand.getOwnerIndex()==0){
                return false ;
            }

        }
        return true;
    }


    public static void sellLandToPlayer(Player player, int landIndex) {
        BareLand tempBareLand=(BareLand)landTable.get(landIndex);
        if(player.getMoney()>=tempBareLand.getPrice()){
        landTable.put(tempBareLand.getLandIndex(),tempBareLand);
        tempBareLand.setColor(player.getColor());
        tempBareLand.setOwner(player) ;
        player.deductMoney(tempBareLand.getPrice());
        System.out.println(player.getName()+">您已经购买到编号为"+tempBareLand.getLandIndex()+"的空地");
        player.takeLands(tempBareLand);
        } else{
            System.out.println("您当前剩余的钱为" + player.getMoney() + "，不足以购买编号为" + tempBareLand.getLandIndex() + "的空地");
        }


    }

    public static void takeLandsFromPlayer(BareLand land) {
        land.setOwner(null);
        land.setColor(null);
        land.setHouseLevel(0);
        land.setDisplayName("0");
        landTable.put(land.getLandIndex(),0);
    }

    public static boolean isOwner(Player player, int landIndex) {
        Player tempPlayer=(Player)landTable.get(landIndex);
        return tempPlayer.getPlayerIndex()==player.getLandIndex();
    }
}
