package space.shooter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import sun.security.util.Debug;

/**
 * Created by piola on 2017-05-27.
 */
public abstract class Ammunition extends GameObject implements Collideable {
	private ScalableAnimation graphics;
	private Vector2 speed;
	private Circle collider;

	public Ammunition(TextureAtlas textures) {
		super();
		graphics = new ScalableAnimation(textures);
		graphics.setPosition(new Vector2(-graphics.getSize().x*10, -graphics.getSize().y*10));
		speed = new Vector2(0, 1);
		collider = new Circle(
				new Vector2(graphics.getPosition().x + graphics.getSize().x/2, graphics.getPosition().y + graphics.getSize().y/2),
				Math.min(graphics.getSize().x, graphics.getSize().y)/2f);
	}

	public Ammunition(TextureAtlas textures, Vector2 scale, Vector2 speed)	{
		super();
		graphics = new ScalableAnimation(textures);
		graphics.setScale(scale);
		graphics.setPosition(new Vector2(-graphics.getSize().x*10, -graphics.getSize().y*10));
		collider = new Circle(
				new Vector2(graphics.getPosition().x + graphics.getSize().x/2, graphics.getPosition().y + graphics.getSize().y/2),
				Math.min(graphics.getSize().x, graphics.getSize().y)/2f);
		this.speed = new Vector2(speed);
	}

	public Ammunition (Ammunition original)	{
		super();
		graphics = new ScalableAnimation(original.graphics);
		graphics.setPosition(new Vector2(-graphics.getSize().x, -graphics.getSize().y));
		speed = new Vector2(original.getSpeed());
		collider = new Circle(original.collider);
	}

	public void move(Vector2 direction)	{
		graphics.getPosition().x += direction.x;
		graphics.getPosition().y += direction.y;
		collider.setPosition(new Vector2(graphics.getPosition().x + graphics.getSize().x/2, graphics.getPosition().y + graphics.getSize().y/2));
	}

	public Vector2 getSpeed() {
		return this.speed;
	}

	public void setSpeed(Vector2 speed) {
		this.speed = speed;
	}

	public Vector2 getPosition()
	{
		return graphics.getPosition();
	}

	public void setPosition(Vector2 position)	{
		graphics.setPosition(new Vector2(position));
		collider.setPosition(new Vector2(position.x + graphics.getSize().x/2, position.y + graphics.getSize().y/2));
	}

	public Vector2 getSize()
	{
		return graphics.getSize();
	}

	public Vector2 getScale()
	{
		return graphics.getScale();
	}

	public void setScale(Vector2 scale)	{
		graphics.setScale(new Vector2(scale));
		collider = new Circle(
				new Vector2(graphics.getPosition().x + graphics.getSize().x/2,graphics.getPosition().y + graphics.getSize().y/2),
				Math.min(graphics.getSize().x, graphics.getSize().y)/2f);
	}

	public Float getRotation(){
		return graphics.getRotation();
	}

	public void setRotation(Float rotation)
	{
		graphics.setRotation(new Float(rotation));
	}

	public void destroyGraphics()
	{
		destroy(graphics);
	}

	public void setVisible(boolean v)
	{
		graphics.setVisible(v);
	}

	public boolean isVisible() {
		return graphics.isVisible();
	}

	public Color getTint()	{return graphics.getTint();}

	public void setTint(Color tint)
	{
		graphics.setTint(tint);
	}

	public void changeGraphics(TextureAtlas newGraphics)	{
		ScalableAnimation orig = graphics;
		destroy(graphics);
		graphics = new ScalableAnimation(newGraphics);
		graphics.setPosition(orig.getPosition());
		Float scalingFactor = Math.min(orig.getSize().x/graphics.getSize().x, orig.getSize().y/graphics.getSize().y);
		graphics.setScale(new Vector2(scalingFactor, scalingFactor));
		collider = new Circle(
				new Vector2(orig.getPosition().x + orig.getSize().x/2, orig.getPosition().y + orig.getSize().y/2),
				Math.min(orig.getSize().x, orig.getSize().y)/2f);
		destroy(orig);
	}

	public void setFrame(Integer frameID)	{
		try{
			graphics.setFrame(frameID);
		}catch (Exception e) {
			new Debug().println(e.getMessage());
		}
	}

	@Override
	public Circle getCollider()
	{
		return collider;
	}
}
