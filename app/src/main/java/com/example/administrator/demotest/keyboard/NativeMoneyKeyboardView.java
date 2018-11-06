//package com.example.administrator.demotest.keyboard;
//
//import android.content.Context;
//import android.support.v4.content.ContextCompat;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.administrator.demotest.R;
//import com.example.administrator.demotest.Utils.Utils;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author zhangheng
// * @date 2018/9/6
// */
//
//public class NativeMoneyKeyboardView extends PopupWindow {
//    private static final String TAG = "NativeMoneyKeyboardView";
//
//    private int[] mRandom = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
//    private LinearLayout linear;
//
//    private Context context_;
//    private LayoutInflater inflater;
//    private Toast mToast;
//    private boolean aboveICS = false; // 系统版本是否高于android 4.0（Ice Cream Sandwich）
//    private Field field;
//    private Object obj;
//    private Method showMethod, hideMethod;
//
//    private OnNumKeyboardChangeListener onKeyboardChangeListener;
//
//    public void setOnKeyboardChangeListener(OnNumKeyboardChangeListener onKeyboardChangeListener) {
//        this.onKeyboardChangeListener = onKeyboardChangeListener;
//    }
//
//    public NativeMoneyKeyboardView(Context context) {
//        super(context);
//        context_ = context;
//        inflater = LayoutInflater.from(context_);
//        mToast = new Toast(context_);
//
//        String os = getDeviceSystemVersion();
//        if (null != os && os.length() >= 1 && Integer.valueOf(os.substring(0, 1)) >= 4) {
//            aboveICS = true;
//        } else {
//            // 低于4.0版本，利用反射技术拿到mTN对象
//            reflectionTN();
//        }
//
//        setOutsideTouchable(true);
//        initView();
//        initKeyboard();
//    }
//
//    private void initView() {
//        View view = inflater.inflate(R.layout.view_money_keyboard, null);
//        this.setContentView(view);
//        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
//        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//        this.setBackgroundDrawable(null);
//
//        ImageView deleteImg = (ImageView) view.findViewById(R.id.gf_delete_btn);
//        deleteImg.setOnClickListener(ImageViewClickListener);
//        deleteImg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getScaledValueX(context_, 92)));
//        TextView okTxt = (TextView) view.findViewById(R.id.gf_ok_btn);
//        okTxt.setOnClickListener(KeyClickListener);
//        okTxt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.getScaledValueX(context_, 92)));
//
//
//        linear = (LinearLayout) view.findViewById(R.id.password_keyboard);
//        linear.setBackgroundColor(ContextCompat.getColor(context_, R.color.gf_keyboard_background));
//    }
//
//    private void initKeyboard() {
//        linear.removeAllViews();
//        initMoneyKeyboard();
//    }
//
//    /**
//     * 4行3列
//     */
//    private void initMoneyKeyboard() {
//        int numCol = 3;
//        int numRow = 4;
//        for (int i = 0; i < numRow; i++) {
//            // 行数
//            LinearLayout linearlayout = new LinearLayout(context_);
//            linearlayout.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            for (int j = 0; j < numCol; j++) {
//                // 列数
//                TextView button = new TextView(context_);
//                ImageView imageView = null;
//                button.setPadding(0, 0, 0, 0);
//                button.setTextColor(ContextCompat.getColor(context_, R.color.gf_keyboard_text));
//                button.setTextSize(22);
//                button.setLayoutParams(new LinearLayout.LayoutParams(0, Utils.getScaledValueX(context_, 45), 1.0f));
//                button.setGravity(Gravity.CENTER);
//                button.setOnClickListener(KeyClickListener);
//                button.setBackgroundResource(R.drawable.gf_btn_style_new);
//                if (i == numRow - 1 && j == 0) {
//                    button.setText(".");
//                    button.setBackgroundResource(R.drawable.gf_btn_style_new);
//                } else if (i == numRow - 1 && j == 1) {// // 0键
//                    if (i * numCol < 10) {
//                        button.setText(String.valueOf(mRandom[i * numCol]));
//                    }
//                } else if (i == numRow - 1 && j == 2) { // 向下箭头
//                    imageView = new ImageView(context_);
//                    imageView.setId(R.id.native_keyboard_hide_button);
//                    imageView.setLayoutParams(new LinearLayout.LayoutParams(0, Utils.getScaledValueX(context_, 45), 1.0f));
//                    imageView.setImageResource(R.drawable.down);
//                    imageView.setBackgroundResource(R.drawable.gf_num_del_style_new);
//                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                    imageView.setOnClickListener(ImageViewClickListener);
//                } else if ((i * numCol + j) < 10) {// 其余按键
//                    button.setText(String.valueOf(mRandom[i * numCol + j]));
//                }
//
//                if (imageView != null) {
//                    linearlayout.addView(imageView);
//                } else {
//                    linearlayout.addView(button);
//                }
//
//                if (j != numCol - 1) {
//                    View divider = new View(context_);
//                    divider.setLayoutParams(new LinearLayout.LayoutParams(
//                            Utils.getScaledValueX(context_, 1), LinearLayout.LayoutParams.MATCH_PARENT));
//                    divider.setBackgroundColor(ContextCompat.getColor(context_, R.color.gf_keyboard_title_background));
//                    linearlayout.addView(divider);
//                }
//            }
//            linear.setPadding(0, 0, 0, 0);
//            linear.addView(linearlayout);
//            View divider = new View(context_);
//            divider.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, Utils.getScaledValueX(context_, 1)));
//            divider.setBackgroundColor(ContextCompat.getColor(context_, R.color.gf_keyboard_title_background));
//            linear.addView(divider);
//        }
//    }
//
//    private View.OnClickListener ImageViewClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            int viewID = view.getId();
//            if (viewID == R.id.gf_delete_btn) {// DEL键
//                if (onKeyboardChangeListener != null) {
//                    onKeyboardChangeListener.onDelete();
//                }
//            } else if (viewID == R.id.native_keyboard_hide_button) {
//                dismiss();
//            }
//        }
//    };
//
//
//    private View.OnClickListener KeyClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            TextView button;
//            if (v instanceof TextView) {
//                button = (TextView) v;
//            } else {
//                button = (Button) v;
//            }
//
//            int viewID = button.getId();
//            if (viewID == R.id.gf_ok_btn) {// 隐藏键盘键
//                dismiss();
//            } else {
//                String realValue = button.getText().toString().trim();
//                Map<String, String> inputMap = new HashMap<String, String>();
//                inputMap.put("psw", realValue);
//
//                if (onKeyboardChangeListener != null) {
//                    onKeyboardChangeListener.onChange(inputMap);
//                }
//            }
//        }
//    };
//
//    /**
//     * 通过反射获取隐藏和显示toast的方法 适用于android 4.0以下系统
//     */
//    private void reflectionTN() {
//        try {
//            field = mToast.getClass().getDeclaredField("mTN");
//            field.setAccessible(true);
//            obj = field.get(mToast);
//            showMethod = obj.getClass().getDeclaredMethod("show4Module");
//            hideMethod = obj.getClass().getDeclaredMethod("hide");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 获取系统版本
//     */
//    public String getDeviceSystemVersion() {
//        String version = android.os.Build.VERSION.RELEASE;
//        if (null != version) {
//            version = version.replace(' ', '-');
//        } else {
//            version = "";
//        }
//        return version;
//    }
//
//    public interface OnNumKeyboardChangeListener {
//        void onChange(Map<String, String> data);
//
//        void onDelete();
//    }
//}
