// DOTRESS_1 에서 player, block, 구현, 중력, 충돌구현
// 주석달았음 모르는부분 더 공부해서 주석달기
// 좌우이동 간단하게 만들고 10차이면 x값 +10, y값-10으로 이동하게만들자 ->완료
// 캐논만들기 
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

public class DOTRESS_3 extends Application {
	
	private int WIDTH = 1280; // 창 넓이
	private int HEIGHT = 720; // 창 높이
	
	private HashMap<KeyCode, Boolean> keys = new HashMap<KeyCode, Boolean>(); //입력키에 따라서 true와 false값 출력 
	
	private ArrayList<Node> blocks = new ArrayList<Node>(); //Node값만 들어가는 ArrayList blocks
	
	private Pane gameRoot = new Pane();
	private Pane inGameRoot = new Pane();
	
	private Node player;
	private Point2D playerVelocity = new Point2D(0, 0);
	
	//////
	
	private Node createObject(int x, int y, int w, int h, Color color) {
		Rectangle object = new Rectangle(w, h);
		object.setTranslateX(x);
		object.setTranslateY(y);
		object.setFill(color);
		
		inGameRoot.getChildren().add(object);
		return object;
	}	// object를 생성해서 inGameRoot에 넣어준다
	
	//객체랜덤생성좌표
		int maxNumberX = 1180;
		int minNumberX = 100;
		int maxNumberY = 50;
		int minNumberY = 20;
		int randomNumberX = (int)Math.floor(Math.random()*(maxNumberX-minNumberX+1)+minNumberX);
		int randomNumberY = (int)Math.floor(Math.random()*(maxNumberY-minNumberY+1)+minNumberY);
			//Math.floor()함수는 실수를 입력하면 내림하여 정수를 반환하는 함수
			//Math.random()함수는 0.0이상, 1.0미만 사이의 값을 반환하는 함수 
			//  -> 0~100의 값을 반환받고싶으면 Math.random()*100 + 1을 하면된다
			//Math.random() * (최대값-최소값+1) + 최소값을 하면 최소값~최대값 사이의 값을 반환한다 
	
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
		player = createObject(randomNumberX, randomNumberY, 20, 20, Color.DODGERBLUE);
		
		gameRoot.getChildren().addAll(bg, inGameRoot);
	}	//	bg생성, createObject메서드를 이용해서 player와 block객체 생성 후 gameRoot에 자식으로 넣어
	
	
	
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
//		keys객체를 만드는 HashMap클래스에 쓰이는 메소드 getOrDefault = key가 존재하면 key값을 반환하고 존재하지않으면 디폴트값인 false를 반환
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
							if (player.getTranslateX() + 20 == block.getTranslateX()) { // 우측방향 이동시 player의 x값은 block+20 block의 x값과 같다면
								if(player.getTranslateY()+10 < block.getTranslateY()) { // player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다
									player.setTranslateY(player.getTranslateY()-10);
								}
								return;
							}
						} else  {	//LEFT
							if (player.getTranslateX() == block.getTranslateX() + 10) { // 좌측방향 이동시 player의 x값과 block의 x값+10이 같다면 
								if(player.getTranslateY()+10 < block.getTranslateY()) { // player의 y값+10이 block의 y값보다 작다면 y값을 -10해준다
									player.setTranslateY(player.getTranslateY()-10);
								}
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
							if (player.getTranslateY() + 20 == block.getTranslateY()) { //player가 낙하중일때 Y값이 block의 Y값보다 20(플레이어크기)크면 player의 translateY를 -1(1만큼띄운다)
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
