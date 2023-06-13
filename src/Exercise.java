
public class Exercise {
    private String type;
    private int duration;
    private int caloriesBurned;
    private String date;

    public Exercise(String type, int duration, int caloriesBurned, String date) {
        this.type = type;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public String getDate() {
        return date;
    }
}
