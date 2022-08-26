package IO;

//import java.util.List;
//import java.awt.List;
//import java.util.Map;

import java.io.*;

public class IOTest1 {
    public static void main(String[] args) throws IOException {
        try(InputStream read = new FileInputStream("F:\\javatest\\readme.txt")){
            try(OutputStream output = new FileOutputStream("F:\\\\javatest\\\\output.txt")){
                byte[] k = new byte[10];
                int n = read.read(k);
                int b = 0;
                while(n!=-1){
                    b=0;
                    StringBuilder a = new StringBuilder();
                    for(int i:k){
                        a.append((char)i);
                        b++;
                        if(b==n){
                            break;

                        }

                    }
//                try(OutputStream output = new FileOutputStream("F:\\javatest\\output.txt")){
//                    output.write(k);
//                }
                    System.out.println(a.toString());
                    output.write(k,0,n);
                    n = read.read(k);

                }
            }



        }
    }

}

