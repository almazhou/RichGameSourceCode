package src.player;

import src.Administration.ABHL;
import src.Game.Game;
import src.Gift.Mascot;
import src.Gift.MoneyCard;
import src.Gift.PointCard;
import src.map.*;
import src.tools.Blockade;
import src.tools.Bomb;
import src.tools.Robot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dell
 * Date: 13-1-25
 * Time: 下午4:07
 * To change this template use File | Settings | File Templates.
 */
public class Player {
    public static final int TIME_IN_HOSPITAL = 3;
    public static final int TIME_IN_PRISON = 2;
    private int currentIndex=0;
    private int totalPoint =0;
    private int totalMoney=0;
    private int bombNum=0,blockNum=0,robotNum=0,landNum=0,cottageNum=0,houseNum=0,skyscraperNum=0,bareLandNum=0;
    List bombList, blockList, robotList,landList;
    private static final int SEARCHSTEP = 10;
    private static final int LANDNUM = 70;
    //public int landNum;
    private int playerIndex;
    private String name;
    private static final int HOSPITAL_INDEX = 14;
    private Color color=Color.WHITE;
    private List giftList;
    private int timeInHospital=0;
    private int timeInPrison=0;
    private String abbreviation=null;
    public  String commandWord=null;


    public Player(int playerIndex){
        bombList =new ArrayList();
        blockList = new ArrayList();
        robotList =new ArrayList();
        landList=new ArrayList();
        giftList=new ArrayList();
        this.playerIndex=playerIndex;
        totalPoint=10000;
        totalMoney=10000;
        if(playerIndex==1){
            name="钱夫人";
            color=Color.YELLOW;
            abbreviation="Q";
        }else if(playerIndex==2){
            name="阿土伯";
            color=Color.GREEN;
            abbreviation="A";
        } else if(playerIndex==3){
            name="孙小美";
            color=Color.RED;
            abbreviation="S";
        } else if(playerIndex==4){
            name="金贝贝";
            color=Color.BLUE;
            abbreviation="J";
        }

        }

    public void forward(RichGameMap map, int rollingSteps) {
        int preIndex=currentIndex;
        if(Game.inHospital(playerIndex,map)&&timeInHospital>0){
           timeInHospital--;
           System.out.println(this.name+">玩家仍然在医院养病！还有"+timeInHospital+"天出院！");
           return;
       }
        if(Game.isInPrison(playerIndex, map)&&timeInPrison>0){
            timeInPrison--;
            System.out.println(this.name+">玩家在监狱中！还有"+timeInPrison+"天出狱！");
            return;
        }
       if(map.checkBlock(currentIndex, rollingSteps)){
            currentIndex=map.getBlockIndex(currentIndex, rollingSteps);
           System.out.print(this.name+">前方编号为"+currentIndex+"的土地上有路障!");
            map.clearBlock(currentIndex);
        } else if(checkBomb(map, rollingSteps)) {
            int bombIndex=map.getBombIndex(currentIndex, rollingSteps);
           assert(bombIndex>=0);
            map.clearBomb(bombIndex);
            currentIndex=HOSPITAL_INDEX;
            timeInHospital= TIME_IN_HOSPITAL;
           System.out.println(this.name+">玩家被炸弹炸伤，送进医院！将住院"+timeInHospital+"天!");
            return;
     }  else {
           currentIndex+= rollingSteps;
            }
        if(currentIndex>=LANDNUM){
            currentIndex%=LANDNUM;
        }
        Game.clearDisplayName(map,preIndex);
        Game.setDisplayName(playerIndex,map);
        /*if(Game.inHospital(playerIndex, map)&&timeInHospital==0){
            timeInHospital=3;
            return;
        }*/
        if(Game.isInPrison(playerIndex, map)&&timeInPrison==0){
            timeInPrison= TIME_IN_PRISON;
            System.out.println(this.name+">玩家步入监狱！将被监禁"+timeInPrison+"天!");
            return;
        }
        System.out.println("到达编号为"+currentIndex+"的地！");
        if(Game.isInToolHouse(playerIndex, map)){
            System.out.println(this.name+">欢迎光临道具屋，请选择您所需要的道具(1-3)：");
            ToolHouse toolHouse=(ToolHouse)map.landList.get(currentIndex);
            toolHouse.displayTools();
            String toolIndexInString=Game.getPlayerCommand(this);

            chooseTools(toolIndexInString);
            return;
        }
        if(Game.isInGiftHouse(playerIndex, map)){
            System.out.println(this.name+">欢迎光临礼品屋，请选择一件您喜欢的礼品：");
            GiftHouse.displayGifts();
            String giftIndexInString=Game.getPlayerCommand(this);
            chooseGift(giftIndexInString);
            return;
        }
        if(Game.isInMagicHouse(playerIndex, map)){
            return;
        }
        if(Game.isInBareLand(playerIndex, map)) {
        if(Game.isOwner(this,currentIndex)){
            System.out.println(this.name+">是否升级该处地产"+ABHL.getPrice(currentIndex,map)+"元（Y/N）？");
            String upGradeLandOrNot=Game.getPlayerCommand(this);
            if(upGradeLandOrNot.equalsIgnoreCase("Y")) {
            Game.upGradeLand(map,this,currentIndex);
            } else {
                return;
            }
        }
        else{
            if(ABHL.checkIfSold(map,currentIndex)){
            Game.payPassingFee(map, currentIndex, this);
            }
            else {
                System.out.println(this.name+">是否购买该处空地"+ABHL.getPrice(currentIndex,map)+"元（Y/N）？");
                String buyLandOrNot=Game.getPlayerCommand(this);
                boolean flag=buyLandOrNot.equalsIgnoreCase("Y")||buyLandOrNot.equalsIgnoreCase("N");
                while (!(buyLandOrNot.equalsIgnoreCase("Y")||buyLandOrNot.equalsIgnoreCase("N"))){
                    System.out.println(this.name+">请输入正确的命令（Y/N）");
                    buyLandOrNot=Game.getPlayerCommand(this);
                }
                if(buyLandOrNot.equalsIgnoreCase("Y")) {
                Game.buyLand(map,this,currentIndex);
                return;}
                else if(buyLandOrNot.equalsIgnoreCase("N")) {
                return;
                }
                return;
            }

        }

        return;
        }
        if(Game.isInMine(playerIndex, map)){
            Game.doMining(playerIndex);
            return;

        }


    }

