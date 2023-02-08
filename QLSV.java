package maventest;

import java.io.Serializable;

public class QLSV implements Serializable{
private int Masv ;
private String Name ; 
private String Classname ; 
private String Adress;
private int HK1;
private int HK2;

public  QLSV(){};
public  QLSV(int Masv , String Name , String Classname ,String Adress, int HK1,int HK2)
{

    this.Masv = Masv;
    this.Name = Name;
    this.Classname = Classname;
    this.Adress = Adress;
    this.HK1 = HK1;
    this.HK2 = HK2;
};
public void setMasv(int masv) {
    Masv = masv;
}
public int getMasv() {
    return Masv;
}
public void setAdress(String adress) {
    Adress = adress;
}
public String getAdress() {
    return Adress;
}
public void setClassname(String classname) {
    Classname = classname;

}public String getClassname() {
    return Classname;
}public void setName(String name) {
    Name = name;
}public String getName() {
    return Name;
}
public void setHK1(int hK1) {
    HK1 = hK1;
}public int getHK1() {
    return HK1;
}public void setHK2(int hK2) {
    HK2 = hK2;
}public int getHK2() {
    return HK2;
}
public float  tbc(){
 return ((HK1 + HK2) / 2);
}
@Override
public String toString() {
    // TODO Auto-generated method stub
   return String.format("%4d%30s%10s%30s%5d%5d  %.1f",Masv,Name,Classname,Adress,HK1,HK2,tbc());
   // return String.format("%4d%30s%10s%30s%5d%5d",Masv,Name,Classname,Adress,HK1,HK2);

}
}