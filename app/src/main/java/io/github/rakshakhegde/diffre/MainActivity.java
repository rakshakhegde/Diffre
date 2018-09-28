package io.github.rakshakhegde.diffre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

	SeekBar seekbar;
	DiffreView diffreView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		seekbar = (SeekBar) findViewById(R.id.seekBar);
		diffreView = (DiffreView) findViewById(R.id.fillShapeView);

		diffreView.setProgress(seekbar.getProgress() / 100F);

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				diffreView.setProgress(progress / 100F);
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
