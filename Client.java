package maventest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import maventest.FileMenu;

public class Client implements Runnable {

    private Socket client;

    public Client(Socket client) {
        this.client = client;
    }

    // TCP
    // function login client
    public static void loginClient() throws UnknownHostException, IOException, ClassNotFoundException, SQLException {
        Socket socket = new Socket("localhost", 3333);
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);

        // nhap du lieu tu client
        try{
        while (true) {
            System.out.print("Client:");
            String str = sc.nextLine();
            out.writeUTF(str);
            // day du lieu len sever
            // dataOutputStream.flush();
            String str2 = in.readUTF();
         
            if (str.equals("q") ||str2.equals("q")) {
                System.err.println("\n\t\tServer asks client to disconnect now!!!");
                break;
            }   System.out.println("Server say : " + str2);
            // lay du lieu tu sever ve
            // String str2 = dataInputStream.readUTF();
            // System.out.println("server say : "+ str2);
            // if (str2.equals("menu")) {
            // ArrayList<QLSV> menu = FileMenu.Menu();
            // // test Serializable thanh byte chuyen qua server
            // // int length = dataInputStream.readInt();
            // // byte[] data = new byte[length];
            // // dataInputStream.readFully(data);
            // // ByteArrayInputStream bais = new ByteArrayInputStream(data);
            // // ObjectInputStream ois = new ObjectInputStream(bais);
            // // ArrayList<QLSV> menu = (ArrayList<QLSV>) ois.readObject();

            // // System.out.println("Received menu from server: " + menu);
            // } else {
            // System.out.println("Server say: " + str2);
            // }
        }
        in.close();
        socket.close();
    }catch (EOFException e ) {
        // TODO: handle exception
        System.out.println("you disconect successfully");
    }
}

    // UDP
    public static void loginClientUDP() throws IOException {
        DatagramSocket socket;

        InetAddress address;

        int port = 2222;

        byte[] buff = new byte[1024];

        socket = new DatagramSocket();

        address = InetAddress.getByName("localhost");
        // tao goi tin gui di
        DatagramPacket packet = new DatagramPacket(buff, buff.length);
        // nhap du lieu vao tu ban phim
        System.out.print("Nhap lenh: ");
        BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

        String line;
        // tao tep tin gui qua server
        while ((line = userIn.readLine()) != null) {

            byte[] data = line.getBytes();

            packet.setData(data);

            packet.setLength(data.length);

            packet.setAddress(address);
            packet.setPort(port);
            socket.send(packet); // send server

            if (line.equals("end"))
                break;

            packet.setData(buff);

            packet.setLength(buff.length);

            socket.receive(packet);

            String receive = new String(packet.getData(), 0, packet.getLength());

            System.out.println(receive);

        }

        socket.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        loginClient();
        // loginClientUDP();

    }

    @Override
    public void run() {
        try {
            loginClient();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // TODO Auto-generated method stub

    }
}
