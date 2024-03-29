package livesnooker.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import livesnooker.model.Ball;
import livesnooker.model.BallType;
import livesnooker.model.Player;

public class ScoreBoard {
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;

	private Player player1 = new Player();
	private Player player2 = new Player();
	private ScoreBoardPanel scoreboard;
	private int frameNum = 0;
	private int turn = PLAYER_1;
	private BallType targetBallType = null;

	public ScoreBoard() {
		this.scoreboard = new ScoreBoardPanel();
		player1.setMatchScore(12);
		player2.setMatchScore(13);
		player1.setName("O'Sullivan");
		player2.setName("Dingjunhui");
		player1.setFrameScore(147);
	}

	public JPanel getScoreBoardPanel() {
		return scoreboard;
	}

	class ScoreBoardPanel extends JPanel {
		Color purple = new Color(150, 0, 200);
		Color yellow = Color.yellow;
		Color white = new Color(220, 220, 220);
		Color black = new Color(50, 50, 50);
		Color blue = new Color(0, 0, 100);

		int width;
		int height;
		int vmiddle;
		int hmiddle;
		int matchScoreAreaWidth;
		int frameScoreAreaWidth;

		public ScoreBoardPanel() {
			this.setSize(new Dimension(600, 50));
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			calcValues();
			drawBackground(g2d);
			drawMatchScore(g2d);
			drawTargetBall(g2d);
			drawBreak(g2d);
		}

		private void drawBreak(Graphics2D g) {
			int brk = 0;
			if (turn == PLAYER_1) {
				brk = player1.getCurrentBreak();
			} else {
				brk = player2.getCurrentBreak();
			}
			 if (brk <= 0)
				return;
			Font font = new Font("Calibri", Font.BOLD, 20);
			FontMetrics fm = getFontMetrics(font);
			g.setColor(white);
			g.setFont(font);
			String brkStr = "BREAK " + brk;
			int w = fm.stringWidth(brkStr);
			int v_start = height - (vmiddle - 20 +6) / 2;
			g.drawString(brkStr, width - w - 30, v_start);
		}

		private void calcValues() {
			width = getWidth();
			height = getHeight();
			vmiddle = height / 2;
			hmiddle = width / 2;
			matchScoreAreaWidth = width / 5;
			frameScoreAreaWidth = width / 5;
		}

		private void drawBackground(Graphics2D g) {
			g.setColor(blue);
			g.fillRect(0, 0, width - 1, height - 1);
			g.setColor(Color.black);
			g.drawRect(0, 0, width - 1, height - 1);
			g.setColor(purple);
			g.fillRect(1, 1, width - 2, vmiddle - 1);
			g.setColor(yellow);
			g.fillRect((width - matchScoreAreaWidth) / 2, 1,
					matchScoreAreaWidth, vmiddle - 1);
			g.setColor(white);
			g.drawLine(1, vmiddle, width - 2, vmiddle);
		}

		private void drawMatchScore(Graphics2D g) {

			if (player1 == null || player2 == null) {
				return;
			}
			// total match
			Font font = new Font("Calibri", Font.BOLD, 20);
			FontMetrics fm = getFontMetrics(font);
			g.setColor(black);
			g.setFont(font);
			String totalMatch = "(" + frameNum + ")";
			int totalMatchWidth = fm.stringWidth(totalMatch);
			int v_start = (vmiddle - 4 - font.getSize()) / 2 + font.getSize();
			g.drawString(totalMatch, (width - totalMatchWidth) / 2, v_start);
			// match score
			font = new Font("Arial", Font.BOLD, 20);
			fm = getFontMetrics(font);
			String matchStr = "" + player1.getMatchScore();
			int matchWidth = fm.stringWidth(matchStr);
			g.setFont(font);
			g.drawString(matchStr, (width - totalMatchWidth) / 2 - matchWidth
					- 8, v_start);
			matchStr = "" + player2.getMatchScore();
			g.drawString(matchStr, (width + totalMatchWidth) / 2 + 8, v_start);
			// frame score
			g.setColor(white);
			String frameStr = "" + player1.getFrameScore();
			int frameWidth = fm.stringWidth(frameStr);
			g.drawString(frameStr, (width - matchScoreAreaWidth) / 2
					- frameWidth - 10, v_start);
			g.drawString("" + player2.getFrameScore(),
					(width + matchScoreAreaWidth) / 2 + 10, v_start);

			// player name
			g.drawString(player1.getName().toUpperCase(), 30, v_start);
			int nameWidth = fm.stringWidth(player2.getName().toUpperCase());
			g.drawString(player2.getName().toUpperCase(), width - 30
					- nameWidth, v_start);

			// draw turn
			g.setColor(yellow);
			if (turn == PLAYER_1) {
				g.fillOval(height / 8, height / 7, height / 4, height / 4);
			} else {
				g.fillOval(width - 3 * height / 8, height / 7, height / 4,
						height / 4);
			}
		}

		private void drawTargetBall(Graphics2D g) {
			if (targetBallType == null)
				return;
			Ellipse2D.Double ball = new Ellipse2D.Double(0, 0, Ball.RADIUS * 2,
					Ball.RADIUS * 2);
			double y = vmiddle + vmiddle / 2 - Ball.RADIUS;
			double x = Ball.RADIUS;
			ball.y = y;
			if (targetBallType == BallType.COLOR_BALL) {
				for (int i = BallType.YELLOW_BALL.getTypeValue(); i <= BallType.BLACK_BALL
						.getTypeValue(); i++) {
					g.setColor(PlayGround.ballColors[i]);
					ball.x = x + (i - BallType.YELLOW_BALL.getTypeValue()) * 3
							* Ball.RADIUS;
					g.fill(ball);
				}
			} else {
				g
						.setColor(PlayGround.ballColors[targetBallType
								.getTypeValue()]);
				ball.x = x;
				g.fill(ball);
			}
		}
	}

	public void refresh() {
		this.scoreboard.repaint();
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public BallType getTargetBallType() {
		return targetBallType;
	}

	public void setTargetBallType(BallType targetBallType) {
		this.targetBallType = targetBallType;
	}

	public ScoreBoardPanel getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(ScoreBoardPanel scoreboard) {
		this.scoreboard = scoreboard;
	}

	public int getFrameNum() {
		return frameNum;
	}

	public void setFrameNum(int frameNum) {
		this.frameNum = frameNum;
	}
}
