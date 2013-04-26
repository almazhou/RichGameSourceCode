package src.Gift;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-30
 * Time: 下午4:10
 * To change this template use File | Settings | File Templates.
 */
public enum Gift {
    MoneyCard("奖金",1,2000),PointCard("点卡",2,200),Mascot("福神",3,5);
    private  int giftIndex;
    private String name;
    private int value;
    private Gift(String name,int giftIndex,int value){
        this.name=name;
        this.giftIndex=giftIndex;
        this.value=value;
    }
    public  String getName() {
        return name;
    }

    public int getGiftIndex() {
        return giftIndex;
    }
    public int getValue() {
        return value;
    }


    public static Gift getGiftByIndex(int giftIndex) {
        if(giftIndex==MoneyCard.getGiftIndex()){
            return MoneyCard;
        }else if(giftIndex==PointCard.getGiftIndex()) {
            return PointCard;
        } else if(giftIndex==Mascot.getGiftIndex()){
            return Mascot;
        }
        return null;
    }
}
