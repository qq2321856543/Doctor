package com.wd.common.Base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    private P presenter;
    Boolean is=false;
    private Dialog mLoadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), getLayout(), null);
        presenter = initPresenter();
        ButterKnife.bind(this,view);
        StatusBarUtil.setTransparent(getActivity());
        initView(view);
        Log.i("ppp","onCreateView");
        is=true;
        doNetWork();
        return view;
    }
    private void doNetWork(){
        if (getUserVisibleHint()){
            initData();
            Log.i("ppp","doNetWork"+is);

        }
    }

    protected abstract P initPresenter();

    protected abstract int getLayout();

    protected abstract void initView(View view);


    protected abstract void initData();

    public P getPresenter(){
        return presenter;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("ppp","setUserVisibleHint");

        if (is&&isVisibleToUser){
            initData();
            Log.i("ppp","setUserVisibleHint"+is+isVisibleToUser);

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i("ppp","onDetach");

    }
//    public void showDialog(){
//        if (mLoadingDialog==null){
//            mLoadingDialog = new Dialog(getContext());
//            mLoadingDialog.setCancelable(false);
//            View view = View.inflate(getContext(), R.layout.dialog_loading, null);
//            ImageView iv=view.findViewById(R.id.iv);
//            Glide.with(this).asGif().load(R.mipmap.timg).into(iv);
//            mLoadingDialog.addContentView(view,
//                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT));
//        }
//        mLoadingDialog.show();
//    }
    public void hideDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
