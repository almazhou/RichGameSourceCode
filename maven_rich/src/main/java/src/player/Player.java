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
import java.util.*;
import java.util.List;

public class Player {
    private int currentIndex=0;
    Map <String,Integer>capital = new HashMap();
    List<BareLand>landList;
    private List<Tool> toolList;
    private int playerIndex;
    private String name;
    RichGameMap map;

    private Color color=Color.WHITE;

    private int timeInHospital=0;
    private int timeInPrison=0;
    private String abbreviation=null;
    public  String commandWord=null;
    private static final int SEARCHSTEP = 10;



    public Player(int playerIndex, RichGameMap map){
        this.map = map;
        toolList=new ArrayList<Tool>();
        landList=new ArrayList<BareLand>();
        this.playerIndex=playerIndex;
        capital.put("money",10000);
        capital.put("point",10000);
        capital.put("freePassNum",0);
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

    public  void query() {
            System.out.println("玩家"+getName()+"的资产信息如下");
            System.out.println("资金："+getMoney()+"元");
            System.out.println("点数："+getPoint()+"点");
            System.out.println("地产：空地"+getBareLandNum()+"处；茅屋"+getCottageNum()+"处；洋房"+getHouseNum()+"处；摩天楼"+getSkyscraperNum()+"处。");
            System.out.println("道具：路障"+getToolNum(Tool.Blockade)+"个；炸弹"+getToolNum(Tool.Bomb)+"个；机器娃娃"+getToolNum(Tool.Robot)+"个");
        }

    public void buyLand(int landIndex) {
        if(!ABHL.checkIfSold(landIndex)) {
            ABHL.sellLandToPlayer(this,landIndex);
        }
    }

    public void forward(int rollingSteps) {
        if (preCheck()) return;
        walk(rollingSteps);
        postCheck();
    }

    private void postCheck() {
        if(map.isInPrison(currentIndex,this)&&timeInPrison==0){
            timeInPrison= SpecialNum.TIME_IN_PRISON.getNum();
            System.out.println(this.name+">玩家步入监狱！将被监禁"+timeInPrison+"天!");
            return;
        }
        System.out.println("到达编号为"+currentIndex+"的地！");

        LandForm tempLand=(LandForm)map.landList.get(currentIndex);
        tempLand.addPlayer(this);
        tempLand.PassByImpact(this);
    }

    private void walk(int rollingSteps) {
        if(map.checkBlock(currentIndex, rollingSteps)){
            currentIndex=map.getBlockIndex(currentIndex, rollingSteps);
           System.out.print(this.name+">前方编号为"+currentIndex+"的土地上有路障!");
            map.clearBlock(currentIndex);
        } else if(checkBomb(rollingSteps)) {
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
        map.clearDisplayName(map,currentIndex);
        map.setDisplayName(this,currentIndex);
    }

    private boolean preCheck( ) {
        if(map.isInPrison(currentIndex, this)&&timeInPrison!=0){
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
        return capital.get("point");
    }

    public boolean checkBomb(int rollingSteps) {
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

    public boolean bomb(int offset) {
        return setTools(offset,Tool.Bomb);
    }

    private boolean setTools(int offset, Tool tool) {
        int landIndex = getLandIndexWithOffSet(offset);
        if(canSetTools(landIndex)){
        LandForm tempLandForm=(LandForm)map.landList.get(landIndex);
        if(!toolList.isEmpty()) {
            tempLandForm.setTool(tool);
            toolList.remove(tool);
            System.out.println("炸弹设置成功");
            return true ;
        }

        }
        System.out.println("，无法放置炸弹");
        return false ;
    }

    public boolean block(int offset) {
        return setTools(offset,Tool.Blockade);
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

    private boolean canSetTools(int landIndex) {
        return map.isWithinRange(landIndex, currentIndex) && !map.hasOtherTools(landIndex) && !map.hasPlayer(landIndex);
    }

    public void robot() {
        if(getToolNum(Tool.Robot)>0) {
        clearBombAndBlock();
        toolList.remove(Tool.Robot);
        }
    }

    public void clearBombAndBlock() {
        int startIndex=getLandIndexWithOffSet(-SEARCHSTEP);
        for(int i=startIndex;i<startIndex+2*SEARCHSTEP;i++) {
            int tempIndex=i%SpecialNum.LANDNUM.getNum();
            LandForm tempLandForm=(LandForm)map.landList.get(tempIndex);
            tempLandForm.clearBlock();
            tempLandForm.clearBomb();
            map.clearDisplayName(map, tempLandForm.getLandIndex());
        }
    }

    public int getMoney() {
        return capital.get("money");
    }

    public void setMoney(int money) {
        capital.put("money",money);
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
            Gift gift=Gift.getGiftByIndex(giftIndex);
            if(gift !=null) {
                takeGift(gift);
            }
        }
        catch (NumberFormatException e){
            System.out.println(this.name+">您输入了非数字字符："+e);


        }


    }

    private void takeGift(Gift gift) {
        capital.put(gift.getName(),capital.get(gift.getName())+gift.getValue());
    }

    public void sell(int landIndex) {
        sellLands(landIndex);
        for(int i=0;i<landList.size();i++){
            BareLand land=landList.get(i);
            if(land.getLandIndex()==landIndex){
                landList.remove(i);
            }
        }
    }
    private boolean pointIsEnough(int point) {
        return capital.get("point")>=point;
    }


    public void deductMoney(int deductMoney){
        int totalMoney = capital.get("money");
        capital.put("money",totalMoney-deductMoney);
    }

    public void addMoney(int addAmount) {
        int totalMoney = capital.get("money");
        capital.put("money",totalMoney+addAmount);
    }

    public void selltool(int toolIndex) {
        Tool tool=null;
        if(toolIndex== Tool.Blockade.getToolIndex()){
            tool=Tool.Blockade;
        }else if(toolIndex== Tool.Robot.getToolIndex()){
            tool=Tool.Robot;
        } else if(toolIndex== Tool.Bomb.getToolIndex()){
            tool=Tool.Bomb;
        }else{
            System.out.println("输入出错，道具编号为1-3！");
            return;
        }
        if(tool!=null){
        sellToolsInName(tool);
        }
    }
    public void sellToolsInName(Tool tool) {
        if(getToolNum(tool)>0){
        addPoint(tool.getPoint());
        toolList.remove(tool);
        System.out.println(this.name+">"+ tool.getName()+"出售成功！售价为"+ tool.getPoint()+"点！");
        }else {
            System.out.println(this.name+">"+ tool.getName()+"的个数为0，无法出售！");
        }
    }

    public void addPoint(int addAmount) {
        capital.put("point",capital.get("point")+addAmount);
    }
    private void deductPoint(int deductAmount){
        capital.put("point",capital.get("point")-deductAmount);
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
        capital.put("point",amount);
    }
    public void useMascot() {
        System.out.println("福神护身，免费通过！");
        if(getFreePassingNum()!=0){
            capital.put("freePassNum", capital.get("freePassNum") - 1);
        }

    }

    public int getFreePassingNum() {
        return capital.get("freePassNum");
    }

    public int getTimeInPrison() {
        return timeInPrison;
    }

    public int getTimeInHospital() {
        return timeInHospital;
    }
    public void payPassingFee(BareLand passingLand, Player owner, Game rich) {
        double passingFee;
        if(!hasMascot()){
            passingFee=Math.pow(2,passingLand.getHouseLevel())*passingLand.getPrice()*1/2;
            payFeeTo(owner, (int)passingFee, rich);
        }
        }

    public boolean hasMascot() {
        return getFreePassingNum()>0;
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


    public void upGradeLand(int landIndex) {
          BareLand tempBareLand=(BareLand)map.landList.get(landIndex);
          if(getMoney()>tempBareLand.getPrice()){
          deductMoney(tempBareLand.getPrice());
          tempBareLand.upGrade();
          }
    }

    public int getTotalToolNum() {
        return toolList.size();
    }
    public void roll() {
        int rollingStep=(int)Math.ceil(Math.random() * 6);
        System.out.print("向前前进" + rollingStep + "步，");
        forward(rollingStep);
        map.displayMap();

    }
   public void quit() {
        System.out.println("强制结束游戏");
        System.exit(0);
    }

    public void help() {
        System.out.println("roll    掷骰子命令，行走1~6步。步数由随即算法产生。   \n" +
                "block n    玩家拥有路障后，可将路障放置到离当前位置前后10步的距离，任一玩家经过路障，都将被拦截。该道具一次有效。n指定与当前位置的相对距离，范围 [-10~10]，负数表示后方。\n" +
                "bomb n    可将路障放置到离当前位置前后10步的距离，任一玩家j 经过在该位置，将被炸伤，送往医院，住院三天。n指定与当前位置的相对距离，范围 [-10~10],负数表示后方。\n" +
                "robot    使用该道具，可清扫前方路面上10步以内的其它道具，如炸弹、路障。\n" +
                "sell x     出售自己的房产，x 地图上的绝对位置，即地产的编号。\n" +
                "selltool x    出售道具，x 道具编号\n" +
                "query     显示自家资产信息   \n" +
                "help     查看命令帮助   \n" +
                "quit     强制退出");
    }
    public void sellLands(int landIndex) {
        LandForm tempLand=(LandForm)map.landList.get(landIndex);
        if(Game.isBareLand(tempLand)){
        BareLand landToSell=(BareLand)map.landList.get(landIndex);
        if(ABHL.isOwner(this, landIndex)){
        int paidMoney=landToSell.getPrice()*2*(landToSell.getHouseLevel()+1);
        System.out.println(getName()+">成功出售编号为" + tempLand.getLandIndex() + "的地产!售价为"+paidMoney);
        addMoney(paidMoney);
        Game.makeBareLand(landToSell);
        }else{
            System.out.println("玩家不是编号为" + tempLand.getLandIndex() + "的地产的主人!");
        }
        }else {
            System.out.println("编号为"+tempLand.getLandIndex()+"的土地为非空地，无法出售!");
        }
    }
}