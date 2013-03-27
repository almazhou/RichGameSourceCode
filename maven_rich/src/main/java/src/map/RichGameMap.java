package src.map;

import enigma.console.Console;
import enigma.console.TextAttributes;
import src.Game.Game;
import src.player.Player;
import src.tools.Blockade;
import src.tools.Bomb;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
public class RichGameMap {
    public static final int BARELAND_NUM = 58;
    public static final int MINE_NUM = 6;
    public static final int MAGIC_HOUSE_INDEX = 63;
    public static final int HOSPITAL_INDEX = 14;
    public static final int TOOL_HOUSE_INDEX = 28;
    public static final int GIFT_HOUSE_INDEX = 35;
    public static final int PRISON_INDEX = 49;
    public  List landList=new ArrayList();
    //LandForm []land;
    LandForm startPoint,toolHouse,magicHouse,giftHouse,hospital,prison;
    LandForm []bareLands;
    LandForm  []mines;
    private static final int LANDNUM = 70;

    public RichGameMap(){
        startPoint=new StartPoint();
        landList.add(startPoint);
        bareLands=new BareLand[BARELAND_NUM];
        mines=new Mine[MINE_NUM];
        int k=0;
        for(int i=1;i<= MAGIC_HOUSE_INDEX;i++) {
            if(i== HOSPITAL_INDEX) {
                hospital=new Hospital(i);
                landList.add(hospital);
            }else if(i== TOOL_HOUSE_INDEX){
                toolHouse=new ToolHouse(i);
                landList.add(toolHouse);
            } else if(i== GIFT_HOUSE_INDEX){
                giftHouse=new GiftHouse(i);
                landList.add(giftHouse);

            }else if(i== PRISON_INDEX){
                prison=new Prison(i);
                landList.add(prison);
            } else if(i==MAGIC_HOUSE_INDEX){
                magicHouse=new MagicHouse(i);
                landList.add(magicHouse);
            }  else {
                bareLands[k]=new BareLand(i);
                landList.add(bareLands[k]);
                k++;
            }
        }
        for(int i=0;i<MINE_NUM;i++){
            mines[i]=new Mine(MAGIC_HOUSE_INDEX+i+1);
            landList.add(mines[i]);

        }
    }
    public void displayMap(Console console){
        for(int j=0;j<=7;j++){
        for(int i=0;i<29;i++){
            if(j==0) {
            LandForm tempLandForm=(LandForm)landList.get(i);
            print(i, tempLandForm,console);
            }else if(j==7){
            LandForm tempLandForm=(LandForm)landList.get(MAGIC_HOUSE_INDEX-i);
            print(MAGIC_HOUSE_INDEX-i, tempLandForm, console);
            } else{
            if(i==0){
            LandForm tempLandForm=(LandForm)landList.get(LANDNUM-j);
            print(LANDNUM-j, tempLandForm, console);
            }else if(i==TOOL_HOUSE_INDEX){
            LandForm tempLandForm=(LandForm)landList.get(i+j);
            print(i+j, tempLandForm, console);
            }else{
            System.out.print(" ");
            }
            }

        }
            System.out.println(" ");
        }

    }

    private void print(int i, LandForm tempLandForm, Console console) {
        Color landColor=tempLandForm.getColor();
        if(landColor==null){ landColor=Color.WHITE; }
        TextAttributes tempAttrs = new TextAttributes(landColor);
        console.setTextAttributes(tempAttrs);

        System.out.print(tempLandForm.displayName);
        /*if(!hasPlayer(i)){
        displayWithoutPlayersOn(i, tempLandForm);
        }else {
            System.out.print(tempLandForm.displayName);
        }*/
    }

    private void displayWithoutPlayersOn(int i, LandForm tempLandForm) {
        if(tempLandForm.blockFlag){
            System.out.print(Blockade.getDisplayName());
        }else if(tempLandForm.bombFlag){
            System.out.print(Bomb.getDisplayName());
        }else if(tempLandForm.getClass()==BareLand.class){
            BareLand tempLand=(BareLand)landList.get(i);
            if(tempLand.getHouseLevel()>0) {
            System.out.print(tempLand.getHouseLevel());
            }else {
                System.out.print(tempLandForm.displayName);
            }
        }else {
            System.out.print(tempLandForm.displayName);
        }
    }


