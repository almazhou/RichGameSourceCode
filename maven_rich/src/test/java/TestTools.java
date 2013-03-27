import enigma.console.Console;
import enigma.core.Enigma;
import org.junit.Test;
import src.Administration.ABHL;
import src.Game.Game;
import src.map.RichGameMap;
import src.player.Player;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-31
 * Time: 下午9:25
 * To change this template use File | Settings | File Templates.
 */
public class TestTools {
    Game rich=new Game(2);
    Player player= Game.player[0];
    Player player1= Game.player[1];
    RichGameMap map=new RichGameMap();
    ABHL abhl=new ABHL(map);
    private static final Console console;
    static
    {
        console = Enigma.getConsole("Rich");
    }
    @Test
    public void should_not_buying_bomb_when_player_has_10_tools(){
        Game.debugFlag=true;
        for(int i=0;i<10;i++){
            rich.buyBomb(player);
        }
        rich.buyBomb(player);
        int toolNum=player.getRobotNum()+player.getBombNum()+player.getBlockNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
    public void should_not_buying_block_when_player_has_10_tools(){
        Game.debugFlag=true;
        for(int i=0;i<10;i++){
            rich.buyBomb(player);
        }
        rich.buyBlock(player);
        int toolNum=player.getRobotNum()+player.getBombNum()+player.getBlockNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
    public void should_not_buying_robot_when_player_has_10_tools(){
        Game.debugFlag=true;
        for(int i=0;i<10;i++){
            rich.buyBomb(player);
        }
        rich.buyRobot(player);
        int toolNum=player.getRobotNum()+player.getBombNum()+player.getBlockNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
         public void should_player_receive_50_point_by_selling_a_block(){
        Game.debugFlag=true;
        rich.buyBlock(player);
        int point=player.getPoint();
        player.sellTools(1);
        int point1=player.getPoint();
        assertThat(point1-point,is(50));

    }
    @Test
    public void should_player_pay_50_point_by_buying_a_block(){
        Game.debugFlag=true;
        int point=player.getPoint();
        rich.buyBlock(player);
        int point1=player.getPoint();
        assertThat(point-point1,is(50));

    }
    @Test
    public void should_player_receive_30_point_by_selling_a_Robot(){
        Game.debugFlag=true;
        rich.buyRobot(player);
        int point=player.getPoint();
        player.sellTools(2);
        int point1=player.getPoint();
        assertThat(point1-point,is(30));

    }
    @Test
    public void should_player_receive_50_point_by_selling_a_bomb(){
        Game.debugFlag=true;
        rich.buyBomb(player);
        int point=player.getPoint();
        player.sellTools(3);
        int point1=player.getPoint();
        assertThat(point1-point,is(50));

    }
    @Test
    public void should_not_setBomb_when_targetIndex_is_out_of_range(){
        //When
        Game.debugFlag=true;
        rich.buyBomb(player);
        boolean flag=player.setBomb(map,15);
        //Then
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Block_when_it_already_has_block(){
        Game.debugFlag=true;
        rich.buyBomb(player);
        rich.buyBlock(player);
        player.setBlock(map,5);
        boolean flag=player.setBlock(map,5);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Block_when_it_already_has_bomb(){
        Game.debugFlag=true;
        rich.buyBlock(player);
        rich.buyBomb(player);
        player.setBomb(map,5);
        boolean flag=player.setBlock(map,5);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Bomb_when_it_already_has_bomb(){
        Game.debugFlag=true;
        rich.buyBomb(player);
        rich.buyBomb(player);
        player.setBomb(map,5);
        boolean flag=player.setBomb(map,5);
        assertThat(flag,is(false));


    }
    @Test
    public void should_not_set_Bomb_when_it_already_has_block(){
        Game.debugFlag=true;
        rich.buyBomb(player);
        rich.buyBlock(player);
        player.setBlock(map,5);
        boolean flag=player.setBomb(map,5);
        assertThat(flag,is(false));


    }
    @Test
    public void should_not_set_Block_when_any_player_is_in_the_destIndex(){
        // Player player1=new Player(2);
        Game.debugFlag=true;
        Game.setPlayerLocation(0);
        player.forward(map,6);
        player1.forward(map,3);
        rich.buyBlock(player);
        boolean flag=player.setBlock(map,-3);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Bomb_when_any_player_is_in_the_destIndex(){
        // Player player1=new Player(2);
        Game.debugFlag=true;
        Game.setPlayerLocation(0);
        player.forward(map,6);
        player1.forward(map,3);
        rich.buyBomb(player);
        boolean flag=player.setBomb(map,-3);
        assertThat(flag,is(false));
    }
    @Test
    public void should_display_users_abbreviation_when_player_is_on_this_land(){
        Game.debugFlag=true;
        map.displayMap(console);
        player.forward(map,6);
    }
}