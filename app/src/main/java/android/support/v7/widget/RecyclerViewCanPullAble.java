package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.youloft.lilith.common.widgets.view.Pullable;

/**
 *
 * Extends RecyclerView access self hide field
 *
 * Created by zchao on 2017/7/7.
 * desc: 实现了{@link Pullable}接口，这个RecyclerView可以用于下拉刷新控件中
 * version:
 */
public class RecyclerViewCanPullAble extends RecyclerView implements Pullable{
    public RecyclerViewCanPullAble(Context context) {
        super(context);
    }

    public RecyclerViewCanPullAble(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewCanPullAble(Context context, @Nullable AttributeSet attrs, int defStyle) {
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


    @Override
    public boolean canPullDown() {
        int firstVisible = -1;
        if(getLayoutManager() != null){
            if (getLayoutManager() instanceof LinearLayoutManager) {
                firstVisible = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager) getLayoutManager()).findFirstVisibleItemPositions(null);
                firstVisible = firstVisibleItemPositions[0];
            }
        }
        if (getChildCount() == 0)
        {
            // 没有item的时候也可以下拉刷新
            return true;
        } else // 滑到ListView的顶部了
            return firstVisible == 0
                    && getChildAt(0).getTop() >= 0;
    }
}
