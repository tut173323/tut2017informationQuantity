package s4.b173323; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
*/


public class Frequencer implements FrequencerInterface{
    // Code to Test, *warning: This code  contains intentional problem*
    byte [] myTarget;
    byte [] mySpace;
    //ついか
    boolean targetReady = false;
    boolean spaceReady = false;
    
    int [] suffixArray;
    //ここまで
    public void setTarget(byte [] target) {
        myTarget = target;
        //ここから
        if(myTarget.length > 0){
            targetReady = true;
        }
        //ここまで
    }
    public void setSpace(byte []space) {
        mySpace = space;
        //追加
        if(mySpace.length > 0 ){
            spaceReady = true;
        }
        suffixArray = new int[space.length];
        for(int i = 0 ; i < space.length ; i++){//初期化
            suffixArray[i] = i;
        }
        //そーとここから
        for(int i = 0; i < (suffixArray.length-1) ;i++ ){
            for(int j = (suffixArray.length-1) ; j>i ;j--){
                if( suffixCompare(j-1,j) == 1 ){
                    int temp = suffixArray[j-1];
                    suffixArray[j-1] = suffixArray[j];
                    suffixArray[j] = temp;
                }
            }
        }
        //そーとここまで
        printSuffixArray();
        //ここまで
    }
    
    public int frequency() {
        
        /*追加開始 -target, spaceの長さに関して安全に宣言できるか、try-catchで確認
                   また、target, spaceの値が0なら、仕様通りの値を返す*/
        /*
        try {
            int targetLength = myTarget.length;
            if(targetLength == 0) {throw new Exception();}
        } catch (Exception e){
            System.out.println("Exception occured: Target is not set or Target Length is zero");
            return -1;
        }
        try {
            int spaceLength = mySpace.length;
            if(spaceLength == 0) {throw new Exception();}
        } catch (Exception e) {
            System.out.println("Exception occured: Space is not set or Space Length is zero");
            return 0;
        }*/
        //追加終わり
        /*
        int targetLength = myTarget.length;
        int spaceLength = mySpace.length;
        
        
        int count = 0;
        for(int start = 0; start<spaceLength; start++) { // Is it OK?
            boolean abort = false;
            for(int i = 0; i<targetLength; i++) {
                //修正開始　-if文追加、元if文をelse ifに変更してstart+iでspaceLength超過を予防
            if(start+i >= spaceLength - 1) { abort = true; break; }
            else if(myTarget[i] != mySpace[start+i]) { abort = true; break; }
            //修正終わり
            }
            if(abort == false) { count++; }
        }
        return count;*/
        
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }

    // I know that here is a potential problem in the declaration.
    public int subByteFrequency(int start, int end) {//length -> end
	// Not yet, but it is not currently used by anyone.
	//return -1;
        //ここから
        int spaceLength = mySpace.length;
        int count = 0;
        
        for(int offset = 0; offset < spaceLength - (end - start); offset++) {
            boolean abort = false;
            for(int i = 0; i < (end - start); i++) {
                if(myTarget[start+i] != mySpace[offset+i]) {
                    abort = true;
                    break;
                }
            }
            if(abort == false) {
                count++;
            }
        }
        
        int first = subByteStartIndex(start, end);
        
        int last1 = subByteEndIndex(start, end);
        
        for(int k = start; k < end; k++) {
            System.out.write(myTarget[k]);
        }
        System.out.printf(": first = %d last1 = %d\n", first, last1);
        
        return last1 - first;
    }

    public static void main(String[] args) {
	Frequencer myObject;
	int freq;
	try {
	    System.out.println("checking my Frequencer");
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    System.out.print("\"H\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
	    if(4 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
	}
	catch(Exception e) {
	    System.out.println("Exception occurred: STOP");
	}
    }
    //追加
    private int suffixCompare(int i,int j){
        int si = suffixArray[i];
        int sj = suffixArray[j];
        int s = 0;
        
        if(si > s) s = si;
        if(sj > s) s = sj;
        
        int n = mySpace.length - s;
        
        for(int k = 0; k < n; k++) {
            if(mySpace[si + k] > mySpace[sj + k]) return 1;
            if(mySpace[si + k] < mySpace[sj + k]) return -1;
        }
        
        if(si < sj) return 1;
        if(si > sj) return -1;
        
        return 0;
    }
    //ここまで
    //ここから
    private void printSuffixArray(){
        int s = 0;
        if(spaceReady){
            for(int i=0;i<mySpace.length;i++){
                s = suffixArray[i];
                System.out.print(Integer.toHexString(i)+":");
                for(int j=s;j<mySpace.length;j++){
                    System.out.write(mySpace[j]);
                }
            System.out.write('\n');
            }
        }
    }
    //ここまで
    //ここから
    private int targetCompare(int i, int start, int end) {
        
        int startIndex = subByteStartIndex(start, end);
        
        int endIndex = subByteEndIndex(start, end);
        
        if(startIndex <= i && i < endIndex) return 0;
        if(i < startIndex) return -1;
        if(endIndex <= i) return 1;
    
        return 2;
    }
    //ここまで
    //ここから
    private int subByteStartIndex(int start, int end) {
        
        //ソート済みのsuffixのインデックスを検索して返す
        for(int i = 0; i < mySpace.length; i++) {
            int s = suffixArray[i];
            for(int j = s; j < myTarget.length + s; j++) {
                if(mySpace[j] == myTarget[start+j-s]) {
                    if((j - s) == end - 1) {
                        return i;
                    }
                }
                else {
                    break;
                }
            }
        }
        
        return suffixArray.length;
    }
    
    private int subByteEndIndex(int start, int end) {
        
        //ソート済みのsuffixの次のインデックスを検索して返す
        
        int flag1 = 0;
        int flag2 = 0;
        
        
        for(int i = 0; i < mySpace.length; i++) {
            int s = suffixArray[i];
            
            for(int j = s; j < myTarget.length + s; j++) {
            
                flag2 = flag1;
                if(mySpace[j] == myTarget[start+j-s]) {
                    if((j - s) == end - 1) {
                        flag1 = 1;
                        
                    }
                }
                else {
                    flag1 = 0;
                    break;
                }
            }
            if(flag1 - flag2 == -1) return i;
        }
        
        return suffixArray.length;
    }
    //ここまで
    
}	    
	    
