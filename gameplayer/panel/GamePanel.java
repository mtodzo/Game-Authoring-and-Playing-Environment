package gameplayer.panel;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import com.sun.javafx.tools.packager.Log;
import authoring.frontend.exceptions.MissingPropertiesException;
import engine.sprites.FrontEndSprite;
import engine.sprites.towers.CannotAffordException;
import engine.sprites.towers.FrontEndTower;
import frontend.PropertiesReader;
import frontend.UIFactory;
import gameplayer.GameplayerAlert;
import gameplayer.screen.GameScreen;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.shape.Circle;

/**
 * @Author Alexi Kontos & Andrew Arnold
 */




public class GamePanel extends Panel{


    private final GameScreen GAME_SCREEN;
    private FrontEndTower towerSelected;
    private Map<String,String> GAMEPLAYER_PROPERTIES;
    private PropertiesReader PROP_READ;
    private boolean towerPlaceMode = false;
    private Pane spriteAdd;
    private final UIFactory UIFACTORY;
    private Boolean towerClick = false;
    private Circle rangeIndicator;
    private ScrollPane scroll;
    private FrontEndTower clickedTower;
    private GridPane grid;
	private GameplayerAlert ALERT;

    private final String DEFAULT_BACKGROUND_FILE_PATH;
    private String CONSTANTS_FILE_PATH;
    private boolean backgroundSet;

    public GamePanel(GameScreen gameScreen) {
	GAME_SCREEN = gameScreen;
	GAMEPLAYER_PROPERTIES = GAME_SCREEN.getGameplayerProperties();
	DEFAULT_BACKGROUND_FILE_PATH = GAMEPLAYER_PROPERTIES.get("defaultBackgroundFilePath");
	PROP_READ = new PropertiesReader();
	UIFACTORY = new UIFactory();
	towerSelected =  null;
	CONSTANTS_FILE_PATH = GAMEPLAYER_PROPERTIES.get("constantsFilePath");
	backgroundSet = false;
    }

    @Override
    public void makePanel() {

	Pane gamePane = new Pane();
	scroll = new ScrollPane(gamePane);
	gamePane.setId(GAMEPLAYER_PROPERTIES.get("gamePanelID"));

	gamePane.setOnMouseClicked(e -> {
	    try {
		handleMouseInput(e.getX(), e.getY());
	    } catch (MissingPropertiesException e1) {
		// TODO Auto-generated catch block
	    }
	});

	spriteAdd = gamePane;
	PANEL = scroll;
    }

    private boolean setBackgroundImage(String backgroundFilePath) {
	if(PANEL == null) {
	    makePanel();
	}
	Bounds centerBounds = scroll.getViewportBounds();
	if(centerBounds.getHeight() ==0 && centerBounds.getWidth() == 0) {
	    return false;
	}
	ImageView imageView;

	double imageWidth = centerBounds.getWidth() * Double.parseDouble(GAMEPLAYER_PROPERTIES.get("sandboxWidthMultiplier"));
	double imageHeight = centerBounds.getHeight() * Double.parseDouble(GAMEPLAYER_PROPERTIES.get("sandboxHeightMultiplier"));
	spriteAdd.setMaxHeight(imageHeight);
	spriteAdd.setMaxWidth(imageWidth);
	try {
	    imageView = new ImageView(new Image(backgroundFilePath, imageWidth,imageHeight , false, false));
	} catch (IllegalArgumentException e){
	    imageView = new ImageView(new Image("file:" + DEFAULT_BACKGROUND_FILE_PATH,imageWidth, imageHeight, false, false));
	}
	spriteAdd.getChildren().add(imageView);
	return true;
    }

    public boolean setPath(Map<String, List<Point>> imageMap, String backgroundImageFilePath, int pathSize, int width, int height, boolean transparent) {
	backgroundSet = setBackgroundImage(backgroundImageFilePath);
	if(backgroundSet) {
	    PathMaker pathMaker = new PathMaker(GAMEPLAYER_PROPERTIES, GAME_SCREEN.getScreenManager());
	    grid = pathMaker.initGrid(imageMap, backgroundImageFilePath, pathSize, width, height, transparent);

	    if (spriteAdd == null) {
		makePanel();
	    }
	    spriteAdd.getChildren().add(grid);
	}
	return backgroundSet;
    }


    private void resetCursor() {
	GAME_SCREEN.getScreenManager().getStageManager().getScene().setCursor(Cursor.DEFAULT);
	spriteAdd.setOnMouseEntered(e -> GAME_SCREEN.getScreenManager().getStageManager().getScene().setCursor(Cursor.DEFAULT));
	spriteAdd.setOnMouseExited(e -> GAME_SCREEN.getScreenManager().getStageManager().getScene().setCursor(Cursor.DEFAULT));
	spriteAdd.setOnMouseMoved(null);
	removeTowerRangeIndicator();

    }

