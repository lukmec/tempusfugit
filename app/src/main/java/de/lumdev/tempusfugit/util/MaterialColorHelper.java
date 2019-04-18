package de.lumdev.tempusfugit.util;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.ColorInt;
import androidx.room.ColumnInfo;

public class MaterialColorHelper {

    private static SparseArray<String> color_textColor_Map = fillSparseArray();
    public final static int FLAG_BLACK = 0;
    public final static int FLAG_WHITE = 1;
    public final static int RETURN_FLAG = 0;
    public final static int RETURN_COLOR = 1;
    public final static int COLOR_BLACK = -16777216;
    public final static int COLOR_WHITE = -1;

    public MaterialColorHelper(){
    }

    public static String getTextColor(int color){
        return color_textColor_Map.get(color);
    }
    public static int getTextColor(int color, int returnType){
        if (color_textColor_Map.get(color).equals("Black")){
            if (returnType == RETURN_FLAG) return FLAG_BLACK;
            if (returnType == RETURN_COLOR){
                return changeColorBrightness(color, 0.2f);
            }
        }
        if (color_textColor_Map.get(color).equals("White")){
            if (returnType == RETURN_FLAG) return FLAG_WHITE;
            if (returnType == RETURN_COLOR){
                return changeColorBrightness(changeColorSaturation(color, 0.05f), 10.0f);
            }
        }
        //when color is not availbale in list, always return balck as default
        if (returnType == RETURN_FLAG){
            return FLAG_BLACK;
        }else{
            return changeColorBrightness(COLOR_BLACK, 0.9f);
        }
    }

