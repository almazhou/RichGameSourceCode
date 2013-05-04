package src.map;

import src.NUMS.SpecialHouseIndex;
import src.NUMS.SpecialNum;
import src.player.Player;
import src.tools.Tool;

import java.util.ArrayList;
import java.util.List;
public class RichGameMap {

    public  List landList=new ArrayList();
    LandForm startPoint,toolHouse,magicHouse,giftHouse,hospital,prison;
    LandForm []bareLands;
    LandForm  []mines;
    public RichGameMap(){
        startPoint=new StartPoint();
        landList.add(startPoint);
        bareLands=new BareLand[SpecialNum.BARELAND_NUM.getNum()];
        mines=new Mine[SpecialNum.MINE_NUM.getNum()];
        int k=0;
        for(int i=1;i<= SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex();i++) {
            if(i== SpecialHouseIndex.HOSPITAL_INDEX.getHouseIndex()) {
                hospital=new Hospital(i);
                landList.add(hospital);
            }else if(i== SpecialHouseIndex.TOOL_HOUSE_INDEX.getHouseIndex()){
                toolHouse=new ToolHouse(i);
                landList.add(toolHouse);
            } else if(i== SpecialHouseIndex.GIFT_HOUSE_INDEX.getHouseIndex()){
                giftHouse=new GiftHouse();
                landList.add(giftHouse);

            }else if(i== SpecialHouseIndex.PRISON_INDEX.getHouseIndex()){
                prison=new Prison(i);
                landList.add(prison);
            } else if(i== SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex()){
                magicHouse=new MagicHouse(i);
                landList.add(magicHouse);
            }  else {
                bareLands[k]=new BareLand(i);
                landList.add(bareLands[k]);
                k++;
            }
        }
        for(int i=0;i<SpecialNum.MINE_NUM.getNum();i++){
            mines[i]=new Mine(SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex()+i+1);
            landList.add(mines[i]);

        }
    }
    public void displayMap(){
        for(int j=0;j<=7;j++){
        for(int i=0;i<29;i++){
            if(j==0) {
            LandForm tempLandForm=(LandForm)landList.get(i);
            print(i, tempLandForm);
            }else if(j==7){
            LandForm tempLandForm=(LandForm)landList.get(SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex()-i);
            print(SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex()-i, tempLandForm);
            } else{
            if(i==0){
            LandForm tempLandForm=(LandForm)landList.get(SpecialNum.LANDNUM.getNum()-j);
            print(SpecialNum.LANDNUM.getNum()-j, tempLandForm);
            }else if(i== SpecialHouseIndex.TOOL_HOUSE_INDEX.getHouseIndex()){
            LandForm tempLandForm=(LandForm)landList.get(i+j);
            print(i+j, tempLandForm);
            }else{
            System.out.print(" ");
            }
            }

        }
            System.out.println(" ");
        }

    }

    private void print(int i, LandForm tempLandForm) {

        System.out.print(tempLandForm.displayName);

    }


    public void clearBomb(int bombIndex) {
        LandForm tempLandForm=(LandForm)landList.get(bombIndex);
        assert(tempLandForm.isBombed()==true);
        tempLandForm.clearBomb();

    }


    public void clearBlock(int blockIndex) {
        LandForm tempLandForm=(LandForm)landList.get(blockIndex);
        assert(tempLandForm.isBlocked()==true);
        tempLandForm.clearBlock();
    }

    public boolean isBareLand(LandForm tempLand) {
        return tempLand.getName().equals("0");
    }

    public boolean hasPlayer(int targetIndex) {
        LandForm currentLand = (LandForm)landList.get(targetIndex);
        return !currentLand.getPlayerOnLand().isEmpty();
    }

    public boolean hasOtherTools(int landIndex) {
        LandForm tempLandForm=(LandForm)landList.get(landIndex);
        return tempLandForm.isBlocked()||tempLandForm.isBombed();
    }

    public boolean isWithinRange(int targetIndex, int currentIndex) {
        if(Math.abs(targetIndex-currentIndex)<11){
            return true;
        }  else if(SpecialNum.LANDNUM.getNum()-Math.abs(targetIndex-currentIndex)<11){
            return true;
        }
        System.out.print("超出范围");
        return false;
    }

    public int getBlockIndex(int startIndex, int offSet) {
        for(int i=startIndex;i<=startIndex+offSet;i++) {
            int k=i;
            if(i>=SpecialNum.LANDNUM.getNum()){
                k=i%SpecialNum.LANDNUM.getNum();
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
            if(i>=SpecialNum.LANDNUM.getNum()){
                k=i%SpecialNum.LANDNUM.getNum();
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
            if(i>=SpecialNum.LANDNUM.getNum()){
                k=i%SpecialNum.LANDNUM.getNum();
            }
            LandForm tempLandForm=(LandForm)landList.get(k);
            if(tempLandForm.isBlocked()){
                return tempLandForm.isBlocked();
            }
        }
        return false;
    }

    public void clearDisplayName(RichGameMap map, int landIndex) {
        LandForm tempLand = (LandForm)map.landList.get(landIndex);
        if(!tempLand.getPlayerOnLand().isEmpty()){
            Player tempPlayer = (Player)tempLand.getPlayerOnLand().get(tempLand.getPlayerOnLand().size()-1);
            tempLand.setDisplayName(tempPlayer.getAbbreviation());
        }else if(tempLand.isBlocked()){
            tempLand.setDisplayName(Tool.Blockade.getDisplayName());
        }else if(tempLand.isBombed()){
            tempLand.setDisplayName(Tool.Bomb.getDisplayName());
        }else if(isBareLand(tempLand)){
            BareLand bareLand=(BareLand)map.landList.get(landIndex);
            bareLand.setDisplayName(String.valueOf(bareLand.getHouseLevel()));
        }else {
            tempLand.setDisplayName(tempLand.getName());
        }
    }

}
