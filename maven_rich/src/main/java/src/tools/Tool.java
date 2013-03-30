package src.tools;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-27
 * Time: 下午8:54
 * To change this template use File | Settings | File Templates.
 */
public enum Tool {
       Blockade("路障",1,50,"#"),Bomb("炸弹",3,50,"@"),Robot("机器娃娃",2,30," ");
    private  String name;
    private  int toolIndex;
    private int point;
    private String displayName;
    private Tool(String name, int toolIndex, int point, String displayName) {
        this.name=name;
        this.toolIndex=toolIndex;
        this.point=point;
        this.displayName=displayName;
    }
    public int getToolIndex() {
        return toolIndex;
    }
    public String getName() {
        return name;
    }
    public String getDisplayName() {
        return displayName;
    }
    public int getPoint() {
        return point;
    }

}
