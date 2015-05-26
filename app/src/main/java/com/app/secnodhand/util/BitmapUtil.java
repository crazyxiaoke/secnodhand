package com.app.secnodhand.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import com.jingwei.card.LocalOCR;
import com.process.cert.entity.CertImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
	public static String getRealPathFromURI(Activity activity, Uri uri) {
		String path = null;
		if (uri.getScheme().equals("content")) {
			String[] proj = { MediaColumns.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);
		} else {
			path = uri.getPath();
		}
		return path;
	}

	/**
	 * 有的手机有的系统在拍照之后会自动对图片进行旋转，需要修正这个旋转的角度
	 * 
	 * @param path
	 * @return
	 */
	public static int readPictureRotateDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param bitmap
	 * @param angle
	 * @return
	 */
	public static Bitmap rotateImageView(Bitmap bitmap, int angle) {
		if (angle == 0) {
			return bitmap;
		} else {
			// 旋转图片
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		}
	}

	public static Bitmap decodeSampledBitmapFromFile(String pathName) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		int max = Math.max(width, height);

		if (max > 3000) {
			Log.d("result", "max1");
			inSampleSize = 4;
		} else if (max > 2000) {
			inSampleSize = 3;
			Log.d("result", "max2");
		} else if (max > 1000 && max < 2000) {
			inSampleSize = 2;
			Log.d("result", "max3");
		}
		Log.d("result", "max0");
		return inSampleSize;
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, float desWidth,
			float desHeight) {
		if (bitmap == null) {
			return null;
		}
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		if (bmpWidth > desWidth || bmpHeight > desHeight) {
			Matrix matrix = new Matrix();

			float scalFactor = Math.min(desWidth / bmpWidth, desHeight
					/ bmpHeight);
			matrix.postScale(scalFactor, scalFactor);

			Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth,
					bmpHeight, matrix, false);
			bitmap.recycle();
			bitmap = resizeBitmap;
		}
		return bitmap;
	}

	public static File bitmapToFile(Bitmap bitmap) {
		try {
			bitmap = resizeBitmap(bitmap, 500, 500);
			File file = new File("/tmp.jpg");
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
			return file;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 获取圆形Bitmap
	 *
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getCircleBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();
		int diameter = width;
		float center = diameter / 2f;
		float dy = (height - diameter) / 2f;
		float dx = 0;
		if (height < width) {
			diameter = height;
			dy = 0;
			dx = (width - diameter) / 2f;
		}

		Bitmap circleBitmap = Bitmap.createBitmap(diameter, diameter,
				Config.ARGB_8888);
		Canvas c = new Canvas(circleBitmap);

		int color = 0xffffffff;
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setAntiAlias(true);
		c.drawARGB(0, 0, 0, 0);
		c.drawCircle(center, center, center, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		c.save();
		c.translate(-dx, -dy);
		c.drawBitmap(bitmap, 0, 0, paint);
		c.restore();
		return circleBitmap;
	}

	// 锐化效果函数
	public static Bitmap changeToSharpen(Bitmap bitmap) {
		// 拉普拉斯矩阵
		int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap returnBitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int idx = 0;
		float alpha = 0.3F;
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) // y
		{
			for (int k = 1, len = width - 1; k < len; k++) // x
			{
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + n) * width + k + m];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return returnBitmap;
	}

	public static Bitmap cutImage(Bitmap originBitmap) {
		if (originBitmap == null)
			return null;
		int width = originBitmap.getWidth();
		int height = originBitmap.getHeight();
		if (width < height)
			return null;
		int[] pix = new int[width * height];
		originBitmap.getPixels(pix, 0, width, 0, 0, width, height);
		CertImage certImage = LocalOCR.processImage(width, height, pix);
		Bitmap resultImg = null;
		if (certImage != null && certImage.width > 0 && certImage.height > 0) {
			resultImg = Bitmap.createBitmap(certImage.width, certImage.height,
					Config.ARGB_8888);
			resultImg.setPixels(certImage.img, 0, certImage.width, 0, 0,
					certImage.width, certImage.height);
		}
		return resultImg;
	}

	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static void saveBitmap(Bitmap bitmap, String pathName,
			Boolean isRecycle) {
		FileOutputStream m_fileOutPutStream = null;
		try {
			m_fileOutPutStream = new FileOutputStream(pathName);// 写入的文件路径
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (bitmap != null)
			bitmap.compress(CompressFormat.JPEG, 100, m_fileOutPutStream);
		try {
			m_fileOutPutStream.flush();
			m_fileOutPutStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (isRecycle && bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}


}
