package com.jsyh.buyer.ui.search;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsyh.buyer.BaseActivity;
import com.jsyh.buyer.R;
import com.jsyh.buyer.adapter.SearchAdapter;
import com.jsyh.buyer.data.local.MsgDao;
import com.jsyh.buyer.ui.iview.SearchView;
import com.jsyh.buyer.ui.presenter.SearchPresent;
import com.jsyh.buyer.utils.StateBarUitl;
import com.jsyh.pushlibrary.MessageModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SearchView,SearchAdapter.OnItemClickLinstener{

    @BindView(R.id.searchLayout)
    LinearLayout mSearchLayout;

    @BindView(R.id.searchRight)
    TextView searchRight;
    @BindView(R.id.searchRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.btnClear)
    Button btnClear;

    @BindView(R.id.ivBack)
    ImageView mBack;

    @BindView(R.id.etSearchKeyWord)
    EditText mSearchContent;

    private RecyclerView.LayoutManager lm;
    private SearchAdapter mAdapter;
    private SearchPresent mPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchPresent(this, this);

        mStateColor = Color.WHITE;

        if (canChangeStateFont()) {

            StateBarUitl.changeLollipopStateBarColor(this, mStateColor);
            StateBarUitl.setStatusBarFontDark(this, true);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSearchLayout.getLayoutParams();
            params.setMargins(0, StateBarUitl.getStatusBarHeight(this), 0, 0);

        } else {
            mStateColor = getResources().getColor(R.color.kitkatStateBarColor);

            StateBarUitl.changeStateBarColor(this, mStateColor);
        }
        mSearchContent.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    chechSearch();
                }
                return false;
            }
        });

        lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lm.setAutoMeasureEnabled(true);
        mAdapter = new SearchAdapter();
        mAdapter.setItemClickLinstener(this);

        mRecycler.setLayoutManager(lm);
        mRecycler.setAdapter(mAdapter);

        mPresenter.loadHistory();

    }

    @Override
    protected int getRootView() {
        return R.layout.search_activity;
    }

    @OnClick({R.id.searchRight, R.id.btnClear,R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.searchRight:
                chechSearch();
                break;
            case R.id.btnClear:
                mPresenter.deleteAll();

                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private void chechSearch() {
        String searchContent = mSearchContent.getText().toString();
        if (!TextUtils.isEmpty(searchContent)) {
            mPresenter.addHistory(searchContent.trim());
            searchByKeyword(searchContent.trim());
        } else {
            Toast.makeText(this, "输入关键字搜索哦!", Toast.LENGTH_SHORT).show();
            mSearchContent.requestFocus();
        }
    }

    public void searchByKeyword(String keyword) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("keyword", keyword);

        startActivity(intent);

        finish();
    }

//===================== item click ============================
    @Override
    public void onIntemClickListener(String keyword) {
        searchByKeyword(keyword);
        mPresenter.addHistory(keyword);
    }

    //========================view================================

    @Override
    public void onLoadHistory(List<String> data) {
        if (data != null && !data.isEmpty()) {
            mAdapter.setDatas(data);
            btnClear.setVisibility(View.VISIBLE);

        } else {
            btnClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDelete() {
        mAdapter.clear();
        btnClear.setVisibility(View.GONE);
    }

    @Override
    public void onAdd(String name) {

//        searchByKeyword(name);

//        mAdapter.setDatas(name);
//        btnClear.setVisibility(View.VISIBLE);
    }

}
