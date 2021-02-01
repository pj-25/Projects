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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;


public class Main extends Application
{
    public Image BackgroundImage = new Image("/img/MAIN.png");
    private static HashMap<String, Socket> hashMap = new HashMap<>();
    String allMessages = null;
    @Override
    public void start(Stage primaryStage)
    {
        Font GossipFont = Font.loadFont("file:src/font/Quest.otf",150);
        Scene FirstScene = null;
        BorderPane borderPane = new BorderPane();
        //BackgroundSize backgroundSize = new BackgroundSize(0,0,false,false,false,true);
        borderPane.setBackground(new Background(new BackgroundImage(BackgroundImage,BackgroundRepeat.NO_REPEAT,null,BackgroundPosition.CENTER,null)));
        Text GossipLabel = new Text("g0ss1p");
        GossipLabel.setFont(GossipFont);

        Button signInButton = new Button("Sign In");
        Button signUpButton = new Button("Sign Up");
        HBox choiceButton = new HBox(50,signInButton,signUpButton);
        choiceButton.setAlignment(Pos.CENTER);

        Button Close = new Button("Close");
        VBox mainVbox  = new VBox(50,GossipLabel,choiceButton,Close);
        mainVbox.setAlignment(Pos.CENTER);
        BorderPane.setAlignment(mainVbox, Pos.CENTER);
        BorderPane.setMargin(mainVbox, new Insets(primaryStage.getHeight()*0.25,primaryStage.getWidth()*0.25,primaryStage.getHeight()*0.25,primaryStage.getWidth()*0.25));
        borderPane.setCenter(mainVbox);
        borderPane.setAlignment(mainVbox,Pos.CENTER);
        Close.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.close();
            }
        });
        FirstScene = new Scene(borderPane,primaryStage.getWidth(),primaryStage.getHeight());
        primaryStage.setScene(FirstScene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        Scene finalFirstScene = FirstScene;
        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Font GossipFont = Font.loadFont("file:src/font/Quest.otf",150);
                Scene mainScreen = null;
                BorderPane borderPane = new BorderPane();
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
                mainScreen = new Scene(borderPane,primaryStage.getMaxWidth(),primaryStage.getMaxHeight());
                Close.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        primaryStage.setScene(finalFirstScene);
                        primaryStage.setFullScreen(true);
                        primaryStage.show();
                    }
                });
                logInButton.setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent event) {
                        if (user.getText().trim().isBlank()) {
                            Button OK = new Button("OK");
                            Text errorMessage = new Text("Enter The Valid Name for Username...");
                            VBox vBox = new VBox(20, errorMessage, OK);
                            BorderPane borderPane1 = new BorderPane();
                            vBox.setAlignment(Pos.CENTER);
                            borderPane1.setCenter(vBox);
                            Scene Error = new Scene(borderPane1, 500, 100);
                            Stage ErrorStage = new Stage();
                            OK.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent actionEvent) {
                                    ErrorStage.close();
                                }
                            });
                            ErrorStage.setScene(Error);
                            ErrorStage.show();
                        } else {
                            Socket socket = null;
                            InputStream inputStream;
                            OutputStream outputStream;
                            Scanner input;
                            PrintWriter printWriter;
                            String pass = password.getText().trim();
                            String username = user.getText().trim();
                            String message = makeServiceSignIn(username, passwordHash(pass));
                            try {
                                socket = new Socket(InetAddress.getLocalHost(), 8080);
                                if (socket.isConnected()) {
                                    inputStream = socket.getInputStream();
                                    outputStream = socket.getOutputStream();
                                    input = new Scanner(inputStream, StandardCharsets.UTF_8);
                                    printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
                                    printWriter.println(message);
                                    input.nextLine().trim();
                                    String incoming = input.nextLine().trim();
                                    System.out.println(incoming + "!---done");
                                    if (incoming.equals("done")) {
                                        Runnable runnable = new Runnable() {
                                            @Override
                                            public void run()
                                            {
                                                Client client = null;
                                                client = new Client(user.getText());
                                                hashMap.put(user.getText().trim(), client.getSocket());
                                                client.run();
                                            }
                                        };
                                        Platform.runLater(runnable);
                                    } else {
                                        Button OK = new Button("OK");
                                        Text errorMessage = new Text("You have entered an invalid username or password!");
                                        VBox vBox = new VBox(20, errorMessage, OK);
                                        BorderPane borderPane1 = new BorderPane();
                                        vBox.setAlignment(Pos.CENTER);
                                        borderPane1.setCenter(vBox);
                                        Scene Error = new Scene(borderPane1, 500, 100);
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
                                } else {
                                    Button OK = new Button("OK");
                                    Text errorMessage = new Text("Server is not accepting your request at this moment!");
                                    VBox vBox = new VBox(20, errorMessage, OK);
                                    BorderPane borderPane1 = new BorderPane();
                                    vBox.setAlignment(Pos.CENTER);
                                    borderPane1.setCenter(vBox);
                                    Scene Error = new Scene(borderPane1, 500, 100);
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
                            } catch (IOException e) {
                                Button OK = new Button("OK");
                                Text errorMessage = new Text("Server is not accepting your request at this moment!");
                                VBox vBox = new VBox(20, errorMessage, OK);
                                BorderPane borderPane1 = new BorderPane();
                                vBox.setAlignment(Pos.CENTER);
                                borderPane1.setCenter(vBox);
                                Scene Error = new Scene(borderPane1, 500, 100);
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

                    }
                });
                primaryStage.setScene(mainScreen);
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                Font GossipFont = Font.loadFont("file:src/font/Quest.otf",150);
                Scene mainScreen = null;
                BorderPane borderPane = new BorderPane();
                //BackgroundSize backgroundSize = new BackgroundSize(0,0,false,false,false,true);
                borderPane.setBackground(new Background(new BackgroundImage(BackgroundImage,BackgroundRepeat.NO_REPEAT,null,BackgroundPosition.CENTER,null)));
                Text GossipLabel = new Text("g0ss1p");
                GossipLabel.setFont(GossipFont);
                Label logIn = new Label("Sign Up");
                logIn.setFont(Font.loadFont("file:src/font/Quest.otf",50));
                logIn.setTextFill(Color.RED);
                //logIn.setTextFill(Paint.valueOf("white"));
                logIn.setAlignment(Pos.CENTER);

                HBox userLine = new HBox();
                Label username = new Label("Username:                  ");
                TextField user = new TextField();
                userLine.getChildren().addAll(username,user);
                userLine.setAlignment(Pos.CENTER);

                HBox passwordLineOne = new HBox();
                Label passwordLabel = new Label("Enter Password:           ");
                TextField passwordOne = new TextField();
                passwordLineOne.getChildren().addAll(passwordLabel,passwordOne);
                passwordLineOne.setAlignment(Pos.CENTER);


                HBox passwordLineTwo = new HBox();
                Label passwordLabelTwo = new Label("Enter Password Again: ");
                TextField passwordTwo = new TextField();
                passwordLineTwo.getChildren().addAll(passwordLabelTwo,passwordTwo);
                passwordLineTwo.setAlignment(Pos.CENTER);

                VBox passWords = new VBox(30,passwordLineOne,passwordLineTwo);

                Button signUp = new Button("Sign Up");
                Button Close = new Button("Close");

                VBox mainVbox  = new VBox();
                HBox buttons = new HBox(30,signUp,Close);
                buttons.setAlignment(Pos.CENTER);
                mainVbox.getChildren().addAll(GossipLabel,logIn,userLine,passWords,buttons);
                mainVbox.setSpacing(20);
                mainVbox.setAlignment(Pos.CENTER);
                BorderPane.setAlignment(mainVbox, Pos.CENTER);
                BorderPane.setMargin(mainVbox, new Insets(primaryStage.getHeight()*0.25,primaryStage.getWidth()*0.25,primaryStage.getHeight()*0.25,primaryStage.getWidth()*0.25));
                borderPane.setCenter(mainVbox);
                borderPane.setAlignment(mainVbox,Pos.CENTER);
                mainScreen = new Scene(borderPane,primaryStage.getWidth(),primaryStage.getHeight());
                Close.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        primaryStage.setScene(finalFirstScene);
                        primaryStage.setFullScreen(true);
                        primaryStage.show();
                    }
                });
                signUp.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent)
                    {
                        if(passwordOne.getText().trim().equals(passwordOne.getText().trim()))
                        {
                            Socket socket = null;
                            InputStream inputStream;
                            OutputStream outputStream;
                            Scanner input;
                            PrintWriter printWriter;
                            String password = passwordOne.getText().trim();
                            String username = user.getText().trim();
                            String message = makeServiceSignUp(username,passwordHash(password));
                            try {
                                socket = new Socket(InetAddress.getLocalHost(),8080);
                                if (socket.isConnected())
                                {
                                    inputStream = socket.getInputStream();
                                    outputStream = socket.getOutputStream();
                                    input = new Scanner(inputStream, StandardCharsets.UTF_8);
                                    printWriter = new PrintWriter(new OutputStreamWriter(outputStream,StandardCharsets.UTF_8),true);
                                    printWriter.println(message);
                                    String incoming = input.nextLine().trim();
                                    incoming = input.nextLine().trim();
                                    System.out.println(incoming+"!---done");
                                    if(incoming.equals("done"))
                                    {
                                        Button OK  = new Button("OK");
                                        Label label1 = new Label("Thank you for getting in touch! \n\n");
                                        Label label2 = new Label("We appreciate you contacting Gossip.\n\n");
                                        Label label3 = new Label("\"Have a great day! \n\n");
                                        VBox errorMessage = new VBox(10,label1,label2,label3);
                                        errorMessage.setAlignment(Pos.CENTER);
                                        VBox vBox = new VBox(20,errorMessage,OK);
                                        BorderPane borderPane1 = new BorderPane();
                                        vBox.setAlignment(Pos.CENTER);
                                        borderPane1.setCenter(vBox);
                                        Scene Error = new Scene(borderPane1,500,300);
                                        Stage ErrorStage = new Stage();
                                        Socket finalSocket = socket;
                                        OK.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                ErrorStage.close();
                                                try {
                                                    finalSocket.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        ErrorStage.setScene(Error);
                                        ErrorStage.show();
                                    }
                                }
                                else
                                {
                                    Button OK  = new Button("OK");
                                    Text errorMessage = new Text("Server is not accepting your request at this moment!");
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
                            catch (IOException e)
                            {
                                Button OK  = new Button("OK");
                                Text errorMessage = new Text("Server is not accepting your request at this moment!");
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
                        else
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error!");
                            alert.setContentText("Enter The Same Password for Both Fields.");
                            alert.show();

                        }
                    }
                });
                primaryStage.setScene(mainScreen);
                //primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setFullScreen(true);
                primaryStage.show();
            }
        });

    }
    public static void main(String[] args) {
        launch(args);
    }
    private String makeServiceSignUp(String username,String password)
    {
        return "#-*-#--"+username+"--signUp--"+password;
    }
    private String makeServiceSignIn(String username,String password)
    {
        return "#-*-#--"+username+"--signIn--"+password;
    }
    public String passwordHash(String str)
    {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-512");
            md5.update(str.getBytes());
            byte[] digestBytes = md5.digest();
            for (byte b:digestBytes)
            {
                stringBuilder.append(String.format("%02x",b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return  stringBuilder.toString();
    }

}
