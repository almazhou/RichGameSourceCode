package src.player;

import src.Administration.ABHL;
import src.Game.Game;
import src.Gift.Gift;
import src.NUMS.SpecialHouseIndex;
import src.NUMS.SpecialNum;
import src.map.*;
import src.tools.Tool;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dell
 * Date: 13-1-25
 * Time: 下午4:07
 * To change this template use File | Settings | File Templates.
 */
public class Player {



    private int currentIndex=0;
    private int totalPoint =0;
    private int totalMoney=0;
    private int bombNum=0,blockNum=0,robotNum=0,landNum=0,cottageNum=0,houseNum=0,skyscraperNum=0,bareLandNum=0;
    List bombList, blockList, robotList,landList;

    private int playerIndex;
    private String name;

    private Color color=Color.WHITE;
    private List<Gift> giftList;
    private int timeInHospital=0;
    private int timeInPrison=0;
    private String abbreviation=null;
    public  String commandWord=null;
    private static final int SEARCHSTEP = 10;

    private int freePassNum=0;


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

    public void buyLand(RichGameMap map, int landIndex) {
        if(!ABHL.checkIfSold(map, landIndex)) {
            BareLand tempBareLand=(BareLand)map.landList.get(landIndex);
            ABHL.sellLandToPlayer(this,tempBareLand);
        }
    }