    private static SparseArray fillSparseArray(){
        color_textColor_Map = new SparseArray<>();
        color_textColor_Map.append(-5138, "Black");
        color_textColor_Map.append(-12846, "Black");
        color_textColor_Map.append(-1074534, "Black");
        color_textColor_Map.append(-1739917, "Black");
        color_textColor_Map.append(-1092784, "White");
        color_textColor_Map.append(-769226, "White");
        color_textColor_Map.append(-1754827, "White");
        color_textColor_Map.append(-2937041, "White");
        color_textColor_Map.append(-3790808, "White");
        color_textColor_Map.append(-4776932, "White");
        color_textColor_Map.append(-30080, "Black");
        color_textColor_Map.append(-44462, "White");
        color_textColor_Map.append(-59580, "White");
        color_textColor_Map.append(-2818048, "White");
        color_textColor_Map.append(-203540, "Black");
        color_textColor_Map.append(-476208, "Black");
        color_textColor_Map.append(-749647, "Black");
        color_textColor_Map.append(-1023342, "White");
        color_textColor_Map.append(-1294214, "White");
        color_textColor_Map.append(-1499549, "White");
        color_textColor_Map.append(-2614432, "White");
        color_textColor_Map.append(-4056997, "White");
        color_textColor_Map.append(-5434281, "White");
        color_textColor_Map.append(-7860657, "White");
        color_textColor_Map.append(-32597, "Black");
        color_textColor_Map.append(-49023, "White");
        color_textColor_Map.append(-720809, "White");
        color_textColor_Map.append(-3862174, "White");
        color_textColor_Map.append(-793099, "Black");
        color_textColor_Map.append(-1982745, "Black");
        color_textColor_Map.append(-3238952, "Black");
        color_textColor_Map.append(-4560696, "White");
        color_textColor_Map.append(-5552196, "White");
        color_textColor_Map.append(-6543440, "White");
        color_textColor_Map.append(-7461718, "White");
        color_textColor_Map.append(-8708190, "White");
        color_textColor_Map.append(-9823334, "White");
        color_textColor_Map.append(-11922292, "White");
        color_textColor_Map.append(-1408772, "Black");
        color_textColor_Map.append(-2080517, "White");
        color_textColor_Map.append(-2817799, "White");
        color_textColor_Map.append(-5635841, "White");
        color_textColor_Map.append(-1185802, "Black");
        color_textColor_Map.append(-3029783, "Black");
        color_textColor_Map.append(-5005861, "Black");
        color_textColor_Map.append(-6982195, "White");
        color_textColor_Map.append(-8497214, "White");
        color_textColor_Map.append(-10011977, "White");
        color_textColor_Map.append(-10603087, "White");
        color_textColor_Map.append(-11457112, "White");
        color_textColor_Map.append(-12245088, "White");
        color_textColor_Map.append(-13558894, "White");
        color_textColor_Map.append(-5011201, "Black");
        color_textColor_Map.append(-8630785, "White");
        color_textColor_Map.append(-10149889, "White");
        color_textColor_Map.append(-10354454, "White");
        color_textColor_Map.append(-1512714, "Black");
        color_textColor_Map.append(-3814679, "Black");
        color_textColor_Map.append(-6313766, "Black");
        color_textColor_Map.append(-8812853, "White");
        color_textColor_Map.append(-10720320, "White");
        color_textColor_Map.append(-12627531, "White");
        color_textColor_Map.append(-13022805, "White");
        color_textColor_Map.append(-13615201, "White");
        color_textColor_Map.append(-14142061, "White");
        color_textColor_Map.append(-15064194, "White");
        color_textColor_Map.append(-7561473, "Black");
        color_textColor_Map.append(-11309570, "White");
        color_textColor_Map.append(-12756226, "White");
        color_textColor_Map.append(-13611010, "White");
        color_textColor_Map.append(-1838339, "Black");
        color_textColor_Map.append(-4464901, "Black");
        color_textColor_Map.append(-7288071, "Black");
        color_textColor_Map.append(-10177034, "Black");
        color_textColor_Map.append(-12409355, "Black");
        color_textColor_Map.append(-14575885, "White");
        color_textColor_Map.append(-14776091, "White");
        color_textColor_Map.append(-15108398, "White");
        color_textColor_Map.append(-15374912, "White");
        color_textColor_Map.append(-15906911, "White");
        color_textColor_Map.append(-8211969, "Black");
        color_textColor_Map.append(-12285185, "White");
        color_textColor_Map.append(-14059009, "White");
        color_textColor_Map.append(-14064897, "White");
        color_textColor_Map.append(-1968642, "Black");
        color_textColor_Map.append(-4987396, "Black");
        color_textColor_Map.append(-8268550, "Black");
        color_textColor_Map.append(-11549705, "Black");
        color_textColor_Map.append(-14043402, "Black");
        color_textColor_Map.append(-16537100, "Black");
        color_textColor_Map.append(-16540699, "White");
        color_textColor_Map.append(-16611119, "White");
        color_textColor_Map.append(-16615491, "White");
        color_textColor_Map.append(-16689253, "White");
        color_textColor_Map.append(-8333057, "Black");
        color_textColor_Map.append(-12532481, "Black");
        color_textColor_Map.append(-16731905, "Black");
        color_textColor_Map.append(-16739862, "White");
        color_textColor_Map.append(-2033670, "Black");
        color_textColor_Map.append(-5051406, "Black");
        color_textColor_Map.append(-8331542, "Black");
        color_textColor_Map.append(-11677471, "Black");
        color_textColor_Map.append(-14235942, "Black");
        color_textColor_Map.append(-16728876, "Black");
        color_textColor_Map.append(-16732991, "Black");
        color_textColor_Map.append(-16738393, "White");
        color_textColor_Map.append(-16743537, "White");
        color_textColor_Map.append(-16752540, "White");
        color_textColor_Map.append(-8060929, "Black");
        color_textColor_Map.append(-15138817, "Black");
        color_textColor_Map.append(-16718337, "Black");
        color_textColor_Map.append(-16729900, "Black");
        color_textColor_Map.append(-2034959, "Black");
        color_textColor_Map.append(-5054501, "Black");
        color_textColor_Map.append(-8336444, "Black");
        color_textColor_Map.append(-11684180, "Black");
        color_textColor_Map.append(-14244198, "Black");
        color_textColor_Map.append(-16738680, "White");
        color_textColor_Map.append(-16742021, "White");
        color_textColor_Map.append(-16746133, "White");
        color_textColor_Map.append(-16750244, "White");
        color_textColor_Map.append(-16757440, "White");
        color_textColor_Map.append(-5767189, "Black");
        color_textColor_Map.append(-10158118, "Black");
        color_textColor_Map.append(-14816842, "Black");
        color_textColor_Map.append(-16728155, "Black");
        color_textColor_Map.append(-1509911, "Black");
        color_textColor_Map.append(-3610935, "Black");
        color_textColor_Map.append(-5908825, "Black");
        color_textColor_Map.append(-8271996, "Black");
        color_textColor_Map.append(-10044566, "Black");
        color_textColor_Map.append(-11751600, "Black");
        color_textColor_Map.append(-12345273, "White");
        color_textColor_Map.append(-13070788, "White");
        color_textColor_Map.append(-13730510, "White");
        color_textColor_Map.append(-14983648, "White");
        color_textColor_Map.append(-4589878, "Black");
        color_textColor_Map.append(-9834322, "Black");
        color_textColor_Map.append(-16718218, "Black");
        color_textColor_Map.append(-16725933, "Black");
        color_textColor_Map.append(-919319, "Black");
        color_textColor_Map.append(-2298424, "Black");
        color_textColor_Map.append(-3808859, "Black");
        color_textColor_Map.append(-5319295, "Black");
        color_textColor_Map.append(-6501275, "Black");
        color_textColor_Map.append(-7617718, "Black");
        color_textColor_Map.append(-8604862, "Black");
        color_textColor_Map.append(-9920712, "White");
        color_textColor_Map.append(-11171025, "White");
        color_textColor_Map.append(-13407970, "White");
        color_textColor_Map.append(-3342448, "Black");
        color_textColor_Map.append(-5046439, "Black");
        color_textColor_Map.append(-8978685, "Black");
        color_textColor_Map.append(-10167017, "Black");
        color_textColor_Map.append(-394265, "Black");
        color_textColor_Map.append(-985917, "Black");
        color_textColor_Map.append(-1642852, "Black");
        color_textColor_Map.append(-2300043, "Black");
        color_textColor_Map.append(-2825897, "Black");
        color_textColor_Map.append(-3285959, "Black");
        color_textColor_Map.append(-4142541, "Black");
        color_textColor_Map.append(-5262293, "Black");
        color_textColor_Map.append(-6382300, "Black");
        color_textColor_Map.append(-8227049, "White");
        color_textColor_Map.append(-721023, "Black");
        color_textColor_Map.append(-1114303, "Black");
        color_textColor_Map.append(-3735808, "Black");
        color_textColor_Map.append(-5314048, "Black");
        color_textColor_Map.append(-537, "Black");
        color_textColor_Map.append(-1596, "Black");
        color_textColor_Map.append(-2659, "Black");
        color_textColor_Map.append(-3722, "Black");
        color_textColor_Map.append(-4520, "Black");
        color_textColor_Map.append(-5317, "Black");
        color_textColor_Map.append(-141259, "Black");
        color_textColor_Map.append(-278483, "Black");
        color_textColor_Map.append(-415707, "Black");
        color_textColor_Map.append(-688361, "Black");
        color_textColor_Map.append(-115, "Black");
        color_textColor_Map.append(-256, "Black");
        color_textColor_Map.append(-5632, "Black");
        color_textColor_Map.append(-10752, "Black");
        color_textColor_Map.append(-1823, "Black");
        color_textColor_Map.append(-4941, "Black");
        color_textColor_Map.append(-8062, "Black");
        color_textColor_Map.append(-10929, "Black");
        color_textColor_Map.append(-13784, "Black");
        color_textColor_Map.append(-16121, "Black");
        color_textColor_Map.append(-19712, "Black");
        color_textColor_Map.append(-24576, "Black");
        color_textColor_Map.append(-28928, "Black");
        color_textColor_Map.append(-37120, "Black");
        color_textColor_Map.append(-6785, "Black");
        color_textColor_Map.append(-10432, "Black");
        color_textColor_Map.append(-15360, "Black");
        color_textColor_Map.append(-21760, "Black");
        color_textColor_Map.append(-3104, "Black");
        color_textColor_Map.append(-8014, "Black");
        color_textColor_Map.append(-13184, "Black");
        color_textColor_Map.append(-18611, "Black");
        color_textColor_Map.append(-22746, "Black");
        color_textColor_Map.append(-26624, "Black");
        color_textColor_Map.append(-291840, "Black");
        color_textColor_Map.append(-689152, "Black");
        color_textColor_Map.append(-1086464, "White");
        color_textColor_Map.append(-1683200, "White");
        color_textColor_Map.append(-11904, "Black");
        color_textColor_Map.append(-21696, "Black");
        color_textColor_Map.append(-28416, "Black");
        color_textColor_Map.append(-37632, "Black");
        color_textColor_Map.append(-267801, "Black");
        color_textColor_Map.append(-13124, "Black");
        color_textColor_Map.append(-21615, "Black");
        color_textColor_Map.append(-30107, "Black");
        color_textColor_Map.append(-36797, "Black");
        color_textColor_Map.append(-43230, "White");
        color_textColor_Map.append(-765666, "White");
        color_textColor_Map.append(-1684967, "White");
        color_textColor_Map.append(-2604267, "White");
        color_textColor_Map.append(-4246004, "White");
        color_textColor_Map.append(-24960, "Black");
        color_textColor_Map.append(-37312, "Black");
        color_textColor_Map.append(-49920, "White");
        color_textColor_Map.append(-2282496, "White");
        color_textColor_Map.append(-1053719, "Black");
        color_textColor_Map.append(-2634552, "Black");
        color_textColor_Map.append(-4412764, "Black");
        color_textColor_Map.append(-6190977, "White");
        color_textColor_Map.append(-7508381, "White");
        color_textColor_Map.append(-8825528, "White");
        color_textColor_Map.append(-9614271, "White");
        color_textColor_Map.append(-10665929, "White");
        color_textColor_Map.append(-11652050, "White");
        color_textColor_Map.append(-12703965, "White");
        color_textColor_Map.append(-328966, "Black");
        color_textColor_Map.append(-657931, "Black");
        color_textColor_Map.append(-1118482, "Black");
        color_textColor_Map.append(-2039584, "Black");
        color_textColor_Map.append(-4342339, "Black");
        color_textColor_Map.append(-6381922, "Black");
        color_textColor_Map.append(-9079435, "White");
        color_textColor_Map.append(-10395295, "White");
        color_textColor_Map.append(-12434878, "White");
        color_textColor_Map.append(-14606047, "White");
        color_textColor_Map.append(-1249295, "Black");
        color_textColor_Map.append(-3155748, "Black");
        color_textColor_Map.append(-5194043, "Black");
        color_textColor_Map.append(-7297874, "Black");
        color_textColor_Map.append(-8875876, "White");
        color_textColor_Map.append(-10453621, "White");
        color_textColor_Map.append(-11243910, "White");
        color_textColor_Map.append(-12232092, "White");
        color_textColor_Map.append(-13154481, "White");
        color_textColor_Map.append(-14273992, "White");
        color_textColor_Map.append(-16777216, "White");
        color_textColor_Map.append(-1, "Black");
        return color_textColor_Map;
    }

