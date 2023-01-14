package com.jsyh.buyer.ui.presenter;

import android.content.Context;

import com.jsyh.buyer.data.local.SearchDao;
import com.jsyh.buyer.ui.iview.SearchView;

import java.util.List;

/**
 * Created by mo on 17-4-18.
 */

public class SearchPresent {

    private SearchDao mDao;

    private SearchView mView;

    public SearchPresent(Context context, SearchView mView) {
        mDao = new SearchDao(context);
        this.mView = mView;
    }


    public void loadHistory() {

        List<String> history = mDao.getAll();

        mView.onLoadHistory(history);
    }

    public void deleteAll() {
        mDao.clearAll();
        mView.onDelete();
    }


    public void addHistory(String name) {
        long id = mDao.insert(name);

        mView.onAdd( name);
    }

}
