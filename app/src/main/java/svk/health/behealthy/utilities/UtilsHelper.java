package svk.health.behealthy.utilities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UtilsHelper {

    public TextView createTitle(TextView textView, String title, Float textSize, int top, int bottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, top, 10, bottom);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(params);
        textView.setText(title);

        return textView;
    }

}
