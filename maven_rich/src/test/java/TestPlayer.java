import org.junit.Before;
import org.junit.Test;
import src.Administration.ABHL;
import src.Game.Game;
import src.map.RichGameMap;
import src.player.Player;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class TestPlayer {
    //Given
    Game rich;
    Player player;
    RichGameMap map;
    ABHL abhl;
    @Before
    public void setUp(){

        Game.debugFlag=true;
        rich=new Game(2);
        player= rich.getPlayer(0);
        map=new RichGameMap();
        abhl=new ABHL(map);

    }


      @Test
    public void should_forward_by_n_when_rolling_for_n(){
          //when
          player.forward(map, 10, rich);
          int index=player.getLandIndex();
          //then
          assertThat(index,is(10));
    }
    @Test
    public void should_forward_by_k_when_there_is_a_block_after_k_step(){
          //when
        rich.buyBlock(player) ;
        rich.buyBlock(player);
        boolean flag=player.setBlock(map,3);
        assertThat(flag,is(true));
        player.forward(map, 6, rich);
        int index=player.getLandIndex();
        //then
        assertThat(index,is(3));

    }
   @Test
    public void should_not_set_block_when_targetIndex_is_out_of_range(){
        //when
        player.setBlock(map,15);
        player.forward(map,17, rich);
        //then
        assertThat(player.getLandIndex(),is(17));
    }
    @Test
    public void should_not_set_bomb_when_targetIndex_is_out_of_range(){
        //when
        player.setBomb(map,15);
        player.forward(map,17, rich);
        //then
        assertThat(player.getLandIndex(),is(17));
    }

    @Test
    public void should_checkout_a_bomb_when_there_is_a_bomb(){
        rich.buyBomb(player);
        player.setBomb(map,5);
        boolean flag=player.checkBomb(map,10);
        assertThat(flag,is(true));

    }
    @Test
    public void should_checkout_a_block_when_there_is_a_block(){
        rich.buyBlock(player);
        player.setBlock(map,5);
        boolean flag=map.checkBlock(0,5);
        assertThat(flag,is(true));

    }
    @Test
    public void should_be_in_hospital_when_there_is_bomb_in_the_way(){
        rich.buyBomb(player);
        player.setBomb(map,5);
        //boolean flag=player.checkBomb(map,10);
        player.forward(map,10, rich);
        assertThat(player.getLandIndex(),is(14));
    }
    @Test
    public void should_not_forward_again_when_in_hospital(){
        rich.buyBomb(player);
        player.setBomb(map,5);
        //boolean flag=player.checkBomb(map,10);
        player.forward(map,10, rich);
        player.forward(map,5, rich);
        assertThat(player.getLandIndex(),is(14));
    }
    @Test
    public void should_not_forward_for_2_times_when_in_prison(){
        rich.buyBomb(player);
        player.setBomb(map,5);
        //boolean flag=player.checkBomb(map,10);
        player.forward(map,10, rich);
        player.forward(map,5, rich);
        player.forward(map,5, rich);
        assertThat(player.getLandIndex(),is(14));
    }
    @Test
    public void should_not_forward_for_3_times_when_in_hospital(){
        rich.buyBomb(player);
        player.setBomb(map,5);
        //boolean flag=player.checkBomb(map,10);
        player.forward(map,10, rich);
        player.forward(map,5, rich);
        player.forward(map,5, rich);
        player.forward(map,5, rich);
        assertThat(player.getLandIndex(),is(14));
    }
    @Test
    public void should_not_forward_for_2_times_in_prison(){
        //boolean flag=player.checkBomb(map,10);
        player.forward(map,49, rich);
        player.forward(map,3, rich);
        player.forward(map,3, rich);
        assertThat(player.getLandIndex(),is(49));
    }

    @Test
    public void should_be_in_destIndex_when_player_has_a_robot_when_there_is_a_bomb_within_10_steps(){
        player.setBomb(map,5);
        rich.buyRobot(player);
        player.useRobot(map) ;
        player.forward(map,10, rich);
        assertThat(player.getLandIndex(),is(10));
    }
    @Test
    public void should_be_in_destIndex_when_player_has_a_robot_when_there_is_a_block_within_10_steps(){
        //when
        player.setBlock(map,5);
        rich.buyRobot(player);
        player.useRobot(map);
        player.forward(map,10, rich);
        //then
        assertThat(player.getLandIndex(),is(10));
    }
    @Test
    public void should_be_in_the_destIndex_when_there_is_bomb_and_block_in_the_way_but_player_has_robot(){
        //when
        rich.buyBomb(player);
        player.setBlock(map,6);
        player.setBomb(map,8);
        rich.buyRobot(player);
        player.useRobot(map);
        player.forward(map,10, rich);
        //then
        assertThat(player.getLandIndex(),is(10));
    }

    @Test
    public void players_should_get_2000_when_choose_a_moneyCard_at_a_gift_house() {
        int money=player.getMoney();
        player.chooseGift("1");
        int money0=player.getMoney();
        assertThat(money0-money,is(2000));

    }
    @Test
    public void players_should_get_200_point_when_choose_a_pointCard_at_a_gift_house() {
        int money=player.getPoint();
        player.chooseGift("2");
        int money0=player.getPoint();
        assertThat(money0-money,is(200));

    }

}

