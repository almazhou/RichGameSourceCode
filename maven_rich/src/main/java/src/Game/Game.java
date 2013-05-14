package src.Game;

import src.Administration.ABHL;
import src.map.BareLand;
import src.map.LandForm;
import src.map.RichGameMap;
import src.player.Player;
import src.tools.Tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Game {
    private  Player []player;
    private  List<Player> playerList=new ArrayList<Player>();
    public static boolean debugFlag=false;
    RichGameMap map;
    public Game(int playerNum,RichGameMap map) {
        this.map = map;
        player=new Player[playerNum];
        for(int i=0;i<playerNum;i++){
            player[i]=new Player(i+1,map);
            playerList.add(player[i]);
        }
        setPlayerMoney("10000");

    }

    public static void main(String []args){
        System.out.println("请循着2~4位不重复玩家，输入编号即可。(1.钱夫人；2.阿土伯；3.孙小美；4.金贝贝)：");
        String playerIndexGroup=Game.getCommand();
        int []playerIndex=Game.checkInput(playerIndexGroup);
        RichGameMap map = new RichGameMap();
        Game rich=new Game(playerIndex,map);
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
            try{
             rich.doCommand(commandString, gamePlayer);
            }catch (Exception e){
             System.out.print("error");
            }

        }
    }

    private void setPlayerMoney(String amount) {
        try{
            if(Integer.parseInt(amount)>=1000&&Integer.parseInt(amount)<=50000){
            for(int i=0;i<playerList.size();i++) {
                Player tempPlayer=playerList.get(i);
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


    private  void doCommand(String commandString, Player gamePlayer) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String newCommandString = commandString.toLowerCase();
        String[] command = newCommandString.split(" ");
        Class[] argTypes = new Class[]{int.class};
        if(command.length>1){
            int commandNum = Integer.parseInt(command[1]);
            Object[] args = new Object[]{commandNum};
            Class currentClass = gamePlayer.getClass();
            Method currentMethod = currentClass.getMethod(command[0],argTypes);
            currentMethod.invoke(gamePlayer,args);
        }else {
            Class currentClass = gamePlayer.getClass();
            Method currentMethod = currentClass.getMethod(command[0]);
            currentMethod.invoke(gamePlayer);
        }


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
                System.exit(0);
            }
        }catch (IOException e){
            System.out.println("Error"+e);
        }
        return s;

    }



    public Game(int []initialPlayer,RichGameMap map){
        this.map = map;
        player=new Player[4];
        for(int i=0;i<4;i++){
            player[i]=new Player(i+1, map);
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
            playerList.get(i).setMoney(money);
        }
    }


    public static void makeBareLand(BareLand landToSell) {
        ABHL.takeLandsFromPlayer(landToSell);
        }

    public static boolean isBareLand(LandForm land) {
        return land.getName().equals("0");
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
                System.exit(0);

            }
            }else if(playerList.size()==1){
                Player winner=playerList.get(0);
                System.out.println("游戏结束，玩家("+winner.getPlayerIndex()+"."+winner.getName()+")赢得比赛");
                System.exit(0);
            }
        }
    }

    public  void playerBankrupt(Player player) {
        playerList.remove(player);
        List tempList=player.getLandList();
        for(int i=0;i<tempList.size();i++){
           BareLand tempLand=(BareLand)tempList.get(i);
           Game.makeBareLand(tempLand);
       }

    }


    public  void buyBlock(Player player) {
                 player.buyTools(Tool.Blockade);
    }

    public void buyBomb(Player player) {
        player.buyTools(Tool.Bomb);
    }

    public void buyRobot(Player player) {
        player.buyTools(Tool.Robot);
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
                System.exit(0);
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
              Player tempPlayer=playerList.get(i);
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
