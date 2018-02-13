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
    byte [] myTarget;  //検索ターゲット配列
    byte [] mySpace;   //検索スペース配列
    
    //ターゲットとスペースが正しい形かセット時に判定する変数
    boolean targetReady = false;
    boolean spaceReady = false;
    
    int [] suffixArray;  //接尾辞配列
    
    //myTargetをセットする
    public void setTarget(byte [] target) {
        myTarget = target;
        
        //targetに長さがあるときのみ、freaquencyでの処理を許可する
        if(myTarget.length > 0){
            targetReady = true;
        }
        
    }
    
    //mySpaceをセットする
    public void setSpace(byte []space) {
        mySpace = space;
        
        //spaceに長さがあるときのみ、frequencyでの処理を許可する
        if(mySpace.length > 0 ){
            spaceReady = true;
        }
        suffixArray = new int[space.length];
        for(int i = 0 ; i < space.length ; i++){//初期化
            suffixArray[i] = i;
        }
        
        //接尾辞配列をクイックソート
        QuickSort(suffixArray, 0, mySpace.length - 1);
        
        //確認用
        printSuffixArray();
        
    }
    
    //クイックソート
    public void QuickSort(int[] suffixArray, int low, int high) {
    
        int pivot;  //ピボット
        
        if(low < high) {
        
            //配列を二分し、仕切りの値を受け取る
            pivot = partition(suffixArray, low, high);
            
            //仕切りから左側と右側をそれぞれ範囲指定して再帰
            QuickSort(suffixArray, low, pivot - 1);
            QuickSort(suffixArray, pivot + 1, high);
        
        }
    }
    
    //配列を二分する関数、クイックソートで使用
    int partition(int[] suffixArray, int low, int high) {
    
        int wall = low; //仕切り
        int tmp;  //一時変数
        
        //左端の値より小さい値を左に、大きい値を右にずらし、仕切りを記憶する
        for(int i = low + 1; i <= high; i++) {
            if(suffixCompare(i, low) <= 0) {
                wall = wall + 1;
                tmp = suffixArray[i];
                suffixArray[i] = suffixArray[wall];
                suffixArray[wall] = tmp;
            }
        }
        
        //仕切りに指定されている左端より小さい値を、左端と交換する
        tmp = suffixArray[low];
        suffixArray[low] = suffixArray[wall];
        suffixArray[wall] = tmp;
        
        //仕切りの値を返す
        return wall;
    }
    
    public int frequency() {
        
        //ターゲットとスペースが正しく設定されているか判定
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }

    // I know that here is a potential problem in the declaration.
    public int subByteFrequency(int start, int end) {//length -> end
	// Not yet, but it is not currently used by anyone.
	//return -1;
        
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
        
        //確認用
        System.out.printf("first = %d last1 = %d\n", first, last1);
        
        return last1 - first;
    }

    public static void main(String[] args) {
        Frequencer myObject;
        int freq;
        try {
            System.out.println("checking my Frequencer");
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("Hi Ho ".getBytes());
            freq = myObject.frequency();
            System.out.print("\"Hi Ho \" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
            if(1 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("Exception occurred: STOP");
        }
    }
    
    //接尾辞配列における大小比較
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
    
    //接尾辞配列が正しく作成されたか確認する表示関数
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
    
    //ターゲットを比較する関数
    private int targetCompare(int i, int start, int end) {
        
        int startIndex = subByteStartIndex(start, end);
        
        int endIndex = subByteEndIndex(start, end);
        
        if(startIndex <= i && i < endIndex) return 0;
        if(i < startIndex) return -1;
        if(endIndex <= i) return 1;
    
        return 2;
    }
    
    //指定された範囲のターゲットと一致する内容が、接尾辞配列のどこから格納されているか返す
    private int subByteStartIndex(int start, int end) {
        
        
        int bottom = 0;  //二分探索における左端
        int top = mySpace.length - 1;  //二分探索における右端
        int midiam;  //二分探索における真ん中
        int suffixmidiam;  //midiamの値における接尾辞配列の値
        boolean continue_flag;  //コンティニューフラグ
        
        byte mySpaceMidiam;  //midiamの値における検索スペースの先頭の値
        byte myTargetStart;  //指定された範囲における検索ターゲットの先頭の値
        
        int targetLength = end - start;  //指定された検索ターゲットの長さ
        int spaceLength = mySpace.length;  //検索スペースの長さ
        
        while( bottom <= top ) {
        
            midiam = (bottom + top) / 2;
            suffixmidiam = suffixArray[midiam];
            mySpaceMidiam = mySpace[suffixmidiam];
            myTargetStart = myTarget[start];
            
            if( mySpaceMidiam == myTargetStart) {

                continue_flag = false;
                
                //検索ターゲットと検索スペースの頭出しが一致するか確認
                //一致しない地点での大小比較を行い、コンティニューフラグを立てる
                for(int i = 1; i < targetLength && i < (spaceLength - suffixmidiam); i++) {
                    if(mySpace[suffixmidiam + i] > myTarget[start + i]) {
                        top = midiam - 1;
                        continue_flag = true;
                        break;
                    } else if(mySpace[suffixmidiam + i] < myTarget[start + i]){
                        bottom = midiam + 1;
                        continue_flag = true;
                        break;
                    }
                }
                
                //フラグが立っていればループの先頭に返る
                if(continue_flag) continue;
            
                //もしmidiamの値における検索スペースの長さが指定された範囲の検索ターゲットより短ければ、
                //左側を棄却してループの先頭に返る
                if(suffixmidiam + targetLength > spaceLength) {
                    bottom = midiam + 1;
                    continue;
                }

                //このとき、midiamの値が検索スペースの端ならば、midiamを返す
                if(midiam <= 0 || midiam >= (mySpace.length - 1)) return midiam;
                
                //midiamの地点が開始点かどうか調べる
                //midiam-1のときの検索スペースと検索ターゲットを比較し、異なるならmidiamを返す
                for(int i = 0; i < targetLength && i < (spaceLength - suffixArray[midiam-1]); i++) {
                    if( mySpace[suffixArray[midiam - 1] + i] != myTarget[start + i])
                        return midiam;
                }
                
                //midiam-1のときの検索スペースが検索ターゲットの長さより短ければ、
                //midiamの値を返す
                if(suffixArray[midiam - 1] + targetLength > spaceLength) return midiam;
                
                //ここまでの条件に当てはまらなければ、左側を棄却してループの先頭へ返る
                top = midiam - 1;

            } else if( mySpaceMidiam > myTargetStart) {
                top = midiam - 1;
            } else if( mySpaceMidiam < myTargetStart){
                bottom = midiam + 1;
            }
        
        }
        
        return suffixArray.length;
    }
    
    //指定された範囲のターゲットと一致する内容が、接尾辞配列のどこまで格納されているか返す
    private int subByteEndIndex(int start, int end) {
        
        int bottom = 0;
        int top = mySpace.length - 1;
        int midiam = 0;
        int suffixmidiam;
        boolean continue_flag;
        
        byte mySpaceMidiam;
        byte myTargetStart;
        
        int targetLength = end - start;
        int spaceLength = mySpace.length;
        
        int count = 0;
        
        while( bottom <= top ) {
            
            midiam = (bottom + top) / 2;
            suffixmidiam = suffixArray[midiam];
            mySpaceMidiam = mySpace[suffixmidiam];
            myTargetStart = myTarget[start];
            
            if( mySpaceMidiam == myTargetStart) {
                continue_flag = false;
                
                
                for(int i = 1; i < targetLength && i < (spaceLength - suffixmidiam); i++) {
                    if(mySpace[suffixmidiam + i] > myTarget[start + i]) {
                        top = midiam - 1;
                        continue_flag = true;
                        break;
                    } else if(mySpace[suffixmidiam + i] < myTarget[start + i]){
                        bottom = midiam + 1;
                        continue_flag = true;
                        break;
                    }
                }
                
                if(continue_flag) continue;
                
                if(suffixmidiam + targetLength > spaceLength) {
                    bottom = midiam + 1;
                    continue;
                }
                
                
                if(midiam <= 0 || midiam >= (spaceLength - 1)) return midiam + 1;
                
                
                for(int i = 0; i < targetLength && i < (spaceLength - suffixArray[midiam + 1]); i++) {
                    if( mySpace[suffixArray[midiam + 1] + i] != myTarget[start + i])
                        return midiam + 1;
                }
                
                if(suffixArray[midiam + 1] + targetLength > spaceLength) return midiam + 1;
                
                bottom = midiam + 1;
                
            } else if( mySpaceMidiam > myTargetStart) {
                top = midiam - 1;
            } else if( mySpaceMidiam < myTargetStart){
                bottom = midiam + 1;
            }
            
        }
        return suffixArray.length;
        
    }
    
}	    
	    
