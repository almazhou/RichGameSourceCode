package src.map;

import src.Gift.Gift;
import src.Gift.Mascot;
import src.Gift.MoneyCard;
import src.Gift.PointCard;
import src.tools.Blockade;
import src.tools.Bomb;
import src.tools.Robot;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-27
 * Time: 上午11:04
 * To change this template use File | Settings | File Templates.
 */
public class GiftHouse extends LandForm{
    Gift moneyCard=new MoneyCard(1);
    Gift pointCard=new PointCard(2);
    Gift mascot=new Mascot(3);
    public GiftHouse(int index){
        super("G",index);
    }

    @Override
    public int getPrice() {
        return 0;
    }

    public static void displayGifts() {
        System.out.println("礼品    编号");
        System.out.println(MoneyCard.getName()+"    "+MoneyCard.getGiftIndex());
        System.out.println(PointCard.getName()+"  "+PointCard.getGiftIndex());
        System.out.println(Mascot.getName()+"    "+Mascot.getGiftIndex());
    }
}