    /**
     * 
     * @param tower will be null if tower placement is canceled
     */
    public void towerSelected(FrontEndTower tower) {
	if(tower!= null && tower != towerSelected ) {
	    towerSelected = tower;
	    ImageView towerImage = tower.getImageView();
	    towerPlaceMode = true;
	    ImageCursor cursor = new ImageCursor(tower.getImageView().getImage());

	    spriteAdd.setOnMouseEntered(e -> {
		GAME_SCREEN.getScreenManager().getStageManager().getScene().setCursor(cursor);
		addRangeIndicator(tower); });

	    spriteAdd.setOnMouseExited(e -> {
		GAME_SCREEN.getScreenManager().getStageManager().getScene().setCursor(Cursor.DEFAULT);
		spriteAdd.getChildren().remove(rangeIndicator); });

	    spriteAdd.setOnMouseMoved(e -> {
		rangeIndicator.setCenterX(e.getX()+(towerImage.getImage().getWidth()/2));
		rangeIndicator.setCenterY(e.getY()+(towerImage.getImage().getHeight()/2)); });
	} else {
	    //maybe make a new towerContructor which creates a null tower?
	    resetCursor();
	    towerPlaceMode = false;
	}
    }

    private void addTowerImageViewAction(FrontEndTower tower) {
	ImageView towerImage = tower.getImageView();
	towerImage.setOnMouseClicked(args ->{
	    GAME_SCREEN.towerClickedOn(tower);
	    addRangeIndicator(tower);
	    towerClick = true;
	    clickedTower = tower;
	   // System.out.println("towerClick is TRUE  AND clicked tower = "+clickedTower);
	});
    }
    
    private void removeTowerRangeIndicator() {
	spriteAdd.getChildren().remove(rangeIndicator);
	towerSelected = null;
    }

    private void addRangeIndicator(FrontEndTower tower) {
	spriteAdd.getChildren().remove(rangeIndicator);
	ImageView towImage = tower.getImageView();
	rangeIndicator = new Circle(towImage.getX()+(towImage.getImage().getWidth()/2),
		towImage.getY()+(towImage.getImage().getHeight()/2), tower.getTowerRange());
	rangeIndicator.setStroke(Color.ORANGE);
	try {
	    String opacity = PROP_READ.findVal(CONSTANTS_FILE_PATH, "TowerRangeIndicatorOpacity");
	    rangeIndicator.setOpacity(Double.parseDouble(opacity));
	    spriteAdd.getChildren().add(rangeIndicator);
	    towImage.toFront();
	} catch (MissingPropertiesException e) {
	    Log.debug(e);
		ALERT = new GameplayerAlert(e.getMessage());
	   // System.out.println("Constants property file not found");

	}
    }

    public void addSprite(FrontEndSprite sprite) {
	//	ImageView test = new ImageView(new Image("file:Images/bomb.png"));
	//	test.setTranslateX(120);
	//	test.setTranslateY(30);
	//	test.setFitHeight(20);
	//	test.setFitWidth(20);
	//	spriteAdd.getChildren().add(test);
	ImageView spriteImage = sprite.getImageView();
	spriteImage.setLayoutX(-spriteImage.getFitWidth()/2);
	spriteImage.setLayoutY(-spriteImage.getFitHeight()/2);
	spriteAdd.getChildren().add(sprite.getImageView());
    }

    public void removeSprite(FrontEndSprite sprite) {
	spriteAdd.getChildren().remove(sprite.getImageView());
    }

    public void removeTower(FrontEndTower tower) {
	spriteAdd.getChildren().remove(tower.getImageView());
	removeTowerRangeIndicator();
    }

    public void exitTowerPlace() {
	towerPlaceMode = false;
    }

    public void handleMouseInput(double x, double y) throws MissingPropertiesException {
	if(towerPlaceMode) {
	    try {
		Point position = new Point((int)x,(int)y);
		FrontEndTower newTower = GAME_SCREEN.placeTower(towerSelected, position);
		ImageView towerImage = newTower.getImageView();
		addTowerImageViewAction(newTower);
		spriteAdd.getChildren().add(towerImage);
		resetCursor();
		towerPlaceMode = false;
	    }
	    catch(CannotAffordException e){
		Log.debug(e);
		ALERT = new GameplayerAlert(e.getMessage());
		//GameScreen popup for cannot afford
		resetCursor();
	    }
	    GAME_SCREEN.blankGamePanelClick();
	}
	else if (!towerClick) {
	    if (clickedTower != null && !spriteAdd.getChildren().contains(rangeIndicator)) {
		GAME_SCREEN.clickToShoot(clickedTower, x, y);
	    }
	    GAME_SCREEN.blankGamePanelClick();
	    removeTowerRangeIndicator();
	}
	towerClick = false;
    }

}
