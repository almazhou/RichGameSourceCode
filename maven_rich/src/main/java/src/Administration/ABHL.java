package src.Administration;

import src.Game.Game;
import src.map.BareLand;
import src.map.LandForm;
import src.map.RichGameMap;
import src.player.Player;

import java.util.*;

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
        for(int i=0;i<map.getLandNum();i++)
        {
            LandForm tempBareLand=(LandForm)map.landList.get(i);
            if(tempBareLand.getName().equals("0")) {
                BareLand tempLand=(BareLand)map.landList.get(i);
                landTable.put(i, tempLand.getOwnerIndex());

            }
        }
       // houseTable=new Hashtable();

    }



    public static boolean checkIfSold(RichGameMap map, int landIndex) {
        LandForm tempBareLand=(LandForm)map.landList.get(landIndex);
        if(landTable.containsKey(tempBareLand.getLandIndex())){
            if(landTable.get(tempBareLand.getLandIndex()).equals(0)){
                return false ;
            }

        }
        return true;
    }


    public static void sellLandToPlayer(Player player, BareLand tempBareLand) {
        if(player.getMoney()>=tempBareLand.getPrice()){
        landTable.put(tempBareLand.getLandIndex(),player.getPlayerIndex());
        tempBareLand.setColor(player.getColor());
        tempBareLand.setOwnerIndex(player.getPlayerIndex()) ;
        Game.deductMoney(player.getPlayerIndex(), tempBareLand.getPrice());
        player.takeLands(tempBareLand);
        } else{
            System.out.println("您当前剩余的钱为" + player.getMoney() + "，不足以购买编号为" + tempBareLand.getLandIndex() + "的空地");
        }


    }

    public static void takeLandsFromPlayer(BareLand land) {
        land.setOwnerIndex(0);
        land.setColor(null);
        land.setHouseLevel(0);
        land.setDisplayName("0");
        landTable.put(land.getLandIndex(),0);
    }

    public static boolean isOwner(Player player, int landIndex) {
        return landTable.get(landIndex).equals(player.getPlayerIndex());
    }

    public static int getPrice(int landIndex, RichGameMap map) {
       BareLand bareLand=(BareLand)map.landList.get(landIndex);
       return bareLand.getPrice();
    }
}
