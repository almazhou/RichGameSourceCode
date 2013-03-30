package src.Game;

import src.Administration.ABHL;
import src.Gift.Gift;
import src.NUMS.SpecialHouseIndex;
import src.NUMS.SpecialNum;
import src.map.*;
import src.player.Player;
import src.tools.Tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


public class Game {
    private  Player []player;
    private  List<Player> playerList=new ArrayList<Player>();
    public static boolean debugFlag=false;
    public Game(int playerNum) {
        player=new Player[playerNum];
        for(int i=0;i<playerNum;i++){
            player[i]=new Player(i+1);
            playerList.add(player[i]);
        }
        setPlayerMoney("10000");

    }

    public static void main(String []args){
        System.out.println("请循着2~4位不重复玩家，输入编号即可。(1.钱夫人；2.阿土伯；3.孙小美；4.金贝贝)：");
        String playerIndexGroup=Game.getCommand();
        int []playerIndex=Game.checkInput(playerIndexGroup);
        Game rich=new Game(playerIndex);
        RichGameMap map=new RichGameMap();
        System.out.println("设置玩家初始资金，范围1000~50000,(默认10000):");
        String moneyAmount=Game.getCommand();
        rich.setPlayerMoney(moneyAmount);
        map.displayMap();
        ABHL abhl=new ABHL(map);
        String commandString;
        int k=0;
        List<Player> playerList=rich.getPlayerList();
        while (playerList.size()>1){
            if(k>=playerList.size()){
                k=0;
            }
            Player gamePlayer=playerList.get(k++);
            System.out.println(gamePlayer.getName() + ">玩家可以使用执行道具，卖房产，查询操作,或者执行roll操作向前进");
            commandString=Game.getPlayerCommand(gamePlayer);
            commandString = rich.analyzeCommand(map, commandString, gamePlayer);
            if(commandString.equalsIgnoreCase("roll")) {
                int rollingStep=Game.roll();
                System.out.print("向前前进" + rollingStep + "步，");
                gamePlayer.forward(map,rollingStep, null);
                map.displayMap();
            }
        }
    }


    private void setPlayerMoney(String amount) {
        try{
            if(Integer.parseInt(amount)>=1000&&Integer.parseInt(amount)<=50000){
            for(int i=0;i<playerList.size();i++) {
                Player tempPlayer=(Player)playerList.get(i);
                tempPlayer.setMoney(Integer.parseInt(amount));
            }
            }else{
                System.out.println("输入有误，请输入1000~50000的正整数");
                amount=Game.getCommand();
                setPlayerMoney(amount);
            }
        }catch (NumberFormatException e){
            System.out.println("输入格式有误，请输入1000~50000的正整数");
            amount=Game.getCommand();
            setPlayerMoney(amount);
        }

    }


    private  String analyzeCommand(RichGameMap map, String commandString, Player gamePlayer) {
        String blockN="[bB][lL][oO][cC][kK] -?[0-9]*0?";
        String bombN="[bB][oO][mM][bB] -?[0-9]*0?";
        String robot="robot";
        String sellToolX="[sS][eE][lL][lL][tT][oO][oO][lL] [1-3]";
        String sellN="[sS][eE][lL][lL] [1-9]*[0-9]";
        String query="query";
        String help="help";
        String quit="quit";
        String roll="roll";
        while (!commandString.equalsIgnoreCase(roll)){
        if(commandString.matches(blockN)){
            int n=getN(commandString, "block");
            setBlock(gamePlayer, map, n);
        } else if(commandString.matches(bombN)){
            int n= getN(commandString, "bomb");
            setBomb(gamePlayer, map, n);
        }else if(commandString.equalsIgnoreCase(robot)){
            useRobot(gamePlayer, map);
        }else if(commandString.matches(sellN)) {
            int n= getN(commandString, "sell");
            sellLands(map, gamePlayer, n);
        }else if(commandString.matches(sellToolX)){
            int n= getN(commandString, "sellTool");
            sellTools(gamePlayer, n);
        } else if(commandString.equalsIgnoreCase(query)){
           query(gamePlayer);
        }else if(commandString.equalsIgnoreCase(help)){
            help();
        }else if(commandString.equalsIgnoreCase(quit)){
            quit();
        }
            commandString=getPlayerCommand(gamePlayer);
        }
        return commandString;
    }

