package com.example.assignment2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.CheckInFragment;
import com.example.assignment2.EmotionFragment;
import com.example.assignment2.FocusFragment;
import com.example.assignment2.MainActivity;
import com.example.assignment2.MainFragment;
import com.example.assignment2.MemoFragment;
import com.example.assignment2.PedometerFragment;
import com.example.assignment2.PlanListActivity;
import com.example.assignment2.R;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        MenuAdapter.ViewHolder vh = new MenuAdapter.ViewHolder(v,context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.menuItem.setText(menu.get(position));
        switch (position%6){
            case 0:
                viewHolder.menuItemLayout.setBackgroundResource(R.drawable.menu_plan);
                break;
            case 1:
                viewHolder.menuItemLayout.setBackgroundResource(R.drawable.menu_checkin);
                break;
            case 2:
                viewHolder.menuItemLayout.setBackgroundResource(R.drawable.menu_memo);
                break;
            case 3:
                viewHolder.menuItemLayout.setBackgroundResource(R.drawable.menu_pedometer);
                break;
            case 4:
                viewHolder.menuItemLayout.setBackgroundResource(R.drawable.menu_focus);
                break;
            case 5:
                viewHolder.menuItemLayout.setBackgroundResource(R.drawable.menu_emotion);
                break;
            default:
                viewHolder.menuItemLayout.setBackgroundColor(0xffFFFFFF);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView menuItem;
        RelativeLayout menuItemLayout;
        Context context;

        public ViewHolder(@NonNull View view, final Context context) {
            super(view);
            menuItem = view.findViewById(R.id.menuItem);
            menuItemLayout = view.findViewById(R.id.menuItemLayout);
            this.context = context;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = new MainFragment();
                    switch (getAdapterPosition()){
                        case 0:
//                            fragment = new PlanFragment();
//                            ((AppCompatActivity)context).getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.layout_fragment, fragment)
//                                    .addToBackStack(null)
//                                    .commit();
                            Intent intent = new Intent(context,PlanListActivity.class);
                            context.startActivity(intent);
                            break;
                        case 1:
                            fragment = new CheckInFragment();
                            ((AppCompatActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.layout_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        case 2:
                            fragment = new MemoFragment();
                            ((AppCompatActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.layout_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        case 3:
                            fragment = new PedometerFragment();
                            ((AppCompatActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.layout_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        case 4:
                            fragment = new FocusFragment();
                            ((AppCompatActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.layout_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
//                        intent = new Intent(getActivity(),FocusActivity.class);
//                        startActivity(intent);
                            break;
                        case 5:
                            fragment = new EmotionFragment();
                            ((AppCompatActivity)context).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.layout_fragment, fragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;

                    }
                    MainActivity activity = (MainActivity) context;
                    activity.drawerLayout.closeDrawers();
                }
            });
        }
    }

    private int resourceId;
    private Context context;
    private List<String> menu;

    public MenuAdapter(Context context, int resource, ArrayList<String> menu) {
        super();
        resourceId = resource;
        this.context = context;
        this.menu = menu;
    }

}
