//Стековый компилятор формул.
public class Compf extends Stack{
    //Типы символов (скобки, знаки операций, иное).
    protected final static int SYM_LEFT = 0,
            SYM_RIGHT = 1,
            SYM_SQUARE_LEFT = 2, // {
            SYM_SQUARE_RIGHT = 3, // }
            SYM_OPER = 4,
            SYM_OTHER = 5;
    boolean startwOther;


    private int symType(char c){
        switch(c){
            case '(':
                return SYM_LEFT;
            case ')':
                return SYM_RIGHT;
            case '{':
                return SYM_SQUARE_LEFT;
            case '}':
                return SYM_SQUARE_RIGHT;
            case '+':
            case '-':
            case '*':
            case '/':
                return SYM_OPER;
            default:
                return symOther(c);
        }
    }

    private void processSymbol(char c){
        switch(symType(c)){
            case SYM_LEFT:
            case SYM_SQUARE_LEFT:
                push(c);
                startwOther = false;
                break;
            case SYM_RIGHT:
                processSuspendedSymbols(c);
                pop();
                startwOther = false;
                break;
            case SYM_SQUARE_RIGHT:
                processSuspendedSymbolsSquare(c);
                pop();
                startwOther = false;
                break;
            case SYM_OPER:
                processSuspendedSymbols(c);
                push(c);
                startwOther = false;
                break;
            case SYM_OTHER:
                nextOther(c);
                break;
        }
    }

    // Squaring (new function Kharitonov)
    private void processSuspendedSymbolsSquare(char c){
        while(precedesSquare(top(), c))
            nextOper(pop());
        if(!precedesSquare(top(), c))
            squaring();
    }
    protected void squaring(){}

    private boolean precedesSquare(char a, char b){
        if(symType(a) == SYM_SQUARE_LEFT||symType(a) == SYM_LEFT) return false;
        if(symType(b) == SYM_SQUARE_RIGHT) return true;

        return priority(a) >= priority(b);
    }

    private void processSuspendedSymbols(char c){
        while(precedes(top(), c))
            nextOper(pop());
    }


    private int priority(char c){
        return c == '+' || c == '-' ? 1 : 2;
    }

    private boolean precedes(char a, char b){
        if((symType(a) == SYM_LEFT)||(symType(a) == SYM_SQUARE_LEFT)) return false;
        if(symType(b) == SYM_RIGHT) return true;

        return priority(a) >= priority(b);
    }

    protected int symOther(char c){
        if (c < 'a' || c > 'z'){
            System.out.println("Недопустимый символ: " + c);
            System.exit(0);
        }

        return SYM_OTHER;
    }

    protected void nextOper(char c){
        System.out.print("" + c + " ");
    }

    protected void nextOther(char c){
        nextOper(c);
    }

    public void compile(char[] str){
        processSymbol('(');

        for(int i = 0; i < str.length; i++)
            processSymbol(str[i]);

        processSymbol(')');

        System.out.print("\n");
    }
}