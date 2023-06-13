import java.util.ArrayList;
import java.util.List;

public class User  {
    private String username;
    private List<Food> foods;
    private List<Exercise> exercises;
    private List<Sleep> sleepRecords;

    public User(String username) {
        this.username = username;
        foods = new ArrayList<>();
        exercises = new ArrayList<>();
        sleepRecords = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public List<Food> getFoods() {
        return foods;
    }
    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Sleep> getSleepRecords() {
        return sleepRecords;
    }
    public void setSleepRecords(List<Sleep> sleepRecords) {
        this.sleepRecords = sleepRecords;
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void addSleepRecord(Sleep sleep) {
        sleepRecords.add(sleep);
    }
}
