package heapfile;

public class test {

    private String[] name;//key
    private int sum;//container
    public static void main(String[] args) {
        
        test t=new test();
        t.add("java");
        t.add("C#");
        t.add("C++");
        t.add("C");
        t.add("pathyn");
        t.add("VB");
        t.add("PHP");
        t.add("shell");
        t.print();
        String teststr="C++";
        if(t.contains(teststr)){
            System.out.println("YES");
        }else{
            System.out.println("NO");
        }
    }
    public test(){
        name=new String[10];
        sum=0;
    }
    //add strings to hash table
    public void add(String s){
        if(sum>=this.name.length/2){
            this.refresh();
        }
        int start=hash1(s);
        int i=start;
        while(name[i]!=null){
            if(name[i].equals(s)){
                return ;
            }
            i=(i+hash2(s))%name.length;
            if(i==start){
                return ;
            }
        }
        name[i]=s;
        sum++;

    }
    //expand the hash table
    public void refresh(){
        test t=new test();
        t.name=new String[this.name.length*2];
        int i=0;
        for(i=0;i<name.length;i++){
            if(this.name[i]!=null){
                t.add(this.name[i]);
            }
        }
        this.name=t.name;
        this.sum=t.sum;
    }
    //hash function
    public int hash1(String s){
        return Math.abs(s.hashCode()%name.length);
    }
    //hash function to deal with clash code
    public int hash2(String s){
        int result=Math.abs(s.hashCode()%(name.length-1));
        if(result%2==0){
            return result+1;
        }
        return result;
    }
    public boolean contains(String s){
        int start=hash1(s);
        int i=start;
        while(name[i]!=null){
            if(name[i].equals(s)){
                return true;
            }
            i=(i+hash2(s)%name.length);
            if(i==start){
                return false;
            }
        }
        return false;
    }
    
    //print all the hash 
    public void print(){
        for(int i = 0; i < name.length; i ++){
            System.out.println(i+":"+name[i]);
        }
    }
}
