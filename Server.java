package servertcp;

import java.net.*;
import java.io.*;
import java.time.Month;
import java.util.HashMap;

public class Server {
    ServerSocket sock = null;
    InputStream is = null;
    OutputStream os = null;
    private static HashMap<Integer, String> number_to_string = new HashMap<Integer, String>() {
        {
            put(0, "Нуль");
            put(1, "Один");
            put(2, "Два");
            put(3, "Три");
            put(4, "Четыре");
            put(5, "Пять");
            put(6, "Шесть");
            put(7, "Семь");
            put(8, "Восемь");
            put(9, "Девять");
            put(10, "Десять");

        }
    };
    static int countclients = 0;

    Server() {
        try {
            sock = new ServerSocket(1024);
            while (true) {
                Socket client = sock.accept();

                countclients++;
                System.out.println("=======================================");
                System.out.println("Client " + countclients + " connected");
                is = client.getInputStream();
                os = client.getOutputStream();
                byte[] bytes = new byte[1024];
                int count = is.read(bytes);
                String str = new String(bytes, "UTF-8");
                String[] numbers = str.split(" ");
                String m = "";
                bytes = new byte[1024];
                for (int i = 0; i < numbers.length - 1; i++) {
                    if (numbers[i].matches("[0-9.]+")) {
                        System.out.println("Cервер принял число " + numbers[i]);

                        m += number_to_string.get(Integer.parseInt(numbers[i]));
                        {
                            m += " ";
                        }
                    }
                }
                bytes = m.getBytes();
                os.write(m.getBytes());
            }

        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        } finally {
            try {
                is.close();//закрытие потока
                os.close();
                sock.close();
            }
            catch(IOException ex)
            {
                System.out.println("не закрылись потоки");
            }
            System.out.println("Client " + countclients + " disconnected");
        }
    }

    public static void main(String args[])  {
        Server ser = new Server();

    }
}