    private static void quit() {
        System.out.println("强制结束游戏");
        System.exit(0);
    }

    private static void help() {
        System.out.println("roll    掷骰子命令，行走1~6步。步数由随即算法产生。   \n" +
                "block n    玩家拥有路障后，可将路障放置到离当前位置前后10步的距离，任一玩家经过路障，都将被拦截。该道具一次有效。n指定与当前位置的相对距离，范围 [-10~10]，负数表示后方。\n" +
                "bomb n    可将路障放置到离当前位置前后10步的距离，任一玩家j 经过在该位置，将被炸伤，送往医院，住院三天。n指定与当前位置的相对距离，范围 [-10~10],负数表示后方。\n" +
                "robot    使用该道具，可清扫前方路面上10步以内的其它道具，如炸弹、路障。\n" +
                "sell x     出售自己的房产，x 地图上的绝对位置，即地产的编号。\n" +
                "sellTool x    出售道具，x 道具编号\n" +
                "query     显示自家资产信息   \n" +
                "help     查看命令帮助   \n" +
                "quit     强制退出");
    }

    private static void query(Player player) {
        System.out.println("玩家"+player.getName()+"的资产信息如下");
        System.out.println("资金："+player.getMoney()+"元");
        System.out.println("点数："+player.getPoint()+"点");
        System.out.println("地产：空地"+player.getBareLandNum()+"处；茅屋"+player.getCottageNum()+"处；洋房"+player.getHouseNum()+"处；摩天楼"+player.getSkyscraperNum()+"处。");
        System.out.println("道具：路障"+player.getBlockNum()+"个；炸弹"+player.getBombNum()+"个；机器娃娃"+player.getRobotNum()+"个");
    }

    private  void useRobot(Player player, RichGameMap map) {
        if(player.getRobotNum()>0){
            player.useRobot(map, null);
            System.out.println("成功使用"+ Tool.Robot.getName());
        }else{
            System.out.println("机器娃娃的个数为0，无法设置！");
        }
    }

    private static void setBomb(Player player, RichGameMap map, int offset) {
            player.setBomb(map,offset, null);
    }

    private static void setBlock(Player player, RichGameMap map, int offset) {
        player.setBlock(map,offset, null);

    }

    private static int getN(String command, String inputWord) {
        StringTokenizer analyzeWord=new StringTokenizer(command," ");
        int n=-20;
        if(analyzeWord.nextToken().equalsIgnoreCase(inputWord)){
           n=Integer.parseInt(analyzeWord.nextToken());
        }
        return n;
    }

    private static int[] checkInput(String inputString) {
       boolean flag=false;
       int temp;
       char []inputChars=inputString.toCharArray();
        int index[]=new int[inputChars.length];
        String regex="[1234]";
       while(!flag){
          try{
              temp=Integer.parseInt(inputString);
              flag=true;

              for(int i=0;i<inputChars.length;i++){
                  String str=new String(inputChars,i,1);
                  if(str.matches(regex)) {
                  index[i]=Integer.parseInt(str);
                  }
                  else{
                      System.out.println("输入超出范围，请输入2~4位不重复玩家，输入编号即可。(1.钱夫人；2.阿土伯；3.孙小美；4.金贝贝)：");
                      String newCommand=getCommand();
                      return checkInput(newCommand);
                  }
              }


          }catch (NumberFormatException e){
            System.out.println("您输入非法字符");
            System.out.println("请输入2~4位不重复玩家，输入编号即可。(1.钱夫人；2.阿土伯；3.孙小美；4.金贝贝)：");
            String newCommand=getCommand();
            return checkInput(newCommand);
          }
       }
        return index;

    }

