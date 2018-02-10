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
        QuickSort(suffixArray, 0, mySpace.length - 1);
        
        /*
        for(int i = 0; i < (suffixArray.length-1) ;i++ ){
            for(int j = (suffixArray.length-1) ; j>i ;j--){
                if( suffixCompare(j-1,j) == 1 ){
                    int temp = suffixArray[j-1];
                    suffixArray[j-1] = suffixArray[j];
                    suffixArray[j] = temp;
                }
            }
        }*/
        //そーとここまで
        printSuffixArray();
        //ここまで
    }
    
    public void QuickSort(int[] suffixArray, int low, int high) {
    
        int pivot;
        
        if(low < high) {
        
            pivot = partition(suffixArray, low, high);
            
            QuickSort(suffixArray, low, pivot - 1);
            QuickSort(suffixArray, pivot + 1, high);
        
        }
    }
    
    int partition(int[] suffixArray, int low, int high) {
    
        int wall = low;
        int tmp;
        
        for(int i = low + 1; i <= high; i++) {
            if(suffixCompare(i, low) <= 0) {
                wall = wall + 1;
                tmp = suffixArray[i];
                suffixArray[i] = suffixArray[wall];
                suffixArray[wall] = tmp;
            }
        }
        
        tmp = suffixArray[low];
        suffixArray[low] = suffixArray[wall];
        suffixArray[wall] = tmp;
        
        
        return wall;
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
                //修正開始 -if文追加、元if文をelse ifに変更してstart+iでspaceLength超過を予防
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
            //System.out.write(myTarget[k]);
        }
        //System.out.printf(": first = %d last1 = %d\n", first, last1);
        
        return last1 - first;
    }

    public static void main(String[] args) {
        Frequencer myObject;
        int freq;
        try {
            System.out.println("checking my Frequencer");
            myObject = new Frequencer();
            myObject.setSpace("Hi Ho Hi Ho".getBytes());
            myObject.setTarget("Hi".getBytes());
            freq = myObject.frequency();
            System.out.print("\"H\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
            if(2 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
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
        
        /*
        int bottom = 0;
        int top = mySpace.length - 1;
        int midiam = 0;
        int right, left;
        int suffixmidiam = 0;
        int suffixRight, suffixLeft;
        boolean continue_flag;
        
        byte mySpaceMidiam;
        byte myTargetStart = 0;
        
        byte mySpaceLeft;
        byte mySpaceRight;
        
        int targetLength = end - start;
        int spaceLength = mySpace.length;
        
        int count = 0;
        
        //String targetstr = new String(myTarget, start, end - start, UTF-8);
        System.out.println("targetlength = " + targetLength);
        System.out.print("myTarget=");
        for(int i = start; i < end ; i++)
            System.out.write(myTarget[i]);
        System.out.println();
        
        while( bottom <= top ) {
            
            boolean flag1 = false;
            boolean flag2 = false;
            
            midiam = (bottom + top) / 2;
            right = (midiam + top) / 2;
            left = (bottom + midiam) / 2;
            if(right == midiam && right != spaceLength - 1) right++;
            if(left == midiam && left != 0) left--;
            
            suffixmidiam = suffixArray[midiam];
            if(count + suffixmidiam >= spaceLength)
                suffixmidiam = spaceLength - count - 1;
            
            suffixRight = suffixArray[right];
            if(suffixRight + count >= spaceLength)
                suffixRight = spaceLength - count - 1;
            
            suffixLeft = suffixArray[left];
            if(suffixLeft + count >= spaceLength)
                suffixLeft = spaceLength - count - 1;
            
            mySpaceMidiam = mySpace[suffixmidiam + count];
            myTargetStart = myTarget[start + count];
            
            mySpaceRight = mySpace[suffixRight + count];
            mySpaceLeft  = mySpace[suffixLeft + count];
            
            System.out.println("bottom = " + bottom + ", midiam = " + midiam + ", top = " + top);
            System.out.print("mySpace[midiam] = ");
            System.out.write(mySpaceMidiam);
            System.out.print(" , myTarget[start+count] = ");
            System.out.write(myTargetStart);
            System.out.println(" , count = "+count);
            System.out.print("mySpace[left("+(left)+")] = ");
            System.out.write(mySpaceLeft);
            System.out.print(" , mySpace[right("+(right)+")] = ");
            System.out.write(mySpaceRight);
            System.out.println();
            
            if( mySpaceMidiam == myTargetStart) {
                if (midiam == 0 || midiam == spaceLength - 1) {
                    System.out.println("StartIndex = " + midiam);
                    return midiam;
                    
                } else if(mySpaceLeft == myTargetStart && mySpaceRight == myTargetStart) {
                    if(targetLength > (count + 1) && spaceLength > (count + 1)) {
                        count++;
                        continue;
                    } else if( spaceLength - suffixmidiam < targetLength) {
                        bottom = midiam + 1;
                    
                    } else { System.out.println("StartIndex = " + (midiam - 1)); return midiam - 1; }
                    
                } else if(mySpaceRight > myTargetStart || mySpaceLeft == myTargetStart) {
                    top = midiam - 1;
                
                } else if(mySpaceLeft < myTargetStart || mySpaceRight == myTargetStart) {
                    bottom = midiam + 1;
                    
                }
            
            
            } else if( mySpaceMidiam > myTargetStart) {
                top = midiam - 1;
            } else if( mySpaceMidiam < myTargetStart){
                bottom = midiam + 1;
            }
            
            if(flag1) System.out.println("通過001");
            if(flag2) System.out.println("通過002");
            
            
        }
        boolean flag = false;
        System.out.println("spaceLength = " + (spaceLength - suffixmidiam));
        if((spaceLength - 1) / 2  - 1 == midiam) {
            for(int i = 0; i < targetLength; i++) {
                if(myTarget[start + i] != mySpace[suffixArray[(spaceLength - 1) / 2] + i]) {
                    flag = false;
                    break;
                }
                flag = true;
            }
            if(flag) {
                System.out.println("StartIndex = "+(midiam + 1));
                return midiam + 1;
            }
        }
        if(mySpace[suffixArray[midiam - 1] + count] == myTargetStart && spaceLength - suffixmidiam > targetLength) {
            System.out.println("StartIndex = "+(midiam - 1));
            return midiam - 1;
        } else {
            System.out.println("StartIndex = "+midiam);
            return midiam;
        }*/
        /*
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
         
         //String targetstr = new String(myTarget, start, end - start, UTF-8);
         
         System.out.print("myTarget=");
         for(int i = start; i < end ; i++)
               System.out.write(myTarget[i]);
         System.out.println();
        
        while( bottom <= top ) {
         
            midiam = (bottom + top) / 2;
            suffixmidiam = suffixArray[midiam];
            if(count + suffixmidiam >= spaceLength)
                suffixmidiam = spaceLength - count - 1;
            
            mySpaceMidiam = mySpace[suffixmidiam+count];
            myTargetStart = myTarget[start+count];
            System.out.println("midiam="+midiam);
            System.out.print("mySpace[midiam] = ");
            System.out.write(mySpaceMidiam);
            System.out.print(" , myTarget[start+count] = ");
            System.out.write(myTargetStart);
            System.out.println(" , count = "+count);
             if( mySpaceMidiam == myTargetStart) {
                 if(targetLength <= (count + 1)) {
                     
                     if(midiam <= 0 || midiam >= (mySpace.length - 1)) {
                         System.out.println("StartIndex = " + midiam);
                         return midiam;
                     }
                     
                     if(suffixArray[midiam - 1] + targetLength < spaceLength) {
                         
                         if(mySpace[suffixArray[midiam - 1]+count] != myTargetStart) {
                             System.out.println("StartIndex = " + midiam);
                             return midiam;
                         } else {
                             top = midiam - 1;
                         }
                     }
                     else top = midiam - 1;
                 } else {
                     count++;
                     
                     if(suffixmidiam + targetLength < spaceLength) {
                         if(mySpace[suffixmidiam + count] > myTarget[start + count]) {
                             top = midiam - 1;
                             continue;
                         } else if(mySpace[suffixmidiam + count] < myTarget[start + count]){
                             bottom = midiam + 1;
                             continue;
                         }
                     }
                     
                     //top = midiam - 1;
                 }
             } else if (count == 0){
                 if( mySpaceMidiam > myTargetStart) {
                     if(targetLength < (count + 1)) count++;
                     top = midiam - 1;
                 } else if( mySpaceMidiam < myTargetStart){
                     if(targetLength < (count + 1)) count++;
                     bottom = midiam + 1;
                 }
                 
             } else if( mySpaceMidiam > myTargetStart && mySpace[suffixmidiam+count - 1] == myTarget[start + count - 1]) {
                 if(targetLength < (count + 1)) count++;
                 top = midiam - 1;
             } else if( mySpaceMidiam < myTargetStart && mySpace[suffixmidiam+count - 1] == myTarget[start + count - 1]){
                 if(targetLength < (count + 1)) count++;
                 bottom = midiam + 1;
             } else if(mySpace[suffixmidiam + count - 1] > myTarget[start + count - 1]) {
                 top = midiam - 1;
             } else if(mySpace[suffixmidiam+count - 1] < myTarget[start + count - 1]) {
                 bottom = midiam + 1;
             }
         
        }*/

        
        
        
        int bottom = 0;
        int top = mySpace.length - 1;
        int midiam;
        int suffixmidiam;
        boolean continue_flag;
        
        byte mySpaceMidiam;
        byte myTargetStart;
        
        int targetLength = end - start;
        int spaceLength = mySpace.length;
        
        //String targetstr = new String(myTarget, start, end - start, UTF-8);
        
        //System.out.print("myTarget=");
        //for(int i = start; i < end ; i++)
        //    System.out.write(myTarget[i]);
        //System.out.println();
        
        while( bottom <= top ) {
        
            midiam = (bottom + top) / 2;
            suffixmidiam = suffixArray[midiam];
            mySpaceMidiam = mySpace[suffixmidiam];
            myTargetStart = myTarget[start];
            //System.out.println("midiam="+midiam);
            if( mySpaceMidiam == myTargetStart) {
                
                continue_flag = false;
                
                if(suffixmidiam + targetLength < spaceLength) {

                    for(int i = 1; i < targetLength ; i++) {
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
                }
                
                if(midiam <= 0 || midiam >= (mySpace.length - 1)) return midiam;
                
                if(suffixArray[midiam - 1] + targetLength < spaceLength) {
                    
                    for(int i = 0; i < targetLength ; i++) {
                        //System.out.println("suffixArray[" + (midiam - 1) + "]="+suffixArray[midiam-1]+" i=" + i);
                        if( mySpace[suffixArray[midiam - 1] + i] != myTarget[start + i]) {
                            //System.out.println("StartIndex = " + midiam);
                            return midiam;
                        }
                    }
                }
                top = midiam - 1;

            } else if( mySpaceMidiam > myTargetStart) {
                top = midiam - 1;
            } else if( mySpaceMidiam < myTargetStart){
                bottom = midiam + 1;
            }
        
        }
        
        
        
        /*
        //ソート済みのsuffixのインデックスを検索して返す
        for(int i = 0; i < mySpace.length; i++) {
            int s = suffixArray[i];
            
            // jの範囲をmyTarget.length+sからend+myTarget.length+s-start&& j < mySpace.lengthに変更
            for(int j = s; j < myTarget.length + s - start && j < mySpace.length; j++) {

                if(mySpace[j] == myTarget[start+j-s]) {

                    // (j-s) -> (start+j-s)
                    if((start + j - s) == end - 1) {
                        return i;
                    }
                }
                else {
                    break;
                }
            }
        }*/
        
        
        return suffixArray.length;
    }
    
    private int subByteEndIndex(int start, int end) {
        
        /*
        
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
        
        //String targetstr = new String(myTarget, start, end - start, UTF-8);
        
        //System.out.print("myTarget=");
        //for(int i = start; i < end ; i++)
        //    System.out.write(myTarget[i]);
        //System.out.println();
        
        while( bottom <= top ) {
            
            midiam = (bottom + top) / 2;
            suffixmidiam = suffixArray[midiam];
            if(count + suffixmidiam >= spaceLength)
                suffixmidiam = spaceLength - count - 1;
            
            mySpaceMidiam = mySpace[suffixmidiam+count];
            myTargetStart = myTarget[start+count];
            //System.out.println("midiam="+midiam);
            //System.out.print("mySpace[midiam] = ");
            //System.out.write(mySpaceMidiam);
            //System.out.print(" , myTarget[start+count] = ");
            //System.out.write(myTargetStart);
            //System.out.println(" , count = "+count);
            if( mySpaceMidiam == myTargetStart) {
                if(targetLength <= (count + 1)) {
                    
                    if(midiam <= 0 || midiam >= (mySpace.length - 1)) {
                        //System.out.println("EndIndex = " + midiam);
                        return midiam;
                    }
                    
                    if(suffixArray[midiam + 1] + targetLength < spaceLength) {
                        
                        if(mySpace[suffixArray[midiam + 1]+count] != myTargetStart) {
                            //System.out.println("EndIndex = " + (midiam + 1));
                            return midiam + 1;
                        } else {
                            bottom = midiam + 1;
                        }
                    }
                    else bottom = midiam + 1;
                } else {
                    count++;
                    
                    if(suffixmidiam + targetLength < spaceLength) {
                        if(mySpace[suffixmidiam + count] > myTarget[start + count]) {
                            top = midiam - 1;
                            continue;
                        } else if(mySpace[suffixmidiam + count] < myTarget[start + count]){
                            bottom = midiam + 1;
                            continue;
                        }
                    }
                    
                    //top = midiam - 1;
                }
            } else if (count == 0){
                if( mySpaceMidiam > myTargetStart) {
                    if(targetLength < (count + 1)) count++;
                    top = midiam - 1;
                } else if( mySpaceMidiam < myTargetStart){
                    if(targetLength < (count + 1)) count++;
                    bottom = midiam + 1;
                }
                
            } else if( mySpaceMidiam > myTargetStart && mySpace[suffixmidiam+count - 1] == myTarget[start + count - 1]) {
                if(targetLength < (count + 1)) count++;
                top = midiam - 1;
            } else if( mySpaceMidiam < myTargetStart && mySpace[suffixmidiam+count - 1] == myTarget[start + count - 1]){
                if(targetLength < (count + 1)) count++;
                bottom = midiam + 1;
            } else if(mySpace[suffixmidiam + count - 1] > myTarget[start + count - 1]) {
                top = midiam - 1;
            } else if(mySpace[suffixmidiam+count - 1] < myTarget[start + count - 1]) {
                bottom = midiam + 1;
            }
            
        }
        //System.out.println("もちぞうEndIndex = " + midiam);
        return midiam;*/
        
        
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
        
        //String targetstr = new String(myTarget, start, end - start, UTF-8);
        
        //System.out.print("myTarget=");
        //for(int i = start; i < end ; i++)
        //      System.out.write(myTarget[i]);
        //System.out.println();
        
        
        while( bottom <= top ) {
            
            midiam = (bottom + top) / 2;
            //System.out.println("midiam="+midiam);
            
            if( mySpace[suffixArray[midiam]] == myTarget[start]) {
                
                continue_flag = false;
                
                if(suffixArray[midiam] + (end - start) < mySpace.length) {
                    for(int i = 0; i < (end - start) ; i++) {
                        if(mySpace[suffixArray[midiam] + i] > myTarget[start + i]) {
                            top = midiam - 1;
                            continue_flag = true;
                            break;
                        } else if(mySpace[suffixArray[midiam] + i] < myTarget[start + i]){
                            bottom = midiam + 1;
                            continue_flag = true;
                            break;
                        }
                    }
                    if(continue_flag) continue;
                }
                
                if(midiam <= 0 || midiam >= mySpace.length - 1) return midiam;
                
                if(suffixArray[midiam + 1] + (end - start) < mySpace.length) {
                    for(int i = 0; i < (end - start) ; i++) {
                        if( mySpace[suffixArray[midiam + 1] + i] != myTarget[start + i]) {
                            //System.out.println("EndIndex = " + (midiam + 1));
                            return midiam + 1;
                        }
                    }
                }
                
                bottom = midiam + 1;
                
            } else if( mySpace[suffixArray[midiam]] > myTarget[start]) {
                top = midiam - 1;
            } else if( mySpace[suffixArray[midiam]] < myTarget[start]){
                bottom = midiam + 1;
            }
            
        }
        return suffixArray.length;
        
        //ソート済みのsuffixの次のインデックスを検索して返す
        /*
        int flag1 = 0;
        int flag2 = 0;
        
        for(int i = 0; i < mySpace.length; i++) {
            int s = suffixArray[i];
            
            // jの範囲をmyTarget.length+sからend+myTarget.length+s-start&& j < mySpace.lengthに変更
            for(int j = s; j < myTarget.length + s - start && j < mySpace.length; j++) {
 
                flag2 = flag1;
                if(mySpace[j] == myTarget[start+j-s]) {
                    
                    // (j-s) -> (start+j-s)
                    if((start + j - s) == end - 1) {
                        flag1 = 1;
                    }
                }
                else {
                    
                    flag1 = 0;
                    break;
                }
            }
            if(flag1 - flag2 == -1) {
                System.out.println("EndIndex = " + i);
                return i;
            }
        }
        
        return suffixArray.length;*/
    }
    //ここまで
    
}	    
	    
