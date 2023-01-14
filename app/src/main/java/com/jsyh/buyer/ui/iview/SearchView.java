package com.jsyh.buyer.ui.iview;

import java.util.List;

/**
 * Created by mo on 17-4-18.
 */

public interface SearchView {

    void onLoadHistory(List<String> data);

    void onDelete();

    void onAdd(String name);


}
