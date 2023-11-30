package com.example.mcprojectv5;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ratings")
public class Rating {
    @PrimaryKey(autoGenerate = true)
    private long PKey;
    private int nausea;
    private int headache;
    private int diarrhea;
    private int SoreThroat;
    private int fever;
    private int MuscleAche;
    private int LossOfSmell;
    private int Cough;
    private int ShortBreath;
    private int Tired;
    private float HeartValue;
    private float LungValue;

    public void setValue(float Hrating,float LRating,int id, int value) {
        this.PKey=0;
        this.HeartValue=Hrating;
        this.LungValue=LRating;
        this.nausea=0;
        this.Cough=0;
        this.fever=0;
        this.diarrhea=0;
        this.headache=0;
        this.LossOfSmell=0;
        this.MuscleAche=0;
        this.ShortBreath=0;
        this.SoreThroat=0;
        this.Tired=0;
        switch (id){
            case 1:this.nausea=value;
            break;
            case 2:this.headache=value;
            break;
            case 3:this.diarrhea=value;
            break;
            case 4:this.SoreThroat=value;
            break;
            case 5:this.fever=value;
            break;
            case 6:this.MuscleAche=value;
            break;
            case 7: this.LossOfSmell=value;
            break;
            case 8:this.Cough=value;
            break;
            case 9:this.ShortBreath=value;
            break;
            case 10:this.Tired=value;
            break;

        }
    }
    public long getPKey(){
        return PKey;
    }
    public float getHeartValue(){
        return HeartValue;
    }
    public float getLungValue(){
        return LungValue;
    }
    public int getNausea(){
        return nausea;
    }
    public int getHeadache(){
        return headache;
    }
    public int getTired(){
        return Tired;
    }
    public int getLossOfSmell(){
        return LossOfSmell;
    }
    public int getDiarrhea(){
        return diarrhea;
    }
    public int getSoreThroat(){
        return SoreThroat;
    }
    public int getFever(){
        return fever;
    }
    public int getMuscleAche(){
        return MuscleAche;
    }
    public int getHLossOfSmell(){
        return LossOfSmell;
    }
    public int getCough(){
        return Cough;
    }
    public int getShortBreath(){
        return ShortBreath;
    }
    public int getFeelingTired(){
        return Tired;
    }
    public void setPKey(long value){
        this.PKey=value;
    }
    public void setDiarrhea(int value){
        this.diarrhea=value;
    }
    public void setSoreThroat(int value){
        this.SoreThroat=value;
    }
    public void setHeadache(int value){
        this.headache=value;
    }
    public void setFever(int value){
        this.fever=value;
    }
    public void setMuscleAche(int value){
        this.MuscleAche=value;
    }
    public void setLossOfSmell(int value){
        this.LossOfSmell=value;
    }
    public void setNausea(int value){
        this.nausea=value;
    }
    public void setCough(int value){
        this.Cough=value;
    }
    public void setTired(int value){
        this.Tired=value;
    }
    public void setShortBreath(int value){
        this.ShortBreath=value;
    }
    public void setHeartValue(float value){
        this.HeartValue=value;
    }
    public void setLungValue(float value){
        this.LungValue=value;
    }
}

