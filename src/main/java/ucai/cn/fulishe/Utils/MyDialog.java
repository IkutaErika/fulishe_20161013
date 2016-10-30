package ucai.cn.fulishe.Utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Binder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ucai.cn.fulishe.Dao.NetDao;
import ucai.cn.fulishe.Dao.UserDao;
import ucai.cn.fulishe.R;
import ucai.cn.fulishe.activity.FuliCenterApplication;
import ucai.cn.fulishe.bean.Result;
import ucai.cn.fulishe.bean.UserBean;

/**
 * Created by Administrator on 2016/10/25.
 */
public class MyDialog extends Dialog {
    @Bind(R.id.ed_dialog)
    EditText edDialog;
    @Bind(R.id.positiveButton)
    Button sureDialog;
    @Bind(R.id.negativeButton)
    Button cancelDialog;
    private TextView title;
    public MyDialog(Context context) {
        super(context);

        setMyDialog();
    }
    private void setMyDialog() {
        View myview = LayoutInflater.from(getContext()).inflate(R.layout.normal_dialog, null);
        ButterKnife.bind(this,myview);
        super.setContentView(myview);
    }
    public EditText getEdDialog() {
        return edDialog;
    }

    public void setEdDialog(EditText edDialog) {
        this.edDialog = edDialog;
    }

   public void SetOnPositiveClickListener(View.OnClickListener listener){
    sureDialog.setOnClickListener(listener);
   }
   public void SetOnNegativeClickListener(View.OnClickListener listener){
    cancelDialog.setOnClickListener(listener);
   }

    @Override
    protected void onStop() {
        ButterKnife.unbind(this);
        super.onStop();
    }
}
