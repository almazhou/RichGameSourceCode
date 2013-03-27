package src.tools;

import src.map.LandForm;
import src.map.RichGameMap;


public class Robot extends OwnedTools {
    private static final int LANDNUM = 70;
    private static final int SEARCHSTEP = 10;
    private static int point;
    private static String name;
    private static int toolIndex;
    private static String displayName;
    public Robot(){
        name="机器娃娃";
        toolIndex=2;
        point =30;
        displayName=" ";
    }
    public static int getToolIndex() {
        return toolIndex;
    }
    public static String getName() {
        return name;
    }
    public static String getDisplayName() {
        return displayName;
    }
    public static int getPoint() {
        return point;
    }

 /*   public void clearBomb(RichGameMap map, int playerIndex) {
        map.clearBomb(playerIndex);

    }   */

    public boolean isWithinRange(int bombIndex,int currentIndex) {
        if(Math.abs(bombIndex-currentIndex)<11){
            return true;
        }  else if(LANDNUM-Math.abs(bombIndex-currentIndex)<11){
            return true;
        }
        return false;

    }
    public void clearBombAndBlock(RichGameMap map, int playerIndex) {
        int startIndex=(playerIndex-SEARCHSTEP+LANDNUM)%LANDNUM;
        int stopIndex=(playerIndex+SEARCHSTEP+LANDNUM)%LANDNUM;
        if(startIndex>stopIndex){
            for(int i=startIndex;i<LANDNUM;i++){
                LandForm tempLandForm=(LandForm)map.landList.get(i);
                tempLandForm.clearBomb(map);
                tempLandForm.clearBlock(map);
            }
            for(int i=0;i<stopIndex;i++){
                LandForm tempLandForm=(LandForm)map.landList.get(i);
                tempLandForm.clearBomb(map);
                tempLandForm.clearBlock(map);
            }

        }
        else{
            for(int i=startIndex;i<stopIndex;i++){
                LandForm tempLandForm=(LandForm)map.landList.get(i);
                tempLandForm.clearBomb(map);
                tempLandForm.clearBlock(map);
            }
        }

    }



}
