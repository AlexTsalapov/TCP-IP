package clienttcp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client extends Application {

    Socket sock = null;
    InputStream is = null;
    OutputStream os = null;

    public void error() {
        Alert war = new Alert(Alert.AlertType.ERROR);
        war.setTitle("Ошибка");
        war.setHeaderText("Некоректный ввод!");
        war.setContentText("Запрещен ввод всех символов кроме: 0-9 и SPACE не более одного символа подряд");
        war.showAndWait();
    }

    public static void main(String args[]) {
        Client c = new Client();
        Application.launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("КЛИЕНТ");
        TextField tf_ip = new TextField("127.0.0.1");//ip adress клиента
        tf_ip.setEditable(false);
        TextField tf_port = new TextField("1024");// port клиента   tf2 = new TextField();
        tf_port.setEditable(false);
        Button btn_connect = new Button("connect ");
        Button btn_send = new Button("  send  ");
        TextField tf_input = new TextField();
        TextField tf_output = new TextField();
        Label la_ip = new Label("IP ADRESS");
        Label la_port = new Label("PORT");
        Label la_input = new Label("INPUT");
        Label la_output = new Label("OUTPUT");


        Pane p = new Pane();
        p.setMinHeight(20);
        Pane p1 = new Pane();
        p1.setMinHeight(20);
        Pane p2 = new Pane();
        p2.setMinHeight(20);
        Pane p3 = new Pane();
        p3.setMinHeight(10);

        TilePane tp1 = new TilePane(la_ip, tf_ip);
        TilePane tp2 = new TilePane(la_port, tf_port, btn_connect);
        TilePane tp3 = new TilePane(la_input, tf_input, btn_send);
        TilePane tp4 = new TilePane(la_output, tf_output);
        VBox vb3 = new VBox(p, tp1,p3, tp2, p1, tp3, p2, tp4);

        vb3.setStyle("-fx-background-color: #6643f3");
        Scene scene = new Scene(vb3,750,550);

        stage.setScene(scene);

        stage.show();
        btn_connect.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                try {
                    sock = new Socket(InetAddress.getByName(tf_ip.getText()), Integer.parseInt(tf_port.getText()));
                    //создается сокет по ip адрессу и порту
                } catch (NumberFormatException e) {
                } catch (UnknownHostException e) {
                } catch (IOException e) {
                    System.out.println("сокет не создался");
                }
            }
        });
        btn_send.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (sock == null) {
                    try {
                        sock = new Socket(InetAddress.getByName(tf_ip.getText()), Integer.parseInt(tf_port.getText()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    is = sock.getInputStream(); // входной поток для чтения данных
                    os = sock.getOutputStream();// выходной поток для записи данных
                    String numbers = ""; //перменная,в которую записываются введенные числа
                    numbers += tf_input.getText() + " ";
                   if(numbers.contains("  "))
                   {
                     int count= numbers.indexOf("  ");
                     numbers=numbers.substring(0,numbers.indexOf("  "));
                     numbers+=numbers.substring(numbers.indexOf("  ")+1,numbers.length());

                   }
                    if (!numbers.matches("[0-9 ]+")) {
                        error();
                        return;
                    }
                    String[] strings = numbers.split(" ");
                    for (int i = 0; i < strings.length; i++) {
                        if (Integer.parseInt(strings[i]) > 10) {
                            error();
                            return;
                        }
                    }

                    os.write(numbers.getBytes()); // отправляем введенные данные. Тип string переводим в byte
                    byte[] bytes = new byte[1024];
                    is.read(bytes); //получаем назад информацию,которую послал сервер
                    String str = new String(bytes, "UTF-8"); // переводим тип byte в String


                    tf_output.setText(str);


                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {

                    try {
                        os.close();//закрытие выходного потока
                        is.close();//закрытие входного потока
                        sock.close();//закрытие сокета, выделенного для работы с сервером
                        sock = null;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
            }
        });
    }
}




