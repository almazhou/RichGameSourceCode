package src.map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-27
 * Time: 上午10:39
 * To change this template use File | Settings | File Templates.
 */
public class Mine extends LandForm{
    Mine(int index){
        super("$",index);
    }


    public static int getPoint(int landIndex) {
        if(landIndex==64){
            return 20;
        }else if(landIndex==65){
            return 80;
        }else if(landIndex==66){
            return 100;
        }else if(landIndex==67){
            return 40;
        }else if(landIndex==68) {
            return 80;
        } else if(landIndex==69){
            return 60;
        }
        return 0;
    }
}
