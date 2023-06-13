
public class Food {
    private String name;
    private int calories;
    private String date;

    public Food(String name, int calories, String date) {
        this.name = name;
        this.calories = calories;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public String getDate() {
        return date;
    }
}
