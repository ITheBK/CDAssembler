/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

/**
 *
 * @author arvind
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("hello".substring(0,3));
        String regNum = Integer.toBinaryString(Integer.parseInt("r12".split("r")[1]))+"";
        System.out.println(regNum.charAt(2));
        int[] encode = {0,0,0,0,0,0,0,0,0,0};
        int pos=5;
        int j=0;
        for(int i=pos;i<pos+4;i++)
        {
            //System.out.println(regNum.charAt(j));
            encode[i]=Integer.parseInt(regNum.charAt(j)+"");
            j++;
        }
        for(int i=0;i<encode.length;i++)
            System.out.print(encode[i]+" ");
        String rl="{r0-r2,pc}";
        System.out.println(rl.substring(1,rl.length()-1));
    }
}
