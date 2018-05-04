package io.github.rakshakhegde.diffre;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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
public abstract class DiffreView extends View {

	int width;
	int height;
	float percent = 0.1F;
	Path progressStrokePath = new Path();

	final Path textPath = new Path();
	final Path croppedProgressPath = new Path();
	final Path croppedTextPath = new Path();


	private static RectF rectF = new RectF();

	private final int COLOR_ORANGE = 0xFFFD9727;
	private final String textString = "16:00 – 16:30";
	private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final float radius;
	private final int textPadding;
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

		textPadding = res.getDimensionPixelSize(R.dimen.paintTextPadding);

		radius = res.getDimensionPixelSize(R.dimen.paintRadius);

		paint.setTextAlign(Paint.Align.CENTER);
	}

	public static Path getRoundRectPath(float left, float top, float right, float bottom, float radius) {
		Path roundRectPath = new Path();
		rectF.set(left, top, right, bottom);
		roundRectPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
		return roundRectPath;
	}

	public static void setRectPath(Path path, float left, float top, float right, float bottom) {
		rectF.set(left, top, right, bottom);
		path.rewind();
		path.addRect(rectF, Path.Direction.CW);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		paint.getTextBounds(textString, 0, textString.length(), textBounds);

		final int textWidth = textBounds.width();
		final int textHeight = textBounds.height();

		width = textWidth + textPadding;
		height = textHeight + textPadding;

		final int cx = width / 2;
		final int cy = (height + textHeight) / 2;

		paint.getTextPath(textString, 0, textString.length(), cx, cy, textPath);

		progressStrokePath = getRoundRectPath(0, 0, width, height, radius);

		computePaths();

		// Adding 1 to prevent artifacts
		setMeasuredDimension(width + 1, height + 1);
	}

	public void setProgress(final float _percent) {
		assert _percent >= 0F && _percent <= 1F;
		percent = _percent;
		computePaths();
		invalidate();
	}

	private void computePaths() {
		computeCroppedProgressPath();
		computeCroppedTextPath();
	}

	public abstract void computeCroppedProgressPath();

	public abstract void computeCroppedTextPath();

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paint.setColor(COLOR_ORANGE);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(progressStrokePath, paint);

		paint.setStyle(Paint.Style.FILL);
		canvas.drawPath(croppedProgressPath, paint);
		canvas.drawPath(croppedTextPath, paint);
	}
}
