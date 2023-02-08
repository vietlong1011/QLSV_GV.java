package maventest;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.sound.sampled.Port;
import maventest.QLSV;
import maventest.FileMenu;

public class Server {

  private ServerSocket serverSocket;

  // ham loginserver ban dau
  // one Thread
  public static void loginServer1() throws IOException, SQLException {
    try (ServerSocket serverSocket = new ServerSocket(3333)) {
      Socket socket = serverSocket.accept();
      DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
      DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
      Scanner sc = new Scanner(System.in);
      while (true) { // doc du lieu tu client truyen len
        String str = dataInputStream.readUTF();
        if (str.equals("q")) {
          break;
        }
        System.out.println("client say:" + str);
        // truyen du lieu tu sever ve client
        System.out.print("Sever:");
        if (str.equals("menu")) {
          // test Serializable thanh byte chuyen qua server
          // ArrayList<QLSV> menu = FileMenu.Menu();
          String menuMessage = "{\"m\": 1.them\n 2.Sua\n 3.Xoa\n 4.Doc file\n 5. Ghi file\n 6.Hien thi List\n 7.Exit}";
          if (str.equals("1"))
            System.out.println("chuc nang 1");

        } else if (str.equals("2")) {
          FileMenu.dbserver();
          // Thêm
          System.out.println("Them");
          // functionsv.addsv(arr);
        } else {
          String str2 = sc.nextLine();
          dataOutputStream.writeUTF(str2);
        }
      }
      dataInputStream.close();
      socket.close();
    }
  }

  // ham login server new
  // mutithread
  public static void loginServer(Socket connectionSocket) throws Exception {
    // Đọc dữ liệu từ client
    BufferedReader bf = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    String clientSentence = bf.readLine();

    // Xử lý dữ liệu đầu vào và trả về kết quả
    String str = clientSentence.toUpperCase() + '\n';
    System.out.println(str);

    // Gửi kết quả về cho client
    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
    outToClient.writeBytes(str);
  }

