package src.Gift;

public enum Gift {
    MoneyCard("money",1,2000),PointCard("point",2,200),Mascot("freePassNum",3,5);
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
