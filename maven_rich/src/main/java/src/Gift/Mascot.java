package src.Gift;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-30
 * Time: 下午4:22
 * To change this template use File | Settings | File Templates.
 */
public class Mascot extends Gift {
    private int freePassNum;
    private static int giftIndex;
    private static String name;

    public Mascot(int giftIndex) {
        this.giftIndex=giftIndex;
        name="福神";
        this.freePassNum=5;
    }
    public static String getName() {
        return name;
    }

    public static int getGiftIndex() {
        return giftIndex;
    }

    public void deduct() {
        freePassNum--;

    }

    public int getFreePassNum() {
        return freePassNum;
    }
}
