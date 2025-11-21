import com.google.gson.*;
import java.io.*;
import java.security.MessageDigest;
import java.util.*;

public abstract class userService {
    private static final String USERS_FILE = "users.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<User> loadUsers() {
        List<User> list = new ArrayList<>();
        try {
            File f = new File(USERS_FILE);
            if (!f.exists() || f.length() == 0) {
                f.createNewFile();
                FileWriter fw = new FileWriter(f);
                fw.write("{\"users\": []}");
                fw.close();
            }
            JsonObject root = gson.fromJson(new FileReader(f), JsonObject.class);
            JsonArray arr = root.getAsJsonArray("users");
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                if (obj.has("role")) {
                    String role = obj.get("role").getAsString();
                    if (role.equalsIgnoreCase("student")) {
                        list.add(gson.fromJson(obj, Student.class));
                    } else if (role.equalsIgnoreCase("instructor")) {
                        list.add(gson.fromJson(obj, Instructor.class));
                    } else if (role.equalsIgnoreCase("admin")) {
                        list.add(gson.fromJson(obj, Admin.class));
                    }
                }
            }
            return list;

        } catch (Exception e) {e.printStackTrace();}
        return list;
    }

    public static void saveUsers(List<User> users) {
        try {
            JsonObject root = new JsonObject();
            JsonArray arr = new JsonArray();
            for (User u : users) {arr.add(gson.toJsonTree(u, u.getClass()));}
            root.add("users", arr);
            FileWriter fw = new FileWriter(USERS_FILE);
            gson.toJson(root, fw);
            fw.close();
        }
        catch (Exception e) {e.printStackTrace();}
    }

    public static int confirmSignup(User newUser) throws Exception {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getUserEmail().equals(newUser.getUserEmail())) return 1;
            else if(u.getUserId().equals(newUser.getUserId())) return 2;
        }
        newUser.setUserPassword(hashPassword(newUser.getUserPassword()));
        users.add(newUser);
        saveUsers(users);
        return 0;
    }

    public static User confirmLogin(String email, String password) throws Exception {
        List<User> users = loadUsers();
        String hash = hashPassword(password);
        for (User u : users) {if (u.getUserEmail().equals(email) && u.getUserPassword().equals(hash)) return u;}
        return null;
    }

    public static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) hex.append(String.format("%02x", b));
        return hex.toString();
    }


}