    public void forward(RichGameMap map, int rollingSteps, Game rich) {
        int preIndex=currentIndex;
        if(currentIndex== SpecialHouseIndex.HOSPITAL_INDEX.getHouseIndex()&&timeInHospital>0){
           timeInHospital--;
           System.out.println(this.name+">玩家仍然在医院养病！还有"+timeInHospital+"天出院！");
           return;
       }
        if(currentIndex== SpecialHouseIndex.PRISON_INDEX.getHouseIndex()&&timeInPrison>0){
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
            currentIndex= SpecialHouseIndex.HOSPITAL_INDEX.getHouseIndex();
            timeInHospital= SpecialNum.TIME_IN_HOSPITAL.getNum();
           System.out.println(this.name+">玩家被炸弹炸伤，送进医院！将住院"+timeInHospital+"天!");
            return;
     }  else {
           currentIndex+= rollingSteps;
            }
        if(currentIndex>=SpecialNum.LANDNUM.getNum()){
            currentIndex%=SpecialNum.LANDNUM.getNum();
        }
        map.clearDisplayName(map,currentIndex,rich);
        rich.setDisplayName(playerIndex,map);
        if(rich.isInPrison(playerIndex, map)&&timeInPrison==0){
            timeInPrison= SpecialNum.TIME_IN_PRISON.getNum();
            System.out.println(this.name+">玩家步入监狱！将被监禁"+timeInPrison+"天!");
            return;
        }
        System.out.println("到达编号为"+currentIndex+"的地！");
        if(currentIndex== SpecialHouseIndex.TOOL_HOUSE_INDEX.getHouseIndex()){
            System.out.println(this.name+">欢迎光临道具屋，请选择您所需要的道具(1-3)：");
            ToolHouse toolHouse=(ToolHouse)map.landList.get(currentIndex);
            toolHouse.displayTools();
            String toolIndexInString=Game.getPlayerCommand(this);

            chooseTools(toolIndexInString);
            return;
        }
        if(currentIndex== SpecialHouseIndex.GIFT_HOUSE_INDEX.getHouseIndex()){
            System.out.println(this.name+">欢迎光临礼品屋，请选择一件您喜欢的礼品：");
            GiftHouse giftHouse=new GiftHouse();
            giftHouse.displayGifts();
            String giftIndexInString=Game.getPlayerCommand(this);
            chooseGift(giftIndexInString);
            return;
        }
        if(currentIndex== SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex()){
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
                buyLand(map, 3);
                }
                else if(buyLandOrNot.equalsIgnoreCase("N")) {

                }

            }

        }

        return;
        }
        if(currentIndex== SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex()){
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
        if(toolIndex== Tool.Blockade.getToolIndex()){
            buyBlock();
        } else if(toolIndex== Tool.Robot.getToolIndex()){
            buyRobot();
        } else if(toolIndex== Tool.Bomb.getToolIndex()){
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
            if(i>=SpecialNum.LANDNUM.getNum()) {
                k=i%SpecialNum.LANDNUM.getNum();
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
        System.out.println("炸弹设置成功");
        return true ;
        }
        System.out.println("，无法放置炸弹");
        return false ;
    }

    private int getLandIndexWithOffSet(int offset) {
        int landIndex=currentIndex+offset;
        if(landIndex>=SpecialNum.LANDNUM.getNum()){
            landIndex%=SpecialNum.LANDNUM.getNum();
        }
        if(landIndex<0){
            landIndex=SpecialNum.LANDNUM.getNum()+landIndex;
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
        Tool robot=(Tool) robotList.get(0);
        clearBombAndBlock(map, rich);
        robotList.remove(0);
        robotNum--;
    }

    public void clearBombAndBlock(RichGameMap map, Game rich) {
        int startIndex=(currentIndex-SEARCHSTEP+SpecialNum.LANDNUM.getNum())%SpecialNum.LANDNUM.getNum();
        int stopIndex=(currentIndex+SEARCHSTEP+SpecialNum.LANDNUM.getNum())%SpecialNum.LANDNUM.getNum();
        if(startIndex>stopIndex){
            for(int i=startIndex;i<SpecialNum.LANDNUM.getNum();i++){
                LandForm tempLandForm=(LandForm)map.landList.get(i);
                tempLandForm.clearBomb(map, rich);
                tempLandForm.clearBlock(map, rich);
            }
            for(int i=0;i<stopIndex;i++){
                LandForm tempLandForm=(LandForm)map.landList.get(i);
                tempLandForm.clearBomb(map, rich);
                tempLandForm.clearBlock(map, rich);
            }

        }
        else{
            for(int i=startIndex;i<stopIndex;i++){
                LandForm tempLandForm=(LandForm)map.landList.get(i);
                tempLandForm.clearBomb(map, null);
                tempLandForm.clearBlock(map, null);
            }
        }

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
            if(giftIndex==Gift.MoneyCard.getGiftIndex()){
                giftList.add(Gift.MoneyCard);
                addMoney(Gift.MoneyCard.getValue());
            }else if(giftIndex==Gift.PointCard.getGiftIndex()) {
                giftList.add(Gift.PointCard);
                addPoint(Gift.PointCard.getValue());
            } else if(giftIndex==Gift.Mascot.getGiftIndex()){
                giftList.add(Gift.Mascot);
                freePassNum=Gift.Mascot.getValue();
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
        if(pointIsEnough(Tool.Blockade.getPoint())&&!toolNumberExceed10()) {
        deductPoint(Tool.Blockade.getPoint());
        blockList.add(Tool.Blockade);
        blockNum++;
        }
    }

    private boolean pointIsEnough(int point) {
        return totalPoint>=point;
    }

    public void buyBomb() {
        if(pointIsEnough(Tool.Bomb.getPoint())&&!toolNumberExceed10()){
        deductPoint(Tool.Bomb.getPoint());
        bombList.add(Tool.Bomb);
        bombNum++;
        }
    }

    public void buyRobot() {
        if(pointIsEnough(Tool.Robot.getPoint())&&!toolNumberExceed10()) {
        deductPoint(Tool.Robot.getPoint());
        robotList.add(Tool.Robot);
        robotNum++;
        }
    }

    public void deductMoney(int deductMoney){
        totalMoney-=deductMoney;
    }

    public void addMoney(int addAmount) {
        totalMoney+=addAmount;
    }

    public void sellTools(int toolIndex, Game rich) {
          rich.sellTools(this,toolIndex);
    }

    public void sellBomb() {
        if(bombNum>0){
        addPoint(Tool.Bomb.getPoint());
        bombList.remove(0);
        bombNum--;
        System.out.println(this.name+">"+ Tool.Bomb.getName()+"出售成功！售价为"+ Tool.Bomb.getPoint()+"点！");
        }else {
            System.out.println(this.name+">"+ Tool.Bomb.getName()+"的个数为0，无法出售！");
        }

    }

    public void sellRobot() {
       if(robotNum>0){
        addPoint(Tool.Robot.getPoint());
        robotList.remove(0);
        robotNum--;
        System.out.println(this.name+">"+ Tool.Robot.getName()+"出售成功！售价为"+ Tool.Robot.getPoint()+"点！");
       } else {
           System.out.println(this.name+">"+ Tool.Robot.getName()+"的个数为0，无法出售！");
       }
    }

    public void sellBlock() {
        if(blockNum>0){
        addPoint(Tool.Blockade.getPoint());
        blockList.remove(0);
        blockNum--;
        System.out.println(this.name+">"+ Tool.Blockade.getName()+"出售成功！售价为"+ Tool.Blockade.getPoint()+"点！");
        }else {
            System.out.println(this.name+">"+ Tool.Blockade.getName()+"的个数为0，无法出售！");
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
    public void useMascot() {
        for(Iterator it=giftList.iterator();it.hasNext();){
            Gift tempGift= (Gift) it.next();
            if(tempGift==Gift.Mascot){
                freePassNum--;
                if(freePassNum==0){
                    giftList.remove(tempGift);
                }
            }
        }
    }

    public int getFreePassingNum() {
        return freePassNum;
    }
}