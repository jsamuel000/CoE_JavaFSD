import java.util.*;
public class StringProcessor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        str = str.toLowerCase();
        //System.out.println(reverse(str));
        System.out.println(splitAndCapitalize(str));
    }

   /*  public static String reverse(String str) {
        String rev="";
        for(int i=str.length()-1;i>=0;i--)
        {
            rev+=str.charAt(i);
        }
        return rev;
    }*/

    public static String splitAndCapitalize(String str)
    {
        String[] str1 = str.split(" ");
        for(int i=0; i<str1.length;i++)
        {
            char c=Character.toUpperCase(str1[i].charAt(0));
            str1[i]=str1[i].replace(str1[i].charAt(0),c);
        }
        return String.join(" ",str1);
    }
}
