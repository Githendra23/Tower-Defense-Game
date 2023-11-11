package com.towerdefense.game.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.towerdefense.game.Coordinate;
import com.towerdefense.game.NoSuchGameException;
import com.badlogic.gdx.math.Rectangle;
import com.towerdefense.game.tower.ATower;

import javax.swing.*;

public abstract class Button {
    protected TextureRegion img;
    protected TextureRegion selectedImg;
    protected Coordinate coords;
    protected boolean isSetPressed = false;
    protected Rectangle hitbox;
    protected ShapeRenderer shapeRenderer;


    public Button(int x, int y, String img, String selectedImg) {
        this.coords = new Coordinate();
        this.img = new TextureRegion(new Texture(img));
        this.selectedImg = new TextureRegion(new Texture(selectedImg));
        this.hitbox = new Rectangle(x, y, this.selectedImg.getRegionWidth(), this.selectedImg.getRegionHeight());
        this.shapeRenderer = new ShapeRenderer();
        this.setCoords(x, y);
    }

    public TextureRegion getTexture() {
        return img;
    }

    public boolean isMouseInside(int mouseX, int mouseY) {
        boolean isMouseX = mouseX >= coords.getAxisX() && mouseX <= coords.getAxisX() + img.getRegionWidth();
        boolean isMouseY = mouseY >= coords.getAxisY() && mouseY <= coords.getAxisY() + img.getRegionHeight();

        return isMouseX && isMouseY;
    }

    public int getAxisX() {
        return coords.getAxisX();
    }

    public int getAxisY() {
        return coords.getAxisY();
    }

    public void setCoords(int x, int y) {
            this.coords.setAxisX(x);
            this.coords.setAxisY(y);
    }

    public boolean isClicked(int mouseX, int mouseY) {
        return Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && this.isMouseInside(mouseX, mouseY);
    }

    public TextureRegion getSelectedImg() {
        return this.selectedImg;
    }

    public void setPressed(boolean is) {
        this.isSetPressed = is;
    }

    public boolean getIsSetPressed() {
        return this.isSetPressed;
    }

    public boolean isOverlaping(ATower tower) {
        return this.hitbox.overlaps(tower.hitbox());
    }

    public void displayHitbox(int x, int y, SpriteBatch batch) {
        hitbox.x = x;
        hitbox.y = y;

        batch.setColor(Color.RED);

        // Draw the borders using lines
        batch.draw(new TextureRegion(new Texture("white_pixel.png")), x, y, this.selectedImg.getRegionWidth(), 1);
        batch.draw(new TextureRegion(new Texture("white_pixel.png")), x, y + this.selectedImg.getRegionHeight(), this.selectedImg.getRegionWidth(), 1);
        batch.draw(new TextureRegion(new Texture("white_pixel.png")), x, y, 1, this.selectedImg.getRegionHeight());
        batch.draw(new TextureRegion(new Texture("white_pixel.png")), x + this.selectedImg.getRegionWidth(), y, 1, this.selectedImg.getRegionHeight());

        // Reset the color to white
        batch.setColor(Color.WHITE);
    }
}
