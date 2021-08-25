package com.watabou.noosa;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.watabou.glscripts.Script;
import com.watabou.glwrap.Blending;
import com.watabou.glwrap.Quad;
import com.watabou.glwrap.Texture;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Point;

//essentially contains a libGDX text input field, plus a PD-rendered background
public class TextInput extends Component {

	private Stage stage;
	private Container container;
	private TextField textField;

	private Skin skin;

	private NinePatch bg;

	public TextInput( NinePatch bg, boolean multiline, int size ){
		super();
		this.bg = bg;
		add(bg);

		//use a custom viewport here to ensure stage camera matches game camera
		Viewport viewport = new Viewport() {};
		viewport.setWorldSize(Game.width, Game.height);
		viewport.setScreenBounds(0, Game.bottomInset, Game.width, Game.height);
		viewport.setCamera(new OrthographicCamera());
		stage = new Stage(viewport);
		Game.inputHandler.addInputProcessor(stage);

		container = new Container<TextField>();
		stage.addActor(container);
		container.setTransform(true);

		skin = new Skin(FileUtils.getFileHandle(Files.FileType.Internal, "gdx/textfield.json"));

		TextField.TextFieldStyle style = skin.get(TextField.TextFieldStyle.class);
		style.font = Game.platform.getFont(size, "", false, false);
		style.background = null;
		textField = multiline ? new TextArea("", style) : new TextField("", style);
		textField.setProgrammaticChangeEvents(true);

		if (!multiline) textField.setAlignment(Align.center);

		textField.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				BitmapFont f = Game.platform.getFont(size, textField.getText(), false, false);
				TextField.TextFieldStyle style = textField.getStyle();
				if (f != style.font){
					style.font = f;
					textField.setStyle(style);
				}
			}
		});

		if (!multiline){
			textField.setTextFieldListener(new TextField.TextFieldListener(){
				public void keyTyped (TextField textField, char c){
					if (c == '\r' || c == '\n'){
						enterPressed();
					}
				}
			});
		}

		container.setActor(textField);
		stage.setKeyboardFocus(textField);
		Gdx.input.setOnscreenKeyboardVisible(true);
	}

	public void enterPressed(){
		//do nothing by default
	};

	public void setText(String text){
		textField.setText(text);
		textField.setCursorPosition(textField.getText().length());
	}

	public void setMaxLength(int maxLength){
		textField.setMaxLength(maxLength);
	}

	public String getText(){
		return textField.getText();
	}

	@Override
	protected void layout() {
		super.layout();

		float contX = x;
		float contY = y;
		float contW = width;
		float contH = height;

		if (bg != null){
			bg.x = x;
			bg.y = y;
			bg.size(width, height);

			contX += bg.marginLeft();
			contY += bg.marginTop();
			contW -= bg.marginHor();
			contH -= bg.marginVer();
		}

		float zoom = Camera.main.zoom;
		Camera c = camera();
		if (c != null){
			zoom = c.zoom;
			Point p = c.cameraToScreen(contX, contY);
			contX = p.x/zoom;
			contY = p.y/zoom;
		}

		container.align(Align.topLeft);
		container.setPosition(contX*zoom, (Game.height-(contY*zoom)));
		container.size(contW*zoom, contH*zoom);
	}

	@Override
	public void update() {
		super.update();
		stage.act(Game.elapsed);
	}

	@Override
	public void draw() {
		super.draw();
		Quad.releaseIndices();
		Script.unuse();
		Texture.clear();
		stage.draw();
		Quad.bindIndices();
		Blending.useDefault();
	}

	@Override
	public synchronized void destroy() {
		super.destroy();
		if (stage != null) {
			stage.dispose();
			skin.dispose();
			Game.inputHandler.removeInputProcessor(stage);
			Gdx.input.setOnscreenKeyboardVisible(false);
			Game.platform.updateSystemUI();
		}
	}
}
