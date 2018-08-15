package space.shooter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import sun.security.util.Debug;


/**
 * Created by piola on 2017-05-27.
 */
public class Cannon extends GameObject
{
	private Vector2 position;
	private Float rotation;
	private Integer fireDelay;
	private Ammunition ammo;
	private Long lastShot;

	public Cannon(Ammunition ammo) {
		super();
		this.position = new Vector2(0,0);
		this.rotation = new Float(0);
		this.fireDelay = 0;
		this.ammo = ammo;
		this.ammo.setPosition(new Vector2(this.position.x-this.ammo.getSize().x/2, this.position.y));
		this.lastShot = System.currentTimeMillis();
	}

	public Cannon(Ammunition ammo, Integer fireDelay, Float rotation) {
		super();
		this.position = new Vector2(0,0);
		this.rotation = rotation;
		this.fireDelay = fireDelay;
		this.ammo = ammo;
		this.ammo.setPosition(new Vector2(this.position.x-this.ammo.getSize().x/2, this.position.y));
		this.lastShot = System.currentTimeMillis();
	}

	public Cannon(Cannon original)	{
		super();
		this.position = new Vector2(original.position);
		this.fireDelay = new Integer(original.fireDelay);
		try {
			this.ammo = original.ammo.getClass().getDeclaredConstructor(original.ammo.getClass()).newInstance(original.ammo);
			this.ammo.setVisible(false);
		} catch (Exception e) {
			new Debug().println("Exception when copying cannon: " + e.toString());
			new Debug().println(e.getStackTrace().toString());
		}

		this.lastShot = new Long(original.lastShot);
		this.rotation = new Float(original.rotation);
	}

	public void fire()	{
		if (System.currentTimeMillis() < lastShot + fireDelay)
			return;
		lastShot = System.currentTimeMillis();
		Ammunition amm = null;
		try {
			amm = ammo.getClass().getDeclaredConstructor(ammo.getClass()).newInstance(ammo);
			amm.setVisible(true);
			amm.setPosition(new Vector2(this.position.x-amm.getSize().x/2, this.position.y));
			amm.getSpeed().rotate(-rotation);

		} catch (Exception e){
			new Debug().println("Exception when firing: " + e.toString());
			new Debug().println(e.getStackTrace().toString());
		}
	}

	public void move(Vector2 direction)	{
		position.x += direction.x;
		position.y += direction.y;
		ammo.getPosition().x += direction.x;
		ammo.getPosition().y += direction.y;
	}

	public void rotateClockwise(float degrees, Boolean rotateBullets)	{
		rotation += degrees;
		rotation %= 360;
		if (rotateBullets)
			ammo.setRotation(this.rotation);
	}

	public void rotateBulletClockwise(float degrees)
	{
		ammo.setRotation(ammo.getRotation() + degrees);
	}

	public Vector2 getBulletScale()
	{
		return ammo.getScale();
	}

	public void setBulletScale(Vector2 scale)
	{
		this.ammo.setScale(scale);
	}

	public Vector2 getPosition() {
		return position;
	}

	public Ammunition getAmmo() {
		return ammo;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
		this.ammo.setPosition(new Vector2(this.position.x-this.ammo.getSize().x/2, this.position.y));
	}

	public Integer getFireDelay() {
		return fireDelay;
	}

	public void setFireDelay(Integer fireDelay) {
		this.fireDelay = fireDelay;
	}

	public Vector2 getBulletSpeed() {
		return ammo.getSpeed();
	}

	public void setBulletSpeed(Vector2 bulletSpeed) {
		ammo.setSpeed(bulletSpeed);
	}

	public Float getRotation() {
		return rotation;
	}

	public void setRotation(Float rotation, Boolean rotateBullets) {
		this.rotation = rotation;
		if(rotateBullets)
			ammo.setRotation(this.rotation);
	}

	public void setBulletRotation(Float rotation)
	{
		ammo.setRotation(rotation);
	}

	public Color getBulletTint()
	{
		return ammo.getTint();
	}

	public void setBulletTint(Color tint)
	{
		ammo.setTint(tint);
	}

	@Override
	void update() {

	}
}
