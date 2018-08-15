package space.shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

public class MainGame extends ApplicationAdapter
{
	Ship mainShip;
	Texture background1, background2;
	SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		background1 = new Texture(Gdx.files.internal("Sprites/starfield.png"));
		background2 = new Texture(Gdx.files.internal("Sprites/starfield.png"));
		mainShip =  new Ship(new TextureAtlas(Gdx.files.internal("Sprites/ship/ship.atlas")),
				new Vector2(200,200),	new Vector2(0.25f, 0.5f));
		BasicBullet basicBullet = new BasicBullet(
				new TextureAtlas(Gdx.files.internal("Sprites/bullets/bullet.atlas")),
				new Vector2(0.25f, 0.15f),	new Vector2(0, 5));
		BasicAsteroid basicAsteroid = new BasicAsteroid(
				new TextureAtlas(Gdx.files.internal("Sprites/asteroids/asteroid.atlas")),
				new Vector2(0.20f, 0.30f),	new Vector2(0, 50));
		basicBullet.setTint(Color.PINK);
		Cannon basicCannon = new Cannon(basicBullet, 400, 0f);

		mainShip.setPosition(new Vector2(Gdx.graphics.getWidth()/2 - mainShip.getGraphics().getSize().x/2, 0));
		mainShip.attachCannons(basicCannon, 3);
		Cannon basicSpawner = new Cannon(basicAsteroid, 200,0f);
		basicSpawner.setRotation(180f, false);
		basicSpawner.setBulletRotation(0f);
		ObstacleSpawner obsSpawner = new ObstacleSpawner(basicSpawner, 500);
		obsSpawner.setSpeedRange(5, 15);
		obsSpawner.setDegreeRange(-5f, 5f);
		obsSpawner.setEnabled(false);
		UIManager.instance().addListener(obsSpawner);
		UIManager.instance().addListener(mainShip);
		mainShip.addListener(UIManager.instance());
		mainShip.addListener(obsSpawner);
		GameObject.destroy(basicCannon);
		GameObject.destroy(basicSpawner);
		posX1 = 0;
		posX2 = Gdx.graphics.getHeight();
	}
	Integer posX1, posX2;

	@Override
	public void render() {
		Gdx.graphics.setTitle( "Cosmo attack [FPS: " + Gdx.graphics.getFramesPerSecond() + "]");
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		posX1-=2;
		posX2-=2;
		if (posX1 < -Gdx.graphics.getHeight())
			posX1= Gdx.graphics.getHeight();
		if (posX2 < -Gdx.graphics.getHeight())
			posX2= Gdx.graphics.getHeight();
		batch.begin();
		batch.draw(background1, 0,posX1, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(background2, 0,posX2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
		GameObject.updateWorld();
	}

	@Override
	public void dispose() {

	}
}
