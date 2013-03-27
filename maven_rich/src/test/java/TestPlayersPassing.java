
import org.junit.Before;
import org.junit.Test;
import src.Administration.ABHL;
import src.Game.Game;
import src.map.RichGameMap;
import src.player.Player;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-30
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class TestPlayersPassing {
    Game rich;
    Player player,player1;
    RichGameMap map;
    ABHL abhl;
    @Before
    public void setUp(){
        rich=new Game(2);
        player= rich.getPlayer(0);
        player1= rich.getPlayer(1);
        map=new RichGameMap();
        abhl=new ABHL(map);

    }

    @Test
    public void should_players_pay_for_100_when_passing_a_bareLand_of_others_in_3(){
        rich.buyLand(map, player,3);
        int money0=player1.getMoney();
        map.displayMap();
        player1.forward(map,3, rich);
        map.displayMap();
        int money=player1.getMoney();
        //then
        assertThat(money0-money,is(100));

    }
    @Test
    public void should_owner_of_land_3_will_receive_100_if_other_player_passing_in_3(){
        rich.buyLand(map, player,3);
        int money0=player.getMoney();
        player1.forward(map,3, rich);
        int money=player.getMoney();
        //then
        assertThat(money-money0,is(100));

    }
    @Test
    public void should_players_pay_for_200_when_passing_a_cottage_of_others_in_3(){
        rich.buyLand(map, player,3);
        rich.upGradeLand(map, player, 3);
        int money0=player1.getMoney();
        player1.forward(map,3, rich);
        int money=player1.getMoney();
        //then
        assertThat(money0-money,is(200));
    }
    @Test
    public void should_owner_of_land_3_will_receive_200_if_other_player_passing_a_cottage(){
        rich.buyLand(map, player,3);
        rich.upGradeLand(map, player, 3);
        int money0=player.getMoney();
        player1.forward(map,3, rich);
        int money=player.getMoney();
        //then
        assertThat(money-money0,is(200));
    }
    @Test
    public void should_players_pay_for_400_when_passing_a_House_of_others_in_3(){
        rich.buyLand(map, player,3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        int money0=player1.getMoney();
        player1.forward(map,3, rich);
        int money=player1.getMoney();
        //then
        assertThat(money0-money,is(400));
    }
    @Test
    public void should_owner_of_land_3_will_receive_400_if_other_player_passing_a_House(){
        rich.buyLand(map, player,3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        int money0=player.getMoney();
        player1.forward(map,3, rich);
        int money=player.getMoney();
        //then
        assertThat(money-money0,is(400));
    }
    @Test
    public void should_players_pay_for_800_when_passing_a_cottage_of_others_in_3(){
        rich.buyLand(map, player,3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        int money0=player1.getMoney();
        player1.forward(map,3, rich);
        int money=player1.getMoney();
        //then
        assertThat(money0-money,is(800));
    }
    @Test
    public void should_owner_of_land_3_will_receive_800_if_other_player_passing_a_skyscraper(){
        rich.buyLand(map, player,3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        int money0=player.getMoney();
        player1.forward(map,3, rich);
        int money=player.getMoney();
        //then
        assertThat(money-money0,is(800));
    }
    @Test
    public void should_not_pay_passingFee_when_owner_is_in_hospital_3(){
        //when
        rich.setPlayerLocation(0);
        rich.buyLand(map, player1,10);
        rich.buyBomb(player);
        boolean flag=player.setBomb(map,7, rich);
        assertThat(flag,is(true));
        player1.forward(map,7, rich);
        int money0=player.getMoney();
        player.forward(map,10, rich);
        int money=player.getMoney();
        //then
        assertThat(money0-money,is(0));

    }
    @Test
    public void should_not_pay_passingFee_when_player_has_mascot_and_passing_bareland_of_others_in_40(){
        //when
        Game.debugFlag=true;
        rich.setPlayerLocation(0);
        player.forward(map,35, rich);
        player.chooseGift("3");
        rich.buyLand(map, player1,40);
        int money0=player.getMoney();
        player.forward(map,5, rich);
        int money=player.getMoney();
        assertThat(money0-money,is(0));

    }



}
