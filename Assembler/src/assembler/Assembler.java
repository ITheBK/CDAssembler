/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arvind
 */
public class Assembler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int pos$data=0;
        FirstPhase obj = new FirstPhase();
        obj.firstPass();
        try {
            FileWriter writerpass1 = new FileWriter("C:\\Assembler\\firstpass.txt");
            FileWriter writerpass2 = new FileWriter("C:\\Assembler\\secondpass.txt");
            //BufferedWriter bw = new BufferedWriter(writer);
            for(int i=0;i<obj.instructions.size();i++)
            {
                if(!obj.instructions.get(i).equalsIgnoreCase(".data:"))
                {
                    String s = obj.addr[i]+" "+obj.instructions.get(i);
                    
                    if(s.contains(":"))
                    {
                        String[] a=s.split(":");
                        if(a[1].contains(".byte"))
                            a[1]=a[1].replace(".byte","byte");
                        if(a[1].contains(".word"))
                            a[1]=a[1].replace(".word","word");
                        s=a[0]+" "+a[1];
                    }
                    System.out.println(s);
                    writerpass1.write(s+"\n");
                   // bw.close();
                }
            }
                 for(int i=0;i<obj.instructions.size();i++)
            {
                if(!obj.instructions.get(i).equalsIgnoreCase(".data:"))
                {
                  //  System.out.print(obj.instructions.get(i)+" "+obj.addr[i]+" ");
                    int[] arr = new EncodeInstruction().encode(obj.instructions.get(i), obj.addr[i]);
                    for(int j=31;j>=0;j--)
                    {
                        System.out.print(arr[j]);
    //                    if(j%4==0)
    //                        System.out.println();
                    }
                    System.out.println();
                }
                else if(obj.instructions.get(i).equalsIgnoreCase(".data:"))
                {
                    pos$data=i;
                    break;
                }    
            }
            System.out.println(".data part "+obj.instructions.get(pos$data+1));
            writerpass1.close();
            writerpass2.close();
        } catch (IOException ex) {
            Logger.getLogger(Main1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
