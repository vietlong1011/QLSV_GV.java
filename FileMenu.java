package maventest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.Serializable;
import java.sql.*;

import maventest.functionsv;
import maventest.ReadWriteFile;
import maventest.AppTest;

public class FileMenu implements Serializable {
    // ham lay du lieu tu database ra
    public static ArrayList<QLSV> getData() {
        ArrayList<QLSV> list = new ArrayList<>();

        Connection c = AppTest.openConnection();
        if (c != null) {
            System.out.println("Connect thanh cong");
            try {
                Statement st = c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String str = "Select * From student";
                ResultSet rs = st.executeQuery(str);
                while (rs.next()) {
                    int Masv = rs.getInt("Masv");
                    String Name = rs.getString("Name");
                    String Classname = rs.getString("Class");
                    String Adress = rs.getString("Adress");
                    int HK1 = rs.getInt("HK1");
                    int HK2 = rs.getInt("HK2");

                    QLSV qlsv = new QLSV(Masv, Name, Classname, Adress, HK1, HK2);
                    list.add(qlsv);
                }
                for (QLSV q : list) {
                    System.out.println(q);
                }

                if (!c.isClosed())
                    c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("that bai");
        }
        return list;
    }

    // ham luu du lieu vao database (add)
    public static void saveToDB(ArrayList<QLSV> arr) {
        Connection conn = null;
        PreparedStatement stmt = null;
        final String hostName = "localhost";
        final String dbName = "qlsv";
        final String userName = "root";
        final String password = "10112002";

        try {
            String DB_URL = "jdbc:mysql://" + hostName + ":3306/" + dbName;

            // Connect to the database
            conn = DriverManager.getConnection(DB_URL, userName, password);

            for (QLSV student : arr) {
                // check primary database
                String sqlCheck = "SELECT * FROM student WHERE Masv = ?";
                stmt = conn.prepareStatement(sqlCheck);
                stmt.setInt(1, student.getMasv());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Delete the duplicate data
                    String sqlDelete = "DELETE FROM student WHERE Masv = ?";
                    stmt = conn.prepareStatement(sqlDelete);
                    stmt.setInt(1, student.getMasv());
                    stmt.executeUpdate();
                }
                // Create the insert statement
                String sql = "INSERT INTO student (Masv, Name, Class, Adress, HK1, HK2) VALUES (?,?,?,?,?,?)";
                stmt = conn.prepareStatement(sql);

                // Set the values for the insert statement
                stmt.setInt(1, student.getMasv());
                stmt.setString(2, student.getName());
                stmt.setString(3, student.getClassname());
                stmt.setString(4, student.getAdress());
                stmt.setInt(5, student.getHK1());
                stmt.setInt(6, student.getHK2());

                // Execute the insert statement
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the resources
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ham xoa du lieu trong database
    public static void deleteStudent(int masv) throws SQLException {
        Connection c = AppTest.openConnection();
        if (c != null) {
            System.out.println("Connect thanh cong");
            try {
                String sql = "DELETE FROM student WHERE Masv = ?";
                PreparedStatement pstmt = c.prepareStatement(sql);
                pstmt.setInt(1, masv);
                int rowDeleted = pstmt.executeUpdate();
                if (rowDeleted > 0) {
                    System.out.println("\n\t\t Xóa thành công");
                } else {
                    System.out.println("Không tìm thấy sinh viên có mã " + masv);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!c.isClosed()) {
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Kết nối thất bại");
        }
    }

    // ham edit du lieu trong database
    public static void editStudent(int masv, String a) throws SQLException {
        Connection c = AppTest.openConnection();
        if (c != null) {
            System.out.println("Connect thanh cong");
            try {
                boolean exit = false;
                while (!exit) {
                    System.out.println("1. Edit Masv");
                    System.out.println("2. Edit Name");
                    System.out.println("3. Edit Classname");
                    System.out.println("4. Edit Address");
                    System.out.println("5. Edit diem hoc ky 1");
                    System.out.println("6. Edit diem hoc ky 2");
                    System.out.println("7. Exit!!!");
                    Scanner sc = new Scanner(System.in);

                    System.out.print("Nhap lua chon : ");
                    int choice = sc.nextInt();
                    String colum = " ";
                    switch (choice) {
                        case 1:
                            colum = "Masv";
                            break;
                        case 2:
                            colum = "Name";
                            break;
                        case 3:
                            colum = "Class";
                            break;
                        case 4:
                            colum = "Adress";
                            break;
                        case 5:
                            colum = "HK1";
                            break;
                        case 6:
                            colum = "HK2";
                            break;
                        case 7:
                            exit = true;
                            break;
                        default:
                            System.out.println("Lua chon khum hop le, chon lai .");
                    }
                    String sql = " UPDATE student SET  " + colum + " = ? WHERE Masv= ?";
                    // String sql = " UPDATE student SET Name = ? WHERE Masv= ?";
                   // System.out.println("a : " + sql);

                    PreparedStatement pstmt = c.prepareStatement(sql);
                    pstmt.setString(1, a);
                    pstmt.setInt(2, masv);

                    int edit = pstmt.executeUpdate();
                    if (edit > 0) {
                        System.out.println("Edit thanh cong");
                    } else {
                        System.out.println("Khong tìm thay sinh vien có ma " + masv);
                    }
                } // end while
            } catch (SQLException e) {
            } finally {
                if (!c.isClosed()) {
                    try {
                        c.close();
                    } catch (SQLException e) {
                    }
                }
            }
        } else {
            System.out.println("Kết nối thất bại");
        }
    }

    // display menu client
    public static ArrayList<QLSV> Menu() throws SQLException {
        ArrayList<QLSV> arr = new ArrayList<>();

        // QLSV qlsv = new QLSV(2110 , "long","d16","hai duong",10,8);
        // QLSV qlsv1 = new QLSV(2111 , "long1","d16","hai duong",9,10);
        // QLSV qlsv2 = new QLSV(2112 , "long2","d17","hai duong",10,10);
        // QLSV qlsv3 = new QLSV(2113 , "long3","d16","hai duong",10,10);
        // arr.add(qlsv);
        // arr.add(qlsv1);
        // arr.add(qlsv2);
        // arr.add(qlsv3);
        Scanner input = new Scanner(System.in);

        boolean exit = false;
        // getData();
        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Them");
            System.out.println("2. Sua");
            System.out.println("3. Xoa");
            System.out.println("4. Doc file and add file");
            System.out.println("5. Ghi file");
            System.out.println("6. Hien thi List");
            System.out.println("7. Thoat");
            // up server
            System.out.print("Chon chuc nang: ");
            int choice = input.nextInt();
            // if (choice == 4) {
            // arr = ReadWriteFile.readFromFile();
            // }
            int check = 2;
            switch (choice) {
                case 1:
                    // Thêm
                    System.out.println("Them");
                    functionsv.addsv(arr);
                    break;
                case 2:
                    // Sửa
                    System.out.println("Sua");
                    functionsv.edit(arr);
                    break;
                case 3:
                    // Xóa
                    System.out.println("Xoa");
                    functionsv.delete(arr);
                    break;
                case 4:
                    // Đọc file
                    arr = ReadWriteFile.readFromFile();
                    System.out.println("Doc file thanh cong");
                    System.out.println("Hien thi list");
                    for (QLSV q : arr) {
                        System.out.println(q.toString());
                    }

                    break;
                case 5:
                    // Ghi file
                    System.out.println("Ghi file");
                    ReadWriteFile.writeToFile(arr);
                    break;
                case 6:
                    // hien thi file
                    System.out.println("Hien thi list");
                    for (QLSV q : arr) {
                        System.out.println(q.toString());
                    }; check =1;

                    break;
                case 7:
                    // try{
                    // saveToDB(arr);}
                    // catch (Exception e) {
                    // // TODO: handle exception
                    // System.err.println("Save unsuccessful");
                    // }
                    exit = true;

                    break;
                default:
                    System.out.println("Lua chon khum hop le, chon lai .");
            }
            if (check != 1) {
                for (QLSV q : arr) {
                    System.err.println("\n\t\tdata is changed ");
                    System.out.println(q.toString());
                }
            }
        }
        return arr;

    }

    // display server ( dbserver)
    public static void dbserver() throws SQLException {
        Scanner sc = new Scanner(System.in);
        ArrayList<QLSV> arr1 = ReadWriteFile.readFromFile();
        System.err.println("\n\t\tdata is changed (read to file)");
        for (QLSV q : arr1) {
            System.out.println(q.toString());
        }
        boolean exit = false;
        while (!exit) {
            System.out.println("1. Hien thi database");
            System.out.println("2. Add data changed from database");
            System.out.println("3. Edit database from Masv");
            System.out.println("4. Delete database from Masv");
            System.out.println("5. Exit!!!");
            System.out.print("Chon chuc nang: ");
            int chon = sc.nextInt();
            switch (chon) {
                case 1:
                    getData();
                    break;
                case 2:
                    try {
                        for (QLSV q : arr1) {
                            System.err.println("\n\t\tdata is changed ");
                            System.out.println(q.toString());
                        }
                        System.out.print("\n\tu have want to save the data change to database (yes / no) : ");
                        sc.nextLine();
                        String a = sc.nextLine();
                        if (a.equals("yes")) {
                            saveToDB(arr1);
                            getData();
                            System.out.println("\n\n\t\t\t\t\t Save successful !!!");
                        } else {
                            System.out.println("Save unsuccessful");
                            break;
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.err.println("Save unsuccessful");
                    }
                    break;
                case 3:
                    getData();
                    System.out.print("Nhap ma sinh vien can edit trong database: ");
                    int e = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Nhap gia tri ma ban can sua : ");
                    String data = sc.nextLine();
                    editStudent(e, data);
                    break;
                case 4:
                    System.out.print("Nhap ma sinh vien can xoa khoi database: ");
                    int n = sc.nextInt();
                    deleteStudent(n);
                    break;
                case 5:
                    exit = true;
                    break;

                default:
                    System.out.println("Lua chon khum hop le, chon lai .");
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Menu();
        ArrayList<QLSV> arr = new ArrayList<>();
        dbserver();
        // QLSV qlsv = new QLSV(1234, "new student", "d18", "newaddress", 11, 9);
        // arr.add(qlsv);
        // saveToDB(arr);
        // getData();
    }
}
