package test;

import static org.junit.Assert.*;

import occupiers.Lokum;
import occupiers.SquareOccupier;
import occupiers.SquareOccupierFactory;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cas.AudioPlayers;
import cas.Board;
import cas.GameLoader;
import cas.Level;
import cas.Player;
import cas.Position;
import engines.GUIEngine;
import engines.GameEngine;

public class BoardWhiteBoxTest {
	Board board;
	GameEngine engine;
	SquareOccupierFactory factory;
	private static GameEngine engineObj;
	private static GUIEngine gui;
	private static Level level;
	private static GameLoader loader;
	private static Player tester;
	
	

	@BeforeClass
	public static void setUpClass() {
	    //executed only once, before the first test
		AudioPlayers noSoundPlease = AudioPlayers.getInstance();
		noSoundPlease.disableOrEnableAllBackMusic(false);
		noSoundPlease.disableOrEnableEffects(false);
		engineObj = GameEngine.getInstance();
		gui = GUIEngine.getInstance();
		gui.start();
		loader = GameLoader.getInstance();
		level = Level.getInstance();
		level.loadLevel(1);
		tester = new Player("dsds");
		gui.startGame(tester);
		gui.callLoadGame();	
	}
	@Before
	public void setUp() throws Exception {
		board = Board.getInstance();
		engine = GameEngine.getInstance();
		level = Level.getInstance();
		level.loadLevel(1);
		loader = GameLoader.getInstance();
		factory = SquareOccupierFactory.getInstance();
		Player tester = new Player("LokumGG");
		engine.setPlayer(tester);
		loader.loadGame("mysaves");
		engine.setPlayer(tester);
		engine.createLoadedGame("mysaves");
		engineObj.createNewGame(level);
		tester = new Player("LokumGG");
		engineObj.setPlayer(tester);
		gui.loadGame("mySaves");
		gui.getPlayGUI().setVisible(false);	
		loader.loadGame("mySaves");
		engineObj.setPlayer(tester);
		engineObj.createLoadedGame("mySaves");	
	}

	@Test
	public void getInstanceTest() {
		Board otherBoard = Board.getInstance();
		assertSame(board, otherBoard);
	}
	
	@Test
	public void newBoardTest(){ // generateRandomRegularLokum line 26 NullPointer TimeScore instance i olmasi sebebiyle olabilir!
		board.newBoard();
		boolean bulyon = true;
		for(int i = 0; i < Board.ROW_NUMBER; i++){
			for(int j = 0; j < Board.COLUMN_NUMBER; j++){
				if(board.getLokumAt(new Position(i,j))== null)
					bulyon = false;
			}
		}
		assertTrue(bulyon);	
		assertTrue(board.repOK());
	}
	
	@Test
	public void loadBoardTest(){
		SquareOccupier[][] loadedMatrix = new SquareOccupier[Board.ROW_NUMBER][Board.COLUMN_NUMBER];
		Lokum l = factory.generateLokum("red", "RegularLokum");
		loadedMatrix[0][0] = l;
		
		board.loadBoard(loadedMatrix);
		
		assertTrue(l.equals(board.getLokumAt(new Position(0,0))));
		assertEquals(null, board.getLokumAt(new Position(1,0)));
		assertFalse(board.repOK()); // we expect false because board includes null
		
	}
	
	@Test
	public void removeLokumAtPositionTest(){
		board.removeLokumAt(new Position(0,0));
		
		assertEquals(null, board.getLokumAt(new Position(0,0)));
		assertFalse(board.repOK()); // we expect false because board includes null
		
	}
	
	@Test
	public void setLokumTest(){
		Lokum expected = factory.generateLokum("", "ColorBombLokum");
		
		board.setLokum(expected, new Position(0,0));
		
		assertEquals(expected, board.getLokumAt(new Position(0,0)));
		assertTrue(board.repOK());
	}
	
	@Test
	public void resetTest(){
		board.reset();
		
		boolean bulyon = true;
		
		for(int i = 0; i < Board.ROW_NUMBER; i++){
			for(int j = 0; j < Board.COLUMN_NUMBER; j++){
				if(board.getLokumAt(new Position(i,j)) != null)
					bulyon = false;
			}
		}
		assertTrue(bulyon);
		assertFalse(board.repOK());
	}
	
	@Test
	public void fallLokumsToEmptySquaresTest(){
		board.reset();
		board.fillDown(0);
		
		assertTrue(board.repOK());
	}
	
	@Test
	public void generateLokumsToEmptySquaresTest(){
		board.reset();
		board.fillDown(0);
		
		assertTrue(board.repOK());
	}
	
	@Test
	public void testSwapNullPoisitonTest(){
		boolean bulyon = board.testSwap(new Position(0,0), null);
		
		assertFalse(bulyon);
		assertTrue(board.repOK());	
	}
	
	@Test
	public void testSwapFarPositionsTest(){
		boolean bulyon = board.testSwap(new Position(0,0), new Position(2,2));
		
		assertFalse(bulyon);
		assertTrue(board.repOK());	
	}

	@Test
	public void testSwapColorBombTest(){
		boolean bulyon = board.testSwap(new Position(8,0), new Position(7,0));
		
		assertTrue(bulyon);
		assertTrue(board.repOK());
	}
	
	@Test
	public void testSwapSpecialLokumsTest(){
		boolean bulyon = board.testSwap(new Position(1,6), new Position(2,6));
		
		assertTrue(bulyon);
		assertTrue(board.repOK());
	}
	
	@Test
	public void testSwapSameColorsTest(){
		boolean bulyon = board.testSwap(new Position(0,8), new Position(1,8));
		
		assertFalse(bulyon);
		assertTrue(board.repOK());
	}
	
	@Test
	public void testSwapFindMatchTest(){
		boolean bulyon = board.testSwap(new Position(2,7), new Position(2,8));
		
		assertTrue(bulyon);
		assertTrue(board.repOK());
	}
	
	@Test
	public void testSwapNoWayPositionsTest(){
		boolean bulyon = board.testSwap(new Position(15,15), new Position(15,14));
		
		assertFalse(bulyon);
		assertTrue(board.repOK());
	}
}
