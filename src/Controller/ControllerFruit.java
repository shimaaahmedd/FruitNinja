package Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Model.*;



public class ControllerFruit implements GameActions{
	private ControllerFruit() {
		
	}
	public static ControllerFruit getInstance() {
		return NewSingltonHolder.INSTANCE;
	}
	private static class NewSingltonHolder{
		private static final ControllerFruit INSTANCE = new ControllerFruit();
	}
   List<GameObject> fruit  = new ArrayList<GameObject>();
   public ArrayList<Integer> gameData = new ArrayList<Integer>();
   SpecialFruit Special;
   SpecialBomb SpecialBomb;
   IGameStrategy strategy;
    int score=0;
    int highScore=0;
    int listCount=0;
    int lives = 3;
    int j;
    int k;
    CommandControl control = new CommandControl();
    Random rand=new Random();
    Random specialRand=new Random();
    boolean valid;
	@Override
	public void createGameObject() {
		GameObjectFactory factory = new GameObjectFactory();
		GameObject fruitObject;
		j=rand.nextInt(10);
		if(j!=0){
			fruitObject = factory.createFruit();
			if(strategy instanceof Arcade){
				while(fruitObject instanceof Boom){
					fruitObject = factory.createFruit();
				}
			}
			fruit.add(fruitObject);
			listCount=fruit.size();
		}
		else{
			k=specialRand.nextInt(2);
			if(k==0){	
			fruitObject = factory.createFruit();
			if(strategy instanceof Arcade){
				while(fruitObject instanceof Boom){
					fruitObject = factory.createFruit();
				}
			}
			 if(fruitObject instanceof Boom){
						fruit.add(fruitObject);
						listCount=fruit.size();
					
			}
			else{
					Special= new SpecialFruit(fruitObject);
					fruit.add(Special);
					listCount=fruit.size();
			}
		  }
			else{
				fruitObject = factory.createFruit();
				if(strategy instanceof Arcade){
					while(fruitObject instanceof Boom){
						fruitObject = factory.createFruit();
					}
				}
		   if(fruitObject instanceof Boom){	
			SpecialBomb=new SpecialBomb(fruitObject);
			fruit.add(SpecialBomb);
			listCount=fruit.size();
		}
			else{
				fruit.add(fruitObject);
				listCount=fruit.size();
			}
		  }
		}
	}

	@Override
	public void updateObjectsLocations() {
		// TODO Auto-generated method stub
		
	}
	public boolean sliceObjects(int i) {
	
			fruit.get(i).slice(); 

		if( fruit.get(i).isSliced()) {
			if(fruit.get(i) instanceof SpecialFruit) {
				score = score+1;
		        strategy.setSpeed(0.01);
			}
			else if(fruit.get(i) instanceof SpecialBomb){
				lives=0;
			}
			else if(fruit.get(i) instanceof Boom){
				lives--;
				score--;
			}
			else if(fruit.get(i) instanceof Pineapple){
				score=score*2;
			}
		score++;
			return true;
		}
		
		return false;
	}

	@Override
	public void saveGame() {
		// TODO Auto-generated method stub
		
		gameData.add(score);
		gameData.add(highScore);
		gameData.add(lives);
		control.setCommand(new SaveGameCommand(new Save(gameData,strategy)));
	    control.buttonWasPressed();
	    gameData.clear();
	}

	@Override
	public void loadGame(String choice) {
		// TODO Auto-generated method stub
		control.setCommand(new LoadGameCommand(new Load(gameData,choice,strategy)));
		control.buttonWasPressed();
		
	}

	@Override
	public void ResetGame() {
	fruit.clear();
	lives=3;
	score=0;
	loadGame("highscore");
		
	}
	public int getScore() {
		return  this.score;
	}
	public List<GameObject> getObjectList(){
		return fruit;
	}
	public void updateObjects() {
		createGameObject();
	}
	public boolean hasMovedOfScreenBoundary(int i) {
		valid=fruit.get(i).hasMovedOffScreen();
		if(valid==true&&fruit.get(i) instanceof Boom) {
			
		}
		else if(valid==true&&(!(strategy instanceof Arcade))){
			lives--;
		}
		return valid;
	}
public int getListCount() {
	return this.listCount;
}
public void setListCount(int listCount) {
	this.listCount = listCount;
}

public int getLives() {
	return this.lives;
}
public void setLives(int lives) {
	this.lives = lives;
}
public void setStrategy(IGameStrategy strategy){
	this.strategy = strategy;
}

public void setScore(int newscore) {
	score = newscore;
	
}

@Override
public int getHighScore() {
	return highScore;
}

@Override
public void setHighScore(int highScore) {
	this.highScore = highScore;
	
}

}
