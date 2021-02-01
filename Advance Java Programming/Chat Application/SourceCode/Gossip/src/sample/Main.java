package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;


public class Main extends Application
{
    private static HashMap<String, Socket> hashMap = new HashMap<>();
    String allMessages = null;
    @Override
    public void start(Stage primaryStage)
    {
        Font GossipFont = Font.loadFont("file:src/font/Quest.otf",150);
        Scene mainScreen = null;
        BorderPane borderPane = new BorderPane();
        Image BackgroundImage = new Image("file:src/img/MAIN.jpeg");
        //BackgroundSize backgroundSize = new BackgroundSize(0,0,false,false,false,true);
        borderPane.setBackground(new Background(new BackgroundImage(BackgroundImage,BackgroundRepeat.NO_REPEAT,null,BackgroundPosition.CENTER,null)));
        Text GossipLabel = new Text("g0ss1p");
        GossipLabel.setFont(GossipFont);
        Label logIn = new Label("Log In");
        logIn.setFont(Font.loadFont("file:src/font/Quest.otf",50));
        logIn.setTextFill(Color.RED);
        //logIn.setTextFill(Paint.valueOf("white"));
        logIn.setAlignment(Pos.CENTER);
        HBox userLine = new HBox();
        Label username = new Label("Username        ");
        TextField user = new TextField();
        userLine.getChildren().addAll(username,user);
        userLine.setAlignment(Pos.CENTER);

        HBox passwordLine = new HBox();
        Label passwordLabel = new Label("Password         ");
        TextField password = new TextField();
        passwordLine.getChildren().addAll(passwordLabel,password);
        passwordLine.setAlignment(Pos.CENTER);


        Button logInButton = new Button("Sign In");
        Button Close = new Button("Close");
        VBox mainVbox  = new VBox();
        HBox buttons = new HBox(30,logInButton,Close);
        buttons.setAlignment(Pos.CENTER);
        mainVbox.getChildren().addAll(GossipLabel,logIn,userLine,passwordLine,buttons);
        mainVbox.setSpacing(20);
        mainVbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(mainVbox, Pos.CENTER);
        BorderPane.setMargin(mainVbox, new Insets(primaryStage.getHeight()*0.25,primaryStage.getWidth()*0.25,primaryStage.getHeight()*0.25,primaryStage.getWidth()*0.25));
        borderPane.setCenter(mainVbox);
        borderPane.setAlignment(mainVbox,Pos.CENTER);
        mainScreen = new Scene(borderPane,600,600);
        primaryStage.setScene(mainScreen);
        Close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.close();
            }
        });
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        logInButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if(!user.getText().trim().isBlank())
                {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run()
                        {
                            Client client = null;
                            client = new Client(user.getText());
                            hashMap.put(user.getText().trim(),client.getSocket());
                            client.run();
                        }
                    };
                    Platform.runLater(runnable);
                }
                else
                {
                    Button OK  = new Button("OK");
                    Text errorMessage = new Text("Enter The Valid Name for Username...");
                    VBox vBox = new VBox(20,errorMessage,OK);
                    BorderPane borderPane1 = new BorderPane();
                    vBox.setAlignment(Pos.CENTER);
                    borderPane1.setCenter(vBox);
                    Scene Error = new Scene(borderPane1,500,100);
                    Stage ErrorStage = new Stage();
                    OK.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            ErrorStage.close();
                        }
                    });
                    ErrorStage.setScene(Error);
                    ErrorStage.show();
                }
            }
        });

    }
    public static void main(String[] args) {
        launch(args);
    }
}
