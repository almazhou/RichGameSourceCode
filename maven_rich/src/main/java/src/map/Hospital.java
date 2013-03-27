package src.map;

import src.map.LandForm;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-27
 * Time: 上午10:31
 * To change this template use File | Settings | File Templates.
 */
public class Hospital extends LandForm {
    Hospital(int index){
        super("H",index);
    }

    @Override
    public int getPrice() {
        return 0;
    }
}
