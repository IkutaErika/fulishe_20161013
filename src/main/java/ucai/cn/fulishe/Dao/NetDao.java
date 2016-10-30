package ucai.cn.fulishe.Dao;

import android.content.Context;
import android.util.Log;

import java.io.File;

import ucai.cn.fulishe.Utils.I;
import ucai.cn.fulishe.Utils.L;
import ucai.cn.fulishe.Utils.MD5;
import ucai.cn.fulishe.Utils.OkHttpUtils;
import ucai.cn.fulishe.activity.Category_child_details;
import ucai.cn.fulishe.activity.MainActivity;
import ucai.cn.fulishe.activity.Mycollections;
import ucai.cn.fulishe.activity.NewGoods_Details;
import ucai.cn.fulishe.activity.personal_loginin;
import ucai.cn.fulishe.activity.personal_request;
import ucai.cn.fulishe.bean.BoutiqueBean;
import ucai.cn.fulishe.bean.CartBean;
import ucai.cn.fulishe.bean.CategoryChildBean;
import ucai.cn.fulishe.bean.CategoryGroup;
import ucai.cn.fulishe.bean.CollectBean;
import ucai.cn.fulishe.bean.GoodsDetailsBean;
import ucai.cn.fulishe.bean.MessageBean;
import ucai.cn.fulishe.bean.NewGoodsBean;
import ucai.cn.fulishe.bean.Result;
import ucai.cn.fulishe.fragment.NewGoods;

/**
 * Created by Administrator on 2016/10/18.
 */
public class NetDao {
    Context mcontext;
    int mgoodsid;

    public NetDao(Context mcontext, int mgoodsid) {
        this.mcontext = mcontext;
        this.mgoodsid = mgoodsid;
    }
    public NetDao(Context mcontext) {
        this.mcontext = mcontext;
    }

