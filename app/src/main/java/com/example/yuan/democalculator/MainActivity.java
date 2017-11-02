package com.example.yuan.democalculator;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView textView;


    private int[] Id = {
            R.id.btn_clear,R.id.btn_delete,R.id.btn_divide,R.id.btn_multiply,
            R.id.btn_seven,R.id.btn_eight,R.id.btn_nine,R.id.btn_subtract,
            R.id.btn_four,R.id.btn_five,R.id.btn_six,R.id.btn_add,
            R.id.btn_one,R.id.btn_two,R.id.btn_three,R.id.btn_equal,
            R.id.btn_zero,R.id.btn_point
    };

    private Button btn[] = new Button[Id.length];


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /**
         * 修改状态栏
         */
        if (Build.VERSION.SDK_INT >= 21)
        {
            //调用getWindow().getDecorView()拿到DecorView
            View decorView = getWindow().getDecorView();
            //调用setSystemUiVisibility()方法改变系统UI显示
            //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE表示活动的布局会显示在状态栏上面
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            //调用setStatusBarColor()方法将状态栏设置成透明色
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_main);


        //初始化
        init();







    }

    private void init()
    {
        for (int i = 0; i < Id.length; i++)
        {
            btn[i] = (Button) findViewById(Id[i]);
            btn[i].setOnClickListener(this);
        }

        textView = (TextView) findViewById(R.id.text_show);

    }

    @Override
    public void onClick(View v)
    {
        // 从按钮开关中获取文本
        Button b = (Button)v;
        String buttonText = b.getText().toString();
        //显示框的内容
        String oldStr = textView.getText().toString();
        switch (v.getId())
        {
            //清空键
            case R.id.btn_clear:
                textView.setText("0");
                break;
            //退格键
            case R.id.btn_delete:
                try
                {
                    if (!oldStr.equals("0") && oldStr.length() > 1)
                    {
                        oldStr = oldStr.substring(0, oldStr.length() - 1);
                        textView.setText(oldStr);
                    } else if (oldStr.length() == 1)
                    {
                        textView.setText("0");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            //数字按钮
            case R.id.btn_zero:
            case R.id.btn_one:
            case R.id.btn_two:
            case R.id.btn_three:
            case R.id.btn_four:
            case R.id.btn_five:
            case R.id.btn_six:
            case R.id.btn_seven:
            case R.id.btn_eight:
            case R.id.btn_nine:
                if (oldStr.equals("0"))
                {

                    textView.setText(buttonText);
                }
                else
                {
                    textView.setText(oldStr + buttonText);
                }
                break;
            //加减乘除
            case R.id.btn_add:
            case R.id.btn_subtract:
            case R.id.btn_multiply:
            case R.id.btn_divide:
                String textLast = oldStr.substring(oldStr.length() - 1, oldStr.length());
                //如果最后一个是运算符
                if (textLast.equals("+") || textLast.equals("-") || textLast.equals("×") || textLast.equals("÷"))
                {
                    oldStr = oldStr.substring(0, oldStr.length() - 1);
                }
                textView.setText(oldStr + buttonText);
                break;
            //小数点
            case R.id.btn_point:
                if (oldStr.equals("0"))
                {
                    textView.setText("0.");
                }
                else
                {
                    boolean ok = true;
                    //保证小数点只能同时出现一次
                    for (int i = 0; i < oldStr.length(); i++)
                    {
                        if (oldStr.charAt(i)=='+' || oldStr.charAt(i)=='-' || oldStr.charAt(i)=='×' || oldStr.charAt(i)=='÷')
                        {
                            ok = true;
                        }
                        else if (oldStr.charAt(i) == '.')
                        {
                            ok = false;
                        }
                    }

                    if (ok)
                    {
                        textView.setText(oldStr + ".");
                    }
                }
                break;
            //等于
            case R.id.btn_equal:
                try
                {
                    //计算结果
                    double ans = Calc.calcBracket(oldStr);
                    //去掉小数点后面多余的0
                    DecimalFormat decimalFormat = new DecimalFormat("###################.###########");

                    textView.setText(decimalFormat.format(ans));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    //记录用户首次点击返回键的时间
    private long firstTime=0;

    /**
     * 当某个按键被按下，松开后触发，但不会被任何的该Activity内的任何view处理。
     * 默认没有执行任何操作，只是简单的给一个false作为返回值。
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                long secondTime=System.currentTimeMillis();
                if(secondTime-firstTime>2000)
                {
                    Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                    firstTime=secondTime;
                    return true;
                }
                else
                {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
