package com.wd.common.Base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;

public abstract class BaseAcitvity<P extends BasePresenter> extends AppCompatActivity implements IBaseView {

    private P mPresenter;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayout());
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        StatusBarUtil.setTransparent(this);
        ButterKnife.bind(this);
        mPresenter = initPresenter();

        initView();
        initData();
    }
    public P getPresenter(){
        return mPresenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null){
            mPresenter.detachView();
            mPresenter=null;
        }
    }
//    public void showDialog(){
//        if (mLoadingDialog==null){
//            mLoadingDialog = new Dialog(this);
//            mLoadingDialog.setCancelable(false);
//            View view = View.inflate(this, R.layout.dialog_loading, null);
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
    protected abstract P initPresenter();

    protected abstract int getLayout();

    protected abstract void initView();

    protected abstract void initData();
}
