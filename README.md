# Client-Expense-Tracker-GUI
The full code for a sleek javaFx desktop app that helps users manage personal expenses and financial categories.
This was designed with the intention to be dynamic and reactive to user input and give the feel of any modern expense tracker.

I created this project to learn more about the capabilites and components of Java and i was not dissapointed.
All i had learnt about the basic java syntax and OOP concept were put to the test during this project.

In this project I learnt about the process of using a 'Stage' and the concept behind switching between scenes with a 'Viewnavigator' class.
By alternating between the uses of VBOX and HBOX for the layout, labels, textfields and buttons, the expense tracker interfaxce slowly came to life.
I had to think about how the different 'View' classes that i had created needed to also function properly. This was achieved by a various 'Controller' classes which would use dependency injection to comminucate with the view classes, allowing me to create methods like the one below to keep things organised and easily reusable:

private void initialize(){
        loginView.getLoginButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
               if (!validateUser()){
                   return;
               }

In this project I learnt:
- How to add and style components
- The logic behind extracting data and converting them into JSON using the 'JsonObject' property
- Creating HTTP conncection methods that would be read and write Json date to the backend server that i created seperately.


Setting up the SqlUtil class and the ApiUtil class was probably the most enjoyable part of this project as it required that i went away and gained a better understanding of how information can be passed around. This is the part of my code in this repo that connects to the API server i created in a seperate java project, which was in charge of CRUD operations and persisting data to the SQL database.
