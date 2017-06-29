package com.youloft.lilith;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * Desc: 只是测试arouter的。后边删除
 * Change: 
 * 
 * @version 
 * @author zchao created at 2017/6/29 10:30
 * @see 
*/
@Route(path = "/test/arouterTest")
public class ARouterTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter_test);
    }
}
