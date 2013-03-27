import enigma.console.Console;
import enigma.core.Enigma;
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
    public void should_players_pay_for_100_when_passing_a_bareLand_of_others_in_3(){
        rich.buyLand(map, player,3);
        int money0=player1.getMoney();
        map.displayMap(console);
        player1.forward(map,3);
        map.displayMap(console);
        int money=player1.getMoney();
        //then
        assertThat(money0-money,is(100));

    }
    @Test
    public void should_owner_of_land_3_will_receive_100_if_other_player_passing_in_3(){
        rich.buyLand(map, player,3);
        int money0=player.getMoney();
        player1.forward(map,3);
        int money=player.getMoney();
        //then
        assertThat(money-money0,is(100));

    }
    @Test
    public void should_players_pay_for_200_when_passing_a_cottage_of_others_in_3(){
        rich.buyLand(map, player,3);
        rich.upGradeLand(map, player, 3);
        int money0=player1.getMoney();
        player1.forward(map,3);
        int money=player1.getMoney();
        //then
        assertThat(money0-money,is(200));
    }
    @Test
    public void should_owner_of_land_3_will_receive_200_if_other_player_passing_a_cottage(){
        rich.buyLand(map, player,3);
        rich.upGradeLand(map, player, 3);
        int money0=player.getMoney();
        player1.forward(map,3);
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
        player1.forward(map,3);
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
        player1.forward(map,3);
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
        player1.forward(map,3);
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
        player1.forward(map,3);
        int money=player.getMoney();
        //then
        assertThat(money-money0,is(800));
    }
    @Test
    public void should_not_pay_passingFee_when_owner_is_in_hospital_3(){
        //when
        Game.setPlayerLocation(0);
        rich.buyLand(map, player1,10);
        rich.buyBomb(player);
        boolean flag=player.setBomb(map,7);
        assertThat(flag,is(true));
        player1.forward(map,7);
        int money0=player.getMoney();
        player.forward(map,10);
        int money=player.getMoney();
        //then
        assertThat(money0-money,is(0));

    }
    @Test
    public void should_not_pay_passingFee_when_player_has_mascot_and_passing_bareland_of_others_in_40(){
        //when
        Game.debugFlag=true;
        Game.setPlayerLocation(0);
        player.forward(map,35);
        player.chooseGift("3");
        rich.buyLand(map, player1,40);
        int money0=player.getMoney();
        player.forward(map,5);
        int money=player.getMoney();
        assertThat(money0-money,is(0));

    }



}
