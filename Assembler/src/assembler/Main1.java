/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

/**
 *
 * @author arvind
 */
public class Main1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        FirstPhase obj = new FirstPhase();
        obj.firstPass();
        int[] arr=new EncodeInstruction().encode(obj.instructions.get(0), obj.addr[0]);
        for(int i=arr.length-1;i>=0;i--)
        {
            System.out.print(arr[i]);
            if(i%4==0)
                System.out.println("");
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
