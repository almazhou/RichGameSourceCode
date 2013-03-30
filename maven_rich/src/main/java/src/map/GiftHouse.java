package src.map;

import src.Gift.Gift;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-27
 * Time: 上午11:04
 * To change this template use File | Settings | File Templates.
 */
public class GiftHouse extends LandForm{
    Gift moneyCard=Gift.MoneyCard;
    Gift pointCard=Gift.PointCard;
    Gift mascot=Gift.Mascot;
    public GiftHouse(){
        super("G",35);
    }

    @Override
    public int getPrice() {
        return 0;
    }

    public void displayGifts() {
        System.out.println("礼品    编号");
        System.out.println(moneyCard.getName()+"    "+moneyCard.getGiftIndex());
        System.out.println(pointCard.getName()+"  "+pointCard.getGiftIndex());
        System.out.println(mascot.getName()+"    "+mascot.getGiftIndex());
    }
}
