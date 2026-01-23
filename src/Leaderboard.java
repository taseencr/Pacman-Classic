import java.io.*;
import java.util.*;

public class Leaderboard{
    private String fileName = "highscores.txt";
    private int maxEntries = 10;
    private HighScore[] scores;

    public HighScore[] getHighScores(){
        return scores;
    }

    public Leaderboard() throws Exception{
        scores = new HighScore[maxEntries];
        loadScores();
    }

    public void loadScores() throws Exception{
        File file = new File(fileName);
        if(!file.exists()){
            file.createNewFile();
        }

        Scanner sc = new Scanner(file);
        for(int i=0; sc.hasNextLine() && i<maxEntries; ){
            String line = sc.nextLine();
            String parts[] = line.split(",");
            if(parts.length == 2){
                String name = parts[0];
                int score = Integer.parseInt(parts[1]);
                scores[i] = new HighScore(name, score);
                i++;
            }
        }
        sc.close();
        sortScores();
    }

    public void saveScores() throws Exception{
        PrintWriter writer = new PrintWriter(fileName);
        for(int i=0; i<maxEntries; i++){
            if(scores[i] != null){
                writer.println(scores[i].toString());
            }
        }
        writer.close();
    }

    public void addScore(String name, int score) throws Exception{
        HighScore newScore = new HighScore(name, score);
        for(int i=0; i<maxEntries; i++){
            if(scores[i] == null || score>scores[i].score){
                for(int j=maxEntries-1; j>i; j--){
                    scores[j] = scores[j-1];
                }
                scores[i] = newScore;
                break;
            }
        }
        saveScores();
    }

    public void sortScores(){
        for(int i=0; i<maxEntries-1; i++){
            for(int j=i+1; j<maxEntries; j++){
                if(scores[i]!=null && scores[j]!=null && scores[j].score>scores[i].score){
                    HighScore temp = scores[i];
                    scores[i] = scores[j];
                    scores[j] = temp;
                }
            }
        }
    }
}
