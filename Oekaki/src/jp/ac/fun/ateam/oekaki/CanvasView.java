package jp.ac.fun.ateam.oekaki;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class CanvasView extends View {
	// �S�Ă̐����Ǘ����郊�X�g
	private ArrayList<Line> lines = new ArrayList<Line>();
	// ��{�̐�
	private Line aLine;
	// �`��F
	private int currentColor = Color.BLACK;
	// �R���e�L�X�g
	private Context context;
	// ���̑����̏����l
	private int lineWidth = 6;

	// �R���X�g���N�^�[
	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// this.context = context;
	}

	// �Ō�̐�����������
	public void undo() {
		if (lines.size() > 0) {
			lines.remove(lines.size() - 1);
		}
		invalidate();
	}

	// �S�Ă̐�����������
	public void clear() {
		lines.clear();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawAll(canvas);
	}

	// �S�Ă̐���`�悷��
	public void drawAll(Canvas canvas) {
		// �w�i�𔒂�
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		// �A���`�G�C���A�X��L���ɂ���
		paint.setAntiAlias(true);

		// ���ׂĂ̐���`�悷��
		for (Line line : lines) {

			// �F��ݒ�
			paint.setColor(line.getColor());
			// ������ݒ�
			paint.setStrokeWidth(lineWidth);
			// �|�C���g���Ȃ��Ĉ�{�̐���`�悷��
			for (int i = 0; i < (line.getPoints().size() - 1); i++) {
				Point s = line.getPoints().get(i);
				Point e = line.getPoints().get(i + 1);
				// �Q�_�Ԃ̐�������
				canvas.drawLine(s.x, s.y, e.x, e.y, paint);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// �V�������𐶐�
			aLine = new Line(currentColor);
			// lines�ɐ���ǉ�
			lines.add(aLine);
			break;
		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int y = (int) event.getY();
			Point p = new Point(x, y);
			// ���Ƀ|�C���g��ǉ�
			aLine.addPoint(p);
			break;
		case MotionEvent.ACTION_UP:
			break;
		}

		// ��ʂ��ĕ`��
		invalidate();
		return true;
	}

	// �F��ݒ肷��
	public void setColor(int c) {
		currentColor = c;
	}

	// �M�������[�ɉ摜��ۑ�����
	public void saveFile() {
		Bitmap myBitmap;
		Canvas bitmapCanvas;

		// Bitmap�I�u�W�F�N�g���쐬����
		myBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(),
				Bitmap.Config.ARGB_8888);

		// Bitmap��Canvas�I�u�W�F�N�g�𐶐�����
		bitmapCanvas = new Canvas(myBitmap);
		// ���ׂĂ̐���`��
		drawAll(bitmapCanvas);

		// SD�J�[�h�����p�\�����ׂ�
		String status = Environment.getExternalStorageState();
		File sdcardDir;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			String sdcardDirPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Oekaki";
			sdcardDir = new File(sdcardDirPath);
			// �f�B���N�g�����Ȃ���΍쐬����
			if (!sdcardDir.exists()) {
				sdcardDir.mkdirs();
			}
		} else {
			Toast.makeText(context, "Could not access SD card.",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// �t�@�C��������t�����ɐݒ�
		long msec = System.currentTimeMillis();
		String fname = DateFormat.format("yyyy-MM-dd_kk.mm.ss", msec)
				.toString();
		fname = sdcardDir.getAbsolutePath() + "/" + fname + ".png";
		
		//�摜���t�@�C���ɏ�������
		try{
			FileOutputStream outstream = new FileOutputStream(fname);
			myBitmap.compress(CompressFormat.PNG, 100, outstream);
			outstream.flush();
			outstream.close();
		} catch (IOException e) {
			Toast.makeText(context, "Cound not write file", Toast.LENGTH_LONG)
			.show();
		}
		//�u�M�������[�v�̃f�[�^�x�[�X�ɓo�^
		ContentResolver contentResolver = context.getContentResolver();
		ContentValues contentValues = new ContentValues();
		contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
		contentValues.put(MediaStore.MediaColumns.DATA, fname);
		contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				contentValues);
	}
}
