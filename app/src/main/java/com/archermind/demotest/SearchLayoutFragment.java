package com.archermind.demotest;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 *
 */
public class SearchLayoutFragment extends FristFragment {

    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;

    Unbinder unbinder;
    @BindView(R.id.iv_del)
    ImageView iv_del;



    private View view;

    public SearchLayoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);


        initListener();


        return view;
    }

    /**
     * 删除按键的显示和隐藏
     *
     * @param isVISIBLE
     */
    private void setDeleteVisibility(boolean isVISIBLE) {
        if (isVISIBLE) {
            iv_del.setVisibility(View.VISIBLE);
        } else {
            iv_del.setVisibility(View.GONE);
        }
    }







    /**
     * 初始化监听
     */
    private void initListener() {
        setDeleteVisibility(false);
        iv_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                et_search.setText("");
                return true;
            }
        });
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String txt = s != null ? s.toString() : "";

                setDeleteVisibility(!TextUtils.isEmpty(s));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick({R.id.iv_del, R.id.et_search, R.id.btn_cancel,})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_del:
                int index = et_search.getSelectionStart();
                if (index == 0) {
                    return;
                }
                Editable editable = et_search.getText();
                editable.delete(index - 1, index);
                break;
            case R.id.et_search:
                break;
            case R.id.btn_cancel:
                getActivity().finish();
                break;

        }
    }






    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard(et_search);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideKeyboard(et_search);
    }

    public void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
