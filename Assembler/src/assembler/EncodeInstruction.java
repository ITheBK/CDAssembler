/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import java.util.ArrayList;

/**
 *
 * @author arvind
 */
public class EncodeInstruction
{
    ArrayList<SymbolAddress> symTab = new ArrayList<SymbolAddress>();
    public int[] encode(String instruction,int instructionAddress)
    {
        int[] encoding=new int[32];
        for(int i=0;i<encoding.length;i++)
        {
            encoding[i]=0;
        }
        String label="";
        String mnemonic="";
        String operands="";
        String components[] = instruction.split(" ");
        if(components.length==3)
        {
            label = components[0];
            mnemonic = components[1];
            operands = components[2];
            this.addToSymbolTable(label, instructionAddress);
        }
        else
        {
            mnemonic = components[0];
            operands = components[1];
        }
        
        /*-------------------------Branch Instructions----------------------------*/
        if(mnemonic.startsWith("b")||mnemonic.startsWith("B"))
        {
            int offset=instructionAddress+4-this.getAddress(operands);
            this.addOffset(4,24,offset,encoding);
            if(mnemonic.length()>1)
            {
                if(mnemonic.charAt(1)=='l'||mnemonic.charAt(1)=='L')
                {
                    encoding[24]=1;
                    if(mnemonic.length()==4)
                    {
                        this.setConditionCode(mnemonic.charAt(2)+""+mnemonic.charAt(3), encoding);
                    }
                }
                else if(mnemonic.length()==3)
                {
                    this.setConditionCode(mnemonic.charAt(1)+""+mnemonic.charAt(2), encoding);
                }
            }
            else
            {
                for(int i=31;i>24;i--)//Setting it to never
                    encoding[i]=1;
                encoding[26]=0;
                encoding[24]=0;
            }
        }
        
        /*----------------------------SWI-------------------------------*/
        else if(mnemonic.substring(0,3).equalsIgnoreCase("swi"))
        {
            if(mnemonic.length()>3)
            {
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            }
            else
            {
                for(int i=31;i>24;i--)//Setting it to never
                    encoding[i]=1;
            }
            for(int i=27;i>23;i--)//Setting it to never
                encoding[i]=1;
            this.addOffset(4,24,this.getAbsoluteValue(operands),encoding);
        }
        
  /*--------------------Multiply Instructions------------------------------*/
        else if(mnemonic.substring(0,3).equalsIgnoreCase("mul"))
        {
            if(mnemonic.length()==4)
                encoding[20]=1;
            if(mnemonic.length()==5)
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            else if(mnemonic.length()==6)
            {
                encoding[20]=1;
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            }
            else
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            for(int i=23;i>20;i--)
                encoding[i]=0;
            encoding[4]=1;
            encoding[7]=1;
        }
  
        else if(mnemonic.substring(0,3).equalsIgnoreCase("mla"))
        {
            if(mnemonic.length()==4)
                 encoding[20]=1;
            if(mnemonic.length()==5)
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            else if(mnemonic.length()==6)
            {
                encoding[20]=1;
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            }
            else
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            encoding[21]=1;
            encoding[22]=0;
            encoding[23]=0;
            encoding[4]=1;
            encoding[7]=1;
        }
        else if(mnemonic.substring(0,5).equalsIgnoreCase("umull"))
        {
            if(mnemonic.length()==6)
                encoding[20]=1;
            if(mnemonic.length()==7)
                this.setConditionCode(mnemonic.charAt(5)+""+mnemonic.charAt(6), encoding);
            else if(mnemonic.length()==8)
            {
                encoding[20]=1;
                this.setConditionCode(mnemonic.charAt(5)+""+mnemonic.charAt(6), encoding);
            }
            else
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            encoding[21]=0;
            encoding[22]=0;
            encoding[23]=1;
            encoding[4]=1;
            encoding[7]=1;
        }
        else if(mnemonic.substring(0,5).equalsIgnoreCase("umlal"))
        {
            if(mnemonic.length()==6)
                 encoding[20]=1;
            if(mnemonic.length()==7)
                this.setConditionCode(mnemonic.charAt(5)+""+mnemonic.charAt(6), encoding);
            else if(mnemonic.length()==8)
            {
                encoding[20]=1;
                this.setConditionCode(mnemonic.charAt(5)+""+mnemonic.charAt(6), encoding);
            }
            else
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
                
            encoding[21]=1;
            encoding[22]=0;
            encoding[23]=1;
            encoding[4]=1;
            encoding[7]=1;
        }
        else if(mnemonic.substring(0,5).equalsIgnoreCase("smull"))
        {
            if(mnemonic.length()==6)
                 encoding[20]=1;
            if(mnemonic.length()==7)
                this.setConditionCode(mnemonic.charAt(5)+""+mnemonic.charAt(6), encoding);
            else if(mnemonic.length()==8)
            {
                encoding[20]=1;
                this.setConditionCode(mnemonic.charAt(5)+""+mnemonic.charAt(6), encoding);
            }
            else
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            encoding[21]=0;
            encoding[22]=1;
            encoding[23]=1;
            encoding[4]=1;
            encoding[7]=1;
        }
        else if(mnemonic.substring(0,5).equalsIgnoreCase("umull"))
        {
            if(mnemonic.length()==6)
                 encoding[20]=1;
            if(mnemonic.length()==7)
                this.setConditionCode(mnemonic.charAt(5)+""+mnemonic.charAt(6), encoding);
            else if(mnemonic.length()==8)
            {
                encoding[20]=1;
                this.setConditionCode(mnemonic.charAt(5)+""+mnemonic.charAt(6), encoding);
            }
            else
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            for(int i=23;i>20;i--)
                encoding[i]=1;
            encoding[4]=1;
            encoding[7]=1;
        }
        
        /*---------------------------------CLZ---------------------------------------------*/
        else if(mnemonic.substring(0,3).equalsIgnoreCase("clz"))
        {
            if(mnemonic.length()>3)
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            else
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            encoding[4]=1;
            encoding[21]=1;
            encoding[22]=1;
            encoding[24]=1;     
        }
        
        /*---------------------------LDR/STR for Half Word and Signed Byte--------------*/
        else if((mnemonic.substring(0,3).equalsIgnoreCase("ldr")||mnemonic.substring(0,3).equalsIgnoreCase("str"))&& (mnemonic.toLowerCase().endsWith("sh")||mnemonic.toLowerCase().endsWith("sh")||(mnemonic.length()==4 && mnemonic.toLowerCase().endsWith("h"))||(mnemonic.length()==6 && (""+mnemonic.charAt(6)).equalsIgnoreCase("h"))))        
        {
            encoding[4]=1;
            encoding[7]=1;
            int b=1;
            if(mnemonic.substring(0,3).equalsIgnoreCase("ldr"))
                encoding[20]=1;
            if(operands.contains("!"))
                encoding[21]=1;
            String ops[]=operands.split(",");
            if(ops[1].contains("[") && !(ops[1].contains("]")))
                encoding[24]=1;
            if(ops[2].toLowerCase().contains("r"))
                encoding[22]=0;
            else
                encoding[22]=1;
            if(mnemonic.toLowerCase().endsWith("sh"))
            {
                encoding[6]=1;
                encoding[5]=1;
                b=2;
                if(mnemonic.length()==7)
                   this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
                else
                    for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            }
            else if(mnemonic.toLowerCase().endsWith("sb"))
            {
                encoding[6]=1;
                b=1;
                if(mnemonic.length()==7)
                   this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
                else
                    for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            }
            else if((mnemonic.length()==4 && mnemonic.toLowerCase().endsWith("h")))
            {
                encoding[5]=1;
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
                b=2;
            }
            else if((mnemonic.length()==6 && (""+mnemonic.charAt(6)).equalsIgnoreCase("h")))
            {
                encoding[5]=1;
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
                b=2;
            }
            
            if(encoding[22]==1)
            {
                 if(ops[2].contains("!"))
                        this.addOffset(b, 8, this.getAbsoluteValue(ops[2].substring(0,ops[2].length()-2)), encoding);
                    else if(ops[2].contains("]"))
                        this.addOffset(b,8, this.getAbsoluteValue(ops[2].substring(0,ops[2].length()-1)), encoding);
                    else
                        this.addOffset(b,8, this.getAbsoluteValue(ops[2]), encoding);
            }
        }
        /*----------------------------LDR/STR for Word and Unsigned Byte-------------------------------------*/
        else if(mnemonic.substring(0,3).equalsIgnoreCase("ldr")||mnemonic.substring(0,3).equalsIgnoreCase("str"))
        {
            encoding[26]=1;
            if(mnemonic.substring(0,3).equalsIgnoreCase("ldr"))
                encoding[20]=1;
            if(mnemonic.length()==4)
                encoding[22]=1;
            if(mnemonic.length()==5)
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            else if(mnemonic.length()==6)
            {
                encoding[22]=1;
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding); 
            }
            else
                for(int i=31;i>27;i--)//Setting it to never
                    encoding[i]=1;
            if(operands.contains("!"))
                encoding[21]=1;
            String ops[]=operands.split(",");
            if(ops[1].contains("[") && !(ops[1].contains("]")))
                encoding[24]=1;
            if(ops.length==3)
            {
                encoding[25]=0;
                if(encoding[22]==1)
                {
                    if(ops[2].contains("!"))
                        this.addOffset(1, 12, this.getAbsoluteValue(ops[2].substring(0,ops[2].length()-2)), encoding);
                    else if(ops[2].contains("]"))
                        this.addOffset(1,12, this.getAbsoluteValue(ops[2].substring(0,ops[2].length()-1)), encoding);
                    else
                        this.addOffset(1,12, this.getAbsoluteValue(ops[2]), encoding);
                }
                else
                {
                    if(ops[2].contains("!"))
                        this.addOffset(4, 12, this.getAbsoluteValue(ops[2].substring(0,ops[2].length()-2)), encoding);
                    else if(ops[2].contains("]"))
                        this.addOffset(4,12, this.getAbsoluteValue(ops[2].substring(0,ops[2].length()-1)), encoding);
                    else
                        this.addOffset(4, 12, this.getAbsoluteValue(ops[2]), encoding);
                }
            }
            else if(ops.length==4)
            {
                encoding[25]=1;
                if(ops[2].toLowerCase().contains("lsr"))
                    encoding[5]=1;
                else if(ops[2].toLowerCase().contains("asl"))
                    encoding[6]=1;
                else if(ops[2].toLowerCase().contains("asr"))
                {
                    encoding[5]=1;
                    encoding[6]=1;
                }
                String val[] = ops[2].split(" ");
                if(encoding[22]==1)
                {
                    if(val[1].contains("!"))
                        this.addOffset(1, 5, this.getAbsoluteValue(val[1].substring(0,val[1].length()-2)), encoding);
                    else if(val[2].contains("]"))
                        this.addOffset(1,5, this.getAbsoluteValue(val[1].substring(0,ops[2].length()-1)), encoding);
                    else
                        this.addOffset(1, 5, this.getAbsoluteValue(val[2]), encoding);
                }
                else
                {
                    if(val[1].contains("!"))
                        this.addOffset(4, 5, this.getAbsoluteValue(val[1].substring(0,val[1].length()-2)), encoding);
                    else if(val[2].contains("]"))
                        this.addOffset(4,5, this.getAbsoluteValue(val[1].substring(0,ops[2].length()-1)), encoding);
                    else
                        this.addOffset(4, 5, this.getAbsoluteValue(val[2]), encoding);
                }
            } 
        }
        return encoding;
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
    
    private int getAddress(String operand)
    {
        for(SymbolAddress element:symTab)
        {
            if(element.label.equalsIgnoreCase(operand))
                return element.address;
        }
        System.out.println("!---------- ERROR: Label "+operand+" invalid ----------!");
        System.exit(0);
        return 0;
    }
    public void addOffset(int bhw,int p,int val,int[] encoding)
    {
        p=p-1;
        String o=Integer.toBinaryString(val);
        if(val>(Math.pow(2, p)*bhw-1) || val<-(Math.pow(2, p)*bhw))
        {
            System.out.println("!---------- ERROR: Offset out of range ----------!");
            System.exit(0);
        }
        for(int i=o.length()-1;i>=0;i--)
        {
            if(o.charAt(i)=='1')
                encoding[i]=1;
        }
    }
    
    public void setConditionCode(String code,int[] encoding)
    {
        //put if conditions to set condition code bits
    }
    
    public int getAbsoluteValue(String val)
    {
        //method to get athe absolute value from a string example from 0x02(hex) or 23(int)
        return 0;
    }
}

