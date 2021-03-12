package io.innocent.dream.drawing.screen;

import org.joml.Vector2f;

import io.innocent.dream.InnocentDream;
import io.innocent.dream.drawing.Text;
import io.innocent.dream.drawing.button.Button;
import io.innocent.dream.drawing.button.ButtonAction;
import io.innocent.dream.worldBuilder.WorldFiles;

public class WorldScreen extends AbstractScreen implements ButtonAction {
	
	private static final String EMPTY_SLOT = " - Empty Slot - ";
	public static int worldint;

	private Text titleText;
	private Button world1Btn;
	private Button world2Btn;
	private Button world3Btn;
	private Button world4Btn;
	private Button world5Btn;
	private Button backBtn;
	
	public void prepareToRender() {
		world1Btn = new Button(Text.ofString(EMPTY_SLOT).withScale(0.25f),
				0, 64, 256, 32, "1");
		world2Btn = new Button(Text.ofString(EMPTY_SLOT).withScale(0.25f),
					0, 32, 256, 32, "2");
		world3Btn = new Button(Text.ofString(EMPTY_SLOT).withScale(0.25f),
					0, 0, 256, 32, "3");
		world4Btn = new Button(Text.ofString(EMPTY_SLOT).withScale(0.25f),
				0, -32, 256, 32, "4");
		world5Btn = new Button(Text.ofString(EMPTY_SLOT).withScale(0.25f),
				0, -64, 256, 32, "5");
		backBtn = new Button(Text.ofString("Back").withScale(0.25f),
				0, -96, 64, 32);
		titleText = new Text("World Saves", 1.0f, new Vector2f(0, 96));
		if (WorldFiles.testForWorldFolder(1)) world1Btn.setText(Text.ofString(" World 1 ").withScale(0.25f));
		if (WorldFiles.testForWorldFolder(2)) world2Btn.setText(Text.ofString(" World 2 ").withScale(0.25f));
		if (WorldFiles.testForWorldFolder(3)) world3Btn.setText(Text.ofString(" World 3 ").withScale(0.25f));
		if (WorldFiles.testForWorldFolder(4)) world4Btn.setText(Text.ofString(" World 4 ").withScale(0.25f));
		if (WorldFiles.testForWorldFolder(5)) world5Btn.setText(Text.ofString(" World 5 ").withScale(0.25f));
	}

	public void renderScreen() {
		titleText.render(-(titleText.getWidth() / 2) + 16, 96);
		world1Btn.testButtonPress(this);
		world2Btn.testButtonPress(this);
		world3Btn.testButtonPress(this);
		world4Btn.testButtonPress(this);
		world5Btn.testButtonPress(this);
		backBtn.testButtonPress(this::back);
		world1Btn.drawButton();
		world2Btn.drawButton();
		world3Btn.drawButton();
		world4Btn.drawButton();
		world5Btn.drawButton();
		backBtn.drawButton();
	}

	@Override
	public void buttonClicked(Button button) {
		int worldBtnSel = Integer.parseInt(button.getActionStr());
		if (WorldFiles.testForWorldFolder(worldBtnSel)) {
			worldint = worldBtnSel;
		}
		InnocentDream.MAIN_GAME = new MainGameScreen(worldBtnSel);
		InnocentDream.CURRENT_SCREEN = InnocentDream.MAIN_GAME;
	}
	
	public void back(Button button) {
		InnocentDream.CURRENT_SCREEN = InnocentDream.MAIN_MENU;
	}

}
