package src.Gift;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-30
 * Time: 下午4:20
 * To change this template use File | Settings | File Templates.
 */
public class PointCard extends Gift {
    private static int giftIndex;
    private static String name;
    private static int point;

    public PointCard(int giftIndex) {
        this.giftIndex=giftIndex;
        name="点数卡";
        this.point=200;
    }


    public static String getName() {
        return name;
    }

    public static int getGiftIndex() {
        return giftIndex;
    }


    public static int getPoint() {
        return point;
    }
}
