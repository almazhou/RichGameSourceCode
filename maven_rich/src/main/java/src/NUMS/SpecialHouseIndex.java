package src.NUMS;

/**
 * Created with IntelliJ IDEA.
 * User: dell
 * Date: 13-3-30
 * Time: 下午7:16
 * To change this template use File | Settings | File Templates.
 */
public enum SpecialHouseIndex {
        TOOL_HOUSE_INDEX(28),GIFT_HOUSE_INDEX(35),MAGIC_HOUSE_INDEX(63),HOSPITAL_INDEX(14), PRISON_INDEX(49),LAST_INDEX(69);
         private int houseIndex;
        private SpecialHouseIndex(int houseIndex){
            this.houseIndex=houseIndex;
        }
    public int getHouseIndex(){
        return houseIndex;
    }

}
