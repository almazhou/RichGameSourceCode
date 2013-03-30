import org.junit.Before;
import org.junit.Test;
import src.Administration.ABHL;
import src.Game.Game;
import src.map.BareLand;
import src.map.RichGameMap;
import src.player.Player;

import java.awt.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class TestLands {
    Game rich;
    Player player,player1;
    RichGameMap map;
    ABHL abhl;
    @Before
    public void setUp(){
        rich=new Game(2);
        player= rich.getPlayer(0);
        player1=rich.getPlayer(1);
        map=new RichGameMap();
        abhl=new ABHL(map);

    }

    @Test
    public void should_the_price_of_bareLand_in_6_is_200(){
        //when
        rich.InitMoney(300);
        BareLand tempBareLand=(BareLand)map.landList.get(6);
        int price=tempBareLand.getPrice();
        //then
        assertThat(price,is(200));
    }
    @Test
    public void should_the_price_of_bareLand_in_34_is_500(){
        //when
        BareLand tempBareLand=(BareLand)map.landList.get(34);
        int price=tempBareLand.getPrice();
        //then
        assertThat(price,is(500));
    }
    @Test
    public void should_the_price_of_bareLand_in_40_is_300(){
        //when
        BareLand tempBareLand=(BareLand)map.landList.get(40);
        int price=tempBareLand.getPrice();
        //then
        assertThat(price,is(300));
    }
    @Test
    public void player_can_buy_bareLand_2_when_it_is_not_sold(){
        //when
        player.buyLand(map, 2);

        //then
        assertThat(player.getLandList().size(),is(1));
    }
    @Test
    public void land_will_be_red_if_it_is_bought_by_player1(){
        //when
        player.buyLand(map, 2);
        //String color=boughtLand.getColor();
    }


    @Test
    public void player_will_cost_200_for_upGrade_its_land_in_3(){
        player.buyLand(map, 3);
        int money0=player.getMoney();
        rich.upGradeLand(map, player, 3);
        int money=player.getMoney();
        money=money0-money;
        //then
        assertThat(money,is(200));
    }
    @Test
    public void player_will_cost_500_for_upGrade_its_land_in_30(){

        player.buyLand(map, 30);
        int money0=player.getMoney();
        rich.upGradeLand(map, player, 30);
        int money=player.getMoney();
        money=money0-money;
        //then
        assertThat(money,is(500));
    }
    @Test
    public void player_will_cost_300_for_upGrade_its_land_in_40(){

        player.buyLand(map, 40);
        int money0=player.getMoney();
        rich.upGradeLand(map, player, 40);
        int money=player.getMoney();
        money=money0-money;
        //then
        assertThat(money,is(300));
    }
    @Test
    public void player_can_upgrade_its_land_to_cottage(){
        player.buyLand(map, 3);
        rich.upGradeLand(map, player, 3);
        BareLand land=(BareLand)map.landList.get(3);
        int level=land.getHouseLevel();
        //then
        assertThat(level,is(1));

    }
    @Test
    public void player_can_upgrade_its_cottage_to_House(){
        player.buyLand(map, 3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        BareLand land=(BareLand)map.landList.get(3);
        int level=land.getHouseLevel();
        //then
        assertThat(level,is(2));

    }
    @Test
    public void player_can_upgrade_its_House_to_skyscraper(){
        player.buyLand(map, 3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        BareLand land=(BareLand)map.landList.get(3);
        int level=land.getHouseLevel();
        //then
        assertThat(level,is(3));

    }
    @Test
    public void player_can_not_upgrade_when_the_land_is_a_skyscraper(){
        player.buyLand(map, 3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        rich.upGradeLand(map, player, 3);
        BareLand land=(BareLand)map.landList.get(3);
        int level=land.getHouseLevel();
        //then
        assertThat(level,is(3));

    }
    @Test
    public void player_can_not_upgrade_when_money_is_not_enough(){
        rich.InitMoney(300);
        int money=player.getMoney();
        player.setColor(Color.RED);
        player.buyLand(map, 3);
        rich.upGradeLand(map, player, 3);
        BareLand land=(BareLand)map.landList.get(3);
        int level=land.getHouseLevel();
        //then
        assertThat(level,is(0));

    }
    @Test
    public void player_receive_400_buy_selling_bareLands_in_3(){
        player.buyLand(map, 3);
        int money=player.getMoney();
        player.sellLand(map, 3, rich);
        int money1= player.getMoney();
        assertThat(money1-money,is(400));
    }
    @Test
    public void player_receive_800_buy_selling_cottage_in_3(){
        player.buyLand(map, 3);
        rich.upGradeLand(map,player,3);
        int money=player.getMoney();
        player.sellLand(map, 3, rich);
        int money1= player.getMoney();
        assertThat(money1-money,is(800));
    }
    @Test
    public void player_receive_1200_buy_selling_house_in_3(){
        player.buyLand(map, 3);
        rich.upGradeLand(map,player,3);
        rich.upGradeLand(map,player,3);
        int money=player.getMoney();
        player.sellLand(map, 3, rich);
        int money1= player.getMoney();
        assertThat(money1-money,is(1200));
    }
    @Test
    public void player_receive_1600_buy_selling_house_in_3(){
        player.buyLand(map, 3);
        rich.upGradeLand(map,player,3);
        rich.upGradeLand(map,player,3);
        rich.upGradeLand(map,player,3);
        int money=player.getMoney();
        player.sellLand(map, 3, rich);
        int money1= player.getMoney();
        assertThat(money1-money,is(1600));
    }
    @Test
    public void cottage_would_be_bareLand_after_selling(){
        player.buyLand(map, 3);
        rich.upGradeLand(map,player,3);
        int money=player.getMoney();
        player.sellLand(map, 3, rich);
        int money1= player.getMoney();
        BareLand bareLand=(BareLand)map.landList.get(3);
        int level=bareLand.getHouseLevel();
        assertThat(level,is(0));
    }
    @Test
    public void house_would_be_bareLand_after_selling(){
        player.buyLand(map, 3);
        rich.upGradeLand(map,player,3);
        rich.upGradeLand(map,player,3);
        int money=player.getMoney();
        player.sellLand(map, 3, rich);
        int money1= player.getMoney();
        BareLand bareLand=(BareLand)map.landList.get(3);
        int level=bareLand.getHouseLevel();
        assertThat(level,is(0));
    }
    @Test
    public void skyscraper_would_be_bareLand_after_selling(){
        player.buyLand(map, 3);
        rich.upGradeLand(map,player,3);
        rich.upGradeLand(map,player,3);
        rich.upGradeLand(map,player,3);
        int money=player.getMoney();
        player.sellLand(map, 3, rich);
        int money1= player.getMoney();
        BareLand bareLand=(BareLand)map.landList.get(3);
        int level=bareLand.getHouseLevel();
        assertThat(level,is(0));
    }









}
