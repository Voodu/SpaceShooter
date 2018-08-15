package space.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import sun.security.util.Debug;

import java.util.ArrayList;

/**
 * Created by piola on 2017-05-27.
 */
public class Ship extends GameObject implements Collideable, StateInformator
{
	private Vector2 position, speed;
	private Vector2 startPosition, startSpeed;
	private ScalableAnimation graphics;
	private ArrayList<Cannon> cannons;
	private Boolean spreadFire;
	private Float spreadFireAngle;
	private Circle collider;
	private ArrayList<StateInformator> listeners = new ArrayList<StateInformator>();
	private Integer baseFireDelay = null;

	public Ship(TextureAtlas shipTextures, Vector2 position, Vector2 scale)	{
		super();
		this.position = new Vector2(position);
		speed = new Vector2(10, 10);
		this.startPosition = new Vector2(position);
		startSpeed = new Vector2(10, 10);
		graphics = new ScalableAnimation(shipTextures);
		graphics.setPosition(new Vector2(position));
		graphics.setScale(new Vector2(scale));
		cannons = new ArrayList<Cannon>();
		spreadFire = true;
		spreadFireAngle = 45f;
		collider = new Circle(
				new Vector2(graphics.getPosition().x + graphics.getSize().x/2, graphics.getPosition().y + graphics.getSize().y/2),
				Math.min(graphics.getSize().x, graphics.getSize().y)/2f);
	}

	public void attachCannon(Cannon cannon)	{
		Cannon newCannon = new Cannon(cannon);
		cannons.add(newCannon);
		spreadCannons();
		if (baseFireDelay == null)
			baseFireDelay = cannon.getFireDelay();
	}

	public void attachCannons(Cannon cannon, Integer number){
		Cannon newCannon = null;
		for (int i=0; i<number; i++) {
			newCannon = new Cannon(cannon);
			cannons.add(newCannon);
		}
		spreadCannons();
		if (baseFireDelay == null)
			baseFireDelay = cannon.getFireDelay();
	}
	
	private void spreadCannons()	{
		Vector2 spacing = new Vector2( graphics.getSize().x/(cannons.size()+1), graphics.getSize().y/(cannons.size()+1));
		for (int i=0; i<cannons.size(); i++) {
			cannons.get(i).setPosition(new Vector2(position.x + spacing.x * (i + 1),
					(position.y + graphics.getSize().y - graphics.getSize().y / 3) - Math.abs(-(cannons.size() - 1) / 2f + i) * spacing.y));
			if(spreadFire)
				cannons.get(i).setRotation(-spreadFireAngle /2f+(i+1)* spreadFireAngle /(float)(cannons.size()+1), true);
		}
	}

	public void copyCannon(Integer originalID)	{
		try
		{attachCannon(cannons.get(originalID));
		}catch (Exception e){new Debug().println("Invalid ID in copyCannon");}
	}

	public void detachCannon(int ID)	{
		try	{cannons.remove(ID);
		}catch (Exception e) {
			new Debug().println("Exception while detaching cannon: " + e.toString());
			new Debug().println(e.getStackTrace().toString());
		}
		spreadCannons();
	}

	public void move(Vector2 direction)	{

		position.x += direction.x;
		position.y += direction.y;
		graphics.move(direction);
		for (Cannon cannon: cannons)
			cannon.move(direction);
		collider.setPosition(new Vector2(graphics.getPosition().x + graphics.getSize().x/2, graphics.getPosition().y + graphics.getSize().y/2));
	}

	public void parseInput(Integer keyCode)	{
		switch (keyCode) {
			case Input.Keys.RIGHT:
				try {graphics.frameUp();} catch (Exception e) {new Debug().println(e.getMessage());}
				if(canMove(new Vector2(speed.x, 0))) move(new Vector2(speed.x, 0));
				break;
			case Input.Keys.LEFT:
				try {graphics.frameDown();} catch (Exception e) {new Debug().println(e.getMessage());}
				if(canMove(new Vector2(-speed.x, 0))) move(new Vector2(-speed.x, 0));
				break;
			case Input.Keys.UP:
				if(canMove(new Vector2(0, speed.y))) move(new Vector2(0, speed.y));
				break;
			case Input.Keys.DOWN:
				if(canMove(new Vector2(0, -speed.y))) move(new Vector2(0, -speed.y));
				break;
			case Input.Keys.SPACE:
				for (Cannon cannon: cannons)
					cannon.fire();
				break;
			case Input.Keys.ENTER:
				setSpreadFire(!spreadFire);
				break;
			default:
				break;
		}
	}