    public int findBomb(int startIndex, int stopIndex) {
        startIndex=(startIndex+LANDNUM)%LANDNUM;
        stopIndex=(stopIndex+LANDNUM)%LANDNUM;
        if(startIndex>stopIndex){
            for(int i=startIndex;i<LANDNUM;i++){
                LandForm tempLandForm=(LandForm)landList.get(i);
                if(tempLandForm.bombFlag){
                    return tempLandForm.landIndex;
                }
            }
            for(int i=0;i<stopIndex;i++){
                LandForm tempLandForm=(LandForm)landList.get(i);
                if(tempLandForm.bombFlag){
                    return tempLandForm.landIndex;
                }
            }

        }
        else{
        for(int i=startIndex;i<stopIndex;i++){
            LandForm tempLandForm=(LandForm)landList.get(i);
            if(tempLandForm.bombFlag){
                return tempLandForm.landIndex;
            }
        }
        }
        return -1;

    }

    public void clearBomb(int bombIndex) {
        LandForm tempLandForm=(LandForm)landList.get(bombIndex);
        assert(tempLandForm.bombFlag==true);
        tempLandForm.bombFlag=false;

    }

    public int getLandNum() {
        return LANDNUM;
    }

    public void clearBlock(int blockIndex) {
        LandForm tempLandForm=(LandForm)landList.get(blockIndex);
        assert(tempLandForm.blockFlag==true);
        tempLandForm.blockFlag=false;
    }

    public boolean isBareLand(LandForm tempLand) {
        return tempLand.getName().equals("0");
    }

    public boolean hasPlayer(int targetIndex) {
        for(int i=0;i< Game.playerList.size();i++)
        {
            Player tempPlayer=(Player)Game.playerList.get(i);
            if(tempPlayer.getLandIndex()==targetIndex){
                //System.out.println("该处有玩家");
                return true;
            }
        }

        return false;
    }

    public boolean hasOtherTools(int landIndex) {
        LandForm tempLandForm=(LandForm)landList.get(landIndex);
        return tempLandForm.isBlocked()||tempLandForm.isBombed();
    }

    public boolean isWithinRange(int targetIndex, int currentIndex) {
        if(Math.abs(targetIndex-currentIndex)<11){
            return true;
        }  else if(LANDNUM-Math.abs(targetIndex-currentIndex)<11){
            return true;
        }
        System.out.print("超出范围");
        return false;
    }

    public int getBlockIndex(int startIndex, int offSet) {
        for(int i=startIndex;i<=startIndex+offSet;i++) {
            int k=i;
            if(i>=LANDNUM){
                k=i%LANDNUM;
            }
            LandForm tempLandForm=(LandForm)landList.get(k);
            if(tempLandForm.isBlocked()){
                return tempLandForm.getLandIndex();
            }
        }
        return -1;
    }

    public int getBombIndex(int startIndex, int offSet) {
        for(int i=startIndex;i<=startIndex+offSet;i++) {
            int k=i;
            if(i>=LANDNUM){
                k=i%LANDNUM;
            }
            LandForm tempLandForm=(LandForm)landList.get(k);
            if(tempLandForm.isBombed()){
                return tempLandForm.getLandIndex();
            }
        }
        return -1;
    }

    public boolean checkBlock(int startIndex, int offSet) {
        for(int i=startIndex;i<=startIndex+offSet;i++) {
            int k=i;
            if(i>=LANDNUM){
                k=i%LANDNUM;
            }
            LandForm tempLandForm=(LandForm)landList.get(k);
            if(tempLandForm.isBlocked()){
                return tempLandForm.isBlocked();
            }
        }
        return false;
    }

    public Player getPlayer(int landIndex) {
        for(int i=0;i< Game.playerList.size();i++)
        {   Player tempPlayer=(Player)Game.playerList.get(i);
            if(tempPlayer.getLandIndex()==landIndex){
                //System.out.println("该处有玩家");
                return tempPlayer;
            }
        }
        return null;
    }
}
