package com.wtjr.lqg.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public class XCRecyclerView extends RecyclerView {

    // 自定义滚动监听
    private OnSlidingStatelistener onSlidingStatelistener;

    public void setOnSlidingStatelistener(OnSlidingStatelistener onSlidingStatelistener) {
        this.onSlidingStatelistener = onSlidingStatelistener;
    }

    /**
     * 滑动状态接口
     *
     * @author dell
     */
    public interface OnSlidingStatelistener {
        /**
         * 滑动状态，为了解决阶级滑竿，拖动时，和上下拉切换页面产生的事件冲突
         *
         * @param state
         */
        void slidingState(boolean state);
    }

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();
    private Adapter mAdapter;
    private Adapter mWrapAdapter;
    private static final int TYPE_HEADER = -101;
    private static final int TYPE_FOOTER = -102;
    private static final int TYPE_LIST_ITEM = -103;

    public XCRecyclerView(Context context) {
        this(context, null);
    }

    public XCRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XCRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mWrapAdapter = new WrapAdapter(mHeaderViews, mFooterViews, adapter);
        super.setAdapter(mWrapAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
    }

    public void addHeaderView(View view) {
        mHeaderViews.clear();
        mHeaderViews.add(view);
    }

    public void addFooterView(View view) {
        mFooterViews.clear();
        mFooterViews.add(view);
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    private final AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

//        @Override
//        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
//            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
//        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }
    };

    private class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter mAdapter;
        private List<View> mHeaderViews;
        private List<View> mFooterViews;

        public WrapAdapter(List<View> headerViews, List<View> footerViews, Adapter adapter) {
            this.mAdapter = adapter;
            this.mHeaderViews = headerViews;
            this.mFooterViews = footerViews;
        }

        public int getHeaderCount() {
            return this.mHeaderViews.size();
        }

        public int getFooterCount() {
            return this.mFooterViews.size();
        }

        public boolean isHeader(int position) {
            return position >= 0 && position < this.mHeaderViews.size();
        }

        public boolean isFooter(int position) {
            return position < getItemCount() && position >= getItemCount() - this.mFooterViews.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                return new CustomViewHolder(this.mHeaderViews.get(0));
            } else if (viewType == TYPE_FOOTER) {
                return new CustomViewHolder(this.mFooterViews.get(0));
            } else {
                return this.mAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (isHeader(position)) return;
            if (isFooter(position)) return;
            int rePosition = position - getHeaderCount();
            int itemCount = this.mAdapter.getItemCount();
            if (this.mAdapter != null) {
                if (rePosition < itemCount) {
                    this.mAdapter.onBindViewHolder(holder, rePosition);
                    return;
                }
            }
        }

        @Override
        public long getItemId(int position) {
            if (this.mAdapter != null && position >= getHeaderCount()) {
                int rePosition = position - getHeaderCount();
                int itemCount = this.mAdapter.getItemCount();
                if (rePosition < itemCount) {
                    return this.mAdapter.getItemId(rePosition);
                }
            }
            return -1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return TYPE_HEADER;
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }
            int rePosition = position - getHeaderCount();
            int itemCount = this.mAdapter.getItemCount();
            if (rePosition < itemCount) {
                return this.mAdapter.getItemViewType(position);
            }
            return TYPE_LIST_ITEM;
        }

        @Override
        public int getItemCount() {
            if (this.mAdapter != null) {
                return getHeaderCount() + getFooterCount() + this.mAdapter.getItemCount();
            } else {
                return getHeaderCount() + getFooterCount();
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (this.mAdapter != null) {
                this.mAdapter.registerAdapterDataObserver(observer);
            }
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (this.mAdapter != null) {
                this.mAdapter.unregisterAdapterDataObserver(observer);
            }
        }

        private class CustomViewHolder extends ViewHolder {

            public CustomViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onSlidingStatelistener != null) {
                    onSlidingStatelistener.slidingState(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (onSlidingStatelistener != null) {
                    onSlidingStatelistener.slidingState(false);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

}

