package assembler;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FirstPhase 
{
    static ArrayList<SymbolAddress> symTab = new ArrayList<SymbolAddress>();
    ArrayList<String> instructions = new ArrayList<>(); 
    int addr[];
   protected void firstPass()
   {
       int lc=0,i=0,start=100,data=0;
       
       try 
       {
           File f = new File("C:\\Assembler\\input.txt");
           Scanner in=new Scanner(f);
           int pos$data = 0;
           
           while(in.hasNextLine())
           {
               String s = in.nextLine();
               if(!s.equalsIgnoreCase(".data:"))
               {
                   lc++;
                   instructions.add(s);
               }
               else if(s.equalsIgnoreCase(".data:"))
               {
                   pos$data = lc;
                   lc++;
                   instructions.add(s);
               }
           }
            addr=new int[lc];
          System.out.println(lc);
          for(i=0;i<pos$data;i++)
          {
              addr[i]=start;
              start+=4;
              System.out.println("addr: "+addr[i]+" LC: "+lc);
              this.populateSymbolTable(instructions.get(i), addr[i]);
          }
          data=start+4;
           System.out.println(addr[pos$data-1]);
          addr[pos$data+1]=addr[pos$data-1]+4;
//          System.out.println(addr[11]);
          System.out.println("hgfhg"+addr[pos$data]);
          for(i = pos$data+1;i<lc;i++)
          {
               System.out.println("heloFirst "+addr[i]);
               String s=instructions.get(i);
               boolean error = false;
               String wrong="";
               System.out.println("heloSecond "+addr[i]);
               for(SymbolAddress e:symTab)
               {
                   if(e.label.equals(s.split(":")[0]))
                   {
                       error=true;
                       wrong= s.split(":")[0];
                   }
               }
               if(error)
               {
                   System.out.println("!---------- ERROR: Label "+wrong+" already exists ----------!");
                   //System.exit(0);
               }
//               else
//                   System.out.println("label":);
//               System.out.println(addr.length);
               System.out.println("hello"+addr[i]);
               System.out.println("s="+s);
               symTab.add(new SymbolAddress(s.split(":")[0],addr[i]));
               if(s.contains(".byte") && i<lc-1)
               {
                   System.out.println("byte");
                   addr[i+1] = addr[i] + s.split(",").length;
                   addr[i+1] += (4-addr[i+1]%4);
                   
               }
               else if(s.contains(".word") && i<lc-1)
               {
                   addr[i+1] = addr[i] + s.split(",").length * 4;
                   System.out.println("word");
               }
           }
       } catch (FileNotFoundException ex) {
           Logger.getLogger(FirstPhase.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
   /*public static void main(String[]args)
   {
       FirstPhase ob = new FirstPhase();
       ob.firstPass();
       for(SymbolAddress e: ob.symTab)
       {
           System.out.println(e.address+" "+e.label);
       }
   }*/
   
   private void populateSymbolTable(String instruction,int instructionAddress)
   {
       String label="";
       String components[] = instruction.split(" ");
       if(components.length==3)
       {
           label = components[0];           
           this.addToSymbolTable(label, instructionAddress);
       }
   }
   
   private void addToSymbolTable(String label,int address)
    {
        boolean error = false;
        for(SymbolAddress e:symTab)
        {
            if(e.label.equals(label))
                error=true;
        }
        if(error)
        {
            System.out.println("!---------- ERROR: Label already exists ----------!");
            System.exit(0);
        }
        symTab.add(new SymbolAddress(label,address));
    }
    
}
/*
.data
a: .byte  1,1,1  
*/