package es.codeurjc.ais.tictactoe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BoardTest {
	
	@Test
	public void GivenABoard_When_DisableAll_AllCellsInactive() {
		
		//Given
		Board board = new Board();
		
		//When
		board.disableAll();
		
		//Then
		for (int id = 0; id < 9; id++) {
			assertThat(board.getCell(id).active).isFalse();
		}		
	}
	
	@Test
	public void GivenABoard_When_EnableAll_AllCellsActive() {
		
		//Given
		Board board = new Board();
		
		//When
		board.enableAll();
		
		//Then
		for (int id = 0; id < 9; id++) {
			assertThat(board.getCell(id).active).isTrue();
		}
	}
	
	@Test
	public void GivenABoard_With_AllCellsTaken_IsDraw() {
		
		//Given
		Board board = new Board();
		for (int id = 0; id < 9; id++) {
			board.getCell(id).value = "dummy_label";
		}
		
		           //When             //Then
		assertThat(board.checkDraw()).isTrue();
	}
	
	@Test
	public void GivenABoard_With_OneCellIsFree_IsNotDraw() {
		
		//Given
		Board board = new Board();
		
		for (int id = 0; id < 8; id++) {
			board.getCell(id).value = "dummy_label";
		}
		
		           //When             //Then
		assertThat(board.checkDraw()).isFalse();
	}

	@Test
	public void GivenABoard_with_Player1asWinner_cellsReturned() {
		
		//Given
		Board board = new Board();
		
		String labelPlayer1 = "x";
		String labelPlayer2 = "o";
	
		int[] positionsPlayer1 = { 0, 1, 2 };
		int[] positionsPlayer2 = { 3, 4, 5 };
		
		for (int pos : positionsPlayer1) {
			board.getCell(pos).value = labelPlayer1;	
		}
		
		for (int pos : positionsPlayer2) {
			board.getCell(pos).value = labelPlayer2;	
		}		
                   //When                          		//Then
		assertThat(board.getCellsIfWinner(labelPlayer1)).isEqualTo(positionsPlayer1);
	}

	@Test
	public void GivenABoard_with_Player2asWinner_cellsReturned() {
		
		//Given
		Board board = new Board();
		
		String labelPlayer1 = "x";
		String labelPlayer2 = "o";
	
		int[] positionsPlayer1 = { 0, 1, 6 };
		int[] positionsPlayer2 = { 3, 4, 5 };
		
		for (int pos : positionsPlayer1) {
			board.getCell(pos).value = labelPlayer1;	
		}
		
		for (int pos : positionsPlayer2) {
			board.getCell(pos).value = labelPlayer2;	
		}		
                   //When                          		//Then
		assertThat(board.getCellsIfWinner(labelPlayer1)).isNull();
                   //When                          		//Then
		assertThat(board.getCellsIfWinner(labelPlayer2)).isEqualTo(positionsPlayer2);
	}

	@Test
	public void GivenABoard_when_play_getsWinnerRight() {
		
		//Given
		Board board = new Board();
		
		//https://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples
		List<Entry<String,int[]>> movements = new ArrayList<>();
		
		movements.add(new SimpleEntry<String,int[]>("0xN", null));
		movements.add(new SimpleEntry<String,int[]>("1xN", null));
		int [] winPositions = {0, 1, 2};
		movements.add(new SimpleEntry<String,int[]>("2xW", winPositions));

		for (Entry<String,int[]> movement : movements) {
			int pos = Character.getNumericValue(movement.getKey().charAt(0));
			String label = movement.getKey().substring(1, 1);
			board.getCell(pos).value = label;
			boolean winner = movement.getKey().substring(2).equals("W") ? true : false; 
			if (winner) {
				assertThat(board.getCellsIfWinner(label)).isEqualTo(movement.getValue());
			}
			else {
				assertThat(board.getCellsIfWinner(label)).isNull();
			}
		}
	}
}