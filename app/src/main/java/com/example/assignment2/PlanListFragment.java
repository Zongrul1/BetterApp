package com.example.assignment2;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.Adapter.PlanListAdapter;
import com.example.assignment2.Utils.DateUtil;
import com.example.assignment2.Utils.LogUtil;
import com.example.assignment2.Utils.ToastUtil;
import com.example.assignment2.bean.PlanListItem;
import com.example.assignment2.db.PlanListItemDao;
import com.example.assignment2.listener.OnBackPressListener;
import com.example.assignment2.listener.OnClickListener;
import com.example.assignment2.listener.OnNextListener;
import com.example.assignment2.listener.OnTextChangeListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlanListFragment extends Fragment {
    private Context context;
    private View mainLayout;
    private TextView dateTextView;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private PlanListAdapter adapter;
    private List<PlanListItem> dataList=new ArrayList<>();
    private Date date;
    private String time;
    private long preTime;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainLayout= LayoutInflater.from(context).inflate(R.layout.activity_planlist,container,false);
        initParams();
        initViews();
        return mainLayout;
    }

    private void initParams(){
        Calendar calendar= Calendar.getInstance();
        date=calendar.getTime();
    }
    private void initViews(){
        dateTextView=mainLayout.findViewById(R.id.list_date);
        Typeface rl = ResourcesCompat.getFont(getActivity(),R.font.rl);
        dateTextView=mainLayout.findViewById(R.id.list_date);
        dateTextView.setTypeface(rl);
        dateTextView=mainLayout.findViewById(R.id.list_date);
        recyclerView=mainLayout.findViewById(R.id.list_recycler_view);

        initDate();
        initRecyclerView();
    }

    private void initDate(){
        time= DateUtil.getYearMonthDayNumberic(date);
        dateTextView.setText(DateUtil.getYearMonthDay(date));
    }
    private void initRecyclerView(){
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        initDataList();
        adapter=new PlanListAdapter(dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager.scrollToPosition(dataList.size()-1);
        adapter.setOnFinishListener(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                markStatus(position, PlanListItem.FINISH);
                addEmptyItem(position);
            }
        });
        adapter.setOnUnFinishListener(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                markStatus(position, PlanListItem.UNFINISH);
                addEmptyItem(position);
            }
        });
        adapter.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChange(Editable s, int pos) {
                PlanListAdapter.ViewHolder viewHolder=(PlanListAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(pos);
                if(dataList.get(pos).getStatus()== PlanListItem.NO_RECORD||
                        dataList.get(pos).getStatus()== PlanListItem.NO_CONTENT){
                    if(!TextUtils.isEmpty(s.toString())){
                        if(viewHolder!=null){
                            viewHolder.finish.setVisibility(View.VISIBLE);
                            viewHolder.unFinish.setVisibility(View.VISIBLE);
                        }
                        dataList.get(pos).setStatus(PlanListItem.NO_RECORD);
                        dataList.get(pos).setContent(s.toString());
                        PlanListItemDao.updateItem(dataList.get(pos).getId(), PlanListItem.NO_RECORD,s.toString());
                    }else{
                        if(viewHolder!=null){
                            viewHolder.finish.setVisibility(View.INVISIBLE);
                            viewHolder.unFinish.setVisibility(View.INVISIBLE);
                        }
                        dataList.get(pos).setStatus(PlanListItem.NO_CONTENT);
                        dataList.get(pos).setContent("");
                        PlanListItemDao.updateItem(dataList.get(pos).getId(), PlanListItem.NO_CONTENT,"");
                    }
                }
            }
        });
        adapter.setOnNextListener(new OnNextListener() {
            @Override
            public void onNext(int pos) {
                if(isLastItem(pos)){
                    if (!TextUtils.isEmpty(dataList.get(pos).getContent())){
                        addEmptyItem(pos);
                        adapter.setNewItem(true);
                    }
                }else{
                    int firstVisibleItemPos=((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int nextPos=getNextFocusPos(pos);
                    if(nextPos!=pos){
                        String content=dataList.get(nextPos).getContent();
                        adapter.setNewItem(false);
                        View itemView=recyclerView.getChildAt(nextPos-firstVisibleItemPos);
                        PlanListAdapter.ViewHolder viewHolder=(PlanListAdapter.ViewHolder)recyclerView.getChildViewHolder(itemView);
                        layoutManager.scrollToPositionWithOffset(nextPos,50);
                        viewHolder.content_edit.requestFocus();
                        viewHolder.content_edit.setSelection(content.length());
                    }
                }
            }
        });
        adapter.setOnBackPressListener(new OnBackPressListener() {
            @Override
            public void onBackPress(EditText editText, int pos) {
                if(pos!=dataList.size()-1){
                    int startSelection=editText.getSelectionStart();
                    if(startSelection==0){
                        long currentTime= System.currentTimeMillis();
                        if(currentTime-preTime>2000){
                            ToastUtil.showToast("再按一次删除当前项");
                            preTime=currentTime;
                        }else{
                            if(pos-1>=0){
                                mergeText(pos);
                            }
                            LogUtil.e("pos="+pos);
                            PlanListItemDao.deleteItem(dataList.get(pos).getId());
                            dataList.remove(pos);
                            adapter.notifyItemRemoved(pos);
                        }
                    }
                }
            }
        });
    }
    private void initDataList(){
        dataList= PlanListItemDao.queryAllItems(time);
        LogUtil.e("dataList.size():"+dataList.size());
        if(dataList.size()==0){
            PlanListItem item=createEmptyItem();
            dataList.add(item);
        }
    }
    private PlanListItem createEmptyItem(){
        PlanListItem item=new PlanListItem("", PlanListItem.NO_CONTENT,time);
        long itemId= PlanListItemDao.insertListItem(item);
        item.setId(itemId);
        return item;
    }

    /**
     * 判断当前是不是最后一项
     * @param pos
     */
    private boolean isLastItem(int pos){
        return pos==dataList.size()-1;
    }
    /**
     * 如果当前是最后一项，就增加一个item
     * @param pos
     */
    private void addEmptyItem(int pos){
        if(isLastItem(pos)){
            PlanListItem listItem=createEmptyItem();
            dataList.add(listItem);
            adapter.notifyItemInserted(dataList.size()-1);
            layoutManager.scrollToPosition(dataList.size()-1);
        }
    }

    /**
     * 标记当前项的状态，完成/未完成
     * @param pos
     * @param status
     */
    private void markStatus(int pos, int status){
        dataList.get(pos).setStatus(status);
        adapter.notifyItemChanged(pos);
        PlanListItemDao.updateItem(dataList.get(pos).getId(),status);
    }

    /**
     * 获取当前位置开始向下数，下一个可以focus的editText的位置
     * @param pos adapterPosition
     * @return 返回的是在datalist中的pos
     */
    private int getNextFocusPos(int pos){
        int firstPos=((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int childCount=recyclerView.getChildCount();
        LogUtil.e("firstVisibleItem:"+firstPos+"\tchildCount:"+childCount);
        int returnPos=pos;
        for(int i=pos-firstPos+1;i<childCount;i++){
            if(dataList.get(i+firstPos).getStatus()== PlanListItem.NO_RECORD||
                    dataList.get(i+firstPos).getStatus()== PlanListItem.NO_CONTENT){
                returnPos=i+firstPos;
                break;
            }
        }
        return returnPos;
    }

    /**
     * 合并当前项和上一项的文本内容
     * @param pos
     */
    private void mergeText(int pos){
        String curContent=dataList.get(pos).getContent();
        int preStatus=dataList.get(pos-1).getStatus();
        String preContent=dataList.get(pos-1).getContent();
        if(preStatus== PlanListItem.NO_CONTENT||
                preStatus== PlanListItem.NO_RECORD){
            String content=preContent+curContent;
            dataList.get(pos-1).setContent(content);
            dataList.get(pos-1).setStatus(PlanListItem.NO_RECORD);
            adapter.notifyItemChanged(pos-1);
            PlanListItemDao.updateItem(dataList.get(pos-1).getId(), PlanListItem.NO_RECORD,content);
        }
    }
}
