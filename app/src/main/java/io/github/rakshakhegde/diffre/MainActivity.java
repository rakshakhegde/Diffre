package io.github.rakshakhegde.diffre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

	SeekBar seekbar;
	DiffreView diffreViewApi1;
	DiffreView diffreViewApi19;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		seekbar = (SeekBar) findViewById(R.id.seekBar);
		diffreViewApi1 = (DiffreView) findViewById(R.id.fillShapeViewApi1);
		diffreViewApi19 = (DiffreView) findViewById(R.id.fillShapeViewApi19);

		diffreViewApi1.setProgress(seekbar.getProgress() / 100F);
		if (diffreViewApi19 != null) {
			diffreViewApi19.setProgress(seekbar.getProgress() / 100F);
		}

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				diffreViewApi1.setProgress(progress / 100F);

				if (diffreViewApi19 != null) {
					diffreViewApi19.setProgress(progress / 100F);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}
}