    private static String getCommand() {
        String s="";
        try{
            BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
            s=in.readLine();
            if(s.equalsIgnoreCase("quit")) {
               quit();
            }
        }catch (IOException e){
            System.out.println("Error"+e);
        }
        return s;

    }

    public static int roll() {
        return (int)Math.ceil(Math.random() * 6);

    }

    public Game(int []initialPlayer){
        player=new Player[4];
        for(int i=0;i<4;i++){
            player[i]=new Player(i+1);
        }
        for(int j=0;j<initialPlayer.length;j++){
            playerList.add(player[initialPlayer[j]-1]);
        }
        setPlayerMoney("10000");
        setPlayerPoint(0);
    }

    private void setPlayerPoint(int amount) {
        for(int i=0;i<playerList.size();i++){
            Player tempPlayer=(Player)playerList.get(i);
            tempPlayer.setPoint(amount);
        }
    }

    public  void InitMoney(int money) {
        for(int i=0;i<player.length;i++){
            player[i].setMoney(money);
        }
    }
    public  void payFeeTo(int fromPlayer, int toPlayer, int paidMoney) {
        int money= player[fromPlayer-1].getMoney();
        if(money-paidMoney<0){
            System.out.print("玩家"+fromPlayer+"钱不够过路费，只有"+money+"钱,退出游戏");
            playerBankrupt(player[fromPlayer - 1]);
            return;
        }
        System.out.print("玩家"+player[fromPlayer-1].getName()+"向玩家"+player[toPlayer-1].getName()+"支付"+money+"过路费!");
        deductMoney(fromPlayer,paidMoney);
        addMoney(toPlayer,paidMoney);

    }

    private  void addMoney(int playerIndex, int paidMoney) {
        player[playerIndex-1].addMoney(paidMoney);
    }

    public void payPassingFee(RichGameMap map, int currentIndex, Player player) {
        LandForm tempLand=(LandForm)map.landList.get(player.getLandIndex());
        double passingFee;
        if(map.isBareLand(tempLand)){
            BareLand passingLand=(BareLand)map.landList.get(player.getLandIndex());
            if(ABHL.checkIfSold(map, currentIndex)){
                int ownerIndex=passingLand.getOwnerIndex();
                if(inHospital(ownerIndex, map)|| isInPrison(ownerIndex, map)){
                    if(hasMascot(player)){
                        player.useMascot();
                    }
                    return;

                }
                if(!hasMascot(player)){
                    passingFee=Math.pow(2,passingLand.getHouseLevel())*passingLand.getPrice()*1/2;
                    payFeeTo(player.getPlayerIndex(), ownerIndex, (int)passingFee);

                }else {
                    player.useMascot();
                    System.out.println("福神附身，可免过路费");

                }
            }

        }

    }



    private static boolean hasMascot(Player player) {
        List tempGiftList=player.getGiftList();
        for(Iterator it=tempGiftList.iterator();it.hasNext();){
            Gift tempGift=(Gift)it.next();
            if(tempGift==Gift.Mascot){
                return true;
            }
        }
        return false;
    }

    public void sellLands(RichGameMap map, Player player, int landIndex) {
        LandForm tempLand=(LandForm)map.landList.get(landIndex);
        if(isBareLand(tempLand)){
        BareLand landToSell=(BareLand)map.landList.get(landIndex);
        if(isOwner(player, landIndex)){
        int paidMoney=landToSell.getPrice()*2*(landToSell.getHouseLevel()+1);
        System.out.println(player.getName()+">成功出售编号为" + tempLand.getLandIndex() + "的地产!售价为"+paidMoney);
        addMoney(player.getPlayerIndex(),paidMoney);
        makeBareLand(landToSell);
        }else{
            System.out.println("玩家不是编号为" + tempLand.getLandIndex() + "的地产的主人!");
        }
        }else {
            System.out.println("编号为"+tempLand.getLandIndex()+"的土地为非空地，无法出售!");
        }
    }

    private static void makeBareLand(BareLand landToSell) {
        ABHL.takeLandsFromPlayer(landToSell);
        }

