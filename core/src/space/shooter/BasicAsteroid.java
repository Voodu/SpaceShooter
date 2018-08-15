package space.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by piola on 2017-05-28.
 */
public class BasicAsteroid extends Ammunition
{
	private Integer endurance = 5, startEndurance = 5;
	private Boolean isBonus = false;
	private Integer bonusChance = 40;
	private Bonus bonusIndex = null;

	public BasicAsteroid(TextureAtlas textures)
	{
		super(textures);
	}

	public BasicAsteroid(TextureAtlas textures, Integer startEndurance)	{
		super(textures);
		this.startEndurance = this.endurance = startEndurance;
	}

	public BasicAsteroid(TextureAtlas textures, Vector2 scale, Vector2 speed)	{
		super(textures, scale, speed);
		setVisible(false);
	}

	public BasicAsteroid(BasicAsteroid original) {
		super(original);
		endurance = original.endurance;
		isBonus = original.isBonus;
		bonusChance = original.bonusChance;
		bonusIndex = original.bonusIndex;
	}

	public Integer getEndurance() {
		return endurance;
	}

	public void setEndurance(Integer endurance) {
		this.endurance = endurance;
	}

	public Boolean isBonus() {
		return isBonus;
	}

	private Bonus getInternalBonus()	{
		Integer rand = new Random().nextInt(100);
		if (rand < 6)
			return Bonus.NewCannon;
		if (rand < 53)
			return Bonus.BoostSpeed;
		else
			return Bonus.BoostFirerate;
	}

	public Bonus getBonus()	{
		destroyGraphics();
		GameObject.destroy(this);
		return bonusIndex;
	}

	public Integer getBonusChance() {
		return bonusChance;
	}

	public void setBonusChance(Integer bonusChance) {
		this.bonusChance = bonusChance;
	}

	@Override
	public void update() {
		move(getSpeed());

		if (getPosition().x >= Gdx.graphics.getWidth()
				||  getPosition().x + getSize().x <= 0
				||  getPosition().y >= Gdx.graphics.getHeight()
				||  getPosition().y + getSize().y <= 0)
		{
			destroyGraphics();
			GameObject.destroy(this);
			return;
		}
	}

	@Override
	public void onCollision(Collideable col) {
		if (--endurance == 0)
		{
			if (col instanceof BasicBullet)
				UIManager.instance().addPoints(endurance + Math.abs((int)getSpeed().y));
			if (new Random().nextInt(100) < bonusChance)
			{
				isBonus = true;
				bonusIndex = getInternalBonus();
				changeGraphics(new TextureAtlas(Gdx.files.internal("Sprites/bonuses/bonuses.atlas")));
				setFrame(bonusIndex.ordinal());
				return;
			}
			destroyGraphics();
			GameObject.destroy(this);
		}
	}
}
