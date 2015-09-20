import java.awt.Desktop;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import twitter4j.TwitterException;

public class GameState extends BasicGameState{

	private ArrayList<HashtagObject> hashList;
	private float x;
	private float size;
	private TrueTypeFont font;
	private long lastAdd;
	private StateBasedGame game;
	private Image background;
	private int tick;
	float gameSpeed;
	private ArrayList<String> hashtagList;
	
	public static final int X = 50;
	

	public GameState(StateBasedGame sgb){
		game = sgb;
	}
	
	public GameState initialize() throws SlickException{
		hashList = new ArrayList<HashtagObject>();
		x = Main.SCREEN_HEIGHT/2;
		size = 50;
		Font f = new Font("Verdana", Font.BOLD, 32);
		font = new TrueTypeFont(f, true);
		lastAdd = 0;
		background = new Image("textures/background.png");
		tick=0;
		gameSpeed = 5;
		try {
			hashtagList = GetAvailableTrends.getPopularHashtags();
			System.out.println("hashtags: " + hashtagList.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics arg2) throws SlickException {
		background.draw();
		arg2.drawRect(X-(size/2), x-(size/2), size, size);
		for (HashtagObject obj : hashList) {
			arg2.setFont(font);
			arg2.drawRect(obj.getX()-(obj.getHitbox().getWidth()/2), obj.getY()-(obj.getHitbox().getHeight()/2), obj.getHitbox().getWidth(), obj.getHitbox().getHeight());
			arg2.drawString(obj.getText(), obj.getX()-(obj.getHitbox().getWidth()/2), obj.getY()-(obj.getHitbox().getHeight()/2));
		}
		arg2.setColor(Color.black);
		arg2.drawString("Score: " + tick, 10, 10);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {
		Input i = arg0.getInput();
		if (System.currentTimeMillis() - lastAdd > 500) {
			gameSpeed *= 1.02;
			lastAdd = System.currentTimeMillis();
			float newSpeed = gameSpeed + (Main.nextInt((int) (gameSpeed/2)))-gameSpeed/2;
			hashList.add(new HashtagObject(hashtagList.get(Main.nextInt(hashtagList.size()-1)), newSpeed));
		}
		if (i.isKeyDown(Input.KEY_UP))
			x -= 5;
		if (i.isKeyDown(Input.KEY_DOWN))
			x += 5;
		if (x > Main.SCREEN_HEIGHT-50)
			x = Main.SCREEN_HEIGHT-50;
		if (x < 50)
			x = 50;
		Rectangle hitbox = new Rectangle(X, x, size, size);
		for (int x = 0; x < hashList.size(); x++) {
			hashList.get(x).move();
			if (!hashList.get(x).isAlive())
				hashList.remove(x);
			if (hashList.get(x).getHitbox().intersects(hitbox)){
				((Game)game).setScore(tick+1);
				game.enterState(2, new FadeOutTransition(), new FadeInTransition());
			}
			if(hashList.get(x).getHitbox().intersects(new Rectangle(i.getMouseX(), i.getMouseY(), 1, 1)))
				if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON))
					try {
						Desktop.getDesktop().browse(new URI("https://twitter.com/hashtag/" +  hashList.get(x).getRawHash() + "?src=hash&lang=en"));
					} catch (IOException e) {
						e.printStackTrace();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
		}tick++;
	}

	@Override
	public int getID() {
		try {
			initialize();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		return 1;
	}

}
