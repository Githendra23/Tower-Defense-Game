package com.towerdefense.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.towerdefense.game.UI.Button;
import com.towerdefense.game.UI.CloseButton;
import com.towerdefense.game.UI.Menu;
import com.towerdefense.game.UI.pauseMenu;
import com.towerdefense.game.enemy.Giant;
import com.towerdefense.game.enemy.Zombie;

public class TowerDefense extends ApplicationAdapter {
	private int coins = 0;
	private int frameCount;
	private SpriteBatch batch;
	private BitmapFont font;
	private Zombie zombie;
	private Giant giant;
	private Castle castle;

	private boolean isPaused = false;
	private Texture menuPause;
	private Menu pausemenu;
	private Button closeButton;
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;

	@Override
	public void create () {

		batch = new SpriteBatch();
		castle = new Castle(2000);
		zombie = new Zombie();
		giant = new Giant();
		pausemenu = new pauseMenu();
		closeButton = new CloseButton(500, 500);

		// map
		map = new TmxMapLoader().load("map/map.tmx");

		mapRenderer = new OrthogonalTiledMapRenderer(map);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0); // Center the camera
		camera.update();

		Pixmap pixmapMouse = new Pixmap(Gdx.files.internal("mouse.png")); // Make sure the path is correct
		int xHotspot = 15, yHotspot = 15;
		Cursor cursor = Gdx.graphics.newCursor(pixmapMouse, xHotspot, yHotspot);
		pixmapMouse.dispose(); // We don't need the pixmap anymore
		Gdx.graphics.setCursor(cursor);

		font = new BitmapFont();
		font.setColor(1, 1, 1, 1); // Set the font color
	}

	float X = 200, Y = 200;
	@Override
	public void render () {
		int mouseX = Gdx.input.getX();
		int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
		frameCount++;

		// Render the map
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mapRenderer.setView(camera);
		mapRenderer.render();

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			isPaused = !isPaused;
		}

		batch.begin();
		// display FPS
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);

		font.draw(batch, "Mouse coords: " + mouseX + "X, " + mouseY + "Y", 10, Gdx.graphics.getHeight() - 30);

		batch.draw(zombie.getImg(), 100, 100);
		batch.draw(giant.getImg(), X, Y);
		batch.draw(castle.getImg(), Gdx.graphics.getWidth() - 200, ((float) Gdx.graphics.getHeight() / 2) - 150);

		if (!isPaused) {
			// all movements should be inside this condition
			if (frameCount >= 24 ) {
				X += giant.getSpeed() + 3;
				frameCount = 0;
			}

		}
		else {
			float centerX = Gdx.graphics.getWidth() / 2f - (pausemenu.getAxisX()) / 2f;
			float centerY = Gdx.graphics.getHeight() / 2f - (pausemenu.getAxisY()) / 2f;

			batch.draw(pausemenu.getImg(),centerX, centerY, 730, 370);
			batch.draw(closeButton.getTexture(), 500, 500);

			if (closeButton.isClicked(mouseX, mouseY)) {
				System.out.println("done");
			}
		}

		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		map.dispose();
		mapRenderer.dispose();
	}
}