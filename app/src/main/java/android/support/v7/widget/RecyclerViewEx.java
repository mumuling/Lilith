package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * Extends RecyclerView access self hide field
 *
 * Created by zchao on 2017/7/7.
 * desc:
 * version:
 */
public class RecyclerViewEx extends RecyclerView {
    public RecyclerViewEx(Context context) {
        super(context);
    }

    public RecyclerViewEx(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewEx(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * get　View instance from hide field {recycler}
     * @param adapterPostiion
     * @return
     */
    public View getCachedViewForPosition(int adapterPostiion){
        if(mRecycler!=null){
            return  mRecycler.getViewForPosition(adapterPostiion);
        }
        return null;
    }


}
