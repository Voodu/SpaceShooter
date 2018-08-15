package space.shooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import sun.security.util.Debug;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by piola on 2017-05-29.
 */
public abstract class GameObject {
	private Boolean enabled = true;
	private static ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	private static ArrayList<Collideable> collidingObjects = new ArrayList<Collideable>();
	private static Stack<GameObject> trash = new Stack<GameObject>();
	private static Stack<GameObject> news = new Stack<GameObject>();
	private static SpriteBatch globalBatch = new SpriteBatch();

	public GameObject()	{
		news.push(this);
		if (this instanceof Collideable)
			collidingObjects.add((Collideable)this);
	}

	abstract void update();

	public final static void updateWorld()	{
		while (!news.empty())
			gameObjects.add(news.pop());
		for (int i=gameObjects.size()-1; i >= 0; i--) {
			if (gameObjects.get(i).enabled == false)
				continue;
			gameObjects.get(i).update();
			if (gameObjects.get(i) instanceof Drawable)
				updateGraphics((Drawable)gameObjects.get(i));
			if (gameObjects.get(i) instanceof Collideable)
				updateCollisions((Collideable)gameObjects.get(i));
		}

		while (!trash.empty()) {
			try{
				gameObjects.remove(trash.peek());
			}catch (Exception e){
				new Debug().println("Exception while removing gameObject: " + e.toString());
				new Debug().println(e.getStackTrace().toString());
			}
			if (trash.peek() instanceof Collideable)
				try	{collidingObjects.remove(trash.peek());
				}catch (Exception e){
					new Debug().println("Exception while removing collideable: " + e.toString());
					new Debug().println(e.getStackTrace().toString());
				}
			trash.pop();
		}
		//new Debug().println("objects: " + gameObjects.size());
		//new Debug().println("collidingObjects: " + collidingObjects.size());
	}

	private final static void updateGraphics(Drawable dro)	{
		if (!dro.isVisible())
			return;
		globalBatch.begin();
		dro.draw(globalBatch);
		globalBatch.end();
	}

	private final static void updateCollisions(Collideable co)	{
		for (Collideable c:collidingObjects) {
			if (!c.equals(co))
				if (co.getCollider().overlaps(c.getCollider())) {
					co.onCollision(c);
					break;
				}
		}
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public final static void destroy(GameObject go)
	{
		trash.push(go);
	}

}
