# Client Expense Tracker (JavaFX)

A sleek JavaFX desktop app that helps users manage personal expenses and financial categories. Designed to be dynamic and reactive to user input, this project mimics the feel of modern expense tracking tools.

## 📸 Screenshot

![App Screenshot](screenshot.png)


---

## 🎯 Purpose

I built this project to deepen my understanding of Java fundamentals — especially object-oriented programming, GUI development with JavaFX, and client-server communication. What started as a way to practice Java quickly became a hands-on exploration of **layouts, event handling, and API communication**.

---

## 💡 What I Learned

### 🔁 Scene Navigation and Layouts
- Worked with `Stage`, `Scene`, `VBox`, and `HBox` to construct clean and responsive UI layouts.
- Created a reusable `ViewNavigator` class to manage transitions between views.
- Applied styling through CSS and explored JavaFX component customization.

### 👥 MVC Pattern in Practice
- Separated UI logic (`View` classes) from application logic (`Controller` classes).
- Used **dependency injection** to connect views to their controllers, keeping code organized and reusable.

Example pattern:
```java
private void initialize() {
    loginView.getLoginButton().setOnMouseClicked(event -> {
        if (!validateUser()) return;
        // navigate to dashboard
    });
}
```
### Backend Communication
- Built a separate Java backend to handle user and transaction data.
- Used HttpURLConnection and JsonObject to send/receive data via HTTP.
- Implemented basic CRUD operations by connecting the client app to the backend API.

Example pattern:
```java
 public static List<TransactionCategory> getAllTransactionCategoriesByUser(User user) {
        List<TransactionCategory> categories = new ArrayList<>();

        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction-category/user/" + user.getId(),
                    ApiUtil.RequestMethod.GET, null);

            if (conn.getResponseCode() != 200) {
                System.out.println("error: " + conn.getResponseCode());
                ;
            }

            String result = ApiUtil.readApiResponse(conn);
            JsonArray resultJsonArray = new JsonParser().parse(result).getAsJsonArray();

            for (JsonElement jsonElement : resultJsonArray) {
                int categoryId = jsonElement.getAsJsonObject().get("id").getAsInt();
                String categoryName = jsonElement.getAsJsonObject().get("categoryName").getAsString();
                String categoryColour = jsonElement.getAsJsonObject().get("categoryColour").getAsString();

                categories.add(new TransactionCategory(categoryId, categoryName, categoryColour));
            }
            return categories;
```

### 🗃️ SQL and Utility Classes
- Created SqlUtil and ApiUtil utility classes to streamline backend interaction.
- Learned how to parse and format JSON.

---

## 🛠 Technologies Used
- Java 23
- javaFX
- MySQL
- CSS Bootstrap
- Maven

---

### 🧠 Reflections

This was my first major Java GUI project, and it pushed me to combine everything I’ve learned,
from Java syntax and OOP to scene navigation and HTTP communication. 
I especially enjoyed building the backend separately and seeing how client-server communication works in real time.
The code for the backend server can be found in the **Expense-Tracker-Backend** repository.


























