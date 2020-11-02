package com.example.assignment2.Subsciber;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.assignment2.LoginActivity;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import rx.Subscriber;

public class MainSubscriber<T> extends Subscriber<T> {

    private HelperSubscriber mSubscriberOnNextListener;
    private Context context;

    public MainSubscriber(HelperSubscriber mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "network error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            new AlertDialog.Builder(context).setTitle("Error").setMessage("This account log in in another device").show();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            try {
                mSubscriberOnNextListener.onNext(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
