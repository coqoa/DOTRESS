// DOTRESS_1 에서 player, block, 구현, 중력, 충돌구현
// 주석달았음 모르는부분 더 공부해서 주석달기
//좌우이동 간단하게 만들고 10차이면 x값 +10, y값-10으로 이동하게만들자 
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

public class DOTRESS_2 extends Application {
	
	private int WIDTH = 1280;
	private int HEIGHT = 720;
	
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>();
	
	private ArrayList<Node> blocks = new ArrayList<Node>();
	
	private Pane gameRoot = new Pane();
	private Pane inGameRoot = new Pane();
	
	private Node player;
	private Point2D playerVelocity = new Point2D(0, 0);
	
	private void MainPage() {
		
		Rectangle bg = new Rectangle(WIDTH, HEIGHT, Color.IVORY); // 창 크기, 바탕화면 색깔 
		
		for (int i = 0; i < BlockData.BLOCK1.length; i++) { //범위 : 0 ~ BlockData클래스의 BLOC1 '배열의 길이'  length;
			String line = BlockData.BLOCK1[i];	
			for (int j = 0; j < line.length(); j++) {		// 범위 :0 ~ BlockData클래스의 각 배열당 '문자열의 길이' length();
				switch (line.charAt(j)) {	// line의 문자열을 하나하나 체크해서 0이면 빈값 1이면 블럭만들기 (
					case '0':
						break;
					case '1':
						Node block = createObject(j*10, i*10, 10, 10, Color.LIGHTGRAY);
						blocks.add(block);
						break;
				}
			}
		}
		player = createObject(0, 100, 20, 20, Color.DODGERBLUE);
		
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
	
	if (isPressed(KeyCode.LEFT) && player.getTranslateX() >= 0) { // LEFT키를 누르고 player객체의 x값이 0보다 크거나 같다면 movePlayerX의 매개변수로 -2를 입력
		movePlayerX(-2);	
	}
	if (isPressed(KeyCode.RIGHT) && player.getTranslateX() + 20 <= WIDTH) { // RIGHT키를 누르고 player객체의 x값+20(player객체크기)이 맵의 WIDTH보다 작거나 같다면 movePlayerX의 매개변수로 2를 입력
		movePlayerX(2);		
	}
	if (playerVelocity.getY() < 10) { 
		playerVelocity = playerVelocity.add(0, 1);	//playerVelocity의 y값이 10보다 작으면 1프레임당 y값 1씩추가
	}
	movePlayerY((int)playerVelocity.getY()); //movePlayerY(int value)에 playerVelocity값 할당 (낙하담당값이라서 연속적으로 실행되야함)
	}

	private void movePlayerX(int value) {
		boolean movingRight = value > 0; // LEFT=false, RIGHT=true
		
			for (int i = 0; i < Math.abs(value); i++) {
				for (Node block : blocks) { //만들어서 blocks라는 객체로 만들어놓은 ArrayList에 넣어둔 block를 하나씩 player와 비
					if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
						if (movingRight) {	//RIGHT
							if (player.getTranslateX() + 20 == block.getTranslateX()) { //우측방향 이동시 player의 x값은 block의 x값-10까지
								return;
							}
						} else  {	//LEFT
							if (player.getTranslateX() == block.getTranslateX() + 10) { //좌측방향 이동시 player의 x값은 block의 x값+10까지
								return;
							}
						}
					}
				}
				player.setTranslateX(player.getTranslateX()+(movingRight ? 1 : -1)); // RIGHT버튼을 누르면 player객체의 x위치를 +1만큼씩, LEFT버튼을 누르면 x위치를 -1만큼씩이
			}
		}

	private void movePlayerY(int value) {
		boolean movingDown = value > 0;
				
			for (int i = 0; i < Math.abs(value); i++) {
				for (Node block : blocks) {
					if (player.getBoundsInParent().intersects(block.getBoundsInParent())) {
						if (movingDown) { 
							if (player.getTranslateY() + 20 == block.getTranslateY()) { //player가 낙하중일때 Y값이 block의 Y값보다 20(플레이어크기)크면 player의 translateY를 -1
								player.setTranslateY(player.getTranslateY() - 1);
								return;
							}
						}
					}
				}
				player.setTranslateY(player.getTranslateY()+1);
			}
		}
	
	
	
	
	@Override
	public void start(Stage primaryStage) {
		
		MainPage();
		
		Scene scene = new Scene(gameRoot);
		scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
		scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
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
