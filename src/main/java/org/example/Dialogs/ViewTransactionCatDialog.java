package org.example.Dialogs;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.example.Components.CategoryComponent;
import org.example.Controllers.DashboardController;
import org.example.Models.TransactionCategory;
import org.example.Models.User;
import org.example.utils.SqlUtil;

import java.util.List;

public class ViewTransactionCatDialog extends CustomDialog{

    private DashboardController dashboardController;

    public ViewTransactionCatDialog(User user, DashboardController dashboardController){
        super(user);
        this.dashboardController = dashboardController;

        setTitle("View Categories");
        setWidth(900);
        setHeight(400);

        ScrollPane mainContainer = createMainContainerContent();
        getDialogPane().setContent(mainContainer);
    }

    private ScrollPane createMainContainerContent() {

        VBox dialogVBox = new VBox(20);

        ScrollPane scrollPane = new ScrollPane(dialogVBox);
        scrollPane.setMinHeight(getHeight() - 50);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

         List<TransactionCategory> transactionCategories = SqlUtil.getAllTransactionCategoriesByUser(user);
         for(TransactionCategory transactionCategory : transactionCategories){
             System.out.println(transactionCategory.getCategoryName());
             CategoryComponent categoryComponent = new CategoryComponent(dashboardController, transactionCategory);
             dialogVBox.getChildren().add(categoryComponent);
         }

         return scrollPane;
    }
}
