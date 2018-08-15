package space.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

/**
 * Created by piola on 2017-05-31.
 */
public class UIManager extends GameObject implements Drawable, StateInformator{
	private static UIManager instance = new UIManager();

	private ArrayList<StateInformator> listeners = new ArrayList<StateInformator>();
	private Boolean visible = true;
	private Stage UILayer = new Stage();
	private Integer score = new Integer(0);

	//UI controls
	final Label title;
	final TextButton buttonStartGame;
	final TextButton buttonExitGame;
	final Label labelScore;
	//-----------

	private UIManager() {
		super();
		float buttonWidth = 200f, buttonHeight=60f;
		Skin skin = new Skin(Gdx.files.internal("UISkins/uiskin.json"));
		//menu content
		title = new Label("Cosmo attack", skin);
		title.setWidth(buttonWidth*2);
		title.setHeight(buttonHeight*2);
		title.setPosition((Gdx.graphics.getWidth()- 2*buttonWidth)/2, (Gdx.graphics.getHeight()+ 3*buttonHeight)/2);
		title.setFontScale(3f);
		title.setAlignment(Align.center);
		buttonStartGame = new TextButton("Start game", skin, "default");
		buttonExitGame = new TextButton("Exit", skin, "default");
		buttonStartGame.setSize(buttonWidth, buttonHeight);
		buttonExitGame.setSize(buttonWidth, buttonHeight);
		buttonStartGame.setPosition((Gdx.graphics.getWidth()- buttonWidth)/2, (Gdx.graphics.getHeight()- buttonHeight)/2);
		buttonExitGame.setPosition((Gdx.graphics.getWidth()- buttonWidth)/2, (Gdx.graphics.getHeight()- 3*buttonHeight)/2);
		buttonStartGame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				informListeners(GameState.Game);
				setupUI(GameState.Game);
			}
		});
		buttonExitGame.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.exit();
			}
		});
		//~menu content
		//game content
		labelScore = new Label("Score: 0", skin);
		labelScore.setWidth(buttonWidth);
		labelScore.setHeight(buttonHeight);
		labelScore.setPosition(0, Gdx.graphics.getHeight()-buttonHeight);
		labelScore.setFontScale(2f);
		//~game content
		setupUI(GameState.Menu);
		Gdx.input.setInputProcessor(UILayer);
	}
	public static UIManager instance() {
		return instance;
	}

	private void setupUI(GameState state)	{
		UILayer.dispose();
		UILayer = new Stage();
		if (state == GameState.Menu || state == GameState.ShipDestroyed)
		{
			UILayer.addActor(title);
			UILayer.addActor(buttonStartGame);
			UILayer.addActor(buttonExitGame);
			Gdx.input.setInputProcessor(UILayer);
		}
		if (state == GameState.Game) {
			score = 0;
			labelScore.setText("Score: 0");
		}
		UILayer.addActor(labelScore);
	}

	public void addListener(StateInformator sl)
	{
		listeners.add(sl);
	}

	public void addPoints(Integer num)	{
		score += num;
		labelScore.setText("Score: " + score.toString());
	}

	public void setVisible(Boolean vis)
	{
		visible = vis;
	}

	private void informListeners(GameState state)	{
		for(StateInformator si:listeners)
			si.stateChanged(state);
	}

	@Override
	public void stateChanged(GameState stateID) {
		setupUI(stateID);
	}

	@Override
	public void draw(SpriteBatch batch) {
		UILayer.draw();
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	void update() {

	}
}
