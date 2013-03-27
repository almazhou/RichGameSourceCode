package src.tools;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-27
 * Time: 下午8:46
 * To change this template use File | Settings | File Templates.
 */
public class Blockade extends OwnedTools {
    public static final int LANDNUM = 70;
    private static int point;
    private static String name;
    private static int toolIndex;
    private static String displayName;

    public Blockade(){
        name="路障";
        toolIndex=1;
        point =50;
        displayName="#";
    }
    public static int getToolIndex() {
        return toolIndex;
    }
    public static String getName() {
        return name;
    }
    public static String getDisplayName() {
        return displayName;
    }
    public static int getPoint() {
        return point;
    }




}
