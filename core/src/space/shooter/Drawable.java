package space.shooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by piola on 2017-05-29.
 */
public interface Drawable {
	void draw(SpriteBatch batch);
	boolean isVisible();
}
