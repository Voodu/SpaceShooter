package space.shooter;

import com.badlogic.gdx.math.Circle;

/**
 * Created by piola on 2017-05-30.
 */
public interface Collideable {
	Circle getCollider();
	void onCollision(Collideable col);
}
