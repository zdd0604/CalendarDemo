package officeapp.zdd.com.calendardemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 2017/7/25.
 */

public class NewCalendar extends LinearLayout {
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView textDate;
    private GridView grid;

    private Calendar curDate = Calendar.getInstance();
    private String displayFormat;
    public NewCalendarLinstener linstener;


    public NewCalendar(Context context) {
        super(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * 业务逻辑部分
     *
     * @param context
     */
    private void initControl(Context context, AttributeSet attrs) {
        bindControl(context);
        bindControlEvent();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewCalendar);
        try {
            String format = typedArray.getString(R.styleable.NewCalendar_dateFormat);
            displayFormat = format;
            if (displayFormat == null)
                displayFormat = "MMM yyy";
        } finally {
            typedArray.recycle();
        }

        renderCalendar();
    }

    /**
     * 绑定控件的事件
     *
     * @param context
     */
    private void bindControl(Context context) {
        LayoutInflater layout = LayoutInflater.from(context);
        layout.inflate(R.layout.calendar_view, this);

        btnPrev = (ImageView) findViewById(R.id.btnPrev);
        btnNext = (ImageView) findViewById(R.id.btnNext);
        textDate = (TextView) findViewById(R.id.textDate);
        grid = (GridView) findViewById(R.id.calendar_GridView);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (linstener != null)
                    linstener.onItemPress((Date) parent.getItemAtPosition(position));
            }
        });
    }


    /**
     * 绑定点击事件的部分
     */
    private void bindControlEvent() {
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, -1);
                renderCalendar();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate.add(Calendar.MONTH, +1);
                renderCalendar();
            }
        });
    }

    /**
     * 渲染
     */
    private void renderCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat(displayFormat);
        textDate.setText(sdf.format(curDate.getTime()));

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calender = (Calendar) curDate.clone();

        //算出当月之前有多少天
        calender.set(Calendar.DAY_OF_MONTH, 1);
        int precDays = calender.get(Calendar.DAY_OF_WEEK) - 1;
        calender.add(Calendar.DAY_OF_MONTH, -precDays);

        int maxCellCount = 6 * 7;
        for (int i = 0; i < maxCellCount; i++) {
            cells.add(calender.getTime());
            calender.add(Calendar.DAY_OF_MONTH, 1);
        }
        grid.setAdapter(new CalendarAdapter(getContext(), cells));
    }

    private class CalendarAdapter extends ArrayAdapter<Date> {
        LayoutInflater inflater;
        Context context;
        CalenderDayTextView calendarTv;

        public CalendarAdapter(@NonNull Context context,
                               ArrayList<Date> days) {
            super(context, R.layout.calender_text_view, days);
            inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position,
                            @Nullable View convertView,
                            @NonNull ViewGroup parent) {
            Date date = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calender_text_view, parent, false);
            }
            calendarTv = (CalenderDayTextView) convertView.findViewById(R.id.text_calender_day);
            int day = date.getDate();
            calendarTv.setText(String.valueOf(day));

            boolean isTheSameMonth = false;
            Date now = new Date();
            if (date.getMonth() == now.getMonth()) {
                isTheSameMonth = true;
            }
            if (isTheSameMonth) {
                calendarTv.setTextColor(Color.parseColor("#000000"));
            } else {
                calendarTv.setTextColor(Color.parseColor("#666666"));
            }

            if (now.getDate() == date.getDate() &&
                    now.getMonth() == date.getMonth() &&
                    now.getYear() == date.getYear()) {
                calendarTv.setTextColor(context.getResources().getColor(R.color.red));
                calendarTv.isToady = true;
            }
            return convertView;
        }
    }

    /**
     * 日期的点击事件
     */
    public interface NewCalendarLinstener {
        void onItemPress(Date day);
    }
}
