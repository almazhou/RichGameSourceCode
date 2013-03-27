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
    private static final int LANDNUM = 70;
    public static final int TOOL_HOUSE_INDEX = 28;
    public static final int GIFT_HOUSE_INDEX = 35;
    public static final int MAGIC_HOUSE_INDEX = 63;
    public static final int HOSPITAL_INDEX = 14;
    public static final int PRISON_INDEX = 49;

    private int currentIndex=0;
    private int totalPoint =0;
    private int totalMoney=0;
    private int bombNum=0,blockNum=0,robotNum=0,landNum=0,cottageNum=0,houseNum=0,skyscraperNum=0,bareLandNum=0;
    List bombList, blockList, robotList,landList;

    private int playerIndex;
    private String name;

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

    public void forward(RichGameMap map, int rollingSteps, Game rich) {
        int preIndex=currentIndex;
        if(currentIndex==HOSPITAL_INDEX&&timeInHospital>0){
           timeInHospital--;
           System.out.println(this.name+">玩家仍然在医院养病！还有"+timeInHospital+"天出院！");
           return;
       }
        if(currentIndex==PRISON_INDEX&&timeInPrison>0){
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
        rich.clearDisplayName(map,preIndex);
        rich.setDisplayName(playerIndex,map);
        if(rich.isInPrison(playerIndex, map)&&timeInPrison==0){
            timeInPrison= TIME_IN_PRISON;
            System.out.println(this.name+">玩家步入监狱！将被监禁"+timeInPrison+"天!");
            return;
        }
        System.out.println("到达编号为"+currentIndex+"的地！");
        if(currentIndex==TOOL_HOUSE_INDEX){
            System.out.println(this.name+">欢迎光临道具屋，请选择您所需要的道具(1-3)：");
            ToolHouse toolHouse=(ToolHouse)map.landList.get(currentIndex);
            toolHouse.displayTools();
            String toolIndexInString=Game.getPlayerCommand(this);

            chooseTools(toolIndexInString);
            return;
        }
        if(currentIndex==GIFT_HOUSE_INDEX){
            System.out.println(this.name+">欢迎光临礼品屋，请选择一件您喜欢的礼品：");
            GiftHouse.displayGifts();
            String giftIndexInString=Game.getPlayerCommand(this);
            chooseGift(giftIndexInString);
            return;
        }
        if(currentIndex==MAGIC_HOUSE_INDEX){
            return;
        }
        if(rich.isInBareLand(playerIndex, map)) {
        if(rich.isOwner(this,currentIndex)){
            System.out.println(this.name+">是否升级该处地产"+ABHL.getPrice(currentIndex,map)+"元（Y/N）？");
            String upGradeLandOrNot=Game.getPlayerCommand(this);
            if(upGradeLandOrNot.equalsIgnoreCase("Y")) {
            rich.upGradeLand(map, this, currentIndex);
            } else {
                return;
            }
        }
        else{
            if(ABHL.checkIfSold(map,currentIndex)){
            rich.payPassingFee(map, currentIndex, this);
            }
            else {
                System.out.println(this.name+">是否购买该处空地"+ABHL.getPrice(currentIndex,map)+"元（Y/N）？");
                String buyLandOrNot=Game.getPlayerCommand(this);
                while (!(buyLandOrNot.equalsIgnoreCase("Y")||buyLandOrNot.equalsIgnoreCase("N"))){
                    System.out.println(this.name+">请输入正确的命令（Y/N）");
                    buyLandOrNot=Game.getPlayerCommand(this);
                }
                if(buyLandOrNot.equalsIgnoreCase("Y")) {
                Game.buyLand(map,this,currentIndex);
                }
                else if(buyLandOrNot.equalsIgnoreCase("N")) {

                }

            }

        }

        return;
        }
        if(currentIndex==MAGIC_HOUSE_INDEX){
            rich.doMining(playerIndex);


        }


    }

    private void chooseTools(String toolIndexInString) {
        try{
        int toolIndex=Integer.parseInt(toolIndexInString);
        if(toolIndex>0&&toolIndex<4){
             buyTools(toolIndex);

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

    private void buyTools(int toolIndex) {
        if(toolNumberExceed10()){
            return;
        }
        if(toolIndex==Blockade.getToolIndex()){
            buyBlock();
        } else if(toolIndex==Robot.getToolIndex()){
            buyRobot();
        } else if(toolIndex==Bomb.getToolIndex()){
            buyBomb();
        }
    }

    private boolean toolNumberExceed10() {
        return robotNum+blockNum+bombNum>=10;
    }

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

    public boolean setBomb(RichGameMap map, int offset, Game rich) {
        int landIndex = getLandIndexWithOffSet(offset);
        if(canSetBomb(map,landIndex, rich)){
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

    private int getLandIndexWithOffSet(int offset) {
        int landIndex=currentIndex+offset;
        if(landIndex>=LANDNUM){
            landIndex%=LANDNUM;
        }
        if(landIndex<0){
            landIndex=LANDNUM+landIndex;
        }
        return landIndex;
    }

    private boolean canSetBomb(RichGameMap map, int landIndex, Game rich) {
        return map.isWithinRange(landIndex, currentIndex)&&!map.hasOtherTools(landIndex)&&!map.hasPlayer(landIndex, rich)&& hasBomb();
    }

    private boolean hasBomb() {
        if(bombNum==0){
        System.out.print(this.name+">炸弹个数为0");
        }
        return bombNum>0;
    }

    public boolean setBlock(RichGameMap map, int offset, Game rich) {
        int landIndex = getLandIndexWithOffSet(offset);
        if(canSetBlock(map,landIndex, rich)){
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

    public boolean canSetBlock(RichGameMap map, int index, Game rich) {
        return map.isWithinRange(index, currentIndex)&& !map.hasOtherTools(index)&&!map.hasPlayer(index, rich)&& hasBlock();
    }

    private boolean hasBlock() {

        if(blockNum>0){
            return true;
        }else {
            System.out.print(this.name+">路障个数为0");
            return false;
        }
    }

    public void useRobot(RichGameMap map, Game rich) {
        Robot robot=(Robot) robotList.get(0);
        robot.clearBombAndBlock(map, currentIndex, rich);
        robotList.remove(0);
        robotNum--;
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
            }
        }
        catch (NumberFormatException e){
            System.out.println(this.name+">您输入了非数字字符："+e);


        }


    }

    public List getGiftList() {
        return giftList;
    }

    public void sellLand(RichGameMap map, int landIndex, Game rich) {
        rich.sellLands(map, this, landIndex);
        for(int i=0;i<landList.size();i++){
            BareLand land=(BareLand)landList.get(i);
            if(land.getLandIndex()==landIndex){
                landList.remove(i);
                landNum--;
            }
        }
    }

    public void buyBlock() {
        if(pointIsEnough(Blockade.getPoint())&&!toolNumberExceed10()) {
        deductPoint(Blockade.getPoint());
        blockList.add(new Blockade());
        blockNum++;
        }
    }

    private boolean pointIsEnough(int point) {
        return totalPoint>=point;
    }

    public void buyBomb() {
        if(pointIsEnough(Bomb.getPoint())&&!toolNumberExceed10()){
        deductPoint(Bomb.getPoint());
        bombList.add(new Bomb());
        bombNum++;
        }
    }

    public void buyRobot() {
        if(pointIsEnough(Robot.getPoint())&&!toolNumberExceed10()) {
        deductPoint(Robot.getPoint());
        robotList.add(new Robot());
        robotNum++;
        }
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

    public void setPoint(int amount) {
        totalPoint=amount;
    }
}