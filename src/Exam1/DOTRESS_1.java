//player, block, 구현, 중력, 충돌구현

package Exam1;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DOTRESS_1 extends Application {
	
	private int WIDTH = 1280;
	private int HEIGHT = 720;
	
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	
	private ArrayList<Node> blocks = new ArrayList<Node>();
	
	private Pane gameRoot = new Pane();
	private Pane inGameRoot = new Pane();
	
	private Node player;
	private Point2D playerVelocity = new Point2D(0, 0);
	
	private void MainPage() {
		
		Rectangle bg = new Rectangle(WIDTH, HEIGHT, Color.WHITE); // BG생성
		
		for (int i = 0; i < BlockData.BLOCK1.length; i++) {
			String line = BlockData.BLOCK1[i];
			for (int j = 0; j < line.length(); j++) {
				switch (line.charAt(j)) {
					case '0':
						break;
					case '1':
						Node block = createObject(j*10, i*10, 10, 10, Color.NAVAJOWHITE);
						blocks.add(block);
						break;
				}
			}
		}
		player = createObject(0, 100, 20, 20, Color.BLUE);
		
		gameRoot.getChildren().addAll(bg, inGameRoot);
	}
	
	private Node createObject(int x, int y, int w, int h, Color color) {
		Rectangle object = new Rectangle(w, h);
		object.setTranslateX(x);
		object.setTranslateY(y);
		object.setFill(color);
		
		inGameRoot.getChildren().add(object);
		return object;
	}
	
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
	}
	
	private void update() { // AnimationTimer로 1프레임마다 업데이트
	
	if (isPressed(KeyCode.A) && player.getTranslateX() >= 0) { // A는 좌로이동, x좌표=0이상부터 이동가능
		movePlayerX(-2);	// 1프레임에 2씩이동가능
	}
	if (isPressed(KeyCode.D) && player.getTranslateX() + 20 <= WIDTH) { // D는 우로이동, x좌표=WIDTH값(1280)-Player크기(20)만큼 이동가능
		movePlayerX(2);		//1프레임에 2씩이동가능
	}
	if (playerVelocity.getY() < 10) { 
		playerVelocity = playerVelocity.add(0, 1);	//playerVelocity의 y값이 10보다 작으면 1프레임당 y값 1씩추가
	}
	movePlayerY((int)playerVelocity.getY()); //movePlayerY(int value)에 playerVelocity값 할당 (낙하담당값이라서 연속적으로 실행되야함)
	}

	private void movePlayerX(int value) {
	boolean movingRight = value > 0; // A=false, D=true
	
	for (int i = 0; i < Math.abs(value); i++) {
		for (Node block : blocks) {
			if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
				if (movingRight) {
					if (player.getTranslateX() + 10 == block.getTranslateX()) { //우측방향 이동시 player의 x값은 block의 x값-10까지
						return;
					}
				} else {
					if (player.getTranslateX() == block.getTranslateX() + 10) { //좌측방향 이동시 player의 x값은 block의 x값+10까지
						return;
					}
				}
			}
		}
		player.setTranslateX(player.getTranslateX()+(movingRight ? 1 : -1));
	}
}

	private void movePlayerY(int value) {
		boolean movingDown = value > 0;
				
				for (int i = 0; i < Math.abs(value); i++) {
					for (Node block : blocks) {
						if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
							if (movingDown) { //movingDown 참
								if (player.getTranslateY() + 20 == block.getTranslateY()) { //player가 낙하중일때 Y값이 block의 Y값보다 20(플레이어크기)크면 player의 translateY를 -1
									player.setTranslateY(player.getTranslateY() - 1);
//									canJump=true;
									return;
								}
							} else { //movingDown 거짓
								if (player.getTranslateY() == block.getTranslateY() + 10) {
									return;
								}
							}
						}
					}
					player.setTranslateY(player.getTranslateY()+(movingDown ? 1 : -1));
				}
			}
	
	
	
	
	@Override
	public void start(Stage primaryStage) {
		
		MainPage();
		
		Scene scene = new Scene(gameRoot);
		scene.setOnKeyPressed(event -> keys.put(event.getCode(), true));
		scene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		primaryStage.setTitle("DOTRESS");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update();
			}
			
		};
		timer.start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
