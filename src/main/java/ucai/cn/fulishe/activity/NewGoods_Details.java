package ucai.cn.fulishe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import ucai.cn.fulishe.Adapter.CartAdapter;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.Utils.CommonUtils;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.Views.FlowIndicator;
import ucai.cn.fulishe.Views.SlideAutoLoopView;
import ucai.cn.fulishe.bean.AlbumsBean;
import ucai.cn.fulishe.bean.GoodsDetailsBean;
import ucai.cn.fulishe.bean.MessageBean;
import ucai.cn.fulishe.bean.PropertiesBean;
import ucai.cn.fulishe.fragment.Cart;

public class NewGoods_Details extends AppCompatActivity {

    @Bind(R.id.tv_goodsdetails_englishname)
    TextView mtvGoodsdetailsEnglishname;
    @Bind(R.id.tv_newgoods_details)
    TextView mtv_newgoods_details;
    @Bind(R.id.tv_goodsdetails_goodsname)
    TextView mtvGoodsdetailsGoodsname;
    @Bind(R.id.tv_newgoods_goodsprice)
    TextView mtvNewgoodsGoodsprice;
    @Bind(R.id.vp_goodsdetails)
    SlideAutoLoopView msalv_goodsdetails;
    @Bind(R.id.newgoodsdetails_flowindicator)
    FlowIndicator newgoodsdetailsFlowindicator;
    int mcount;
    @Bind(R.id.iv_details_menu_share)
    ImageView ivTopMenuShare;
    @Bind(R.id.iv_top_menu_back)
    ImageView ivTopMenuBack;
    @Bind(R.id.iv_details_menu_collected)
    ImageView mivTopMenuCollected;
    int mgoodsid;
    boolean isCollected = false;
    @Bind(R.id.iv_details_menu_cart)
    ImageView ivDetailsMenuCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newgoods_details);
        ButterKnife.bind(this);
        initData();
    }

    public void initData() {
        Intent intent = getIntent();
        mgoodsid = intent.getIntExtra("goods", 7727);
        NetDao.downloadDetails(NewGoods_Details.this, mgoodsid, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean goodsDetails) {
                mtvGoodsdetailsEnglishname.setText(goodsDetails.getGoodsEnglishName());
                mtvGoodsdetailsGoodsname.setText(goodsDetails.getGoodsName());
                mtvNewgoodsGoodsprice.setText(goodsDetails.getShopPrice());
                mtv_newgoods_details.setText(goodsDetails.getGoodsBrief());
                PropertiesBean[] propertiesBean = goodsDetails.getProperties();
                AlbumsBean[] albumsBean = propertiesBean[0].getAlbums();
                newgoodsdetailsFlowindicator.setCount(albumsBean.length);
                String[] mImagesURL = new String[albumsBean.length];
                for (int i = 0; i < albumsBean.length; i++) {
                    mImagesURL[i] = albumsBean[i].getImgUrl();
                    L.i(albumsBean[i].getImgUrl());
                }
                msalv_goodsdetails.startPlayLoop(newgoodsdetailsFlowindicator, mImagesURL, albumsBean.length);
            }

            @Override
            public void onError(String error) {

            }
        });
        ivDetailsMenuCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addToCart();
            }
        });

        mivTopMenuCollected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCollected) {
                    NetDao.addCollections(NewGoods_Details.this, mgoodsid, FuliCenterApplication.getInstance().getUsername(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result.isSuccess()) {
                                CommonUtils.showShortToast("添加成功！");
                                isCollected = !isCollected;
                            } else {
                                CommonUtils.showShortToast("添加失败！");
                                return;
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                    mivTopMenuCollected.setImageResource(R.mipmap.bg_collect_out);

                } else {
                    NetDao.deletecollections(NewGoods_Details.this, mgoodsid, FuliCenterApplication.getInstance().getUsername(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result.isSuccess()) {
                                CommonUtils.showShortToast("删除成功！");
                                isCollected = !isCollected;
                            } else {
                                CommonUtils.showShortToast("删除收藏失败");
                                return;
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                    mivTopMenuCollected.setImageResource(R.mipmap.bg_collect_in);
                }
            }
        });


    }
    private void showShare() {
        ShareSDK.initSDK(NewGoods_Details.this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(NewGoods_Details.this);
    }
    private void addToCart() {
        NetDao.addCart(NewGoods_Details.this, mgoodsid, FuliCenterApplication.getInstance().getUsername(), 1, true, new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result != null) {
                    CommonUtils.showShortToast("添加成功！");

                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void onResume() {
        L.i("OnResume");
        super.onResume();
        NetDao.isCollected(NewGoods_Details.this, mgoodsid, FuliCenterApplication.getInstance().getUsername(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean result) {
                if (result.isSuccess()) {
                    isCollected = true;
                    mivTopMenuCollected.setImageResource(R.mipmap.bg_collect_out);
                } else {
                    isCollected = false;
                    mivTopMenuCollected.setImageResource(R.mipmap.bg_collect_in);
                }

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @OnClick(R.id.iv_details_menu_share)
    public void iv_top_menu_share() {
        showShare();
    }
    @OnClick(R.id.iv_top_menu_back)
    public void iv_top_menu_back() {
        this.finish();
    }

}
