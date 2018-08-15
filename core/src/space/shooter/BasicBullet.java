package space.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by piola on 2017-05-27.
 */
public class BasicBullet extends Ammunition
{
	public BasicBullet(TextureAtlas textures)
	{
		super(textures);
	}

	public BasicBullet(TextureAtlas textures, Vector2 scale, Vector2 speed)
	{
		super(textures, scale, speed);
	}

	public BasicBullet(BasicBullet original) {
		super(original);
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
		if (col instanceof Ship || col instanceof BasicBullet)
			return;
		destroyGraphics();
		GameObject.destroy(this);
	}
}
