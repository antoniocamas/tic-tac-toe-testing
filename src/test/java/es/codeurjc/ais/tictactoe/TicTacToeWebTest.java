package es.codeurjc.ais.tictactoe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;


@RunWith(Parameterized.class)
public class TicTacToeWebTest {
	
	@Parameters
	public static Collection<Object[]> data(){
		
		String player1Name = "Snake";
		String player2Name = "Apple";

		List<Movement> player2Wins4 = new ArrayList<Movement>();
		player2Wins4.add(new Movement(0, player1Name, false, null));
		player2Wins4.add(new Movement(1, player2Name, false, null));
		player2Wins4.add(new Movement(2, player1Name, false, null));
		player2Wins4.add(new Movement(4, player2Name, false, null));
		player2Wins4.add(new Movement(3, player1Name, false, null));
		player2Wins4.add(new Movement(7, player2Name, true, Movement.WINPOSITIONS[4]));
		
		List<Movement> player2Wins5 = new ArrayList<Movement>();
		player2Wins5.add(new Movement(0, player1Name, false, null));
		player2Wins5.add(new Movement(2, player2Name, false, null));
		player2Wins5.add(new Movement(1, player1Name, false, null));
		player2Wins5.add(new Movement(5, player2Name, false, null));
		player2Wins5.add(new Movement(3, player1Name, false, null));
		player2Wins5.add(new Movement(4, player2Name, false, null));
		player2Wins5.add(new Movement(7, player1Name, false, null));
		player2Wins5.add(new Movement(8, player2Name, true, Movement.WINPOSITIONS[5]));
		
		List<Movement> player1Wins3 = new ArrayList<Movement>();
		player1Wins3.add(new Movement(0, player1Name, false, null));
		player1Wins3.add(new Movement(2, player2Name, false, null));
		player1Wins3.add(new Movement(1, player1Name, false, null));
		player1Wins3.add(new Movement(5, player2Name, false, null));
		player1Wins3.add(new Movement(3, player1Name, false, null));
		player1Wins3.add(new Movement(4, player2Name, false, null));
		player1Wins3.add(new Movement(8, player1Name, false, null));
		player1Wins3.add(new Movement(7, player2Name, false, null));
		player1Wins3.add(new Movement(6, player1Name, true,  Movement.WINPOSITIONS[3]));
		
		List<Movement> player12Draw = new ArrayList<Movement>();
		player12Draw.add(new Movement(0, player1Name, false, null));
		player12Draw.add(new Movement(8, player2Name, false, null));
		player12Draw.add(new Movement(5, player1Name, false, null));
		player12Draw.add(new Movement(3, player2Name, false, null));
		player12Draw.add(new Movement(6, player1Name, false, null));
		player12Draw.add(new Movement(2, player2Name, false, null));
		player12Draw.add(new Movement(4, player1Name, false, null));
		player12Draw.add(new Movement(7, player2Name, false, null));
		player12Draw.add(new Movement(1, player1Name, true,  null));
		
	    Object[][] data = {
	    	{ player2Wins4 },
	        { player2Wins5 },
	    	{ player12Draw }
	    };
	    
	    return Arrays.asList(data);
	}
	
	@Parameter(0) public List<Movement> movements;

    private List<WebDriver> drivers = new ArrayList<WebDriver>();

    @BeforeClass
    public static void setupClass() {
            WebDriverManager.chromedriver().setup();
            WebApp.start();
    }

    @AfterClass
    public static void teardownClass() {
            WebApp.stop();
    }

    @Before
    public void setupTest() {
        drivers.add(new ChromeDriver());
        drivers.add(new ChromeDriver());
    }

    @After
    public void teardown() {
    	for(WebDriver driver : drivers) {
            if (driver != null) {
                    driver.quit();
            }
    	}
    	drivers.clear();
    }
    

	@Test
	public void TicTacToeWeb_Generic_System_Test() throws InterruptedException {
		for(WebDriver driver : drivers) {
			driver.get("http://localhost:8080/");
		}
		
		drivers.get(0).findElement(By.id("nickname")).sendKeys(movements.get(0).label);
		drivers.get(0).findElement(By.id("startBtn")).click();
		//Thread.sleep(2000);
		drivers.get(1).findElement(By.id("nickname")).sendKeys(movements.get(1).label);
		drivers.get(1).findElement(By.id("startBtn")).click();
		//Thread.sleep(2000);
		
		int nextTurn = 1;
		
		for (Movement movement : movements) {
			int thisTurn = nextTurn == 1 ? 0 : 1;
			drivers.get(thisTurn).findElement(By.id("cell-" + movement.pos)).click();
			//Thread.sleep(2000);
			
			if (movement.isWinner()) {
				assertThat(
						drivers.get(thisTurn).switchTo().alert().getText()
						).isEqualTo(
								movement.label + " wins! " + movements.get(nextTurn).label + " looses.");
			}
			else if (movement.isDraw()) {
				assertThat(
						drivers.get(thisTurn).switchTo().alert().getText()
						).isEqualTo("Draw!");
			}
			nextTurn = nextTurn == 1 ? 0 : 1;
		}
	}
}