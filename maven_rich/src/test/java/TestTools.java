import org.junit.Before;
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
    Game rich;
    Player player,player1;
    RichGameMap map;
    ABHL abhl;
    @Before
    public void setUp(){
        Game.debugFlag=true;
        rich=new Game(2);
        player= rich.getPlayer(0);
        player1= rich.getPlayer(1);
        map=new RichGameMap();
        abhl=new ABHL(map);

    }


    @Test
    public void should_not_buying_bomb_when_player_has_10_tools(){

        for(int i=0;i<10;i++){
            player.buyBomb();
        }
        player.buyBomb();
        int toolNum=player.getRobotNum()+player.getBombNum()+player.getBlockNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
    public void should_not_buying_block_when_player_has_10_tools(){
        for(int i=0;i<10;i++){
            player.buyBomb();
        }
        player.buyBlock();
        int toolNum=player.getRobotNum()+player.getBombNum()+player.getBlockNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
    public void should_not_buying_robot_when_player_has_10_tools(){
        for(int i=0;i<10;i++){
            player.buyBomb();
        }
        player.buyRobot();
        int toolNum=player.getRobotNum()+player.getBombNum()+player.getBlockNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
         public void should_player_receive_50_point_by_selling_a_block(){
        player.buyBlock();
        int point=player.getPoint();
        player.sellTools(1, rich);
        int point1=player.getPoint();
        assertThat(point1-point,is(50));

    }
    @Test
    public void should_player_pay_50_point_by_buying_a_block(){
        int point=player.getPoint();
        player.buyBlock();
        int point1=player.getPoint();
        assertThat(point-point1,is(50));

    }
    @Test
    public void should_player_receive_30_point_by_selling_a_Robot(){
        player.buyRobot();
        int point=player.getPoint();
        player.sellTools(2, rich);
        int point1=player.getPoint();
        assertThat(point1-point,is(30));

    }
    @Test
    public void should_player_receive_50_point_by_selling_a_bomb(){
        player.buyBomb();
        int point=player.getPoint();
        player.sellTools(3, rich);
        int point1=player.getPoint();
        assertThat(point1-point,is(50));

    }
    @Test
    public void should_not_setBomb_when_targetIndex_is_out_of_range(){
        //When
       player.buyBomb();
        boolean flag=player.setBomb(map,15, rich);
        //Then
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Block_when_it_already_has_block(){
       player.buyBomb();
        player.buyBlock();
        player.setBlock(map,5, rich);
        boolean flag=player.setBlock(map,5, rich);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Block_when_it_already_has_bomb(){
        player.buyBlock();
        player.buyBomb();
        player.setBomb(map,5, rich);
        boolean flag=player.setBlock(map,5, rich);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Bomb_when_it_already_has_bomb(){
        player.buyBomb();
        player.buyBomb();
        player.setBomb(map,5, rich);
        boolean flag=player.setBomb(map,5, rich);
        assertThat(flag,is(false));


    }
    @Test
    public void should_not_set_Bomb_when_it_already_has_block(){
        player.buyBomb();
        player.buyBlock();
        player.setBlock(map,5, rich);
        boolean flag=player.setBomb(map,5, rich);
        assertThat(flag,is(false));


    }
    @Test
    public void should_not_set_Block_when_any_player_is_in_the_destIndex(){
       rich.setPlayerLocation(0);
        player.forward(map,6, rich);
        player1.forward(map,3, rich);
        player.buyBlock();
        boolean flag=player.setBlock(map,-3, rich);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Bomb_when_any_player_is_in_the_destIndex(){
        rich.setPlayerLocation(0);
        player.forward(map,6, rich);
        player1.forward(map,3, rich);
        player.buyBomb();
        boolean flag=player.setBomb(map,-3, rich);
        assertThat(flag,is(false));
    }
    @Test
    public void should_display_users_abbreviation_when_player_is_on_this_land(){
        map.displayMap();
        player.forward(map,6, rich);
    }
}
