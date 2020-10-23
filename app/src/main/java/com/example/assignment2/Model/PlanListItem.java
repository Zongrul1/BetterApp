package com.example.assignment2.Model;

public class PlanListItem {
    public  static final int NO_CONTENT=101;
    public static final int NO_RECORD=102;
    public static final int FINISH=103;
    public static final int UNFINISH=104;

    public static final int TYPE_NORMAL=106;
    public static final int TYPE_EMPTY=107;

    private long id;
    private String content;
    private int status;
    private String time;

    public PlanListItem(String content, int status, String time){
        this.content=content;
        this.status=status;
        this.time=time;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