    private void chooseTools(String toolIndexInString) {
        try{
        int toolIndex=Integer.parseInt(toolIndexInString);
        if(toolIndex>0&&toolIndex<4){
             Game.buyTools(this, toolIndex);
             return;
        }else {
            System.out.println(this.name+">请输入正确的道具编号（1-3）");
            Game.getPlayerCommand(this);
        }
        }
        catch (NumberFormatException e){
            if(toolIndexInString.equalsIgnoreCase("F")){return;}
            System.out.println(this.name+">您输入了非数字字符："+e);

        }
    }


 /*   public boolean hasMascot() {
        for(int i=0;i<giftList.size();i++){
            Gift tempGift=(Gift)giftList.get(i);
            if(tempGift.getName().equals("福神")){
               return true;
            }
        }
        return false;
    }  */


    public int getLandIndex() {
        return currentIndex;
    }

    public int getPoint(){
        return totalPoint;
    }

    public boolean checkBomb(RichGameMap map,int rollingSteps) {
        for(int i=this.currentIndex;i<=this.currentIndex+rollingSteps;i++) {
            int k=i;
            if(i>=LANDNUM) {
                k=i%LANDNUM;
            }
            LandForm tempLandForm=(LandForm)map.landList.get(k);
            if(tempLandForm.isBombed()){
                return tempLandForm.isBombed();
            }
        }
        return false;
    }

    public boolean setBomb(RichGameMap map, int offset) {
        int landIndex=currentIndex+offset;
        if(landIndex>=LANDNUM){
            landIndex%=LANDNUM;
        }
        if(landIndex<0){
            landIndex=LANDNUM+landIndex;
        }
        if(canSetBomb(map,landIndex)){
        LandForm tempLandForm=(LandForm)map.landList.get(landIndex);
        tempLandForm.setBomb();
        bombNum--;
        bombList.remove(0);
        System.out.println(Bomb.getName()+"设置成功");
        return true ;
        }
        System.out.println("，无法放置炸弹");
        return false ;
    }

    private boolean canSetBomb(RichGameMap map, int landIndex) {
        return map.isWithinRange(landIndex, currentIndex)&&!map.hasOtherTools(landIndex)&&!map.hasPlayer(landIndex)&& hasBomb();
    }

    private boolean hasBomb() {
        if(bombNum==0){
        System.out.print(this.name+">炸弹个数为0");
        }
        return bombNum>0;
    }

    public boolean setBlock(RichGameMap map, int offset) {
        int landIndex=currentIndex+offset;
        if(landIndex>=LANDNUM){
            landIndex%=LANDNUM;
        }
        if(landIndex<0){
            landIndex=LANDNUM+landIndex;
        }
        if(canSetBlock(map,landIndex)){
        LandForm tempLandForm=(LandForm)map.landList.get(landIndex);
        tempLandForm.setBlock();
        blockNum--;
        blockList.remove(0);
        System.out.println("路障设置成功！");
            return true;
        }
        System.out.println("，无法放置路障");
        return false;
    }

    public boolean canSetBlock(RichGameMap map, int index) {
        return map.isWithinRange(index, currentIndex)&& !map.hasOtherTools(index)&&!map.hasPlayer(index)&& hasBlock();
    }

    private boolean hasBlock() {

        if(blockNum>0){
            return true;
        }else {
            System.out.print(this.name+">路障个数为0");
            return false;
        }
    }

    public void useRobot(RichGameMap map) {
        int startIndex=currentIndex-SEARCHSTEP;
        int stopIndex=currentIndex+SEARCHSTEP;
        Robot robot=(Robot) robotList.get(0);
        robot.clearBombAndBlock(map, currentIndex);
        robotList.remove(0);
        robotNum--;
    }