    public static void downloadDetails(Context context, int mgoodsid, OkHttpUtils.OnCompleteListener<GoodsDetailsBean> mlistener) {
        OkHttpUtils<GoodsDetailsBean> utils=new OkHttpUtils<>(context);
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(I.Goods.KEY_GOODS_ID,mgoodsid+"")
                .targetClass(GoodsDetailsBean.class)
                .execute(mlistener);
    }
    public static void download(final Context context, int page_id, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_NEW_BOUTIQUE_GOODS);
        utils.addParam(I.GoodsDetails.KEY_CAT_ID, I.CAT_ID + "");
        utils.addParam(I.PAGE_ID, page_id + "");
        utils.addParam(I.PAGE_SIZE, 10 + "");
        utils.targetClass(NewGoodsBean[].class)
                .execute(listener);
    }
    public static void downloadBoutiques(MainActivity mcontext, OkHttpUtils.OnCompleteListener<BoutiqueBean[]> Listener) {
        OkHttpUtils<BoutiqueBean[]> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_BOUTIQUES)
                .targetClass(BoutiqueBean[].class)
                .execute(Listener);
    }
    public static void downloadBoutiques(final Context context,int cat_id, int page_id, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils = new OkHttpUtils<>(context);
        utils.url(I.SERVER_ROOT + I.REQUEST_FIND_NEW_BOUTIQUE_GOODS);
        utils.addParam(I.GoodsDetails.KEY_CAT_ID, cat_id + "");
        utils.addParam(I.PAGE_ID, page_id + "");
        utils.addParam(I.PAGE_SIZE, 10 + "");
        utils.targetClass(NewGoodsBean[].class)
                .execute(listener);
    }

    public static void downloadCategory(MainActivity mcontext, OkHttpUtils.OnCompleteListener<CategoryGroup[]> listener) {
        OkHttpUtils<CategoryGroup[]> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroup[].class)
                .execute(listener);
    }

    public static void downloadCategory(MainActivity mcontext, long groupId, OkHttpUtils.OnCompleteListener<CategoryChildBean[]> Listener) {
        OkHttpUtils<CategoryChildBean[]> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,groupId+"")
                .targetClass(CategoryChildBean[].class)
                .execute(Listener);
    }

    public static void downloadCategory_details_child(Context mcontext, int carid, int pageid, int pageSizeDefault, OkHttpUtils.OnCompleteListener<NewGoodsBean[]> listener) {
        OkHttpUtils<NewGoodsBean[]> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_GOODS_DETAILS)
              .addParam(I.GoodsDetails.KEY_CAT_ID,carid+"")
              .addParam(I.PAGE_ID,pageid+"")
              .addParam(I.PAGE_SIZE,pageSizeDefault+"")
              .targetClass(NewGoodsBean[].class)
                .execute(listener);



    }

    public static void register(Context mcontext, String username, String nickname, String password, OkHttpUtils.OnCompleteListener<Result> onCompleteListener) {
        OkHttpUtils<Result> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_REGISTER)
                .post()
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.NICK,nickname)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(password))
                .targetClass(Result.class)
                .execute(onCompleteListener);

    }

    public static void loginin(Context mcontext, String username, String password, OkHttpUtils.OnCompleteListener<String> onCompleteListener) {
        OkHttpUtils<String> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,username)
                .addParam(I.User.PASSWORD, password)
                .targetClass(String.class)
                .execute(onCompleteListener);
    }

    public static void updateNickname(Context context,String muserName, String muserNick,OkHttpUtils.OnCompleteListener<Result> listener) {
        OkHttpUtils<Result> utils=new OkHttpUtils<>(context);
        utils.url(I.SERVER_ROOT+I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,muserName)
                .addParam(I.User.NICK,muserNick)
                .targetClass(Result.class)
                .execute(listener);

    }

    public static void updateAvatar(Context context, String muserName, File file, OkHttpUtils.OnCompleteListener<String> onCompleteListener) {
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.url(I.SERVER_ROOT+I.REQUEST_UPDATE_AVATAR)
             .addParam(I.NAME_OR_HXID,muserName)
             .addParam(I.AVATAR_TYPE,"user_avatar")
             .addFile2(file)
             .post()
            .targetClass(String.class)
            .execute(onCompleteListener);
   }

    public static void findCollectCount(Context context,String muserName, OkHttpUtils.OnCompleteListener<MessageBean> onCompleteListener) {
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(context);;
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Collect.USER_NAME,muserName)
                .targetClass(MessageBean.class)
                .execute(onCompleteListener);
    }

    public static void downloadcollections(Mycollections mcontext,String muserName, int mPage_id, OkHttpUtils.OnCompleteListener<CollectBean[]> onCompleteListener) {
        OkHttpUtils<CollectBean[]> utils=new OkHttpUtils<>(mcontext);;
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_COLLECTS)
                .addParam(I.Collect.USER_NAME,muserName)
                .addParam(I.PAGE_ID,mPage_id+"")
                .addParam(I.PAGE_SIZE,I.PAGE_SIZE_DEFAULT+"")
                .targetClass(CollectBean[].class)
                .execute(onCompleteListener);
    }

    public static void deletecollections(Context mcontext, int goodsid, String username,OkHttpUtils.OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(mcontext);;
        utils.url(I.SERVER_ROOT+I.REQUEST_DELETE_COLLECT)
                .addParam(I.Goods.KEY_GOODS_ID,goodsid+"")
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(listener);

    }

    public static void addCollections(Context mcontext, int mgoodsid, String username, OkHttpUtils.OnCompleteListener<MessageBean> onCompleteListener) {
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(mcontext);;
        utils.url(I.SERVER_ROOT+I.REQUEST_ADD_COLLECT)
                .addParam(I.Goods.KEY_GOODS_ID,mgoodsid+"")
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(onCompleteListener);
    }

    public static void isCollected(Context context, int mgoodsid, String username, OkHttpUtils.OnCompleteListener<MessageBean> onCompleteListener) {
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(context);;
        utils.url(I.SERVER_ROOT+I.REQUEST_IS_COLLECT)
                .addParam(I.Goods.KEY_GOODS_ID,mgoodsid+"")
                .addParam(I.Collect.USER_NAME,username)
                .targetClass(MessageBean.class)
                .execute(onCompleteListener);

    }

    public static void findcartGoods(MainActivity context, String username, OkHttpUtils.OnCompleteListener<CartBean[]> onCompleteListener) {
    OkHttpUtils<CartBean[]> utils=new OkHttpUtils<>(context);
        utils.url(I.SERVER_ROOT+I.REQUEST_FIND_CARTS)
              .addParam(I.Cart.USER_NAME,username)
              .targetClass(CartBean[].class)
                .execute(onCompleteListener);


    }

    public static void deletegoods(Context mcontext, int goodsid, OkHttpUtils.OnCompleteListener<MessageBean> onCompleteListener) {
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID,goodsid+"")
                .targetClass(MessageBean.class)
                .execute(onCompleteListener);

    }

    public static void updateCartscount(Context mcontext, int id,int count, boolean ischecked, OkHttpUtils.OnCompleteListener<MessageBean> onCompleteListener) {
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,id+"")
                .addParam(I.Cart.COUNT,count+"")
                .addParam(I.Cart.IS_CHECKED,ischecked+"")
                .targetClass(MessageBean.class)
                .execute(onCompleteListener);
    }

    public static void addCart(Context mcontext, int mgoodsid, String username, int i, boolean b, OkHttpUtils.OnCompleteListener<MessageBean> onCompleteListener) {
        OkHttpUtils<MessageBean> utils=new OkHttpUtils<>(mcontext);
        utils.url(I.SERVER_ROOT+I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID,mgoodsid+"")
                .addParam(I.Cart.USER_NAME,username+"")
                .addParam(I.Cart.COUNT,i+"")
                .addParam(I.Cart.IS_CHECKED,b+"")
                .targetClass(MessageBean.class)
                .execute(onCompleteListener);

    }
}
