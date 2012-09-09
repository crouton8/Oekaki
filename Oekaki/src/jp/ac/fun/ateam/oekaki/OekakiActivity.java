package jp.ac.fun.ateam.oekaki;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class OekakiActivity extends Activity {
	private CanvasView canvasView;
	
	//取り消しボタンのイベントリスナー
	public class UndoBtnOnClickListener implements OnClickListener{
		public void onClick(View v) {
			canvasView.undo();
		}
	}
	
	//クリアボタンのイベントリスナー
	public class ClearBtnOnClickListener implements OnClickListener{
		public void onClick(View v) {
			canvasView.clear();
		}
	}
	
	//保存ボタンのイベントリスナー
		public class SaveBtnOnClickListener implements OnClickListener{
			public void onClick(View v) {
				canvasView.saveFile();
			}
		}
	
	//色の選択用ラジオボタンのイベントリスナー
		public class ColorBtnOnChangeListener implements OnCheckedChangeListener{
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton radioButton = (RadioButton) findViewById(checkedId);
				String colorStr = radioButton.getText().toString();
				if(colorStr.equals("黒")){
					canvasView.setColor(Color.BLACK);
				} else if(colorStr.equals("赤")){
					canvasView.setColor(Color.RED);
				}else if(colorStr.equals("緑")){
					canvasView.setColor(Color.GREEN);
				}
			}
		}
		/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oekaki);
        
        canvasView = (CanvasView) findViewById(R.id.MyView);
        
        //色の選択用ラジオボタン
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.colorRadio);
        radioGroup.setOnCheckedChangeListener(new ColorBtnOnChangeListener());
        
        //保存ボタン
        Button saveButton = (Button) findViewById(R.id.MyView);
        saveButton.setOnClickListener(new SaveBtnOnClickListener());
        
      //クリアボタン
        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new ClearBtnOnClickListener());
        
      //取り消しボタン
        Button undoButton = (Button) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new UndoBtnOnClickListener()); 
    }
}