  // UDP
  public static void loginServerUDP() {
    try {
      int port = 2222;

      // String host = "localhost";

      byte[] data = new byte[1024];

      DatagramSocket socket = new DatagramSocket(port);

      DatagramPacket packet = new DatagramPacket(data, data.length);
      System.out.println("Waiting for messages from Client ... ");
      while (true) {

        packet.setData(data);

        packet.setLength(data.length);

        socket.receive(packet); // cho goi tin duoc gui den

        String receive = new String(packet.getData(), 0, packet.getLength());

        if (receive.equals("end")) {

          break;

        }

        System.out.println("Client say :" + receive);

        // receive = "Echo: "+receive;

        // StringTokenizer token = new StringTokenizer(receive," ");

        // String command = token.nextToken();

        // String valueString = token.nextToken();
        String command = receive;
        if (command.equalsIgnoreCase("menu")) {

          // gọi hàm menu từ class FileMenu

          ArrayList<QLSV> list = FileMenu.Menu();

          receive = toString(list);
          // chuyển danh sách sinh viên về dạng byte để gửi qua packet
          byte[] dataSend = list.toString().getBytes();
          packet.setData(dataSend);
          packet.setLength(dataSend.length);
          socket.send(packet);

        } else {

          receive = "Echo: " + receive;
          // lay du lieu khoi goi tin

          byte[] dataSend = receive.getBytes();

          packet.setData(dataSend);

          packet.setLength(dataSend.length);

          socket.send(packet);

        }
      }

      socket.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // to String
  private static String toString(ArrayList<QLSV> list) {
    // String result = "";

    // if(list.size()==0) {

    // return "không tìm thấy";

    // }

    // for (QLSV qlsv : list) {

    // result+=qlsv.toString();

    // }

    // return result;
    return null;
  }

  public static void main(String[] args) throws SQLException, IOException, InterruptedException {// loginServer();
    System.out.println("\tServer is listening at port 3333");
    // Server server=new Server();
    // server.start(3333);
    // Tạo một socket server
    try (ServerSocket welcomeSocket = new ServerSocket(3333)) {
      while (true) {
        // Chờ kết nối từ client
        Socket connectionSocket = welcomeSocket.accept();
        // Tạo một luồng trước bên ngoài hàm loginServer
        Thread t = new Thread(new EchoClientHandler(connectionSocket));
        System.out.println("\n\t\t " + t.getName() + " is connected and running\n");
        t.start();
        // neu muon thread hien tai chay xong thi moi cho thread tiep theo chay
        // t.join();
      }
    }
  }

  public void start(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    while (true)
      new EchoClientHandler(serverSocket.accept()).start();
  }

  public void stop() throws IOException {
    serverSocket.close();
  }

  private static class EchoClientHandler extends Thread {
    private static Socket clientSocket;
    private PrintWriter out;

    public EchoClientHandler(Socket socket) {
      this.clientSocket = socket;
    }

    // function
    public static void functionsvserver() {
      try {
        ArrayList<QLSV> arr = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        // out = new PrintWriter(clientSocket.getOutputStream(), true);
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        // out.writeUTF("Ban de ket noi thanh cong toi server");
        String inputLine;
        while ((inputLine = in.readUTF()) != null) {
          if (".".equals(inputLine) || "q".equals(inputLine)) {
            System.out.println("bye\nA one thread disconect");
            break;
          }
          System.out.println("Client say: " + inputLine);
          if (inputLine.equals("menu")) {
            boolean loop = true;
            while (loop) {
              String menuMessage = "\nMenu:\n 1.Them\n 2.Sua \n 3.Xoa\n 4.Doc file\n 5.Ghi file\n 6.Hien thi List\n 7.Thoat!!!\n";
              out.writeUTF(menuMessage);
              String menufuntion = in.readUTF();
              System.out.println("Client thuc hien chuc nang : " + menufuntion);
              // thuc hien vong lap thu 2(menu2)
              switch (menufuntion) {
                case "1":
                  ArrayList<QLSV> list = arr;
                  System.out.println("Chuc nang 1:add");
                  String menuadd = "\n\t\t1.Them sinh vien mac dinh\n\t\t2.Them sinh vien theo vi tri\n\t\t3.Exit!!!\nNhap lua chon cua ban:";
                  out.writeUTF(menuadd);
                  String luachon = in.readUTF();
                  if ("1".equals(luachon)) {
                    QLSV qlsv = new QLSV();
                    String strmasv = "";
                    // check dieu kien xem masv da ton tai hay chua
                    boolean checkmasv = true;
                    while (checkmasv) {
                      out.writeUTF("Nhap ma sinh vien can them:");
                      strmasv = in.readUTF();
                      boolean found = false;
                      for (QLSV o : arr) {
                        if (String.valueOf(o.getMasv()).equals(strmasv)) {
                          out.writeUTF("Ma sinh vien " + strmasv
                              + " da ton tai - xin vui long nhap lai\npress any key to continue ");
                          in.readUTF();
                          found = true;
                          break;
                        }
                      }
                      if (!found) {
                        checkmasv = false;
                      }
                    }
                    out.writeUTF("Nhap ten sinh vien can them: ");
                    String name = in.readUTF();
                    out.writeUTF("Nhap Classname sinh vien can them: ");
                    String classname = in.readUTF();
                    qlsv.setClassname(classname);
                    out.writeUTF("Nhap dia chi cua sinh vien can them: ");
                    String address = in.readUTF();
                    qlsv.setAdress(address);
                    out.writeUTF("Nhap diem HK1 cua sinh vien can them: ");
                    String strdiem1 = in.readUTF();
                    out.writeUTF("Nhap diem HK2 cua sinh vien can them: ");
                    String strdiem2 = in.readUTF();
                    int masv = Integer.parseInt(strmasv);
                    int diem1 = Integer.parseInt(strdiem1);
                    int diem2 = Integer.parseInt(strdiem2);
                    qlsv.setMasv(masv);
                    qlsv.setClassname(classname);
                    qlsv.setName(name);
                    qlsv.setHK1(diem1);
                    qlsv.setHK2(diem2);
                    list.add(qlsv);
                    for (QLSV qlsv2 : list) {
                      System.out.println("\n\tlistadd : \n" + qlsv2);
                    }
                    break;
                  } else if ("2".equals(luachon)) {
                    QLSV qlsv = new QLSV();
                    out.writeUTF("Nhap ma sinh vien can them:");
                    String strmasv = in.readUTF();
                    out.writeUTF("Nhap ten sinh vien can them: ");
                    String name = in.readUTF();
                    out.writeUTF("Nhap Classname sinh vien can them: ");
                    String classname = in.readUTF();
                    qlsv.setClassname(classname);
                    out.writeUTF("Nhap dia chi cua sinh vienca them: ");
                    String address = in.readUTF();
                    qlsv.setAdress(address);
                    out.writeUTF("Nhap diem HK1 cua sinh vien can them: ");
                    String strdiem1 = in.readUTF();
                    out.writeUTF("Nhap diem HK2 cua sinh vien can them: ");
                    String strdiem2 = in.readUTF();
                    out.writeUTF("Nhap vi tri ma ban muon chen ");
                    String strvitri = in.readUTF();
                    int masv = Integer.parseInt(strmasv);
                    int diem1 = Integer.parseInt(strdiem1);
                    int diem2 = Integer.parseInt(strdiem2);
                    int vitri = Integer.parseInt(strvitri);
                    qlsv.setMasv(masv);
                    qlsv.setClassname(classname);
                    qlsv.setName(name);
                    qlsv.setHK1(diem1);
                    qlsv.setHK2(diem2);
                    list.add(qlsv);
                    for (QLSV qlsv2 : list) {
                      System.out.println("\n\tlistadd : \n" + qlsv2);
                    }

                    list.add(vitri, qlsv);

                    break;

                  } else {
                    break;
                  }

                  // out.writeUTF("Đã thực hiện chức năng 1");
                  // chuc nang sua
                case "2":
                  System.out.println("Chuc nang 2:edit");
                  boolean loopcase2 = true;
                  while (loopcase2) {
                    ArrayList<QLSV> listcase2 = arr;
                    out.writeUTF("\n\tNhap thong tin sinh vien can tim : ");
                    String edit = in.readUTF();
                    // check thong tin sinh vien can tim kiem theo ten hoac masv
                    QLSV student = null;
                    for (QLSV qlsv : listcase2) {
                      if (qlsv.getName().equals(edit) ||
                          (String.valueOf(qlsv.getMasv()).equals(edit))) {
                        // while(true){
                        student = qlsv;
                        break;
                      }
                    }
                    if (student == null) {
                      out.writeUTF("Sinh vien khong ton tai \n\t\tpress any key to continue");
                      in.readUTF();
                      continue;
                    }

                    String menuedit = "\n\t\t thong tin sinh vien can sua doi\n \t1. Sua  ma sinh vien\n \t2. Sua  ten sinh vien\n \t3. Sua  lop hoc cua sinh vien\n \t4. Sua  dia chi cua sinh vien\n \t5. Sua diem hk1 sinh vien\n \t6. Sua diem hk2 sinh vien\n \t7.Exit!!!\n \t\tNhap lua chon cua ban: ";
                    out.writeUTF(menuedit);
                    String check = in.readUTF();
                    System.out.println("Client thuc hien chuc nang : " + check);

                    switch (check) {
                      case "1":
                        // check dieu kien xem masv da ton tai hay chua
                        boolean checkcase1 = true;
                        while (checkcase1) {
                          out.writeUTF("Nhap ma sinh vien moi: ");
                          String strMasvnew = in.readUTF();
                          if (String.valueOf(student.getMasv()).equals(strMasvnew)) {
                            out.writeUTF("Masv da ton tai - vui long nhap lai\n\t\tpress any key to continue");
                            in.readUTF();
                            continue;
                          }
                          checkcase1 = false;
                          int Masvnew = Integer.parseInt(strMasvnew);
                          student.setMasv(Masvnew);
                          out.writeUTF("Masv da duoc thay doi \n\tpress any key to continue");
                          in.readUTF();
                        }
                        break;
                      case "2":
                        out.writeUTF("Nhap ten sinh vien moi: ");
                        String NameNew = in.readUTF();
                        student.setName(NameNew);
                        break;
                      case "3":
                        out.writeUTF("Nhap lop hoc moi: ");
                        String ClassnameNew = in.readUTF();
                        student.setClassname(ClassnameNew);
                        break;
                      case "4":
                        out.writeUTF("Nhap dia chi moi: ");
                        String AdressNew = in.readUTF();
                        student.setAdress(AdressNew);
                        break;
                      case "5":
                        while (true) {
                          try {
                            out.writeUTF("Nhap diem hk1 moi: ");
                            String HK1New = in.readUTF();
                            int diem = Integer.parseInt(HK1New);
                            student.setHK1(diem);
                            break;
                          } catch (Exception e) {
                            out.writeUTF("Vui long nhap gia tri dung");
                          }
                        }
                        break;
                      case "6":
                        while (true) {
                          out.writeUTF("Nhap diem hk2 moi: ");
                          String HK2New = in.readUTF();
                          try {
                            int diem = Integer.parseInt(HK2New);
                            student.setHK2(diem);
                            break;
                          } catch (Exception e) {
                            out.writeUTF("Vui long nhap gia tri dung");
                          }
                        }
                        break;
                      case "7":
                        loopcase2 = false;
                        break;

                      default:
                        out.writeUTF("Vui long chon lai.");
                        break;
                    }
                    break;
                  }
                  break;
                // chuc nang xoa
                case "3":
                  System.out.println("Chuc nang xoa");
                  ArrayList<QLSV> listcase3 = arr;
                  boolean loopcase3 = true;
                  while (loopcase3) {
                    out.writeUTF("\n\tNhap thong tin sinh vien can xoa : ");
                    String delete = in.readUTF();
                    boolean isExisted = false;
                    for (int i = 0; i < listcase3.size(); i++) {
                      if ((listcase3.get(i).getName().equals(delete)) ||
                          (String.valueOf(listcase3.get(i).getMasv()).equals(delete))) {
                        isExisted = true;
                        listcase3.remove(i);
                        break;
                      }
                    }
                    if (!isExisted) {
                      out.writeUTF("Sinh vien khong ton tai\n\t\tpress any key to continue");
                      in.readUTF();
                    } else {
                      out.writeUTF(
                          "\n\t\tXoa sinh vien thanh cong\n\t\t1. Xoa tiep tuc\n\t\t2. Exit\n Nhap lua chon cua ban:");

                      String chon = in.readUTF();
                      if (chon.equals("2")) {
                        loopcase3 = false;
                        break;
                      }
                    }
                  }
                  break;
                case "4":
                  arr = ReadWriteFile.readFromFile();
                  System.out.println("Chuc nang doc file");
                  out.writeUTF(
                      "da thuc hien chuc nang 4 \nDoc file thanh cong \nHien thi list\npress any key to continue");
                  for (QLSV q : arr) {
                    System.out.println(q.toString());
                  }
                  in.readUTF();
                  break;
                case "5":
                  System.out.println("Chuc nang ghi file ");
                  ReadWriteFile.writeToFile(arr);
                  out.writeUTF(
                      "da thuc hien chuc nang 5\npress any key to continue\n\t\tPlease wait a few minutes\n\t\tdata is being updated to the server\n\t\tLoadding....");
                  in.readUTF();
                  FileMenu.dbserver();
                  System.out.print("Enter a message to send to the client : ");
                  String message = sc.nextLine();
                  out.writeUTF(message + "\npress enter to exit!!!");
                  in.readUTF();
                  break;
                case "6":
                  System.out.println("Chuc nang 6");
                  ArrayList<QLSV> listcase6 = arr;
                  StringBuilder sb = new StringBuilder();
                  for (QLSV o : listcase6) {
                    sb.append("\n\t\t ").append(o);
                  }
                  String listAsString = sb.toString();
                  out.writeUTF("list : " + listAsString + "\npress any key to continue");
                  in.readUTF();
                  break;
                case "7":
                  System.out.println("Chuc nang 7: Thoat");
                  loop = false;
                  out.writeUTF("You have successfully exited!!!\n press any key to continue");
                  in.readUTF();
                  break;
                default:
                  System.out.println("Chuc nang khong hop le");
                  out.writeUTF("Chuc nang khong hop le\npress any key to continue");
                  in.readUTF();
                  break;
              }
              // ket thuc vong lap switch
              // out.writeUTF("Nhan enter de tiep tuc");

            }
            // dieu kien 2 cua vong lap 1

          }
          // thoat khoi vong lap 1
          int numberOfThreads = Thread.activeCount();
          if (numberOfThreads > 2) {
            System.out.println("\n\t" + Thread.currentThread().getName() + " is running");
          }
          System.out.print("Server rep: ");
          String outputLine = sc.nextLine();
          if (outputLine.equals("database")) {
            FileMenu.dbserver();
            outputLine = "Loading......";

          }
          out.writeUTF(outputLine);
        }

        try {
          in.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        out.close();
      } catch (Exception e) {
        System.out.println("A one thread disconect");

      }

    }

    @Override
    public void run() {
      // in ra mane thread
      // System.out.println(Thread.currentThread().getName());
      // TODO Auto-generated method stub
      functionsvserver();
    }
  }
}