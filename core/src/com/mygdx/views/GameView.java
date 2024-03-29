package com.mygdx.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.commandhandlers.GameViewInput;
import com.mygdx.event.DifficultySchema.Difficulty;
import com.mygdx.event.EndlessSalvos;
import com.mygdx.event.IEvent;
import com.mygdx.game.ScoreManager;
import com.mygdx.game.TokenManager;
import com.mygdx.util.CountDown;
import com.mygdx.util.International;
import com.mygdx.world.World;

import static com.mygdx.game.BTGGame.*;
import static com.mygdx.util.International.Label.TOUCH;

public class GameView extends ScreenAdapter {
    private final float WORLD_HEIGHT = 100;
    private IEvent event;
    private Difficulty difficulty;
    private World world;
    private Viewport viewport;
    private Stage ui;
    private InputMultiplexer multiplexer;
    private CountDown countDown;
    private GameViewInput gameInput;

    private Label scoreLabel;
    private Label countdownLabel;
    private Button pauseButton;

    public GameView() {
        viewport = new ExtendViewport(WORLD_HEIGHT, WORLD_HEIGHT);
        ui = new Stage(new ScreenViewport());
        gameInput = new GameViewInput();
        multiplexer = new InputMultiplexer(gameInput.getDetector(), ui);
//        multiplexer = new InputMultiplexer(ui, gameInput.getDetector());

        /* UI settings */
        Stack root = new Stack();
        root.setFillParent(true);
        root.setDebug(true);
        Table firstLayer = new Table();
        Container<Label> countdownCnt = new Container<Label>();
        firstLayer.setSkin(skin);

        scoreLabel = new Label("0", skin, "gameScoreLabel");
        countdownLabel = new Label("TOUCH", skin, "countdownLabel");
        pauseButton = new Button(skin, "pauseButton");

        float pauseButtonSize = 0.09f*Gdx.graphics.getHeight();
        //pauseButton.setSize(pauseButtonSize, pauseButtonSize);

        firstLayer.row().expand();
        firstLayer.add(scoreLabel).top().left().padLeft(50);
        firstLayer.add(pauseButton).top().right().width(pauseButtonSize).height(pauseButtonSize);

        root.add(firstLayer);
        countdownCnt.setActor(countdownLabel);
        countdownCnt.center();
        root.add(countdownCnt);

        ui.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            if (!countDown.isOver() && !countDown.isRunning()) {
                countDown.start();
            }
            }
        });
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.launchPauseView();
            }
        });
        ui.addActor(root);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
        countdownLabel.setVisible(true);
        countDown.reset();
        TokenManager.setPaused(true);
    }


    @Override
    public void pause() {
        game.launchPauseView();
    }

    @Override
    public void hide() {
        TokenManager.setPaused(false);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ui.getViewport().update(width, height, true);
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

    }

    public void start (Difficulty difficulty) {
        countDown = new CountDown(3);
        game.setScreen(this);
        this.difficulty = difficulty;
        world = new World(viewport.getWorldWidth(), viewport.getWorldHeight());
        gameInput.setWorld(world);
        event = new EndlessSalvos(world, difficulty);
        scoreLabel.setText(" ");
        event.start();
    }

    @Override
    public void render(float delta) {
        /* UPDATE */
        countDown.update(delta);
        if (countDown.isOver()) {
            countdownLabel.setVisible(false);
            world.update(delta);
            scoreLabel.setText(String.valueOf(getScore()));
            if (event.isOver()) {
                int highScore = ScoreManager.getHighScore(difficulty);
                int score = getScore();
                boolean isHighScore = score > highScore;
                handleScore(score, isHighScore);
                game.launchEndOfGameView(difficulty, score, isHighScore);
            }
        } else {
            if (countDown.isRunning()) {
                countdownLabel.setText(countDown.getSecondStr());
            } else {
                countdownLabel.setText(International.get(TOUCH));
            }
        }

        /* RENDER */
        spriteBatch.begin();
        world.render(spriteBatch, viewport.getCamera());
        spriteBatch.end();
        ui.draw();
    }

    private void handleScore(int score, boolean isHighScore) {
        if (isHighScore) {
            ScoreManager.setHighScore(difficulty, score);
        }
    }

    public int getScore() {
        return world.getGarry().getAttackAvoided();
    }

    @Override
    public void dispose() {
        ui.dispose();
    }

    public void resumeGame() {
        game.setScreen(this);
    }
}


