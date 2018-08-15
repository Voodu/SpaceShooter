package space.shooter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by piola on 2017-05-27.
 */
public class ScalableAnimation extends GameObject implements Drawable
{
	private Vector2 position, scale, size;
	private Float rotation;
	private TextureAtlas textures;
	private final Integer texturesCount;
	private Integer frame;
	private Boolean visible = true;
	private Color tint;

	public ScalableAnimation(TextureAtlas textures)	{
		super();
		this.textures = textures;
		texturesCount = frame = textures.getRegions().size;
		frame /= 2;
		size = new Vector2( textures.getRegions().get(frame).getRegionWidth(),
							textures.getRegions().get(frame).getRegionHeight());
		scale = new Vector2(1f, 1f);
		position = new Vector2(0f, 0f);
		rotation = new Float(0);
		tint = new Color(Color.WHITE);
	}

	public ScalableAnimation(ScalableAnimation orig)	{
		super();
		this.textures = new TextureAtlas();
		this.texturesCount = orig.texturesCount;
		for(int i=0; i<texturesCount; i++)
			this.textures.addRegion (orig.textures.getRegions().get(i).name, orig.textures.getRegions().get(i));
		this.frame = orig.frame;
		this.size = new Vector2(orig.size);
		this.scale = new Vector2(orig.scale);
		this.position = new Vector2(orig.position);
		this.rotation = new Float(orig.rotation);
		this.tint = new Color(orig.tint);
	}

	public void frameUp() throws IndexOutOfBoundsException {
		if (frame + 1 < texturesCount)
			frame++;
		else
			throw new IndexOutOfBoundsException();
	}

	public void frameDown() throws IndexOutOfBoundsException{
		if (frame - 1 >= 0)
			frame--;
		else
			throw new IndexOutOfBoundsException();
	}

	public void move(Vector2 direction)	{
		position.x += direction.x;
		position.y += direction.y;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position)
	{
		this.position = new Vector2(position);
	}

	public Float getRotation() {
		return rotation;
	}

	public void setRotation(Float rotation) {
		this.rotation = -rotation;
	}

	public Vector2 getScale() {
		return scale;
	}

	public void setScale(Vector2 scale)	{
		this.scale = new Vector2(scale);
		size = new Vector2( size.x * scale.x,	size.y * scale.y);
	}

	public Integer getFrame() {
		return frame;
	}

	public void setFrame(int value) throws IllegalArgumentException	{
		if (value >= 0 && value < texturesCount)
			frame = value;
		else
			throw new IllegalArgumentException("Frame does not exits in supplied TextureAtlas");
	}

	public Vector2 getSize() {
		return size;
	}

	public TextureAtlas getTextures() {
		return textures;
	}

	public Integer getTexturesCount() {
		return texturesCount;
	}



	public Color getTint() {
		return tint;
	}

	public void setTint(Color tint) {
		this.tint = new Color(tint);
	}

	public void setVisible(boolean v)
	{
		visible = v;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void draw(SpriteBatch batch)	{
		Color prev = batch.getColor();
		batch.setColor(tint);
		TextureRegion region = textures.getRegions().get(frame);
		Texture texture = region.getTexture();
		batch.draw(texture, position.x, position.y, size.x/2f, size.y/2f, size.x, size.y, 1f, 1f, rotation, region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);
		batch.setColor(prev);
	}

	@Override
	void update() {
	}
}