    public  void upGradeLand(RichGameMap map, Player player, int landIndex) {
        LandForm tempLandToUpGrade=(LandForm)map.landList.get(landIndex);
        if(isBareLand(tempLandToUpGrade)){
        BareLand landToUpGrade=(BareLand)map.landList.get(landIndex);
        if(isOwner(player, landIndex)){
        if(landToUpGrade.getHouseLevel()<3){
            if(player.getMoney()>= landToUpGrade.getPrice()){
            deductMoney(player.getPlayerIndex(),landToUpGrade.getPrice());
            player.manageLand(landToUpGrade);
            landToUpGrade.upGrade();
            }else {
                System.out.println(player.getName() + ">您当前剩余的资金为" + player.getMoney() + "元，不足以进行升级！");
            }
        }else if(landToUpGrade.getHouseLevel()==3){
            System.out.println(player.getName()+">编号为"+landToUpGrade.getLandIndex()+"的房产已经是摩天楼，不用进行升级");
        }
        }
        }
    }

    private static boolean isBareLand(LandForm land) {
        return land.getName().equals("0");
    }

    public static boolean isOwner(Player player, int landIndex) {

        return ABHL.isOwner(player,landIndex);
    }

    public void deductMoney(int playerIndex, int deductMoney) {
        Player tempPlayer=player[playerIndex-1];
        if(tempPlayer.getMoney()>=deductMoney){
            tempPlayer.deductMoney(deductMoney);
        }
        System.out.println(tempPlayer.getName() + ">还剩" + tempPlayer.getMoney() + "钱!");
        if(tempPlayer.getMoney()<=0){
            if(playerList.size()>1){
            playerBankrupt(tempPlayer);
            if(playerList.size()==1){
                Player winner=playerList.get(0);
                System.out.println("游戏结束，玩家("+winner.getPlayerIndex()+"."+winner.getName()+")赢得比赛");
                quit();

            }
            }else if(playerList.size()==1){
                Player winner=playerList.get(0);
                System.out.println("游戏结束，玩家("+winner.getPlayerIndex()+"."+winner.getName()+")赢得比赛");
                quit();
            }
        }
    }

    private void playerBankrupt(Player player) {
        playerList.remove(player);
        List tempList=player.getLandList();
        for(int i=0;i<tempList.size();i++){
           BareLand tempLand=(BareLand)tempList.get(i);
           Game.makeBareLand(tempLand);
       }

    }


    private boolean canBuyTools(Player player, int toolIndex) {
        return moneyIsEnough(player,toolIndex)&&tooManyTools(player);

    }

    private static boolean tooManyTools(Player player) {
        if(player.getBlockNum()+player.getBombNum()+player.getRobotNum()< SpecialNum.MAX_TOOL_NUM.getNum()){
            return true;
        }
        return false;
    }

    private  boolean moneyIsEnough(Player player, int toolIndex) {
        if(player.getPoint() - Tool.Blockade.getPoint() >=0&&toolIndex== Tool.Blockade.getToolIndex()){
            return true;
        }
        if(player.getPoint() - Tool.Robot.getPoint() >=0&&toolIndex== Tool.Robot.getToolIndex()){
            return true;
        }
        if(player.getPoint() - Tool.Bomb.getPoint() >=0&&toolIndex== Tool.Bomb.getToolIndex()){
            return true;
        }
        System.out.println("您当前剩余的点数为" + player.getPoint() + ",不足以购买编号为" + toolIndex + "的道具");
        return false;
    }


    public  void sellTools(Player player, int toolIndex) {
        if(toolIndex== Tool.Blockade.getToolIndex()){
            player.sellBlock();
        }else if(toolIndex== Tool.Robot.getToolIndex()){
            player.sellRobot();
        } else if(toolIndex== Tool.Bomb.getToolIndex()){
            player.sellBomb();
        }else{
            System.out.println("输入出错，道具编号为1-3！");
        }
    }

