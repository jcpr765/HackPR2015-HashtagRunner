import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

public class HashtagObject {

	private String text;
	private float x;
	private float y;
	private float speed;
	private float size;

	public HashtagObject(String text, float speed) {
		this.text = text;
		this.speed = speed;
		size = 32;
		x = Main.SCREEN_WIDTH*2;
		y = new Random().nextInt(Main.SCREEN_HEIGHT);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public Rectangle getHitbox() {
		return new Rectangle(x, y, size * text.length(), size);
	}

	public void move() {
		x -= speed;
	}

	public boolean isAlive() {
		return true;
		//return (x + size) < 0;
	}

	public String getRawHash() {
		if(text.startsWith("#"))
			return text.substring(1, text.length());
		return text;
	}
}
