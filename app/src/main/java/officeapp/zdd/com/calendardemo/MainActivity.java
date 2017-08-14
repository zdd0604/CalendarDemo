package officeapp.zdd.com.calendardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NewCalendar.NewCalendarLinstener {
    @BindView(R.id.newCalendarView)
    NewCalendar newCalendarView;
    int nmb = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        newCalendarView.linstener = this;
    }

    @Override
    public void onItemPress(Date day) {
        DateFormat dft = SimpleDateFormat.getDateInstance();
        Toast.makeText(this, dft.format(day), Toast.LENGTH_SHORT).show();
    }
}