    public boolean isInToolHouse(int playerIndex, RichGameMap map) {
        if(player[playerIndex-1].getLandIndex()== SpecialHouseIndex.TOOL_HOUSE_INDEX.getHouseIndex()){
            setDisplayName(playerIndex,map);
            return true;
        }
        return false;
    }
    public boolean isInGiftHouse(int playerIndex, RichGameMap map) {
        if(player[playerIndex-1].getLandIndex()== SpecialHouseIndex.GIFT_HOUSE_INDEX.getHouseIndex()){
            setDisplayName(playerIndex,map);
            return true;
        }
        return false;
    }
    public boolean isInMagicHouse(int playerIndex, RichGameMap map) {
        if(player[playerIndex-1].getLandIndex()== SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex()){
            setDisplayName(playerIndex,map);
            return true;
        }
        return false;
    }

    public boolean inHospital(int playerIndex, RichGameMap map) {
        if(player[playerIndex-1].getLandIndex()== SpecialHouseIndex.HOSPITAL_INDEX.getHouseIndex()){
            setDisplayName(playerIndex, map);
            return true;
        }
        return false;
    }

    public  void setDisplayName(int playerIndex, RichGameMap map) {
        LandForm tempLandForm=(LandForm)map.landList.get(player[playerIndex - 1].getLandIndex());
        tempLandForm.setDisplayName(player[playerIndex - 1].getAbbreviation());
    }

    public boolean isInPrison(int playerIndex, RichGameMap map) {
        if(player[playerIndex-1].getLandIndex()== SpecialHouseIndex.PRISON_INDEX.getHouseIndex()){
            setDisplayName(playerIndex, map);
            return true;
        }
        return false;
    }


    public boolean isInBareLand(int playerIndex, RichGameMap map) {
        if(isInGiftHouse(playerIndex, map)||inHospital(playerIndex, map)|| isInMagicHouse(playerIndex, map)|| isInPrison(playerIndex, map)|| isInToolHouse(playerIndex, map)|| isInMine(playerIndex, map)){
        return false;
        }
        setDisplayName(playerIndex,map);
        return true;
    }

    public boolean isInMine(int playerIndex, RichGameMap map) {
        if(player[playerIndex-1].getLandIndex()> SpecialHouseIndex.MAGIC_HOUSE_INDEX.getHouseIndex()&&player[playerIndex-1].getLandIndex()<= SpecialHouseIndex.LAST_INDEX.getHouseIndex()){
            setDisplayName(playerIndex,map);
            return true;
        }
        return false;
    }

    public  void doMining(int playerIndex) {
         int landIndex=player[playerIndex-1].getLandIndex();
         int point=Mine.getPoint(landIndex);
        System.out.println("进入矿地，收获"+point+"点");
         addPoint(playerIndex, point);
    }

    private  void addPoint(int playerIndex, int addAmount) {
        player[playerIndex-1].addPoint(addAmount);
    }


    public  void buyBlock(Player player) {
                 player.buyBlock();
    }

    public void buyBomb(Player player) {
        player.buyBomb();
    }

    public void buyRobot(Player player) {
        player.buyRobot();
    }

    public static String getPlayerCommand(Player player) {
        if(Game.debugFlag==true){
            return "N";
        }
        String s="";
        try{
            BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
            System.out.println("请输入命令");
            s=in.readLine();
            if(s.equalsIgnoreCase("quit")) {
                quit();
            }
            player.commandWord=s;
            s=null;
        }catch (IOException e){
          System.out.println("Error"+e);
        }
        return player.commandWord;
    }



    public  void setPlayerLocation(int landIndex) {
          for (int i=0;i<playerList.size();i++) {
              Player tempPlayer=(Player)playerList.get(i);
              tempPlayer.setLocation(landIndex);
          }
    }

    public  List<Player> getPlayerList() {
        return playerList;
    }

    public  Player getPlayer(int index) {
        for(Iterator<Player> it=playerList.iterator();it.hasNext();){
            Player tempPlayer=it.next();
            if(tempPlayer.getPlayerIndex()==index+1){
                return tempPlayer;
            }
        }
        return null;

    }
}
