package src.map;


import java.awt.*;

public class BareLand extends LandForm{

    private static final int PRICE_ONE = 200;
    private static final int PRICE_TWO = 500;
    private static final int PRICE_THREE = 300;
    private int level;
    //private Color color=Color.WHITE;
    private int ownerIndex =0;



    BareLand(int index){
        super("0",index);
        level=0;

    }

    public  int getPrice() {
        if(landIndex >0&& landIndex <29){
            return PRICE_ONE;
        }else if(landIndex >28&& landIndex <35){
            return PRICE_TWO;
        }else{
            return PRICE_THREE;
        }
    }

    public void upGrade() {
        level=level+1;
        assert (level<4);
        displayName=String.valueOf(level);

    }

    public int getHouseLevel() {
        return level;
    }

    public Color getColor() {
            return color;
    }

    public void setColor(Color color) {
        this.color=color;
    }


    public void setOwnerIndex(int playerIndex) {
        ownerIndex =playerIndex;
    }

    public int getOwnerIndex() {
        return ownerIndex;
    }

    public void setHouseLevel(int level) {
        this.level=level;
    }
}