    private int findBomb(RichGameMap map, int startIndex, int stopIndex) {
            return map.findBomb(startIndex,stopIndex);
    }

    public int getMoney() {
        return totalMoney;
    }

    public void setMoney(int money) {
        if(money>=0){
        totalMoney=money;
        }
    }

    public void setColor(Color color) {

        this.color=color;
    }

    public Color getColor() {
        return color;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void takeLands(BareLand tempBareLand) {
        manageLand(tempBareLand);
        landList.add(tempBareLand);
        landNum++;
    }

    public List getLandList() {
          return landList;
    }

    public int getRobotNum() {
        return robotNum;
    }

    public int getBombNum() {
        return bombNum;
    }

    public int getBlockNum() {
        return blockNum;
    }
   public String  getAbbreviation() {
       return abbreviation;
   }

    public void chooseGift(String giftIndexInString) {
        try{
            int giftIndex=Integer.parseInt(giftIndexInString);
            if(giftIndex==MoneyCard.getGiftIndex()){
                giftList.add(new MoneyCard(giftIndex));
                addMoney(MoneyCard.getMoney());
            }else if(giftIndex==PointCard.getGiftIndex()) {
                giftList.add(new PointCard(giftIndex));
                addPoint(PointCard.getPoint());
            } else if(giftIndex==Mascot.getGiftIndex()){
                giftList.add(new Mascot(giftIndex));
            }  else {
               return;
            }
        }
        catch (NumberFormatException e){
            System.out.println(this.name+">您输入了非数字字符："+e);
            return;

        }


    }

    public List getGiftList() {
        return giftList;
    }

    public void sellLand(RichGameMap map, int landIndex) {
        Game.sellLands(map, this,landIndex);
        for(int i=0;i<landList.size();i++){
            BareLand land=(BareLand)landList.get(i);
            if(land.getLandIndex()==landIndex){
                landList.remove(i);
                landNum--;
            }
        }
    }

    public void addBlock(Blockade block) {
        deductPoint(Blockade.getPoint());
        blockList.add(block);
        blockNum++;
    }

    public void addBomb(Bomb bomb) {
        deductPoint(Bomb.getPoint());
        bombList.add(bomb);
        bombNum++;
    }

    public void addRobot(Robot robot) {
        deductPoint(Robot.getPoint());
        robotList.add(robot);
        robotNum++;
    }

    public void deductMoney(int deductMoney){
        totalMoney-=deductMoney;
    }

    public void addMoney(int addAmount) {
        totalMoney+=addAmount;
    }

    public void sellTools(int toolIndex) {
          Game.sellTools(this,toolIndex);
    }

    public void sellBomb() {
        if(bombNum>0){
        addPoint(Bomb.getPoint());
        bombList.remove(0);
        bombNum--;
        System.out.println(this.name+">"+Bomb.getName()+"出售成功！售价为"+Bomb.getPoint()+"点！");
        }else {
            System.out.println(this.name+">"+Bomb.getName()+"的个数为0，无法出售！");
        }

    }

    public void sellRobot() {
       if(robotNum>0){
        addPoint(Robot.getPoint());
        robotList.remove(0);
        robotNum--;
        System.out.println(this.name+">"+Robot.getName()+"出售成功！售价为"+Robot.getPoint()+"点！");
       } else {
           System.out.println(this.name+">"+Robot.getName()+"的个数为0，无法出售！");
       }
    }

    public void sellBlock() {
        if(blockNum>0){
        addPoint(Blockade.getPoint());
        blockList.remove(0);
        blockNum--;
        System.out.println(this.name+">"+Blockade.getName()+"出售成功！售价为"+Blockade.getPoint()+"点！");
        }else {
            System.out.println(this.name+">"+Blockade.getName()+"的个数为0，无法出售！");
        }
    }

    public void addPoint(int addAmount) {
        totalPoint+=addAmount;
    }
    private void deductPoint(int deductAmount){
        totalPoint-=deductAmount;
    }

    public String getName() {
        return name;
    }

    public void query() {


       /* for(int i=0;i<landList.size();i++){
            BareLand tempLand=(BareLand)landList.get(i);
            if(tempLand.getHouseLevel()==0){
                bareLandNum++;
            }else if(tempLand.getHouseLevel()==1){}
               cottageNum++;
        }

    }*/

    }

    public void manageLand(BareLand land) {
        if(land.getHouseLevel()==0) {
            bareLandNum++;
        }else if(land.getHouseLevel()==1){
            cottageNum++;
            bareLandNum--;
        }else if(land.getHouseLevel()==2){
            houseNum++;
            cottageNum--;
        } else if(land.getHouseLevel()==3){
            skyscraperNum++;
            houseNum--;
        }
    }

    public int getBareLandNum() {
        return bareLandNum;
    }

    public int getCottageNum() {
        return cottageNum;
    }

    public int getHouseNum() {
        return houseNum;
    }

    public int getSkyscraperNum() {
        return skyscraperNum;
    }

    public void setLocation(int Index) {
        this.currentIndex=Index;
    }
}