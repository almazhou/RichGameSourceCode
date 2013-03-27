package src.Gift;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-30
 * Time: 下午4:17
 * To change this template use File | Settings | File Templates.
 */
public class MoneyCard extends Gift {
    private static int money;
    private static int giftIndex;
    private static String name;
    public MoneyCard(int giftIndex) {
        this.giftIndex=giftIndex;
        name="奖金";
        this.money=2000;
    }

    public static int getMoney() {
        return money;
    }
    public static String getName() {
        return name;
    }

    public static int getGiftIndex() {
        return giftIndex;
    }
}
