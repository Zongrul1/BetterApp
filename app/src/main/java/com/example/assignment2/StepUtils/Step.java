package com.example.assignment2.StepUtils;

import android.util.Log;

import com.example.assignment2.listener.StepChangeListener;
import com.example.assignment2.listener.StepListener;

//we apply the rule that if steps<5 within 3 seconds, then we will discard the cache(do not count step)
public class Step implements StepListener {
        private int current_Count; //the current steps
        private int temp_count;  //the cache steps
        private long timeOfLastPeak = 0;
        private long timeOfThisPeak = 0;
        private StepChangeListener stepChangeListener;
        private StepDetec stepDetector;

        public Step() {
            stepDetector = new StepDetec();
            stepDetector.initListener(this);
        }

        @Override
        public void countStep() {
            this.timeOfLastPeak = this.timeOfThisPeak;
            this.timeOfThisPeak = System.currentTimeMillis();
         //   Log.i("countStep","Sensor data refresh callback");
//        notifyListener();
            if (this.timeOfThisPeak - this.timeOfLastPeak <= 3000L) {
                if (this.temp_count < 5) {
                    this.temp_count++;
                } else if (this.temp_count == 5) {
                    this.temp_count++;
                    this.current_Count += this.temp_count;
                    notifyListener();
                } else {
                    this.current_Count++;
                    notifyListener();
                }
            } else {//means overtime(time>3s)
                this.temp_count = 1;
            }

        }
        public void setSteps(int initCurrentStep){
            this.current_Count = initCurrentStep;
            this.temp_count = 0;
            timeOfLastPeak = 0;
            timeOfThisPeak = 0;
            notifyListener();
        }

        public StepDetec getStepDetector(){
            return stepDetector;
        }

        public void notifyListener(){
            if(this.stepChangeListener != null){
         //       Log.i("countStep","Update statistics");
                this.stepChangeListener.stepChanged(this.current_Count);  //当前步数通过接口传递给调用者
            }
        }
        public  void initListener(StepChangeListener listener){
            this.stepChangeListener = listener;
        }
    }