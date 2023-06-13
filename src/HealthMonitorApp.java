import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HealthMonitorApp {
    private static final String FILE_NAME = "src/data.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    private List<User> users;

    public HealthMonitorApp() {
        users = new ArrayList<>();
    }

    public void run() {
        loadData();

        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
            System.out.println("Welcome to Health Monitor App");
            System.out.println("1. Create a new user");
            System.out.println("2. Log in with username");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        saveData();
        System.out.println("Exiting Health Monitor App");
    }

    private void createUser(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();

        User user = new User(username);
        users.add(user);
        System.out.println("User created successfully.");
    }

    private void loginUser(Scanner scanner) {
        System.out.println("Enter username:");
        String username = scanner.nextLine();

        User user = findUserByUsername(username);
        if (user != null) {
            System.out.println("Logged in as: " + user.getUsername());
            manageUser(user, scanner);
        } else {
            System.out.println("User not found. Please try again.");
        }
    }

    private void manageUser(User user, Scanner scanner) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("Logged in as: " + user.getUsername());
            System.out.println("1. Log food entry");
            System.out.println("2. Log exercise");
            System.out.println("3. Log sleep");
            System.out.println("4. View daily caloric balance");
            System.out.println("5. View sleep analysis");
            System.out.println("6. View exercise log");
            System.out.println("7. View health summary");
            System.out.println("0. Log out");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    logFoodEntry(user, scanner);
                    break;
                case 2:
                    logExerciseEntry(user, scanner);
                    break;
                case 3:
                    logSleepEntry(user, scanner);
                    break;
                case 4:
                    viewDailyCaloricBalance(user, scanner);
                    break;
                case 5:
                    viewSleepAnalysis(user, scanner);
                    break;
                case 6:
                    viewExerciseLog(user, scanner);
                    break;
                case 7:
                    viewHealthSummary(user, scanner);
                    break;
                case 0:
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void logFoodEntry(User user, Scanner scanner) {
        System.out.println("Enter food name:");
        String name = scanner.nextLine();

        System.out.println("Enter calories:");
        int calories = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter date (mm/dd/yyyy):");
        String date = scanner.nextLine();

        Food food = new Food(name, calories, date);
        user.addFood(food);
        System.out.println("Food entry logged successfully.");
    }

    private void logExerciseEntry(User user, Scanner scanner) {
        System.out.println("Enter exercise type:");
        String type = scanner.nextLine();

        System.out.println("Enter duration in minutes:");
        int duration = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter calories burned:");
        int caloriesBurned = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter date (mm/dd/yyyy):");
        String date = scanner.nextLine();

        Exercise exercise = new Exercise(type, duration, caloriesBurned, date);
        user.addExercise(exercise);
        System.out.println("Exercise entry logged successfully.");
    }

    private void logSleepEntry(User user, Scanner scanner) {
        System.out.println("Enter sleep start time (mm/dd/yyyy HH:mm):");
        String sleepStartStr = scanner.nextLine();
        Date sleepStart = parseDate(sleepStartStr);

        System.out.println("Enter sleep end time (mm/dd/yyyy HH:mm):");
        String sleepEndStr = scanner.nextLine();
        Date sleepEnd = parseDate(sleepEndStr);

        Sleep sleep = new Sleep(sleepStart, sleepEnd);
        user.addSleepRecord(sleep);
        System.out.println("Sleep entry logged successfully.");
    }

    private void viewDailyCaloricBalance(User user, Scanner scanner) {
        System.out.println("Enter date (MM/dd/yyyy):");
        String date = scanner.nextLine();

        int consumedCalories = 0;
        int burnedCalories = 0;

        for (Food food : user.getFoods()) {
            if (food.getDate().startsWith(date)) {
                consumedCalories += food.getCalories();
            }
        }

        for (Exercise exercise : user.getExercises()) {
            if (exercise.getDate().startsWith(date)) {
                burnedCalories += exercise.getCaloriesBurned();
            }
        }

        int caloricBalance = consumedCalories - burnedCalories;
        System.out.println("Daily Caloric Balance for " + date + ": " + caloricBalance);
    }

    private void viewSleepAnalysis(User user, Scanner scanner) {
        System.out.println("Enter start date (MM/dd/yyyy):");
        String startDateStr = scanner.nextLine();
        Date startDate = parseDate(startDateStr);

        System.out.println("Enter end date (MM/dd/yyyy):");
        String endDateStr = scanner.nextLine();
        Date endDate = parseDate(endDateStr);

        List<Sleep> sleepRecords = user.getSleepRecords();

        int totalHours = 0;
        int numRecords = 0;

        for (Sleep sleep : sleepRecords) {
            Date sleepStart = sleep.getSleepStart();
            if (sleepStart.after(startDate) && sleepStart.before(endDate)) {
                long sleepDuration = sleep.getSleepEnd().getTime() - sleepStart.getTime();
                totalHours += sleepDuration / (60 * 60 * 1000); // Convert milliseconds to hours
                numRecords++;
            }
        }

        if (numRecords > 0) {
            int averageHours = totalHours / numRecords;
            System.out.println("Average hours of sleep per day: " + averageHours);

            for (Sleep sleep : sleepRecords) {
                Date sleepStart = sleep.getSleepStart();
                if (sleepStart.after(startDate) && sleepStart.before(endDate)) {
                    long sleepDuration = sleep.getSleepEnd().getTime() - sleepStart.getTime();
                    int hours = (int) (sleepDuration / (60 * 60 * 1000));
                    if (hours < averageHours) {
                        System.out.println("Sleep duration below average on " + sleepStart + ": " + hours + " hours");
                    }
                }
            }
        } else {
            System.out.println("No sleep records found in the date range.");
        }
    }

    private void viewExerciseLog(User user, Scanner scanner) {
        System.out.println("Exercise Log:");

        List<Exercise> exercises = user.getExercises();
        for (Exercise exercise : exercises) {
            System.out.println("Type: " + exercise.getType());
            System.out.println("Duration: " + exercise.getDuration() + " minutes");
            System.out.println("Calories Burned: " + exercise.getCaloriesBurned());
            System.out.println("Date: " + exercise.getDate());
            System.out.println();
        }
    }

    private void viewHealthSummary(User user, Scanner scanner) {
        System.out.println("Health Summary:");

        List<Food> foods = user.getFoods();
        int totalCaloriesConsumed = 0;

        for (Food food : foods) {
            totalCaloriesConsumed += food.getCalories();
        }

        List<Exercise> exercises = user.getExercises();
        int totalCaloriesBurned = 0;

        for (Exercise exercise : exercises) {
            totalCaloriesBurned += exercise.getCaloriesBurned();
        }

        List<Sleep> sleepRecords = user.getSleepRecords();
        int totalSleepHours = 0;

        for (Sleep sleep : sleepRecords) {
            long sleepDuration = sleep.getSleepEnd().getTime() - sleep.getSleepStart().getTime();
            totalSleepHours += sleepDuration / (60 * 60 * 1000);
        }

        System.out.println("Total calories consumed: " + totalCaloriesConsumed);
        System.out.println("Total calories burned: " + totalCaloriesBurned);
        System.out.println("Total sleep hours: " + totalSleepHours);
    }

    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Error parsing date: " + dateString);
            return null;
        }
    }


    private void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users) {
                writer.write(user.getUsername());
                writer.newLine();
                writer.write(Integer.toString(user.getFoods().size()));
                writer.newLine();
                for (Food food : user.getFoods()) {
                    writer.write(food.getName());
                    writer.newLine();
                    writer.write(Integer.toString(food.getCalories()));
                    writer.newLine();
                    writer.write(food.getDate());
                    writer.newLine();
                }
                writer.write(Integer.toString(user.getExercises().size()));
                writer.newLine();
                for (Exercise exercise : user.getExercises()) {
                    writer.write(exercise.getType());
                    writer.newLine();
                    writer.write(Integer.toString(exercise.getDuration()));
                    writer.newLine();
                    writer.write(Integer.toString(exercise.getCaloriesBurned()));
                    writer.newLine();
                    writer.write(exercise.getDate());
                    writer.newLine();
                }
                writer.write(Integer.toString(user.getSleepRecords().size()));
                writer.newLine();
                for (Sleep sleep : user.getSleepRecords()) {
                    writer.write(DATE_FORMAT.format(sleep.getSleepStart()));
                    writer.newLine();
                    writer.write(DATE_FORMAT.format(sleep.getSleepEnd()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String username = line;
                int numFoods = Integer.parseInt(reader.readLine());
                List<Food> foods = new ArrayList<>();
                for (int i = 0; i < numFoods; i++) {
                    String name = reader.readLine();
                    int calories = Integer.parseInt(reader.readLine());
                    String date = reader.readLine();
                    foods.add(new Food(name, calories, date));
                }
                int numExercises = Integer.parseInt(reader.readLine());
                List<Exercise> exercises = new ArrayList<>();
                for (int i = 0; i < numExercises; i++) {
                    String type = reader.readLine();
                    int duration = Integer.parseInt(reader.readLine());
                    int caloriesBurned = Integer.parseInt(reader.readLine());
                    String date = reader.readLine();
                    exercises.add(new Exercise(type, duration, caloriesBurned, date));
                }
                int numSleepRecords = Integer.parseInt(reader.readLine());
                List<Sleep> sleepRecords = new ArrayList<>();
                for (int i = 0; i < numSleepRecords; i++) {
                    Date sleepStart = DATE_FORMAT.parse(reader.readLine());
                    Date sleepEnd = DATE_FORMAT.parse(reader.readLine());
                    sleepRecords.add(new Sleep(sleepStart, sleepEnd));
                }
                User user = new User(username);
                user.setFoods(foods);
                user.setExercises(exercises);
                user.setSleepRecords(sleepRecords);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error loading data.");
        } catch (ParseException e) {
            System.out.println("Error parsing date.");
        }
    }

    private User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        HealthMonitorApp app = new HealthMonitorApp();
        app.run();
    }
}

