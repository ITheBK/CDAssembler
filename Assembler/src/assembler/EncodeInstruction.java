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
        int dpiNum = -1;
        
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
        
        /*----------------------------Data Processing-------------------------------*/
        
        else if((dpiNum=this.isDPI(mnemonic))!=-1)
        {
            this.encodeDPI(mnemonic, instructionAddress, encoding);
            encoding[26]=encoding[27]=0;
            String ops[] = operands.split(",");
            if(ops.length == 3 && ops[2].trim().startsWith("#"))//2 registers with 32 bit immediate
            {
                String l[] = ops[2].split("\\W+");
                this.getImmediateWithRotate(Integer.parseInt(l[1]));
                this.encode32BitImmediate(ops[2], encoding);
            }
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
        /*---------------------------------MULTIPLE REGISTER TRANSFER INSTRUCTIONS------------------------------------------*/
        else if((mnemonic.substring(0,3).equalsIgnoreCase("ldm"))||(mnemonic.substring(0,3).equalsIgnoreCase("stm")))
        {
            if(mnemonic.length()==7)
                this.setConditionCode(mnemonic.substring(3,5),encoding);
            if((mnemonic.substring(0,3)).equalsIgnoreCase("ldm"))
            {
                encoding[20]=1;//load bit
                if((mnemonic.substring(3,5).equalsIgnoreCase("ib"))||(mnemonic.substring(3,5).equalsIgnoreCase("ed")))
                {
                    encoding[23]=1;
                    encoding[24]=1;
                }
                if((mnemonic.substring(3,5).equalsIgnoreCase("ia"))||(mnemonic.substring(3,5).equalsIgnoreCase("fd")))
                {
                    encoding[23]=1;
                    encoding[24]=0;
                }
                if((mnemonic.substring(3,5).equalsIgnoreCase("db"))||(mnemonic.substring(3,5).equalsIgnoreCase("ea")))
                {
                    encoding[23]=0;
                    encoding[24]=1;
                }
                if((mnemonic.substring(3,5).equalsIgnoreCase("da"))||(mnemonic.substring(3,5).equalsIgnoreCase("fa")))
                {
                    encoding[23]=0;
                    encoding[24]=0;
                }
            }
            if((mnemonic.substring(0,3)).equalsIgnoreCase("stm"))
            {
                encoding[20]=0;//bit for store
                if((mnemonic.substring(3,5).equalsIgnoreCase("ib"))||(mnemonic.substring(3,5).equalsIgnoreCase("fa")))
                {
                    encoding[23]=1;
                    encoding[24]=1;
                }
                if((mnemonic.substring(3,5).equalsIgnoreCase("ia"))||(mnemonic.substring(3,5).equalsIgnoreCase("ea")))
                {
                    encoding[23]=1;
                    encoding[24]=0;
                }
                if((mnemonic.substring(3,5).equalsIgnoreCase("db"))||(mnemonic.substring(3,5).equalsIgnoreCase("fd")))
                {
                    encoding[23]=0;
                    encoding[24]=1;
                }
                if((mnemonic.substring(3,5).equalsIgnoreCase("da"))||(mnemonic.substring(3,5).equalsIgnoreCase("ed")))
                {
                    encoding[23]=0;
                    encoding[24]=0;
                }
            }
            String ops[]=operands.split(",");
            if(ops[0].contains("!"))
                encoding[21]=1;
            this.encodeRegisterBinary(ops[0].substring(0,3), 16, encoding);
            this.encodeRegisterListBinary(ops[1].substring(1,ops[1].length()-1),encoding);
            encoding[27]=1;
        }
        /*---------------------------------STATUS REGISTER TO GENERAL REGISTER TRANSFER INSTRUCTIONS------------------------*/
        else if(mnemonic.substring(0,3).equalsIgnoreCase("mrs"))
        {
            if(mnemonic.length()>3)
            {
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            }
            String ops[] = operands.split(",");
            this.encodeRegisterBinary(ops[0],12 , encoding);
            if(ops[1].equalsIgnoreCase("cpsr"))
                encoding[22]=0;
            if(ops[1].equalsIgnoreCase("spsr"))
                encoding[22]=1;
            encoding[16]=1;
            encoding[17]=1;
            encoding[18]=1;
            encoding[19]=1;
            encoding[24]=1;
        }
        /*--------------------------------GENERAL REGISTER TO STATUS REGISTER TRANSFER INSTRUCTIONS--------------------------*/
        else if(mnemonic.substring(0,3).equalsIgnoreCase("msr"))
        {
            if(mnemonic.length()>3)
            {
                this.setConditionCode(mnemonic.charAt(3)+""+mnemonic.charAt(4), encoding);
            }
            String ops[] = operands.split(",");
            if(ops[0].matches("cpsr_.*")||ops[0].matches("CPSR_.*"))
            {
                encoding[22]=0;
                String field[]=ops[0].split("_");
                if(field[1]=="c")
                    encoding[16]=1;
                if(field[1]=="f")
                    encoding[19]=1;
            }
            if(ops[0].matches("spsr_.*")||ops[0].matches("SPSR_.*"))
            {
                encoding[22]=1;
                String field[]=ops[0].split("_");
                if(field[1]=="c")
                    encoding[16]=1;
                if(field[1]=="f")
                    encoding[19]=1;
            }
            if(ops[1].startsWith("#"))
            {
                encoding[25]=1;   //set bit to 25 for immediate operand
                /*----CODE TO HANDLE IMMEDIATE OPERANDS-----*/    
            }
            if(ops[1].startsWith("r"))
            {
                 for(int i=4;i<12;i++)
                     encoding[i]=0;
                 this.encodeRegisterBinary(ops[1], 0, encoding);
            }
            encoding[24]=1;
            encoding[21]=1;
            for(int i=12;i<16;i++)
                     encoding[i]=1;
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
        //method to get the absolute value from a string example from 0x02(hex) or 23(int)
        return 0;
    }
    
    public void encodeRegisterBinary(String reg,int pos, int[] encode)
    {
        String regNum = Integer.toBinaryString(Integer.parseInt(reg.split("r")[1]))+"";
        int j=3;
        for(int i=pos;i<pos+4;i++)
        {
            encode[i]=Integer.parseInt(regNum.charAt(j)+"");
            j--;
        }
    }

    public void encodeRegisterListBinary(String list, int[] encoding) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        String regs[]=list.split(",");
        for(int i=0;i<regs.length;i++)
        {
            if(regs[i].contains("-"))
            {
                String reg1[]=regs[0].split("-");
                int start = Integer.parseInt(reg1[0].substring(1));
                int end = Integer.parseInt(reg1[1].substring(1));
                for(int j=start;j<end+1;j++)
                    encoding[j]=1;
            }
            else
                encoding[Integer.parseInt(regs[i].substring(1))]=1;
        }
        
        if(!regs[1].equalsIgnoreCase("pc"))
            encoding[Integer.parseInt(regs[1].substring(1))]=1;
        if(regs[1].equalsIgnoreCase("pc"))
            encoding[15]=1;
    }
    
    private int isDPI(String mnemonic)
    {
        if(mnemonic.length()!=3 && mnemonic.length()!=5 && mnemonic.length()!=6)
        {
            return -1;
        }
        String dpiMnemonics[] = {"and","eor","sub","rsb","add","adc","sbc","rsc","tst","teq","cmp","cmn","orr","mov","bic","mvn"};
        for(int i=0;i<dpiMnemonics.length;i++)
        {
            if(mnemonic.substring(0, 3).equalsIgnoreCase(dpiMnemonics[i]))
            {
                return i;
            }
            else
            {
                return -1;
            }
        }
        return -1;
    }
    
    private void encodeDPI(String mnemonic,int opCode,int[] encoding)
    {
        this.setConditionCode(mnemonic.substring(3, 5), encoding);
        this.encodePositions(opCode, 21, 24, encoding);
    }
    
    private void encodePositions(int number,int posS,int posE,int[] encoding)
    {
        String bin = Integer.toBinaryString(number);
        for(int i=posE-posS,j=posS;i>=posS;i--,j++)
        {
            encoding[posS] = Integer.parseInt(bin.charAt(i)+"");
        }
    }
    
    private void getImmediateWithRotate(int val)
    {
        //Method implementing 32 bit immediate values with 8 bit space and 4 bit rotate
        
    }
    
    private void encode32BitImmediate(String value, int[] encoding)
    {
        int val = this.getAbsoluteValue(value);
        /**
         * Have to do it for f000000f
         */
        if(Integer.highestOneBit(val)-Integer.lowestOneBit(val)>7)
        {
            System.out.println("!---------- ERROR: 32 bit value invalid ----------!");
            System.exit(0);
        }
        
        int leftmostOne = Integer.numberOfTrailingZeros(Integer.highestOneBit(val));
        int rightmostOne = Integer.numberOfTrailingZeros(Integer.lowestOneBit(val));
        int $8bitVal = Integer.parseInt((val+"").substring(Integer.lowestOneBit(val), Integer.highestOneBit(val)));
        int diff = leftmostOne == rightmostOne? leftmostOne : rightmostOne - leftmostOne;
        int shiftVal = 0;
        /**
         * 000000ff
         * 00000ff0
         * 0000ff00
         * 000ff000
         * 00ff0000
         * 0ff00000
         * ff000000
         */
        
        for(int i=0;i<7;i++)
        {
            if()
            {
                
            }
        }
    }
}

