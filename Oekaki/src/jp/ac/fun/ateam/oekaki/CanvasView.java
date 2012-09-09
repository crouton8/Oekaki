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
	// 全ての線を管理するリスト
	private ArrayList<Line> lines = new ArrayList<Line>();
	// 一本の線
	private Line aLine;
	// 描画色
	private int currentColor = Color.BLACK;
	// コンテキスト
	private Context context;
	// 線の太さの初期値
	private int lineWidth = 6;

	// コンストラクター
	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// this.context = context;
	}

	// 最後の線を消去する
	public void undo() {
		if (lines.size() > 0) {
			lines.remove(lines.size() - 1);
		}
		invalidate();
	}

	// 全ての線を消去する
	public void clear() {
		lines.clear();
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawAll(canvas);
	}

	// 全ての線を描画する
	public void drawAll(Canvas canvas) {
		// 背景を白に
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		// アンチエイリアスを有効にする
		paint.setAntiAlias(true);

		// すべての線を描画する
		for (Line line : lines) {

			// 色を設定
			paint.setColor(line.getColor());
			// 線幅を設定
			paint.setStrokeWidth(lineWidth);
			// ポイントをつなげて一本の線を描画する
			for (int i = 0; i < (line.getPoints().size() - 1); i++) {
				Point s = line.getPoints().get(i);
				Point e = line.getPoints().get(i + 1);
				// ２点間の線を引く
				canvas.drawLine(s.x, s.y, e.x, e.y, paint);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 新しい線を生成
			aLine = new Line(currentColor);
			// linesに線を追加
			lines.add(aLine);
			break;
		case MotionEvent.ACTION_MOVE:
			int x = (int) event.getX();
			int y = (int) event.getY();
			Point p = new Point(x, y);
			// 線にポイントを追加
			aLine.addPoint(p);
			break;
		case MotionEvent.ACTION_UP:
			break;
		}

		// 画面を再描画
		invalidate();
		return true;
	}

	// 色を設定する
	public void setColor(int c) {
		currentColor = c;
	}

	// ギャラリーに画像を保存する
	public void saveFile() {
		Bitmap myBitmap;
		Canvas bitmapCanvas;

		// Bitmapオブジェクトを作成する
		myBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(),
				Bitmap.Config.ARGB_8888);

		// BitmapのCanvasオブジェクトを生成する
		bitmapCanvas = new Canvas(myBitmap);
		// すべての線を描く
		drawAll(bitmapCanvas);

		// SDカードが利用可能か調べる
		String status = Environment.getExternalStorageState();
		File sdcardDir;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			String sdcardDirPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Oekaki";
			sdcardDir = new File(sdcardDirPath);
			// ディレクトリがなければ作成する
			if (!sdcardDir.exists()) {
				sdcardDir.mkdirs();
			}
		} else {
			Toast.makeText(context, "Could not access SD card.",
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// ファイル名を日付時刻に設定
		long msec = System.currentTimeMillis();
		String fname = DateFormat.format("yyyy-MM-dd_kk.mm.ss", msec)
				.toString();
		fname = sdcardDir.getAbsolutePath() + "/" + fname + ".png";
		
		//画像をファイルに書き込む
		try{
			FileOutputStream outstream = new FileOutputStream(fname);
			myBitmap.compress(CompressFormat.PNG, 100, outstream);
			outstream.flush();
			outstream.close();
		} catch (IOException e) {
			Toast.makeText(context, "Cound not write file", Toast.LENGTH_LONG)
			.show();
		}
		//「ギャラリー」のデータベースに登録
		ContentResolver contentResolver = context.getContentResolver();
		ContentValues contentValues = new ContentValues();
		contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
		contentValues.put(MediaStore.MediaColumns.DATA, fname);
		contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				contentValues);
	}
}
