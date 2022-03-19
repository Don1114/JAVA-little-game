//判定玩家是否被打到的class
public class explode {
    int i = 0;
    public explode(){}
    public boolean touch(int x,int y,int Mx,int My,int bx,int by,int lx,int lw, int cd){
        if(Mx <= x+20 && Mx > x-10 && My >= y-80 && My < y) {
            i=0;
            return true;
        }
        else if(lx <= x+20 && lx+40 >= x && lw == 1) {
            i=0;
            return true;
        }
        else if(bx+60 >= Mx && by-50 <= My && by-40 > My && cd != 0) {
            i=1;
            return true;
        }
        i=0;
        return false;
    }
    public boolean getT(){
        if(i == 1) return true;
        return false;
    }
}
