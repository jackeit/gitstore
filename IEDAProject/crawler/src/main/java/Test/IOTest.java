package Test;


import java.io.*;
import java.util.List;

public class IOTest {
    public static void main(String[] args) throws IOException {
        try(OutputStream fileOutputStream = new FileOutputStream("F:/javatest/outpput2.txt",true)){
            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8")){
                try(PrintWriter printWriter = new PrintWriter(outputStreamWriter)){
                    printWriter.println("hellosdhiuwedyhwuidheuwywfyhcrfeyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy" +
                            "eeeeeeeeeeeeeeeeeefasdadsad");
                }

            }
        }

        String k = "065 野心";
        String j = k.replaceFirst(" ",".");

        System.out.println(j);

    }
    public void IOWriter(List<String> o) throws  IOException{
        try(OutputStream fileOutputStream = new FileOutputStream("F:/javatest/book.txt",true)){
            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8")){
                try(PrintWriter printWriter = new PrintWriter(outputStreamWriter)){
                    for(String i:o){
                        printWriter.println(i);
                    }
                }

            }
        }
    }

}
