package src.NUMS;

/**
 * Created with IntelliJ IDEA.
 * User: dell
 * Date: 13-3-30
 * Time: 下午7:40
 * To change this template use File | Settings | File Templates.
 */
public enum SpecialNum {

   MAX_TOOL_NUM(10),TIME_IN_HOSPITAL(3),TIME_IN_PRISON(2),LANDNUM (70), BARELAND_NUM(58),MINE_NUM(6);
   private int num;
   private SpecialNum(int num){
       this.num=num;
   }

    public int getNum() {
        return num;
    }
}
