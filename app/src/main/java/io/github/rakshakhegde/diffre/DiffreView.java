package io.github.rakshakhegde.diffre;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Works with any background
 * <p>
 * Heavily inspired from Romain Guy's Medium article on "How to underline text":
 * https://medium.com/google-developers/a-better-underline-for-android-90ba3a2e4fb#.hnv0zcm2h
 * <p>
 */
public class DiffreView extends View {

	private final RectF rectF = new RectF();

	private int width;
	private int height;
	private float percent = 0.1F;

	private final Path strokePath = new Path();
    private final Path progressPath = new Path();
	private final Path croppedProgressPath = new Path();
	private final Path textPath = new Path();
	private final Path croppedTextPath = new Path();

	private final int COLOR_ORANGE = 0xFFFD9727;
	private final String textString = "16:00 – 16:30";
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final float radius;
	private final int horizontalTextPadding;
	private final int verticalTextPadding;
	private final Rect textBounds = new Rect();


	public DiffreView(Context context) {
		this(context, null);
	}

	public DiffreView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DiffreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		final Resources res = context.getResources();

		final int paintTextSize = res.getDimensionPixelSize(R.dimen.paintTextSize);
		paint.setTextSize(paintTextSize);

		horizontalTextPadding = res.getDimensionPixelSize(R.dimen.paintHorizontalTextPadding);
		verticalTextPadding = res.getDimensionPixelSize(R.dimen.paintVerticalTextPadding);

		radius = res.getDimensionPixelSize(R.dimen.paintRadius);

		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

		final float strokeWidth = res.getDimensionPixelSize(R.dimen.paintStrokeWidth);
		paint.setStrokeWidth(strokeWidth);

		paint.setColor(COLOR_ORANGE);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		paint.getTextBounds(textString, 0, textString.length(), textBounds);

		final int textWidth = textBounds.width();
		final int textHeight = textBounds.height();

		width = textWidth + horizontalTextPadding;
		height = textHeight + verticalTextPadding;

		final int midWidth = width / 2;
		final int baseline = (height + textHeight) / 2;

		paint.getTextPath(textString, 0, textString.length(), midWidth, baseline, textPath);

		setRoundRectPath(strokePath, 0, 0, width, height, radius);

		computePaths();

		// Adding 1 to prevent artifacts
		setMeasuredDimension(width + 1, height + 1);
	}

	private void computePaths() {
		setRectPath(progressPath, 0, 0, width * percent, height);

		croppedProgressPath.set(progressPath);
		croppedProgressPath.op(textPath, Path.Op.DIFFERENCE);
		croppedProgressPath.op(strokePath, Path.Op.INTERSECT);

		croppedTextPath.op(textPath, progressPath, Path.Op.DIFFERENCE);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(strokePath, paint);

		paint.setStyle(Paint.Style.FILL);
		canvas.drawPath(croppedProgressPath, paint);
		canvas.drawPath(croppedTextPath, paint);
	}

	public void setProgress(@FloatRange(from = 0, to = 1) final float _percent) {
		percent = _percent;
		computePaths();
		invalidate();
	}

	private void setRectPath(Path path, float left, float top, float right, float bottom) {
		rectF.set(left, top, right, bottom);
		path.rewind();
		path.addRect(rectF, Path.Direction.CW);
	}

	private void setRoundRectPath(Path path, float left, float top, float right, float bottom, float radius) {
		rectF.set(left, top, right, bottom);
		path.rewind();
		path.addRoundRect(rectF, radius, radius, Path.Direction.CW);
	}
}
