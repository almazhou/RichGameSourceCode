package src.player;

import src.Administration.ABHL;
import src.Game.Game;
import src.Gift.Gift;
import src.NUMS.SpecialHouseIndex;
import src.NUMS.SpecialNum;
import src.map.BareLand;
import src.map.LandForm;
import src.map.RichGameMap;
import src.tools.Tool;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Player {
    private int currentIndex=0;
    private int totalPoint =0;
    private int totalMoney=0;
    List<BareLand>landList;
    private List<Tool> toolList;

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
        toolList=new ArrayList<Tool>();
        landList=new ArrayList<BareLand>();
        giftList=new ArrayList<Gift>();
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

    public void buyLand(int landIndex) {
        if(!ABHL.checkIfSold(landIndex)) {
            ABHL.sellLandToPlayer(this,landIndex);
        }
    }

    public void forward(RichGameMap map, int rollingSteps, Game rich) {
        if (preCheck(map, rich)) return;
        walk(map, rollingSteps, rich);
        postCheck(map, rich);
    }

    private void postCheck(RichGameMap map, Game rich) {
        if(rich.isInPrison(playerIndex, map)&&timeInPrison==0){
            timeInPrison= SpecialNum.TIME_IN_PRISON.getNum();
            System.out.println(this.name+">玩家步入监狱！将被监禁"+timeInPrison+"天!");
            return;
        }
        System.out.println("到达编号为"+currentIndex+"的地！");

        LandForm tempLand=(LandForm)map.landList.get(currentIndex);
        tempLand.PassByImpact(this);
    }

    private void walk(RichGameMap map, int rollingSteps, Game rich) {
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
     }  else {
           currentIndex+= rollingSteps;
            }
        if(currentIndex>=SpecialNum.LANDNUM.getNum()){
            currentIndex%=SpecialNum.LANDNUM.getNum();
        }
        map.clearDisplayName(map,currentIndex,rich);
        rich.setDisplayName(playerIndex,map);
    }

    private boolean preCheck(RichGameMap map, Game rich) {
        if(rich.isInPrison(playerIndex, map)&&timeInPrison!=0){
            timeInPrison--;
            System.out.println(this.name+">玩家步入监狱！将被监禁"+timeInPrison+"天!");
            return true;
        }
        if(currentIndex== SpecialHouseIndex.HOSPITAL_INDEX.getHouseIndex()&&timeInHospital!=0){
            timeInHospital--;
            System.out.println(this.name+">玩家被炸弹炸伤，送进医院！将住院"+timeInHospital+"天!");
            return true;
        }
        return false;
    }

    public void chooseTools(String toolIndexInString) {
        try{
        int toolIndex=Integer.parseInt(toolIndexInString);
        if(toolIndex>0&&toolIndex<4){
            Tool tool=null;
            if(toolIndex==Tool.Blockade.getToolIndex()){
                tool=Tool.Blockade;
            }else if(toolIndex==Tool.Bomb.getToolIndex()){
                tool=Tool.Bomb;
            } else if(toolIndex==Tool.Robot.getToolIndex()){
                tool=Tool.Robot;
            }
             buyTools(tool);

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

    public void buyTools(Tool tool) {
        if(toolNumberExceed10()){
            return;
        }
        if(pointIsEnough(tool.getPoint())&&!toolNumberExceed10()) {
            deductPoint(tool.getPoint());
            toolList.add(tool);
        }


    }


    private boolean toolNumberExceed10() {
        return toolList.size()>=10;
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
        toolList.remove(Tool.Bomb);
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
        return toolList.contains(Tool.Bomb);
    }

    public boolean setBlock(RichGameMap map, int offset, Game rich) {
        int landIndex = getLandIndexWithOffSet(offset);
        if(canSetBlock(map,landIndex, rich)){
        LandForm tempLandForm=(LandForm)map.landList.get(landIndex);
        tempLandForm.setBlock();
        toolList.remove(Tool.Blockade);
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
        return toolList.contains(Tool.Blockade);
    }

    public void useRobot(RichGameMap map, Game rich) {
        clearBombAndBlock(map, rich);
        toolList.remove(Tool.Robot);
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
                tempLandForm.clearBomb(map, rich);
                tempLandForm.clearBlock(map, rich);
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
        landList.add(tempBareLand);
    }

    public List getLandList() {
          return landList;
    }

    public int getToolNum(Tool tool) {
        int num=0;
        Iterator<Tool> it=toolList.iterator();
        while (it.hasNext()){
            if(it.next().getToolIndex()==tool.getToolIndex()){
               num++;
            }
        }
        return num;
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
    public void sellLand(RichGameMap map, int landIndex, Game rich) {
        rich.sellLands(map, this, landIndex);
        for(int i=0;i<landList.size();i++){
            BareLand land=(BareLand)landList.get(i);
            if(land.getLandIndex()==landIndex){
                landList.remove(i);
            }
        }
    }
    private boolean pointIsEnough(int point) {
        return totalPoint>=point;
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
        Tool tool=Tool.Bomb;
        sellTool(tool);

    }

    private void sellTool(Tool tool) {
        if(getToolNum(tool)>0){
        addPoint(tool.getPoint());
        toolList.remove(tool);
        System.out.println(this.name+">"+ tool.getName()+"出售成功！售价为"+ tool.getPoint()+"点！");
        }else {
            System.out.println(this.name+">"+ tool.getName()+"的个数为0，无法出售！");
        }
    }

    public void sellRobot() {
        Tool tool=Tool.Robot;
        sellTool(tool);
    }

    public void sellBlock() {
        Tool tool=Tool.Blockade;
        sellTool(tool);
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

    public int getBareLandNum() {
        int bareLandNum=0;
        for(int i=0;i<landList.size();i++) {
            BareLand tempLand=landList.get(i);
            if(tempLand.getHouseLevel()==0){
                bareLandNum++;
            }
        }
        return bareLandNum;
    }

    public int getCottageNum() {
        int cottageNum=0;
        for(int i=0;i<landList.size();i++) {
            BareLand tempLand=landList.get(i);
            if(tempLand.getHouseLevel()==1){
                cottageNum++;
            }
        }
        return cottageNum;
    }

    public int getHouseNum() {
        int houseNum=0;
        for(int i=0;i<landList.size();i++) {
            BareLand tempLand=landList.get(i);
            if(tempLand.getHouseLevel()==2){
                houseNum++;
            }
        }
        return houseNum;
    }

    public int getSkyscraperNum() {
        int skyscraperNum=0;
        for(int i=0;i<landList.size();i++) {
            BareLand tempLand=landList.get(i);
            if(tempLand.getHouseLevel()==3){
                skyscraperNum++;
            }
        }
        return skyscraperNum;
    }

    public void setLocation(int Index) {
        this.currentIndex=Index;
    }

    public void setPoint(int amount) {
        totalPoint=amount;
    }
    public void useMascot() {
        System.out.println("福神护身，免费通过！");
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

    public int getTimeInPrison() {
        return timeInPrison;
    }

    public void deductTimeInPrison() {
        timeInPrison--;
    }

    public int getTimeInHospital() {
        return timeInHospital;
    }

    public void deductTimeInHospital() {
        timeInHospital--;
    }


    public void payPassingFee(BareLand passingLand, Player owner, Game rich) {
        double passingFee;
        if(!hasMascot()){
            passingFee=Math.pow(2,passingLand.getHouseLevel())*passingLand.getPrice()*1/2;
            payFeeTo(owner, (int)passingFee, rich);

        }
        }

    public boolean hasMascot() {
        return freePassNum>0;
    }
    public  void payFeeTo(Player toPlayer, int paidMoney, Game rich) {
        int money= getMoney();
        if(money-paidMoney<0){
            System.out.print("玩家"+getName()+"钱不够过路费，只有"+money+"钱,退出游戏");
            rich.playerBankrupt(this);
            return;
        }
        System.out.print("玩家"+getName()+"向玩家"+toPlayer.getName()+"支付"+money+"过路费!");
        deductMoney(paidMoney);
        toPlayer.addMoney(paidMoney);

    }


    public void upGradeLand(RichGameMap map, int landIndex) {
          BareLand tempBareLand=(BareLand)map.landList.get(landIndex);
          if(totalMoney>tempBareLand.getPrice()){
          deductMoney(tempBareLand.getPrice());
          tempBareLand.upGrade();
          }
    }

    public int getTotalToolNum() {
        return toolList.size();
    }

}