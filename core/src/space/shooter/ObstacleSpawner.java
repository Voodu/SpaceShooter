package space.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by piola on 2017-05-29.
 */
public class ObstacleSpawner extends GameObject implements StateInformator
{
	private Cannon obstacleSpawner;
	private Integer spawnDelay;
	private Long lastSpawn = System.currentTimeMillis();
	private Integer spawnRangeMin, spawnRangeMax;
	private Integer speedRangeMin, speedRangeMax;
	private Float scaleRangeMin, scaleRangeMax;
	private Float degreeRangeMin, degreeRangeMax;
	private Boolean rotated = true;
	private Integer levelTimeSeconds = 30;
	private Long lastLevelTime = 0L;
	private Integer startEndurance = 5;

	public ObstacleSpawner(Cannon spawner, Integer spawnDelay)	{
		super();
		obstacleSpawner = new Cannon(spawner);
		obstacleSpawner.setPosition(new Vector2(0, Gdx.graphics.getHeight() - 10));
		this.spawnDelay = spawnDelay;
		spawnRangeMin = 0;
		spawnRangeMax = (int)(Gdx.graphics.getWidth()-obstacleSpawner.getAmmo().getSize().x);
		speedRangeMin = speedRangeMax = (int)obstacleSpawner.getBulletSpeed().y;
		scaleRangeMin = scaleRangeMax = 1f;
		degreeRangeMin = degreeRangeMax = 1f;
	}

	public ObstacleSpawner(ObstacleSpawner orig)	{
		super();
		this.obstacleSpawner = new Cannon(orig.obstacleSpawner);
		this.spawnDelay = orig.spawnDelay;
		this.spawnRangeMin = orig.spawnRangeMin;
		this.spawnRangeMax = orig.spawnRangeMax;
		this.speedRangeMin = orig.speedRangeMin;
		this.speedRangeMax = orig.speedRangeMax;
		this.scaleRangeMin = orig.scaleRangeMin;
		this.scaleRangeMax = orig.scaleRangeMax;
		this.degreeRangeMin = orig.degreeRangeMin;
		this.degreeRangeMax = orig.degreeRangeMax;
		this.startEndurance = orig.startEndurance;
	}

	private void levelUp()	{
		//new Debug().println("LEVEL UP!!!");
		if (obstacleSpawner.getAmmo() instanceof BasicAsteroid) {
			((BasicAsteroid) obstacleSpawner.getAmmo()).setEndurance(((BasicAsteroid) obstacleSpawner.getAmmo()).getEndurance() + 1);
			if (obstacleSpawner.getBulletTint().r > 0.2f)
				obstacleSpawner.setBulletTint(obstacleSpawner.getBulletTint().sub(0.1f,0.1f,0.1f,0));
		}
	}

	public void spawn()
	{
		obstacleSpawner.fire();
	}
	
	public void setSpawnRange(Integer min, Integer max)	{
		this.spawnRangeMin = new Integer(min);
		this.spawnRangeMax = new Integer(max);
	}

	public void setSpeedRange(Integer min, Integer max)	{
		this.speedRangeMin = new Integer(min);
		this.speedRangeMax = new Integer(max);
	}

	public void setDegreeRange(Float min, Float max)	{
		this.degreeRangeMin = new Float(min);
		this.degreeRangeMax = new Float(max);
	}

	@Override
	public void stateChanged(GameState stateID) {
		if (stateID == GameState.Game) {
			this.setEnabled(true);
			lastLevelTime = System.currentTimeMillis();
			if (obstacleSpawner.getAmmo() instanceof BasicAsteroid)
				((BasicAsteroid) obstacleSpawner.getAmmo()).setEndurance(startEndurance);
			obstacleSpawner.setBulletTint(Color.WHITE);
		}
		if (stateID == GameState.Menu || stateID == GameState.ShipDestroyed)
			this.setEnabled(false);
	}

	@Override
	void update() {
		if (System.currentTimeMillis() > lastLevelTime + levelTimeSeconds*1000) {
			levelUp();
			lastLevelTime = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() < lastSpawn + spawnDelay)
			return;
		lastSpawn = System.currentTimeMillis();
		Float rotationHolder = obstacleSpawner.getRotation();
		if (spawnRangeMax != spawnRangeMin)
			obstacleSpawner.setPosition(
					new Vector2(new Random().nextInt(spawnRangeMax - spawnRangeMin) + spawnRangeMin,
							obstacleSpawner.getPosition().y));
		if (speedRangeMax != speedRangeMin)
			obstacleSpawner.setBulletSpeed(
					new Vector2(obstacleSpawner.getBulletSpeed().x,
							new Random().nextInt(speedRangeMax - speedRangeMin) + speedRangeMin));
		if (degreeRangeMax != degreeRangeMin) {
			obstacleSpawner.setRotation(new Random().nextFloat() * (degreeRangeMax - degreeRangeMin) + degreeRangeMin + obstacleSpawner.getRotation(), false);
		}
		if (rotated)
			obstacleSpawner.setBulletRotation(new Random().nextFloat()*360);

		spawn();
		obstacleSpawner.setRotation(rotationHolder, false);
	}
}
