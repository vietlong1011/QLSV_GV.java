
package maventest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import maventest.FileMenu;
import maventest.QLSV;
public class ReadWriteFile {
    private static final String FILENAME = "list.csv";
    // ham ghi file
    public static void writeToFile(ArrayList<QLSV> list) {
        try {
            FileWriter fw = new FileWriter(FILENAME);
          BufferedWriter bw = new BufferedWriter(fw);
            for (QLSV qlsv : list) {
                fw.write(qlsv.toString() + System.lineSeparator());
            }
            bw.close();
            fw.close();
        } catch (IOException e) { System.out.println("");
        }catch (NumberFormatException e2)
        {
            System.out.println("");
        }

    }
    // ham doc file
    public static ArrayList<QLSV> readFromFile() {
        ArrayList<QLSV> list = new ArrayList<>();
        try {
            FileReader fr = new FileReader(FILENAME);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
               // lenh in 
               // System.out.println(line.toString());
               // in theo kieu ky tu
                // int line;
                // while ((line = br.read()) != -1) {
                //     System.out.print((char) line);
                String[] data = line.split("\\s+");
                try{
                int masv = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                String classname = data[2].trim();
                String address = data[3].trim();
                int hk1 = Integer.parseInt(data[4].trim());
                int hk2 = Integer.parseInt(data[5].trim());
                list.add(new QLSV(masv, name, classname, address, hk1, hk2));
                // float tbc  = Float.parseFloat(data[5].trim());
             //list.add(new QLSV(masv, name, classname, address, hk1, hk2));
            }
             catch (NumberFormatException e2)
        {
        System.out.println(" bi loi roi");
          e2.printStackTrace();
        }
             }
                     System.out.println("doc thanh cong");  
            br.close();
            fr.close();
        } 
        catch (IOException e) {
           e.printStackTrace();
        } 

        return list;
      
    }
    public static void main(String[] args) {
       
     
        ArrayList<QLSV> arr = new ArrayList<>();
        QLSV qlsv = new QLSV(2110 , "long","d16","haiduong",10,8);
        QLSV qlsv1 = new QLSV(2111 , "long1","d16","haiduong",9,10);
       QLSV qlsv2 = new QLSV(2112 , "long2","d17","haiduong",10,10);
      QLSV qlsv3 = new QLSV(2113 , "long3","d16","haiduong",10,10);
         arr.add(qlsv);
       arr.add(qlsv1);
     arr.add(qlsv2);
      arr.add(qlsv3);
       writeToFile(arr);
    //    ArrayList<QLSV> arr1  = readFromFile();
    //    for (QLSV o : arr1) {
    //        System.out.println(o.toString());
    //    }
    }

}
