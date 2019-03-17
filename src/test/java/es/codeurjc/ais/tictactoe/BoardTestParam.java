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
	
	static class Movement {
		
		Movement(int pos, String label, boolean winner, int[] winLine){
			this.pos = pos;
			this.label = label;
			this.winner = winner;
			this.winLine = winLine;
		}
		
		public int pos;
		public String label;
		public boolean winner;
		public int[] winLine;
	}
	
	@Parameters
	public static Collection<Object[]> data(){

		int[][] winPositions = { 
				{ 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, 
				{ 1, 4, 7 }, { 2, 5, 8 }, { 0, 4, 8 }, { 6, 4, 2 } };

		List<Movement> player1Wins0 = new ArrayList<Movement>();
		player1Wins0.add(new Movement(0, "x", false, null));
		player1Wins0.add(new Movement(1, "x", false, null));
		player1Wins0.add(new Movement(2, "x", true, winPositions[0] ));
		
		List<Movement> player2Wins4 = new ArrayList<Movement>();
		player2Wins4.add(new Movement(0, "x", false, null));
		player2Wins4.add(new Movement(1, "0", false, null));
		player2Wins4.add(new Movement(2, "x", false, null ));
		player2Wins4.add(new Movement(4, "0", false, null ));
		player2Wins4.add(new Movement(3, "x", false, null ));
		player2Wins4.add(new Movement(7, "0", true, winPositions[4]));
		
		List<Movement> player2Wins5 = new ArrayList<Movement>();
		player2Wins5.add(new Movement(0, "x", false, null));
		player2Wins5.add(new Movement(2, "0", false, null));
		player2Wins5.add(new Movement(1, "x", false, null ));
		player2Wins5.add(new Movement(5, "0", false, null ));
		player2Wins5.add(new Movement(3, "x", false, null ));
		player2Wins5.add(new Movement(4, "0", false, null ));
		player2Wins5.add(new Movement(7, "x", false, null ));
		player2Wins5.add(new Movement(8, "0", true, winPositions[5]));
		
	    Object[][] data = {
	    	{ player1Wins0 },
	    	{ player2Wins4 },
	    	{ player2Wins5 },
	    };
	    
	    return Arrays.asList(data);
	}
	
	@Parameter(0) public List<Movement> movements;

	@Test
	public void GivenABoard_when_play_getsWinnerRight() {
		Board board = new Board();
					
		for (Movement movement : movements) {
			board.getCell(movement.pos).value = movement.label; 
			if (movement.winner) {
				assertThat(board.getCellsIfWinner(movement.label)).isEqualTo(movement.winLine);
			}
			else {
				assertThat(board.getCellsIfWinner(movement.label)).isNull();
			}
		}
	}
}