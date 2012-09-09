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
	
	//�������{�^���̃C�x���g���X�i�[
	public class UndoBtnOnClickListener implements OnClickListener{
		public void onClick(View v) {
			canvasView.undo();
		}
	}
	
	//�N���A�{�^���̃C�x���g���X�i�[
	public class ClearBtnOnClickListener implements OnClickListener{
		public void onClick(View v) {
			canvasView.clear();
		}
	}
	
	//�ۑ��{�^���̃C�x���g���X�i�[
		public class SaveBtnOnClickListener implements OnClickListener{
			public void onClick(View v) {
				canvasView.saveFile();
			}
		}
	
	//�F�̑I��p���W�I�{�^���̃C�x���g���X�i�[
		public class ColorBtnOnChangeListener implements OnCheckedChangeListener{
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton radioButton = (RadioButton) findViewById(checkedId);
				String colorStr = radioButton.getText().toString();
				if(colorStr.equals("��")){
					canvasView.setColor(Color.BLACK);
				} else if(colorStr.equals("��")){
					canvasView.setColor(Color.RED);
				}else if(colorStr.equals("��")){
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
        
        //�F�̑I��p���W�I�{�^��
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.colorRadio);
        radioGroup.setOnCheckedChangeListener(new ColorBtnOnChangeListener());
        
        //�ۑ��{�^��
        Button saveButton = (Button) findViewById(R.id.MyView);
        saveButton.setOnClickListener(new SaveBtnOnClickListener());
        
      //�N���A�{�^��
        Button clearButton = (Button) findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new ClearBtnOnClickListener());
        
      //�������{�^��
        Button undoButton = (Button) findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new UndoBtnOnClickListener()); 
    }
}
