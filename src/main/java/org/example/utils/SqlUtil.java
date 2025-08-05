package org.example.utils;

import com.google.gson.*;
import javafx.scene.control.Alert;
import org.example.Models.Transaction;
import org.example.Models.TransactionCategory;
import org.example.Models.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlUtil {
    //GET

    public static User getUserByEmail(String userEmail) {
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/user?email=" + userEmail,
                    ApiUtil.RequestMethod.GET, null);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error(getUserBYEmail: " + conn.getResponseCode());
                return null;
            }

            String userDataJson = ApiUtil.readApiResponse(conn);
            JsonObject jsonObject = JsonParser.parseString(userDataJson).getAsJsonObject();

            //extract Json Data

            int id = jsonObject.get("id").getAsInt();
            String name = jsonObject.get("name").getAsString();
            String email = jsonObject.get("email").getAsString();
            String password = jsonObject.get("password").getAsString();
            LocalDateTime createdAt = new Gson().fromJson(jsonObject.get("created_at"), LocalDateTime.class);

            return new User(id, name, email, password, createdAt);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }

    public static List<Transaction> getAllTransactionsById(int userId, int startPage,
                                                           int endPage, int size){
        List<Transaction> recentTransactions = new ArrayList<>();

        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction/recent/user/"+ userId +
                                            "?startPage=" +startPage+ "&endPage=" +endPage+ "&size=" +size,
                    ApiUtil.RequestMethod.GET,
                    null);
            if (conn.getResponseCode() != 200) {
                return null;
            }

            String result = ApiUtil.readApiResponse(conn);
            JsonArray resultJsonArray = new JsonParser().parse(result).getAsJsonArray();
            for(int i = 0; i < resultJsonArray.size(); i++){
                JsonObject transactionJsonObj = resultJsonArray.get(i).getAsJsonObject();
                int transactionId = transactionJsonObj.get("id").getAsInt();

                 TransactionCategory transactionCategory = null;
                 if(transactionJsonObj.has("transactionCategory")
                                    && !transactionJsonObj.get("transactionCategory").isJsonNull()){
                     JsonObject transactionCategoryJsonObj = transactionJsonObj.get("transactionCategory").getAsJsonObject();
                     int transactionCategoryId = transactionCategoryJsonObj.get("id").getAsInt();
                     String transactionCategoryName = transactionCategoryJsonObj.get("categoryName").getAsString();
                     String transactionCategoryColour = transactionCategoryJsonObj.get("categoryColour").getAsString();

                     transactionCategory = new TransactionCategory(
                             transactionCategoryId,
                             transactionCategoryName,
                             transactionCategoryColour
                     );
                 }

                 String transactionName = transactionJsonObj.get("transactionName").getAsString();
                 double transactionAmount = transactionJsonObj.get("transactionAmount").getAsDouble();
                 LocalDate transactionDate = LocalDate.parse(transactionJsonObj.get("transactionDate").getAsString());
                 String transactionType = transactionJsonObj.get("transactionType").getAsString();

                 Transaction transaction = new Transaction(
                         transactionId,
                         transactionCategory,
                         transactionName,
                         transactionAmount,
                         transactionDate,
                         transactionType
                 );

                 recentTransactions.add(transaction);
            }

            return recentTransactions;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
    public static List<Transaction> getAllTransactionsByUserIdAndYear(int userId, int year, Integer month){

            List<Transaction> transactions = new ArrayList<>();

            HttpURLConnection conn = null;
            String apiPath = "/api/v1/transaction/user/" + userId + "?year=" + year;
            if(month != null){
                apiPath += "&month=" + month;
            }
            try {
                conn = ApiUtil.fetchApi(apiPath,
                        ApiUtil.RequestMethod.GET,
                        null);
                if (conn.getResponseCode() != 200) {
                    return null;
                }

                String result = ApiUtil.readApiResponse(conn);

                JsonArray resultJson = new JsonParser().parse(result).getAsJsonArray();
                for(int i = 0; i < resultJson.size(); i++){
                    JsonObject transactionJsonObject = resultJson.get(i).getAsJsonObject();
                    int transactionId = transactionJsonObject.get("id").getAsInt();

                    TransactionCategory transactionCategory = null;
                    if(transactionJsonObject.has("transactionCategory") &&
                            !transactionJsonObject.get("transactionCategory").isJsonNull()){
                        JsonObject transactionCategoryJsonObj = transactionJsonObject.get("transactionCategory").getAsJsonObject();
                        int transactionCategoryId = transactionCategoryJsonObj.get("id").getAsInt();
                        String transactionCategoryName = transactionCategoryJsonObj.get("categoryName").getAsString();
                        String transactionCategoryColour = transactionCategoryJsonObj.get("categoryColour").getAsString();

                        transactionCategory = new TransactionCategory(
                                transactionCategoryId,
                                transactionCategoryName,
                                transactionCategoryColour
                        );
                    }

                    String transactionName = transactionJsonObject.get("transactionName").getAsString();
                    double transactionAmount = transactionJsonObject.get("transactionAmount").getAsDouble();
                    LocalDate transactionDate = LocalDate.parse(transactionJsonObject.get("transactionDate").getAsString());
                    String transactionType = transactionJsonObject.get("transactionType").getAsString();

                    Transaction transaction = new Transaction(
                            transactionId,
                            transactionCategory,
                            transactionName,
                            transactionAmount,
                            transactionDate,
                            transactionType
                    );

                    transactions.add(transaction);
                    System.out.println(transactions);
                }

                return transactions;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return null;
    }

    public  static List<Integer> getDistinctYears(int userId){
        List<Integer> distinctYears = new ArrayList<>();

        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction/years/" + userId,
                    ApiUtil.RequestMethod.GET, null);

            if (conn.getResponseCode() != 200) {
                System.out.println("error: getDistinctYears " + conn.getResponseCode());
                ;
            }
            String result = ApiUtil.readApiResponse(conn);
            JsonArray resultJson = new JsonParser().parse(result).getAsJsonArray();
            for(int i = 0; i < resultJson.size(); i++){
                int years = resultJson.get(i).getAsInt();
                distinctYears.add(years);
            }

            return distinctYears;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }

    //POST

    public static boolean postLoginUSer(String email, String password) {
        //Authenticate email and pssword
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/user/login?email=" + email + "&password=" + password,
                    ApiUtil.RequestMethod.POST, null);

            if (conn.getResponseCode() != 200) {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return true;
    }

    public static boolean postCreateUser(JsonObject userData) {
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi(
                    "/api/v1/user",
                    ApiUtil.RequestMethod.POST,
                    userData
            );
            if (conn.getResponseCode() != 200) {
                return false;//Failed to create account
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return true;//succesfull and stored in DB
    }

    public static boolean postTransActionCategory(JsonObject transactionCategoryData) {
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction-category", ApiUtil.RequestMethod.POST, transactionCategoryData);
            if (conn.getResponseCode() != 200) {
                return false;
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    public static boolean putTransActionCategory(int categoryId, String newCategory, String newCategoryColour) {
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction-category/" + categoryId + "?newCategory=" + newCategory +
                    "&newCategoryColour=" + newCategoryColour, ApiUtil.RequestMethod.PUT, null);
            if (conn.getResponseCode() != 200) {
                return false;
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }

    public static boolean deleteTransactionCategory(int id) {
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction-category/" + id, ApiUtil.RequestMethod.DELETE, null);
            if (conn.getResponseCode() != 200) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

    public static boolean deleteTransactionById(int transactionId) {
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction/" + transactionId, ApiUtil.RequestMethod.DELETE, null);
            if (conn.getResponseCode() != 200) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

    public static boolean postTransactionData(JsonObject transactionData) {
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction", ApiUtil.RequestMethod.POST, transactionData);
            if (conn.getResponseCode() != 200) {
                return false;
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }

    public static boolean putTransAction(JsonObject newTransactionData) {
        HttpURLConnection conn = null;
        try {
            conn = ApiUtil.fetchApi("/api/v1/transaction",
                    ApiUtil.RequestMethod.PUT, newTransactionData);
            if (conn.getResponseCode() != 200) {
                System.out.println("Error(putTransaction): " + conn.getResponseCode());
                return false;
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return false;
    }


}
