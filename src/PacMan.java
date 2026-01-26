import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class PacMan extends JPanel implements ActionListener, KeyListener{
    class Block{
        char type;
        int x;
        int y;
        int height;
        int width;
        Image image;

        int startX;
        int startY;
        char direction = 'R';
        int velX = 0;
        int velY = 0;
        int speed;

        Block(char type, int x, int y, int height, int width, Image image){
            this.type = type;
            this.x = x;
            this.y = y;
            this.height = height;
            this.width = width;
            this.image = image;
            this.startX = x;
            this.startY = y;
            this.speed = tileSize/4;
        }

        void updateVelocity(){
            if(this.direction == 'U'){
                this.velX = 0;
                this.velY = -speed;
            }
            else if(this.direction == 'D'){
                this.velX = 0;
                this.velY = speed;
            }
            else if(this.direction == 'R'){
                this.velX = speed;
                this.velY = 0;
            }
            else if(this.direction == 'L'){
                this.velX = -speed;
                this.velY = 0;
            }
        }

        void tryTurn(char direction){
            if(!isGameStarted){
                isGameStarted = true;
                gameLoop.start();
            }
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += this.velX;
            this.y += this.velY;
            for(Block wall : walls){
                if(collision(this, wall)){
                    this.x -= this.velX;
                    this.y -= this.velY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void reset(){
            this.x = this.startX;
            this.y = this.startY;
        }
    }
    

    int rowCount = 21;
    int columnCount = 19;
    int tileSize = 32;
    int boardWidth = columnCount * tileSize;
    int boardHeight = rowCount * tileSize;
    int score = 0;
    int lives = 3;
    boolean isGameStarted = false;
    boolean isGameOver = false;
    boolean isPaused = false;
    String playerName = "";
    boolean nameSubmitted = false;
    GameWindow gameWindow;

    Image wallImage;
    Image blueGhostImageU, blueGhostImageD, blueGhostImageR, blueGhostImageL;
    Image redGhostImageU, redGhostImageD, redGhostImageR, redGhostImageL;
    Image orangeGhostImageU, orangeGhostImageD, orangeGhostImageR, orangeGhostImageL;
    Image pinkGhostImageU, pinkGhostImageD, pinkGhostImageR, pinkGhostImageL;

    Image pacmanUpImage;
    Image pacmanDownImage;
    Image pacmanRightImage;
    Image pacmanLeftImage;
    Image cherryImage;
    Image powerFoodImage;
    Image scaredGhostImage;

    static final int powerFoodCount = 2;
    static final int powerDuration = 200;
    static final int powerModeBlink = 40;
    static final int ghostScore = 55;
    int powerModeTicksLeft = 0;

    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;
    Block cherry;
    
    ArrayList<int[]> foodTilePositions;
    int cherryLifetimeTicks = 200;
    int cherryTicksLeft = 0;
    int ticksUntilNextCherrySpawn = 400;
    Leaderboard leaderboard;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'R', 'L'};
    Random random = new Random();

    private static final int easyMap = 0, mediumMap = 1, hardMap = 2;
    private int currentLevelIndex = easyMap;

    private static final String[][] mapLevel = {
        { // Easy
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XXXX X X X XXXX X",
            "O                 O",
            "X XX X X X X X XX X",
            "X    X       X    X",
            "X XX X XX XX X XX X",
            "X                 X",
            "X XXXX XXrXX XXXX X",
            "O       bpo       O",
            "X XX X XX XX X XX X",
            "X    X       X    X",
            "X XX X XXXXX X XX X",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "O                 O",
            "XXXXXXXXXXXXXXXXXXX"
        },

        { // Medium
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX X X X X X XX X",
            "X                 X",
            "X XX X X X X X XX X",
            "X        r        X",
            "X XX X XX XX X XX X",
            "X  X X       X X  X",
            "XXXX X XX XX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "X  X X       X X  X",
            "X XX X XXXXX X XX X",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
        },

        { // Hard
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "X XX XXXX XXXX XX X",
            "X  X X       X X  X",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "X  X X       X X  X",
            "X XX X XXXXX X XX X",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX" 
        }
    };

    private String[] tileMap;

    public PacMan() throws Exception{
        this(null);
    }
    
    public PacMan(GameWindow gameWindow) throws Exception{
        this.gameWindow = gameWindow;
        leaderboard = new Leaderboard();
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        setLayout(null);

        wallImage = new ImageIcon(getClass().getResource("/Images/wall.png")).getImage();
        blueGhostImageU = new ImageIcon(getClass().getResource("/Images/blueGhostU.png")).getImage();
        blueGhostImageD = new ImageIcon(getClass().getResource("/Images/blueGhostD.png")).getImage();
        blueGhostImageR = new ImageIcon(getClass().getResource("/Images/blueGhostR.png")).getImage();
        blueGhostImageL = new ImageIcon(getClass().getResource("/Images/blueGhostL.png")).getImage();
        redGhostImageU = new ImageIcon(getClass().getResource("/Images/redGhostU.png")).getImage();
        redGhostImageD = new ImageIcon(getClass().getResource("/Images/redGhostD.png")).getImage();
        redGhostImageR = new ImageIcon(getClass().getResource("/Images/redGhostR.png")).getImage();
        redGhostImageL = new ImageIcon(getClass().getResource("/Images/redGhostL.png")).getImage();
        orangeGhostImageU = new ImageIcon(getClass().getResource("/Images/orangeGhostU.png")).getImage();
        orangeGhostImageD = new ImageIcon(getClass().getResource("/Images/orangeGhostD.png")).getImage();
        orangeGhostImageR = new ImageIcon(getClass().getResource("/Images/orangeGhostR.png")).getImage();
        orangeGhostImageL = new ImageIcon(getClass().getResource("/Images/orangeGhostL.png")).getImage();
        pinkGhostImageU = new ImageIcon(getClass().getResource("/Images/pinkGhostU.png")).getImage();
        pinkGhostImageD = new ImageIcon(getClass().getResource("/Images/pinkGhostD.png")).getImage();
        pinkGhostImageR = new ImageIcon(getClass().getResource("/Images/pinkGhostR.png")).getImage();
        pinkGhostImageL = new ImageIcon(getClass().getResource("/Images/pinkGhostL.png")).getImage();
        
        pacmanUpImage = new ImageIcon(getClass().getResource("/Images/pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("/Images/pacmanDown.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("/Images/pacmanRight.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("/Images/pacmanLeft.png")).getImage();
        cherryImage = new ImageIcon(getClass().getResource("/Images/cherry.png")).getImage();
        powerFoodImage = new ImageIcon(getClass().getResource("/Images/powerFood.png")).getImage();
        scaredGhostImage = new ImageIcon(getClass().getResource("/Images/scaredGhost.png")).getImage();

        loadMap();
        for(Block ghost : ghosts){
            setDirection(ghost, directions[random.nextInt(4)]);
        }

        gameLoop = new Timer(50, this);
    }

    public void loadMap(){
        tileMap = mapLevel[currentLevelIndex];
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();
        cherry = null;
        powerModeTicksLeft = 0;
        ticksUntilNextCherrySpawn = 400;
        foodTilePositions = new ArrayList<int[]>();

        for(int r=0; r<rowCount; r++){
            String row = tileMap[r];

            for(int c=0; c<columnCount; c++){
                char tileMapChar = row.charAt(c);
                int x = c*tileSize;
                int y = r*tileSize;

                if(tileMapChar=='X'){
                    Block wall = new Block('w' , x, y, tileSize, tileSize, wallImage);
                    walls.add(wall);
                }
                else if(tileMapChar=='P'){
                    pacman = new Block('P' , x, y, tileSize, tileSize, pacmanRightImage);
                }
                else if(tileMapChar=='b'){
                    Block ghost = new Block('b', x, y, tileSize, tileSize, blueGhostImageL);
                    ghosts.add(ghost);
                }
                else if(tileMapChar=='r'){
                    Block ghost = new Block('r', x, y, tileSize, tileSize, redGhostImageU);
                    ghosts.add(ghost);
                }
                else if(tileMapChar=='o'){
                    Block ghost = new Block('o', x, y, tileSize, tileSize, orangeGhostImageR);
                    ghosts.add(ghost);
                }
                else if(tileMapChar=='p'){
                    Block ghost = new Block('p', x, y, tileSize, tileSize, pinkGhostImageD);
                    ghosts.add(ghost);
                }
                else if(tileMapChar==' '){
                    foodTilePositions.add(new int[]{c, r});
                }
            }
        }

        ArrayList<int[]> validFoodPositions = new ArrayList<int[]>();
        for (int[] pos : foodTilePositions) {
            int c = pos[0], r = pos[1];
            if (r >= 0 && r < rowCount && c >= 0 && c < columnCount && tileMap[r].charAt(c) == ' ') {
                validFoodPositions.add(pos);
            }
        }

        HashSet<Integer> powerIndices = new HashSet<Integer>();
        while (powerIndices.size() < powerFoodCount && powerIndices.size() < validFoodPositions.size()) {
            powerIndices.add(random.nextInt(validFoodPositions.size()));
        }
        for (int i = 0; i < validFoodPositions.size(); i++) {
            int[] pos = validFoodPositions.get(i);
            int c = pos[0], r = pos[1];
            int x = c * tileSize;
            int y = r * tileSize;
            if (powerIndices.contains(i)) {
                Block powerFood = new Block('F', x, y, tileSize, tileSize, powerFoodImage);
                foods.add(powerFood);
            } else {
                Block food = new Block('f', x + 14, y + 14, 4, 4, null);
                foods.add(food);
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        g.setColor(Color.WHITE);
        for(Block food:foods){
            if(food.image != null){
                g.drawImage(food.image, food.x, food.y, food.width, food.height, null);
            } else {
                g.fillRect(food.x, food.y, food.width, food.height);
            }
        }

        if(cherry != null){
            g.drawImage(cherry.image, cherry.x, cherry.y, cherry.width, cherry.height, null);
        }

        for(Block ghost:ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for(Block wall:walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setFont(new Font("Verdana", Font.PLAIN, 19));
        if(isGameOver){
            g.setColor(Color.RED);
            g.drawString("Game Over! Score: " + String.valueOf(score), tileSize/2, (tileSize/2)+(tileSize/5));
        }
        else{
            g.setColor(Color.YELLOW);
            g.drawString("x" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, (tileSize/2)+(tileSize/5));
        }
        
        if(isPaused && !isGameOver){
            drawPauseMenu(g);
        }
        
        if(isGameOver){
            drawGameOverMenu(g);
        }
    }
    
    private void drawPauseMenu(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, boardWidth, boardHeight);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        int menuWidth = 350;
        int menuHeight = 400;
        int menuX = (boardWidth - menuWidth) / 2;
        int menuY = (boardHeight - menuHeight) / 2;
        
        g2d.setColor(new Color(20, 20, 20));
        g2d.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        g2d.setColor(new Color(255, 255, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        
        g2d.setFont(new Font("Verdana", Font.BOLD, 42));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "PAUSED";
        int titleX = menuX + (menuWidth - fm.stringWidth(title)) / 2;
        int titleY = menuY + 60;
        g2d.setColor(new Color(255, 255, 0));
        g2d.drawString(title, titleX, titleY);
        
        int infoY = titleY + 50;
        g2d.setFont(new Font("Verdana", Font.BOLD, 20));
        g2d.setColor(new Color(200, 200, 200));
        String scoreText = "Score: " + score;
        fm = g2d.getFontMetrics();
        int scoreX = menuX + (menuWidth - fm.stringWidth(scoreText)) / 2;
        g2d.drawString(scoreText, scoreX, infoY);
        
        String livesText = "Lives: " + lives;
        fm = g2d.getFontMetrics();
        int livesX = menuX + (menuWidth - fm.stringWidth(livesText)) / 2;
        g2d.drawString(livesText, livesX, infoY + 35);
        
        int optionsY = infoY + 80;
        g2d.setFont(new Font("Verdana", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        
        String[] options = {
            "ESC / P - Resume",
            "R - Restart Game",
            "M - Main Menu"
        };
        
        for(int i = 0; i < options.length; i++){
            fm = g2d.getFontMetrics();
            int optionX = menuX + (menuWidth - fm.stringWidth(options[i])) / 2;
            g2d.drawString(options[i], optionX, optionsY + (i * 35));
        }
    }

    private void drawGameOverMenu(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, boardWidth, boardHeight);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        int menuWidth = 400;
        int menuHeight = 450;
        int menuX = (boardWidth - menuWidth) / 2;
        int menuY = (boardHeight - menuHeight) / 2;
        
        g2d.setColor(new Color(20, 20, 20));
        g2d.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        
        g2d.setFont(new Font("Verdana", Font.BOLD, 42));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "GAME OVER";
        int titleX = menuX + (menuWidth - fm.stringWidth(title)) / 2;
        int titleY = menuY + 60;
        g2d.setColor(new Color(255, 0, 0));
        g2d.drawString(title, titleX, titleY);
        
        int infoY = titleY + 50;
        g2d.setFont(new Font("Verdana", Font.BOLD, 20));
        g2d.setColor(new Color(200, 200, 200));
        String scoreText = "Score: " + score;
        fm = g2d.getFontMetrics();
        int scoreX = menuX + (menuWidth - fm.stringWidth(scoreText)) / 2;
        g2d.drawString(scoreText, scoreX, infoY);
        
        int nameY = infoY + 50;
        g2d.setFont(new Font("Verdana", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        String nameLabel = "Enter your name:";
        fm = g2d.getFontMetrics();
        int nameLabelX = menuX + (menuWidth - fm.stringWidth(nameLabel)) / 2;
        g2d.drawString(nameLabel, nameLabelX, nameY);
        
        int inputY = nameY + 30;
        int inputWidth = 280;
        int inputHeight = 35;
        int inputX = menuX + (menuWidth - inputWidth) / 2;
        
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRoundRect(inputX, inputY, inputWidth, inputHeight, 5, 5);
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(inputX, inputY, inputWidth, inputHeight, 5, 5);
        
        g2d.setFont(new Font("Verdana", Font.PLAIN, 18));
        g2d.setColor(Color.WHITE);
        String displayName = playerName.isEmpty() ? "Type here..." : playerName;
        if(playerName.isEmpty()){
            g2d.setColor(new Color(100, 100, 100));
        }
        fm = g2d.getFontMetrics();
        int textX = inputX + (inputWidth - fm.stringWidth(displayName)) / 2;
        int textY = inputY + (inputHeight + fm.getAscent()) / 2 - 2;
        g2d.drawString(displayName, textX, textY);
        
        if(!nameSubmitted){
            int cursorX = textX + (playerName.isEmpty() ? 0 : fm.stringWidth(playerName));
            g2d.setColor(Color.WHITE);
            g2d.fillRect(cursorX, textY - fm.getAscent() + 2, 2, fm.getAscent() - 4);
        }
        
        int optionsY = inputY + inputHeight + 40;
        g2d.setFont(new Font("Verdana", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        
        String[] options;
        if(!nameSubmitted){
            options = new String[]{
                "",
                "ENTER - Submit Name"
            };
        }
        else{
            options = new String[]{
                "",
                "ENTER / R - Restart",
                "M - Main Menu"
            };
        }
        
        for(int i = 0; i < options.length; i++){
            fm = g2d.getFontMetrics();
            int optionX = menuX + (menuWidth - fm.stringWidth(options[i])) / 2;
            g2d.drawString(options[i], optionX, optionsY + (i * 35));
        }
    }


    void setDirection(Block b, char direction){
        b.direction = direction;
        b.updateVelocity();
        updateGhostImage(b);
    }

    public void move(){
        pacman.x += pacman.velX;
        pacman.y += pacman.velY;
        wrapper(pacman);

        for(Block wall : walls){
            if(collision(pacman, wall)){
                pacman.x -= pacman.velX;
                pacman.y -= pacman.velY;
                break;
            }
        }

        for(Block ghost : ghosts){
            if(ghost.velX == 0 && ghost.velY == 0) setDirection(ghost, getRandomDirection(ghost));
            if(collision(pacman, ghost)){
                if(powerModeTicksLeft > 0){
                    score += ghostScore;
                    ghost.reset();
                    ghost.velX = 0;
                    ghost.velY = 0;
                } else {
                    lives--;
                    if(lives == 0){
                        isGameOver = true;
                        playerName = "";
                        nameSubmitted = false;
                        return;
                    }
                    resetPosition();
                    gameLoop.stop();
                    isGameStarted = false;
                    return;
                }
            }

            int choice = 0;
            for(char direction : directions){
                if(canMove(ghost, direction)) choice++;
            }
            if(choice>2){
                setDirection(ghost, getRandomDirection(ghost));
            }
            ghost.x += ghost.velX;
            ghost.y += ghost.velY;
            wrapper(ghost);

            for(Block wall : walls){
                if(collision(ghost, wall)){
                    ghost.x -= ghost.velX;
                    ghost.y -= ghost.velY;
                    setDirection(ghost, getRandomDirection(ghost));
                }
            }
        }

        Block foodEaten = null;
        for(Block food : foods){
            if(collision(pacman, food)){
                foodEaten = food;
                score += 5;
                if(food.type == 'F'){
                    powerModeTicksLeft = powerDuration;
                    score += 15;
                }
            }
        }
        foods.remove(foodEaten);

        if(powerModeTicksLeft > 0){
            powerModeTicksLeft--;
        }
        for(Block ghost : ghosts){
            updateGhostImage(ghost);
        }

        if(cherry != null){
            if(collision(pacman, cherry)){
                score += 100;
                cherry = null;
                ticksUntilNextCherrySpawn = 300 + random.nextInt(300);
            }
            else{
                cherryTicksLeft--;
                if(cherryTicksLeft <= 0){
                    cherry = null;
                    ticksUntilNextCherrySpawn = 300 + random.nextInt(300);
                }
            }
        }
        else{
            ticksUntilNextCherrySpawn--;
            if(ticksUntilNextCherrySpawn <= 0){
                trySpawnCherry();
                ticksUntilNextCherrySpawn = 300 + random.nextInt(300);
            }
        }

        if(foods.isEmpty()){
            if(currentLevelIndex == easyMap){
                currentLevelIndex = mediumMap;
            }
            else if(currentLevelIndex == mediumMap){
                currentLevelIndex = hardMap;
            }
            loadMap();
            resetPosition();
        }
    }

    private void trySpawnCherry(){
        HashSet<String> occupied = new HashSet<String>();
        for(Block food : foods){
            int tc = food.type == 'F' ? food.x / tileSize : (food.x - 14) / tileSize;
            int tr = food.type == 'F' ? food.y / tileSize : (food.y - 14) / tileSize;
            occupied.add(tc + "," + tr);
        }
        ArrayList<int[]> eaten = new ArrayList<int[]>();
        for(int[] pos : foodTilePositions){
            if(!occupied.contains(pos[0] + "," + pos[1])){
                eaten.add(pos);
            }
        }
        if(eaten.isEmpty()) return;
        int[] pos = eaten.get(random.nextInt(eaten.size()));
        int x = pos[0] * tileSize;
        int y = pos[1] * tileSize;
        cherry = new Block('c', x, y, tileSize, tileSize, cherryImage);
        cherryTicksLeft = cherryLifetimeTicks;
    }

    void updateGhostImage(Block ghost){
        boolean showScared = powerModeTicksLeft > 0;
        boolean blinkPhase = powerModeTicksLeft > 0 && powerModeTicksLeft <= powerModeBlink;
        if(showScared && (!blinkPhase || (powerModeTicksLeft / 5) % 2 != 0)){
            ghost.image = scaredGhostImage;
            return;
        }
        if(ghost.type == 'b'){
            if(ghost.direction == 'U') ghost.image = blueGhostImageU;
            else if(ghost.direction == 'D') ghost.image = blueGhostImageD;
            else if(ghost.direction == 'L') ghost.image = blueGhostImageL;
            else if(ghost.direction == 'R') ghost.image = blueGhostImageR;
        }
        else if(ghost.type == 'r'){
            if(ghost.direction == 'U') ghost.image = redGhostImageU;
            else if(ghost.direction == 'D') ghost.image = redGhostImageD;
            else if(ghost.direction == 'L') ghost.image = redGhostImageL;
            else if(ghost.direction == 'R') ghost.image = redGhostImageR;
        }
        else if(ghost.type == 'o'){
            if(ghost.direction == 'U') ghost.image = orangeGhostImageU;
            else if(ghost.direction == 'D') ghost.image = orangeGhostImageD;
            else if(ghost.direction == 'L') ghost.image = orangeGhostImageL;
            else if(ghost.direction == 'R') ghost.image = orangeGhostImageR;
        }
        else if(ghost.type == 'p'){
            if(ghost.direction == 'U') ghost.image = pinkGhostImageU;
            else if(ghost.direction == 'D') ghost.image = pinkGhostImageD;
            else if(ghost.direction == 'L') ghost.image = pinkGhostImageL;
            else if(ghost.direction == 'R') ghost.image = pinkGhostImageR;
        }
    }


    public boolean collision(Block a, Block b){
        return a.x<b.x+b.width && a.x+a.width>b.x && a.y<b.y+b.height && a.y+a.height>b.y;
    }

    public void resetPosition(){
        pacman.reset();
        pacman.velX = 0;
        pacman.velY = 0;
        for(Block ghost : ghosts){
            ghost.reset();
            ghost.velX = 0;
            ghost.velY = 0;
        }
    }

    public char reverseDirection(char direction){
        if(direction == 'U') return 'D';
        if(direction == 'D') return 'U';
        if(direction == 'R') return 'L';
        else return 'R';
    }

    public boolean canMove(Block b, char direction){
        int prevX = b.x;
        int prevY = b.y;
        int testVel = tileSize/4;

        if(direction == 'U') b.y -= testVel;
        else if(direction == 'D') b.y += testVel;
        else if(direction == 'R') b.x += testVel;
        else if(direction == 'L') b.x -= testVel;

        for(Block wall : walls){
            if(collision(wall, b)){
                b.x = prevX;
                b.y = prevY;
                return false;
            }
        }
        b.x = prevX;
        b.y = prevY;
        return true;
    }

    public char getRandomDirection(Block b){
        char[] possibleDirections = new char[4];
        int count = 0;
        for(char direction : directions){
            if(canMove(b, direction)){
                if(direction != reverseDirection(b.direction)){
                    possibleDirections[count] = direction;
                    count++;
                }
            }
        }
        if(count == 0){
            possibleDirections[count] = reverseDirection(b.direction);
            count++;
        }
        return possibleDirections[random.nextInt(count)];
    }

    public void wrapper(Block b){
        if(b.x < 0) b.x = boardWidth - b.width;
        else if(b.x + b.width > boardWidth) b.x = 0;
        if(b.y < 0) b.y = boardHeight - b.height;
        else if(b.y + b.height > boardHeight) b.y = 0;
    }

    public void actionPerformed(ActionEvent e){
        if(!isPaused && !isGameOver){
            move();
        }
        repaint();
        if(isGameOver){
            gameLoop.stop();
        }
    }


    public void keyTyped(KeyEvent e){
        if(isGameOver && !nameSubmitted){
            char c = e.getKeyChar();
            if(c == '\b'){
                if(playerName.length() > 0){
                    playerName = playerName.substring(0, playerName.length() - 1);
                    repaint();
                }
            } else if(c == '\n' || c == '\r'){
                submitName();
            } else if(Character.isLetterOrDigit(c) || c == ' ' || c == '-' || c == '_'){
                if(playerName.length() < 20){
                    playerName += c;
                    repaint();
                }
            }
        }
    }
    public void keyReleased(KeyEvent e){

    }
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P){
            if(isGameStarted && !isGameOver){
                togglePause();
            }
            return;
        }

        if(isGameOver){
            if(!nameSubmitted){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    submitName();
                }
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    nameSubmitted = true;
                    repaint();
                }
                else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                    if(playerName.length() > 0){
                        playerName = playerName.substring(0, playerName.length() - 1);
                        repaint();
                    }
                }
            }
            else{
                if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_R){
                    restartGame();
                }
                else if(e.getKeyCode() == KeyEvent.VK_M && gameWindow != null){
                    returnToMainMenu();
                }
            }
        }

        if(!isPaused && !isGameOver){
            if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
                pacman.tryTurn('U');
            }
            else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S){
                pacman.tryTurn('D');
            }
            else if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
                pacman.tryTurn('R');
            }
            else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
                pacman.tryTurn('L');
            }
            
            if(pacman.direction == 'U'){
                pacman.image = pacmanUpImage;
            }
            else if(pacman.direction == 'D'){
                pacman.image = pacmanDownImage;
            }
            else if(pacman.direction == 'R'){
                pacman.image = pacmanRightImage;
            }
            else if(pacman.direction == 'L'){
                pacman.image = pacmanLeftImage;
            }
        }
        
        if(isPaused && !isGameOver){
            if(e.getKeyCode() == KeyEvent.VK_R){
                restartGame();
            }
            else if(e.getKeyCode() == KeyEvent.VK_M && gameWindow != null){
                returnToMainMenu();
            }
        }
    }
    
    private void togglePause(){
        isPaused = !isPaused;
        if(isPaused){
            gameLoop.stop();
        }
        else{
            gameLoop.start();
        }
        repaint();
    }
    
    private void submitName(){
        if(!nameSubmitted){
            nameSubmitted = true;
            if(playerName != null && !playerName.trim().isEmpty()){
                try{
                    leaderboard.addScore(playerName.trim(), score);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            repaint();
        }
    }
    
    private void restartGame(){
        currentLevelIndex = easyMap;
        loadMap();
        resetPosition();
        lives = 3;
        score = 0;
        isGameOver = false;
        isPaused = false;
        isGameStarted = false;
        playerName = "";
        nameSubmitted = false;
        gameLoop.stop();
        repaint();
    }
    
    private void returnToMainMenu(){
        if(gameWindow != null){
            isPaused = false;
            isGameStarted = false;
            gameLoop.stop();
            gameWindow.showScreen("HOME");
        }
    }
}
