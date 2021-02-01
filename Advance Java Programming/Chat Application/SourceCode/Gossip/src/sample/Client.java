package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client implements Runnable
{
    private static final Pattern pattern = Pattern.compile("^(--)(\\S+)(--)(\\S+)--");
    private static final Pattern  errorPattern = Pattern.compile("^(!!--!!)");
    private static final Pattern servicePattern = Pattern.compile("^#-\\*-#--(\\S+)--(\\S+)--");
    private static Font msgFont = Font.loadFont("file:src/font/TeachersStudent-Regular.ttf",30);
    private ArrayList<String> peerClients;
    private ScrollPane scrollPane = null;
    //private String allMessages = "";
    private final String hostname;
    private VBox vBoxMessages;
    private  Socket socket;
    private  InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Stage newWindow =null;
    private ComboBox<String> client = new ComboBox<String>();
    PrintWriter printWriter =null;
    Text allMessage = null;
    Scanner input = null;
    private Boolean serverUp ;

    public Socket getSocket()
    {
        return socket;
    }

    public Client(String hostname)
    {
        this.client.setPrefWidth(110);

        this.peerClients = new ArrayList<>();
        this.hostname = hostname.trim();
        try
        {
            socket = new Socket(InetAddress.getByName("18.222.214.51"),8080);
            if (socket.isConnected())
            {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                input = new Scanner(inputStream, StandardCharsets.UTF_8);
                printWriter = new PrintWriter(new OutputStreamWriter(outputStream,StandardCharsets.UTF_8),true);
                serverUp = true;
            }
        }
        catch (IOException e)
        {
            serverUp = false;
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

    @Override
    public void run()
    {
        if (serverUp)
        {
            vBoxMessages = new VBox(5);
            Scene chat = null;

            scrollPane = new ScrollPane();
            Image chatImage = new Image("file:src/img/CHATBACK3.jpeg");
            scrollPane.setBackground(Background.EMPTY);
//            scrollPane.setPrefSize(25,500);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setVvalue(1.0d);
            //allMessage = new Text(allMessages);
            scrollPane.setContent(vBoxMessages);
            scrollPane.setFitToWidth(true);
            Button send = new Button("Send!");
            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; ");

            Text text = new Text("Name :");
            text.setTextAlignment(TextAlignment.CENTER);
            //TextField name= new TextField();
            //name.setMaxWidth(110);
            TextField msg= new TextField();
            Label message = new Label("Messgae :");
            text.setTextAlignment(TextAlignment.CENTER);

            HBox hBox = new HBox(text,client,message,msg,send);
            hBox.setStyle("-fx-background: white; -fx-background-color: white; ");
            hBox.setAlignment(Pos.CENTER);
            VBox vBox = new VBox();

            vBox.getChildren().addAll(scrollPane,hBox);
            BackgroundSize backgroundSize = new BackgroundSize(100,100,false,false,true,true);
            vBox.setBackground(new Background(new BackgroundImage(chatImage,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.DEFAULT,backgroundSize)));
            chat = new Scene(vBox,450 ,500);
            //allMessage.setWrappingWidth(chat.getWidth());
            chat.getStylesheets().add("sample/chat.css");
            newWindow = new Stage();
            newWindow.setTitle(hostname);
            newWindow.setScene(chat);
            newWindow.widthProperty().addListener(event -> {
                scrollPane.setPrefWidth(newWindow.getWidth());
            });
            newWindow.heightProperty().addListener(event -> {
                scrollPane.setPrefHeight(newWindow.getHeight());
            });
            newWindow.widthProperty().addListener(event -> {
                hBox.setPrefWidth(newWindow.getWidth());
            });

            newWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent)
                {
                    printWriter.println(makeService("Close"));
                    try {
                        socket.close();
                    } catch (IOException e) {
                    }
                }
            });

            newWindow.show();

            send.setOnAction(new EventHandler<ActionEvent>()
            {
                @Override
                public void handle(ActionEvent actionEvent)
                {
                    try
                    {
                        String To = client.getSelectionModel().getSelectedItem().trim();
                        String mssg = msg.getText().trim();
                        printWriter.println(encoder(hostname.trim(),client.getSelectionModel().getSelectedItem().trim(),msg.getText()));
                        String newMessage = client.getSelectionModel().getSelectedItem().trim() + ":\n\t" + msg.getText() + "\n";
                        //allMessages = allMessages + newMessage;

                        Text nameTo = new Text(To);
                        nameTo.setWrappingWidth(420);
                        nameTo.setTextAlignment(TextAlignment.RIGHT);
                        nameTo.setFont(msgFont);
                        Text Mssg = new Text(mssg);
                        Mssg.setWrappingWidth(420);
                        Mssg.setFont(msgFont);
                        Mssg.setTextAlignment(TextAlignment.RIGHT);
                        //Text newTextMessage = new Text(newMessage);
                        //newTextMessage.setTextAlignment(TextAlignment.RIGHT);

                        VBox MessageBox = new VBox(5,nameTo,Mssg);
                        MessageBox.setAlignment(Pos.CENTER_RIGHT);
                        VBox.setMargin(MessageBox,new Insets(5,5,5,5));
                        //MessageBox.setStyle("-fx-background: grey; -fx-background-color: grey; -fx-opacity :0.5;-fx-border : 5px solid red");
                        MessageBox.getStyleClass().add("custom-dashed-border");
                        vBoxMessages.getChildren().add(MessageBox);
                        VBox.setMargin(MessageBox,new Insets(2.5,10,2.5,10));

                        //allMessage.setText(allMessages);
                        scrollPane.setVvalue(1.0d);
                    }
                    catch (Exception e)
                    {
                        Button CANCEL = new Button("OK");
                        Text errorMessage = new Text("Enter The Valid Name for Username...");
                        VBox vBox = new VBox(20,errorMessage,CANCEL);
                        BorderPane borderPane1 = new BorderPane();
                        vBox.setAlignment(Pos.CENTER);
                        borderPane1.setCenter(vBox);
                        Scene Error = new Scene(borderPane1,500,100);
                        Stage ErrorStage = new Stage();
                        ErrorStage.setScene(Error);
                        CANCEL.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent)
                            {
                                ErrorStage.close();
                            }
                        });
                        ErrorStage.show();
                    }
                }
            });
            UpdateMessage();
        }

    }
    void UpdateMessage()
    {
        Runnable task = new Runnable() {
            @Override
            public void run()
            {
                while (newWindow.isShowing())
                {
                    if(input.hasNextLine())
                    {
                        String line = input.nextLine();
                        switch (getTypeMessgae(line))
                        {
                            case "Message":
                                String finalLine = line;
                                Platform.runLater(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        String To = getFromIndex(finalLine);
                                        String mssg = decoder(finalLine);
                                        Text nameTo = new Text(To);
                                        nameTo.setFont(msgFont);
                                        nameTo.setWrappingWidth(420);
                                        nameTo.setTextAlignment(TextAlignment.LEFT);
                                        Text Mssg = new Text(mssg);
                                        Mssg.setFont(msgFont);
                                        Mssg.setWrappingWidth(420);
                                        Mssg.setTextAlignment(TextAlignment.LEFT);
                                        /*String FROM = getFromIndex(finalLine);
                                        String msg = decoder(finalLine);
                                        String newMessage = getFromIndex(finalLine) + ":\n\t" + decoder(finalLine) + "\n";
                                        allMessages += getFromIndex(finalLine) + ":\n\t" + decoder(finalLine) + "\n";
                                        allMessage.setText(allMessages);
                                        Text newTextMessage = new Text(newMessage);
                                        newTextMessage.setTextAlignment(TextAlignment.LEFT);*/
                                        
                                        VBox MessageBox = new VBox(nameTo,Mssg);
                                        MessageBox.setAlignment(Pos.CENTER_LEFT);
                                        VBox.setMargin(MessageBox,new Insets(5,5,5,5));
                                        vBoxMessages.getChildren().add(MessageBox);
                                        VBox.setMargin(MessageBox,new Insets(2.5,10,2.5,10));
                                        //MessageBox.setStyle("-fx-background: grey; -fx-background-color: grey; -fx-opacity :0.5;-fx-border-radius : 5px");
                                        MessageBox.getStyleClass().add("custom-dashed-border");
                                        scrollPane.setVvalue(1.0d);
                                    }
                                });
                                break;
                            case "Error":
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        Button OK  = new Button("OK");
                                        Text ErrorMessage = new Text(getErrorMessgae(line));
                                        VBox vBox = new VBox(20,ErrorMessage,OK);
                                        BorderPane errorpan = new BorderPane();
                                        vBox.setAlignment(Pos.CENTER);
                                        errorpan.setCenter(vBox);
                                        Scene ErrorScene = new Scene(errorpan,500,100);
                                        Stage UserError = new Stage();
                                        OK.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                UserError.close();
                                            }
                                        });
                                        UserError.setScene(ErrorScene);
                                        UserError.show();
                                    }
                                });
                                break;
                            case "Service":
                                switch (getTypeServiceResponce(line))
                                {
                                    case "clientList":
                                        Platform.runLater(new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                if(!getServiceMessage(line).isBlank())
                                                {
                                                    for(String name : getServiceMessage(line).split(" "))
                                                    {
                                                        client.getItems().add(name);
                                                    }
                                                }
                                            }
                                        });
                                        break;
                                    case "Echo":
                                        printWriter.println(makeService("Echo"));
                                        break;
                                    case "addMember":
                                        String newMemeber = getServiceMessage(line);
                                        try {
                                            client.getItems().add(newMemeber);
                                        } catch (IllegalStateException e) {
                                        }
                                        break;
                                    case "removeMember":

                                        String removedMember = getServiceMessage(line);

                                        try {
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(client.getSelectionModel().getSelectedItem().equals(removedMember))
                                                    {
                                                        client.setValue("Default");
                                                    }
                                                    client.getItems().remove(removedMember);
                                                }
                                            });

                                        } catch (IllegalStateException e) {
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
    private String encoder(String from,String to,String str)
    {
        return "--"+from+"--"+to+"--"+str;
    }
    private String decoder(String str)
    {
        String ans = str;
        Matcher matcher = pattern.matcher(str);
        if(matcher.find())
        {
            ans =  str.replaceFirst(matcher.group(0),"");
        }
        return ans;
    }
    private static String getFromIndex(String string)
    {
        String name = null;
        Matcher matcher = pattern.matcher(string);
        if(matcher.find())
            name =  matcher.group(2);
        return  name;
    }
    private static String getTypeMessgae(String string)
    {
        String name = "Echo";
        Matcher matcher = pattern.matcher(string);
        if(matcher.find())
            name = "Message";
        else if(errorPattern.matcher(string).find())
            name = "Error";
        else if(servicePattern.matcher(string).find())
            name = "Service";
        return name;
    }
    private static String getErrorMessgae(String string)
    {
        Matcher matcher = errorPattern.matcher(string);
        matcher.find();
        return string.replace(matcher.group(1),"");
    }
    private String makeService(String string)
    {
        return "#-*-#--"+hostname+"--"+string+"--";
    }
    private static String getServiceMessage(String string)
    {
        Matcher matcher = servicePattern.matcher(string);
        matcher.find();
        return string.replace(matcher.group(0),"");
    }
    private String getTypeServiceResponce(String string)
    {
        Matcher matcher = servicePattern.matcher(string);
        matcher.find();
        return matcher.group(2);
    }
}
