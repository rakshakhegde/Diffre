package io.github.rakshakhegde.diffre;

import android.content.Context;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Works with any background. No anti-aliasing for this approach.
 * <p>
 * Heavily inspired from Romain Guy's Medium article on "How to underline text":
 * https://medium.com/google-developers/a-better-underline-for-android-90ba3a2e4fb#.hnv0zcm2h
 * <p>
 * Created by rakshakhegde on 16/02/17.
 */
public class DiffreViewApi1 extends DiffreView {

	final Region textRegion = new Region();
	final Region progressRegion = new Region();
	final Region region = new Region();

	public DiffreViewApi1(Context context) {
		this(context, null);
	}
	public DiffreViewApi1(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public DiffreViewApi1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void computeCroppedProgressPath() {

		region.set(0, 0, (int) (width * percent), height);

		progressRegion.setPath(progressStrokePath, region); // INTERSECT

		textRegion.setPath(textPath, region);
		progressRegion.op(textRegion, Region.Op.DIFFERENCE); // DIFFERENCE

		croppedProgressPath.rewind();
		progressRegion.getBoundaryPath(croppedProgressPath);
	}

	@Override
	public void computeCroppedTextPath() {
		region.set((int) (width * percent), 0, width, height);
		textRegion.setPath(textPath, region); // INTERSECT
		croppedTextPath.rewind();
		textRegion.getBoundaryPath(croppedTextPath);
	}
}
