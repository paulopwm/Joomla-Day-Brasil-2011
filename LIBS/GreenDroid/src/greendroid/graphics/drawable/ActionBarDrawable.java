/*
 * Copyright (C) 2011 Cyril Mottier (http://www.cyrilmottier.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package greendroid.graphics.drawable;
import com.cyrilmottier.android.greendroid.R;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarItem;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import android.util.TypedValue;

/**
 * A specialized {@link Drawable} that is dedicated to {@link ActionBarItem}s.
 * It automatically adapts its color depending on its current state (black when
 * pressed or focused and white otherwise). As a result, the
 * {@link AutoColorDrawable} is a replacement {@link StateListDrawable} that
 * should be used in {@link ActionBar}s.
 * 
 * @author Cyril Mottier
 */
public class ActionBarDrawable extends BitmapDrawable {

    private static final TypedValue sTypedValue = new TypedValue();

    private ColorFilter mNormalCf;
    private ColorFilter mAltCf;

    @Deprecated
    public ActionBarDrawable(Resources res, int resId) {
        this(res, res.getDrawable(resId));
    }

    @Deprecated
    public ActionBarDrawable(Resources res, Drawable d) {
        this(res, d, Color.WHITE, Color.BLACK);
    }

    @Deprecated
    public ActionBarDrawable(Resources res, int resId, int normalColor, int altColor) {
        this(res, res.getDrawable(resId), normalColor, altColor);
    }

    @Deprecated
    public ActionBarDrawable(Resources res, Drawable d, int normalColor, int altColor) {
        super(res, (d instanceof BitmapDrawable) ? ((BitmapDrawable) d).getBitmap() : null);
        mNormalCf = new LightingColorFilter(Color.BLACK, normalColor);
        mAltCf = new LightingColorFilter(Color.BLACK, altColor);
    }

    public ActionBarDrawable(Context context, int resId) {
        this(context, context.getResources().getDrawable(resId));
    }

    public ActionBarDrawable(Context context, Drawable d) {
        this(context, d, getColorFromTheme(context, R.attr.gdActionBarItemColorNormal, Color.WHITE),
                getColorFromTheme(context, R.attr.gdActionBarItemColorAlt, Color.BLACK));
    }

    public ActionBarDrawable(Context context, int resId, int normalColor, int altColor) {
        this(context, context.getResources().getDrawable(resId), normalColor, altColor);
    }

    public ActionBarDrawable(Context context, Drawable d, int normalColor, int altColor) {
        super(context.getResources(), (d instanceof BitmapDrawable) ? ((BitmapDrawable) d).getBitmap() : null);
        mNormalCf = new LightingColorFilter(Color.BLACK, normalColor);
        mAltCf = new LightingColorFilter(Color.BLACK, altColor);
    }

    private static int getColorFromTheme(Context context, int attr, int defaultColor) {
        synchronized (sTypedValue) {
            final TypedValue value = sTypedValue;
            final Theme theme = context.getTheme();
            if (theme != null) {
                theme.resolveAttribute(attr, value, true);
                if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT) {
                    return value.data;
                }
            }

            return defaultColor;
        }
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        final boolean useAlt = StateSet.stateSetMatches(DrawableStateSet.ENABLED_PRESSED_STATE_SET, stateSet)
                || StateSet.stateSetMatches(DrawableStateSet.ENABLED_FOCUSED_STATE_SET, stateSet);
        setColorFilter(useAlt ? mAltCf : mNormalCf);
        return true;
    }
}
