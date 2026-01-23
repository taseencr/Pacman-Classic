public class HighScore{
    String name;
    int score;

    HighScore(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String toString(){
        return name + "," + score;
    }
}
