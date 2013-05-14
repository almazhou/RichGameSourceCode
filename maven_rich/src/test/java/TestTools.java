import org.junit.Before;
import org.junit.Test;
import src.Administration.ABHL;
import src.Game.Game;
import src.map.RichGameMap;
import src.player.Player;
import src.tools.Tool;

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
        map=new RichGameMap();
        rich=new Game(2,map);
        player= rich.getPlayer(0);
        player1= rich.getPlayer(1);
        abhl=new ABHL(map);

    }


    @Test
    public void should_not_buying_bomb_when_player_has_10_tools(){

        for(int i=0;i<10;i++){
            player.buyTools(Tool.Blockade);
        }
        player.buyTools(Tool.Bomb);
        int toolNum=player.getTotalToolNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
    public void should_not_buying_block_when_player_has_10_tools(){
        for(int i=0;i<10;i++){
            player.buyTools(Tool.Robot);
        }
        player.buyTools(Tool.Blockade);
        int toolNum=player.getTotalToolNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
    public void should_not_buying_robot_when_player_has_10_tools(){
        for(int i=0;i<10;i++){
            player.buyTools(Tool.Robot);
        }
        player.buyTools(Tool.Robot);;
        int toolNum=player.getTotalToolNum();
        //then
        assertThat(toolNum,is(10));
    }
    @Test
         public void should_player_receive_50_point_by_selling_a_block(){
        player.buyTools(Tool.Blockade);
        int point=player.getPoint();
        player.selltool(1);
        int point1=player.getPoint();
        assertThat(point1-point,is(50));

    }
    @Test
    public void should_player_pay_50_point_by_buying_a_block(){
        int point=player.getPoint();
        player.buyTools(Tool.Blockade);
        int point1=player.getPoint();
        assertThat(point-point1,is(50));

    }
    @Test
    public void should_player_receive_30_point_by_selling_a_Robot(){
        player.buyTools(Tool.Robot);
        int point=player.getPoint();
        player.selltool(2);
        int point1=player.getPoint();
        assertThat(point1-point,is(30));

    }
    @Test
    public void should_player_receive_50_point_by_selling_a_bomb(){
        player.buyTools(Tool.Bomb);
        int point=player.getPoint();
        player.selltool(3);
        int point1=player.getPoint();
        assertThat(point1-point,is(50));

    }
    @Test
    public void should_not_setBomb_when_targetIndex_is_out_of_range(){
        //When
        player.buyTools(Tool.Bomb);
        boolean flag=player.bomb(15);
        //Then
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Block_when_it_already_has_block(){
        player.buyTools(Tool.Blockade);
        player.buyTools(Tool.Blockade);
        player.block(5);
        boolean flag=player.block(5);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Block_when_it_already_has_bomb(){
        player.buyTools(Tool.Blockade);
        player.buyTools(Tool.Bomb);
        player.bomb(5);
        boolean flag=player.block(5);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Bomb_when_it_already_has_bomb(){
        player.buyTools(Tool.Bomb);
        player.buyTools(Tool.Bomb);
        player.bomb(5);
        boolean flag=player.bomb(5);
        assertThat(flag,is(false));


    }
    @Test
    public void should_not_set_Bomb_when_it_already_has_block(){
        player.buyTools(Tool.Bomb);
        player.buyTools(Tool.Blockade);
        player.block(5);
        boolean flag=player.bomb(5);
        assertThat(flag,is(false));


    }
    @Test
    public void should_not_set_Block_when_any_player_is_in_the_destIndex(){
        rich.setPlayerLocation(0);
        player.forward(6);
        player1.forward(3);
        player.buyTools(Tool.Blockade);
        boolean flag=player.block(-3);
        assertThat(flag,is(false));
    }
    @Test
    public void should_not_set_Bomb_when_any_player_is_in_the_destIndex(){
        rich.setPlayerLocation(0);
        player.forward(6);
        player1.forward(3);
        player.buyTools(Tool.Bomb);
        boolean flag=player.bomb(-3);
        assertThat(flag,is(false));
    }
    @Test
    public void should_display_users_abbreviation_when_player_is_on_this_land(){
        map.displayMap();
        player.forward(6);
    }
    @Test
    public void should_players_free_Pass_be_4_when_used_one(){
        player.chooseGift("3");
        player1.buyLand(3);
        player.forward(3);
        int result=player.getFreePassingNum() ;
        assertThat(result,is(4));
    }
}