	public Boolean canMove(Vector2 place)	{
		if (position.x + place.x >= 0 && position.x + place.x + graphics.getSize().x <= Gdx.graphics.getWidth())
			if (position.y + place.y >= 0 && position.y + place.y + graphics.getSize().y <= Gdx.graphics.getHeight())
				return true;
		return false;
	}

	public void levelOff()	{
		if (graphics.getFrame() > graphics.getTexturesCount() / 2)
			graphics.setFrame(graphics.getFrame()-1);
		else if (graphics.getFrame() < graphics.getTexturesCount() / 2)
			graphics.setFrame(graphics.getFrame()+1);
	}

	public void setPosition(Vector2 position)	{
		graphics.setPosition(position);
		this.position = position;
		spreadCannons();
		collider.setPosition(new Vector2(graphics.getPosition().x + graphics.getSize().x/2, graphics.getPosition().y + graphics.getSize().y/2));
	}

	public void setSpeed(Vector2 speed)
	{
		this.speed = speed;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getSpeed() {
		return speed;
	}

	public ScalableAnimation getGraphics() {
		return graphics;
	}

	public Boolean getSpreadFire() {
		return spreadFire;
	}

	public void setSpreadFire(Boolean spreadFire)	{
		this.spreadFire = spreadFire;
		if(spreadFire)
			for (int i=0; i<cannons.size(); i++)
				cannons.get(i).setRotation(-spreadFireAngle /2f+(i+1)* spreadFireAngle /(float)(cannons.size()+1), true);
		else
			for (int i=0; i<cannons.size(); i++)
				cannons.get(i).setRotation(0f, true);

	}

	public Float getSpreadFireAngle() {
		return spreadFireAngle;
	}

	public void setSpreadFireAngle(Float spreadFireAngle)	{
		if (spreadFireAngle >= 0.0 && spreadFireAngle <= 360.0)
			this.spreadFireAngle = spreadFireAngle;
	}

	private void applyBonus(Bonus bonus)	{
		if (bonus == Bonus.BoostFirerate)
			for(Cannon cannon: cannons) {
				if (cannon.getFireDelay() > 100)
					cannon.setFireDelay(cannon.getFireDelay() - 5);
				else
					break;
			}
		else if (bonus == Bonus.BoostSpeed)
			speed.add(0.25f, 0.25f);
		else if (bonus == Bonus.NewCannon)
			copyCannon(0);
	}

	public void addListener(StateInformator sl)
	{
		listeners.add(sl);
	}

	private void informListeners(GameState state)	{
		for(StateInformator si:listeners)
			si.stateChanged(state);
	}

	@Override
	void update()	{
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))       parseInput(Input.Keys.RIGHT);
		else if (Gdx.input.isKeyPressed(Input.Keys.LEFT))   parseInput(Input.Keys.LEFT);
		else levelOff();
		if (Gdx.input.isKeyPressed(Input.Keys.UP))          parseInput(Input.Keys.UP);
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))   parseInput(Input.Keys.DOWN);
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE))       parseInput(Input.Keys.SPACE);
		if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))   parseInput(Input.Keys.ENTER);
	}

	@Override
	public Circle getCollider() {
		return collider;
	}

	@Override
	public void onCollision(Collideable col)	{
		if (col instanceof BasicAsteroid) {
			if (((BasicAsteroid) col).isBonus()) {
				applyBonus(((BasicAsteroid) col).getBonus());
				return;
			}
			while(cannons.size() > 3)
				detachCannon(0);
			for(Cannon cannon:cannons)
				cannon.setFireDelay(baseFireDelay);
			speed = new Vector2(startSpeed);
			position = new Vector2(startPosition);
			informListeners(GameState.ShipDestroyed);
			this.graphics.setVisible(false);
			this.setEnabled(false);
		}
	}

	@Override
	public void stateChanged(GameState stateID) {
		if (stateID == GameState.Game){
			graphics.setFrame(graphics.getTexturesCount()/2);
			setPosition(new Vector2(Gdx.graphics.getWidth()/2 - graphics.getSize().x/2, 0));
			this.graphics.setVisible(true);
			this.setEnabled(true);
		}
	}
}
