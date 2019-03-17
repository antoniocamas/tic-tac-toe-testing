package es.codeurjc.ais.tictactoe;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BoardTestParam {
	
	@Parameters
	public static Collection<Object[]> data(){

		List<Movement> player1Wins0 = new ArrayList<Movement>();
		player1Wins0.add(new Movement(0, "x", false, null));
		player1Wins0.add(new Movement(1, "x", false, null));
		player1Wins0.add(new Movement(2, "x", true, Movement.WINPOSITIONS[0] ));
		
		List<Movement> player2Wins4 = new ArrayList<Movement>();
		player2Wins4.add(new Movement(0, "x", false, null));
		player2Wins4.add(new Movement(1, "0", false, null));
		player2Wins4.add(new Movement(2, "x", false, null ));
		player2Wins4.add(new Movement(4, "0", false, null ));
		player2Wins4.add(new Movement(3, "x", false, null ));
		player2Wins4.add(new Movement(7, "0", true, Movement.WINPOSITIONS[4]));
		
		List<Movement> player2Wins5 = new ArrayList<Movement>();
		player2Wins5.add(new Movement(0, "x", false, null));
		player2Wins5.add(new Movement(2, "0", false, null));
		player2Wins5.add(new Movement(1, "x", false, null ));
		player2Wins5.add(new Movement(5, "0", false, null ));
		player2Wins5.add(new Movement(3, "x", false, null ));
		player2Wins5.add(new Movement(4, "0", false, null ));
		player2Wins5.add(new Movement(7, "x", false, null ));
		player2Wins5.add(new Movement(8, "0", true, Movement.WINPOSITIONS[5]));
		
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
	    	{ player1Wins0 },
	    	{ player2Wins4 },
	    	{ player2Wins5 },
	    	{ player12Draw }
	    };
	    
	    return Arrays.asList(data);
	}
	
	@Parameter(0) public List<Movement> movements;

	@Test
	public void GivenABoard_when_play_getsWinnerRight() {
		Board board = new Board();
					
		for (Movement movement : movements) {
			board.getCell(movement.pos).value = movement.label; 
			if (movement.isWinner()) {
				assertThat(board.getCellsIfWinner(movement.label)).isEqualTo(movement.winLine);
			}
			else if (movement.isDraw()) {
				assertThat(board.checkDraw()).isTrue();
			}
			else {
				assertThat(board.getCellsIfWinner(movement.label)).isNull();
			}
		}
	}
}