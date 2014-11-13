/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

/**
 *
 * @author arvind
 */
public class SymbolAddress 
{
    String label;
    int address;
    public SymbolAddress(String l,int a)
    {
        this.label=l;
        this.address=a;
    }            
}
