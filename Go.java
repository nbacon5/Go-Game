import java.util.Scanner;
class Go{
    protected PointStatus[][] board;
    protected boolean[][] visited;
    protected boolean[][] captured;
    protected Scanner scan; 
    protected PointStatus turn = PointStatus.BLACK;
    protected int koX;
    protected int koY;

    public Go(){
        board = new PointStatus[9][9];
        visited = new boolean[9][9];
        captured = new boolean[9][9];
        koX = -1;
        koY = -1;

        for (int i=0;i<board.length;i++){
            for (int j=0;j<board[i].length;j++){
                board[i][j] = PointStatus.EMPTY;
            }
        }
    }

    public void display(){ //CLI
        System.out.print("  ");
        for (int i=1;i<board.length+1;i++){
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i=0;i<board.length;i++){
            System.out.print(i+1 + " ");
            for (int j=0;j<board[i].length;j++){
                System.out.print(board[i][j].symbol() + " ");
            }
            System.out.println("");
        }
    }

    public void play(int x, int y){
        if (board[x][y] == PointStatus.EMPTY){
            board[x][y] = turn;
            boolean capture = captureCheck(x, y);
            if (capture == false){ 
                koX = -1;
                koY = -1;
                if (isCaptured(turn.opposite(), x, y) == true){
                    board[x][y] = PointStatus.EMPTY;
                    resetVisited();
                    return;
                }
                resetVisited();
            }
            else{
                if (koRule(x, y) == true){
                    board[x][y] = PointStatus.EMPTY;
                    resetCaptured();
                    return;
                }
                else{
                    captureStones();
                }
            }
            turn = turn.opposite();
        }
    }

    public boolean koRule(int x, int y){
        int captureCount = 0;
        int koStoneX = -1;
        int koStoneY = -1;
        for (int i=0;i<captured.length;i++){
            for (int j=0;j<captured[i].length;j++){
                if (captured[i][j] == true){
                    koStoneX = i;
                    koStoneY = j;
                    captureCount++;
                }
            }
        }
        if (captureCount == 1){
            if (koX == koStoneX && koY == koStoneY){
                return true;
            }
            else{
                koX = x;
                koY = y;
            }
        }
        else{
            koX = -1;
            koY = -1;
        }
        return false;
    }

    public boolean captureCheck(int x, int y){
        boolean doesCapture = false;
        if (x+1 < 9 && board[x+1][y] == turn.opposite()){ 
            if (isCaptured(turn, x+1, y)){ 
                doesCapture = true;
                captureVisited();
            }
        }
        resetVisited();
        if (x-1 > -1 && board[x-1][y] == turn.opposite()){ 
            if (isCaptured(turn, x-1, y)){ 
                doesCapture = true;
                captureVisited();
            }
        }
        resetVisited();
        if (y-1 > -1 && board[x][y-1] == turn.opposite()){ 
            if (isCaptured(turn, x, y-1)){ 
                doesCapture = true;
                captureVisited();
            }
        }
        resetVisited();
        if (y+1 < 9 && board[x][y+1] == turn.opposite()){ 
            if (isCaptured(turn, x, y+1)){ 
                doesCapture = true;
                captureVisited();
            }
        }
        resetVisited();

        return doesCapture;
    }

    public void resetVisited(){
        for (int i=0;i<visited.length;i++){
            for (int j=0;j<visited[i].length;j++){
                visited[i][j] = false;
            }
        }
    }
    
    public void resetCaptured(){
        for (int i=0;i<captured.length;i++){
            for (int j=0;j<captured[i].length;j++){
                captured[i][j] = false;
            }
        }
    }

    public void captureVisited(){
        for (int i=0;i<visited.length;i++){
            for (int j=0;j<visited[i].length;j++){
                if (visited[i][j] == true){
                   captured[i][j] = true; 
                }
            }
        }
    }

    public void captureStones(){
        for (int i=0;i<captured.length;i++){
            for (int j=0;j<captured[i].length;j++){
                if (captured[i][j] == true){
                    board[i][j] = PointStatus.EMPTY;
                    captured[i][j] = false;
                }
            }
        }
    }

    public boolean isCaptured(PointStatus turn, int x, int y){
        if (x < 0 || x > 8 || y > 8 || y < 0) return true;
        if (board[x][y] == turn) return true;
        if (board[x][y] == PointStatus.EMPTY) return false;
        if (visited[x][y] == true) return true;

        visited[x][y] = true;

        if (!isCaptured(turn, x+1, y)) return false; //down    
        if (!isCaptured(turn, x-1, y)) return false; //up   
        if (!isCaptured(turn, x, y-1)) return false; //left   
        if (!isCaptured(turn, x, y+1)) return false; //right   
        return true;
    }

    public static void main(String[] args){
        Go a = new Go();
        a.display();
    }
}