    // Darken a Color with a factor smaller 1f (e.g. 0.8f)
    // Lighten a Color with a factor greater 1f (e.g. 1.2f)
    @ColorInt
    public static int changeColorBrightness(@ColorInt int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }
    // Decrease Saturation of a Color with a factor smaller 1f (e.g. 0.8f)
    // Increase Saturation of a Color with a factor greater 1f (e.g. 1.2f)
    @ColorInt
    public static int changeColorSaturation(@ColorInt int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] *= factor;
        return Color.HSVToColor(hsv);
    }
    //Invert a given color
    //Source: https://stackoverflow.com/questions/4672271/reverse-opposing-colors
    @ColorInt
    public static int getComplementaryColor(@ColorInt int colorToInvert) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(colorToInvert), Color.green(colorToInvert),
                Color.blue(colorToInvert), hsv);
        hsv[0] = (hsv[0] + 180) % 360;
        return Color.HSVToColor(hsv);
    }
    @ColorInt
    public static int getContrastVersionForColor(@ColorInt int color) {
        float[] hsv = new float[3];
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color),
                hsv);
        if (hsv[2] < 0.5) {
            hsv[2] = 0.7f;
        } else {
            hsv[2] = 0.3f;
        }
        hsv[1] = hsv[1] * 0.2f;
        return Color.HSVToColor(hsv);
    }
}
