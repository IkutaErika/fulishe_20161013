package ucai.cn.fulishe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PaymentHandler;
import com.pingplusplus.libone.PingppOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.R;

public class Bought extends AppCompatActivity implements PaymentHandler {

    @Bind(R.id.iv_top_menu_back)
    ImageView ivTopMenuBack;
    @Bind(R.id.et_bought_man)
    EditText etBoughtMan;
    @Bind(R.id.et_bought_number)
    EditText etBoughtNumber;
    @Bind(R.id.spinner_where)
    Spinner spinnerWhere;
    @Bind(R.id.et_bought_street)
    EditText etBoughtStreet;
    @Bind(R.id.btn_cost)
    Button btnCost;
    private static String URL = "http://218.244.151.190/demo/charge";
    int price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bought);
        ButterKnife.bind(this);
        initview();



    }

    private void initview() {
        price=getIntent().getIntExtra("price",0);
        PingppOne.enableChannels(new String[]{"wx", "alipay", "upacp", "bfb", "jdpay_wap"});
        // 提交数据的格式，默认格式为json
        // PingppOne.CONTENT_TYPE = "application/x-www-form-urlencoded";
        PingppOne.CONTENT_TYPE = "application/json";

        // 产生个订单号
        PingppLog.DEBUG = true;
    }

    private void initdata() {
        String orderNo = new SimpleDateFormat("yyyyMMddhhmmss")
                .format(new Date());
        JSONObject bill = new JSONObject();
        // 自定义的额外信息 选填
        JSONObject extras = new JSONObject();
        try {
            extras.put("extra1", "extra1");
            extras.put("extra2", "extra2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bill.put("order_no", orderNo);
            bill.put("amount", price*100);
            bill.put("extras", extras);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //壹收款: 创建支付通道的对话框
        PingppOne.showPaymentChannels(getSupportFragmentManager(), bill.toString(), URL, this);
}

    @OnClick({R.id.iv_top_menu_back, R.id.btn_cost})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_top_menu_back:
                break;
            case R.id.btn_cost:
                if (price==0){
                    finish();
                }
                if (etBoughtMan.equals("")||etBoughtMan==null)
                {
                    etBoughtMan.setError("不能为空！");
                    etBoughtMan.requestFocus();
                }
                if (etBoughtNumber.equals("")||etBoughtNumber==null)
                {
                    etBoughtNumber.setError("不能为空！");
                    etBoughtNumber.requestFocus();
                }
                if (etBoughtStreet.equals("")||etBoughtStreet==null)
                {
                    etBoughtStreet.setError("不能为空！");
                    etBoughtStreet.requestFocus();
                }
                initdata();
                break;
        }
    }

    @Override
    public void handlePaymentResult(Intent data) {
        if (data != null) {
            /**
             * code：支付结果码  -2:服务端错误、 -1：失败、 0：取消、1：成功
             * error_msg：支付结果信息
             */
            int code = data.getExtras().getInt("code");
            String errorMsg = data.getExtras().getString("error_msg");
        }
    }
}

