package es.codeurjc.ais.tictactoe;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.reset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import es.codeurjc.ais.tictactoe.TicTacToeGame.CellMarkedValue;
import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;


@RunWith(Parameterized.class)
public class TicTacToeGameTest {
	
	@Parameters
	public static Collection<Object[]> data(){

		List<Movement> player2Wins4 = new ArrayList<Movement>();
		player2Wins4.add(new Movement(0, "_", false, null));
		player2Wins4.add(new Movement(1, "0", false, null));
		player2Wins4.add(new Movement(2, "x", false, null));
		player2Wins4.add(new Movement(4, "0", false, null));
		player2Wins4.add(new Movement(3, "x", false, null));
		player2Wins4.add(new Movement(7, "0", true, Movement.WINPOSITIONS[4]));
		
		List<Movement> player2Wins5 = new ArrayList<Movement>();
		player2Wins5.add(new Movement(0, "x", false, null));
		player2Wins5.add(new Movement(2, "0", false, null));
		player2Wins5.add(new Movement(1, "x", false, null));
		player2Wins5.add(new Movement(5, "0", false, null));
		player2Wins5.add(new Movement(3, "x", false, null));
		player2Wins5.add(new Movement(4, "0", false, null));
		player2Wins5.add(new Movement(7, "x", false, null));
		player2Wins5.add(new Movement(8, "0", true, Movement.WINPOSITIONS[5]));
		
		List<Movement> player1Wins3 = new ArrayList<Movement>();
		player1Wins3.add(new Movement(0, "x", false, null));
		player1Wins3.add(new Movement(2, "0", false, null));
		player1Wins3.add(new Movement(1, "x", false, null));
		player1Wins3.add(new Movement(5, "0", false, null));
		player1Wins3.add(new Movement(3, "x", false, null));
		player1Wins3.add(new Movement(4, "0", false, null));
		player1Wins3.add(new Movement(8, "x", false, null));
		player1Wins3.add(new Movement(7, "0", false, null));
		player1Wins3.add(new Movement(6, "x", true,  Movement.WINPOSITIONS[3]));
		
		List<Movement> player12Draw = new ArrayList<Movement>();
		player12Draw.add(new Movement(0, "x", false, null));
		player12Draw.add(new Movement(8, "0", false, null));
		player12Draw.add(new Movement(5, "x", false, null));
		player12Draw.add(new Movement(3, "0", false, null));
		player12Draw.add(new Movement(6, "x", false, null));
		player12Draw.add(new Movement(2, "0", false, null));
		player12Draw.add(new Movement(4, "x", false, null));
		player12Draw.add(new Movement(7, "0", false, null));
		player12Draw.add(new Movement(1, "x", true,  null));
		
	    Object[][] data = {
	    	{ player2Wins4 },
	    	{ player2Wins5 },
	    	{ player12Draw }
	    };
	    
	    return Arrays.asList(data);
	}
	
	@Parameter(0) public List<Movement> movements;

	
	@Test
	public void TicTacToeGame_Generic_Integration_Test() {
		
		//Given
		TicTacToeGame game = new TicTacToeGame();
		
		Connection c1 = mock(Connection.class);
		Connection c2 = mock(Connection.class);
			
		game.addConnection(c1);
		game.addConnection(c2);
		
		List<Player> players = new CopyOnWriteArrayList<>();
		players.add(new Player(0, "Snake", "x"));
				
		//When
		game.addPlayer(players.get(0));
				
		//Then
		verify(c1).sendEvent(eq(EventType.JOIN_GAME), eq(players));
		verify(c2).sendEvent(eq(EventType.JOIN_GAME), eq(players));
		
		//When
		players.add(new Player(1, "Apple", "o"));
		game.addPlayer(players.get(1));
		
		//Then
		verify(c1, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(players));
		verify(c2, times(2)).sendEvent(eq(EventType.JOIN_GAME), eq(players));	
	
		verify(c1).sendEvent(eq(EventType.SET_TURN), eq(players.get(0)));
		verify(c2).sendEvent(eq(EventType.SET_TURN), eq(players.get(0)));
		
	
		int nextTurn = 1;
	
		for (Movement movement : movements) {
			int thisTurn = nextTurn == 1 ? 0 : 1;
			game.mark(movement.pos);
			
			ArgumentCaptor<CellMarkedValue> argumentCell = ArgumentCaptor.forClass(CellMarkedValue.class);
			
			verify(c1).sendEvent(eq(EventType.MARK), argumentCell.capture());
			assertThat(argumentCell.getValue().cellId).isEqualTo(movement.pos);
			assertThat(argumentCell.getValue().player.getId()).isEqualTo(thisTurn);
			
			verify(c2).sendEvent(eq(EventType.MARK), argumentCell.capture());
			assertThat(argumentCell.getValue().cellId).isEqualTo(movement.pos);
			assertThat(argumentCell.getValue().player.getId()).isEqualTo(thisTurn);
			
			if (movement.isWinner()) {
				
				ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
				
				verify(c1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
				assertThat(argument.getValue().pos).isEqualTo(movement.winLine);
				assertThat(argument.getValue().player.getId()).isEqualTo(thisTurn);
				
				verify(c2).sendEvent(eq(EventType.GAME_OVER), argument.capture());
				assertThat(argument.getValue().pos).isEqualTo(movement.winLine);
				assertThat(argument.getValue().player.getId()).isEqualTo(thisTurn);
			}
			else if (movement.isDraw()) {
				ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
				
				verify(c1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
				assertThat(argument.getValue()).isNull();
				verify(c2).sendEvent(eq(EventType.GAME_OVER), argument.capture());
				assertThat(argument.getValue()).isNull();
			}
			else {
				verify(c1).sendEvent(eq(EventType.SET_TURN), eq(players.get(nextTurn)));
				verify(c2).sendEvent(eq(EventType.SET_TURN), eq(players.get(nextTurn)));
			}
			nextTurn = nextTurn == 1 ? 0 : 1;
			reset(c1);
			reset(c2);
		}
				
		
	}
}