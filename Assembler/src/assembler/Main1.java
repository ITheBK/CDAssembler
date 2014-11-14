/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arvind
 */
public class Main1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int pos$data=0;
        System.out.println("hello from main1");
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
                    String s = obj.instructions.get(i)+" "+obj.addr[i];
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
            
     //       System.out.println(FirstPhase.symTab);
//            for(int i=0;i<obj.instructions.size();i++)
//            {
//                
//            }
            writerpass1.close();
        } catch (IOException ex) {
            Logger.getLogger(Main1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        System.out.println("hello".substring(0,3));
//        System.out.println("11110000000000000000000000001111".substring(4, 28));
//        System.out.println("11110000000000000000000000001111".length());
//        
//        int diff = Integer.numberOfTrailingZeros(Integer.highestOneBit(256))-Integer.numberOfTrailingZeros(Integer.lowestOneBit(255));
//        System.out.println(diff>7);
//        String dpiMnemonics[] = {"and","eor","sub","rsb","add","adc","sbc","rsc","tst","teq","cmp","cmn","orr","mov","bic","mvn"};
//        System.out.println(Integer.toBinaryString(10));
//        String regNum = Integer.toBinaryString(Integer.parseInt("r12".split("r")[1]))+"";
//        System.out.println(regNum.charAt(2));
//        int[] encode = {0,0,0,0,0,0,0,0,0,0};
//        int pos=5;
//        int j=0;
//        
//        for(int i=pos;i<pos+4;i++)
//        {
//            //System.out.println(regNum.charAt(j));
//            encode[i]=Integer.parseInt(regNum.charAt(j)+"");
//            j++;
//        }
//        for(int i=0;i<encode.length;i++)
//            System.out.print(encode[i]+" ");
//        String rl="{r0-r2,pc}";
//        System.out.println(rl.substring(1,rl.length()-1));
    }
    
}
