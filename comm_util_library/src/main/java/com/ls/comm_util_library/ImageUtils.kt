/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ls.comm_util_library

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.media.ExifInterface
import android.media.Image
import android.media.ThumbnailUtils
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.*

/**
 * Collection of image reading and manipulation utilities in the form of static functions.
 */
abstract class ImageUtils {
    companion object {
        fun saveFile(bitmap: Bitmap,savePath: String): File{
            var file = File(savePath)
            if(!file.parentFile.exists()){
                file.parentFile.mkdirs()
            }
            if(file.exists()){
                file.delete()
            }
            try {
                val bos = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
                bos.flush()
                bos.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return file
        }
        /**
         * 给一张Bitmap添加水印文字。
         *
         * @param bitmap      源图片
         * @param content  水印文本
         * @param textSize 水印字体大小 ，单位pix。
         * @param color    水印字体颜色。
         * @param x        起始坐标x
         * @param y        起始坐标y
         * @param positionFlag  居左/居右
         * @param recycle  是否回收
         * @return 已经添加水印后的Bitmap。
         */
        fun addTextWatermark(bitmap: Bitmap, content: String?, textSize: Int, color: Int, x: Float, y: Float, positionFlag: Boolean): Bitmap {
            if (isEmptyBitmap(bitmap) || content == null)
                return bitmap
            val ret = bitmap.copy(bitmap.config, true)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            val canvas = Canvas(ret)
            paint.color = color
            paint.textSize = textSize.toFloat()
            val bounds = Rect()
            paint.getTextBounds(content, 0, content.length, bounds)

            if (positionFlag) {
                canvas.drawText(content, x, bitmap.height.toFloat() - bounds.height().toFloat() - bounds.top.toFloat() - y, paint)

            } else {
                canvas.drawText(content, bitmap.width.toFloat() - x - bounds.width().toFloat() - bounds.left.toFloat(), bitmap.height.toFloat() - bounds.height().toFloat() - bounds.top.toFloat() - y, paint)

            }
            return ret
        }

        /**
         * Bitmap对象是否为空。
         */
        fun isEmptyBitmap(src: Bitmap?): Boolean {
            return src == null || src.width == 0 || src.height == 0
        }

        /**
         * Helper function used to convert an EXIF orientation enum into a transformation matrix
         * that can be applied to a bitmap.
         *
         * @param orientation - One of the constants from [ExifInterface]
         */
        private fun decodeExifOrientation(orientation: Int): Matrix {
            val matrix = Matrix()

            // Apply transformation corresponding to declared EXIF orientation
            when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> Unit
                ExifInterface.ORIENTATION_UNDEFINED -> Unit
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90F)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180F)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270F)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.postScale(-1F, 1F)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.postScale(1F, -1F)
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.postScale(-1F, 1F)
                    matrix.postRotate(270F)
                }
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.postScale(-1F, 1F)
                    matrix.postRotate(90F)
                }

                // Error out if the EXIF orientation is invalid
                else -> throw IllegalArgumentException("Invalid orientation: $orientation")
            }

            // Return the resulting matrix
            return matrix
        }

        /**
         * Decode a bitmap from a file and apply the transformations described in its EXIF data
         *
         * @param file - The image file to be read using [BitmapFactory.decodeFile]
         */
        fun decodeBitmap(file: File): Bitmap {
            // First, decode EXIF data and retrieve transformation matrix
            val exif = ExifInterface(file.absolutePath)
            val transformation = decodeExifOrientation(exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90))

            // Read bitmap using factory methods, and transform it using EXIF data
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            return Bitmap.createBitmap(
                    BitmapFactory.decodeFile(file.absolutePath),
                    0, 0, bitmap.width, bitmap.height, transformation, true)
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun decodeBitmap(image: Image): Bitmap {
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.capacity()).also { buffer.get(it) }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        /**
         * This function cuts out a circular thumbnail from the provided bitmap. This is done by
         * first scaling the image down to a square with width of [diameter], and then marking all
         * pixels outside of the inner circle as transparent.
         *
         * @param bitmap - The [Bitmap] to be taken a thumbnail of
         * @param diameter - Size in pixels for the diameter of the resulting circle
         */
        fun cropCircularThumbnail(bitmap: Bitmap, diameter: Int = 128): Bitmap {
            // Extract a much smaller bitmap to serve as thumbnail
            val thumbnail = ThumbnailUtils.extractThumbnail(bitmap, diameter, diameter)

            // Create an additional bitmap of same size as thumbnail to carve a circle out of
            val circular = Bitmap.createBitmap(
                    diameter, diameter, Bitmap.Config.ARGB_8888)

            // Paint will be used as a mask to cut out the circle
            val paint = Paint().apply {
                color = Color.BLACK
            }

            Canvas(circular).apply {
                drawARGB(0, 0, 0, 0)
                drawCircle(diameter / 2F, diameter / 2F, diameter / 2F - 8, paint)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
                val rect = Rect(0, 0, diameter, diameter)
                drawBitmap(thumbnail, rect, rect, paint)
            }

            return circular
        }

        fun resizeBitmap(bitmap: Bitmap,width: Int,height: Int): Bitmap{
            val originWidth = bitmap.width
            val originHeight = bitmap.height
            val scaleWidth = width / originWidth.toFloat()
            val scaleHeight = height / originHeight.toFloat()
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            return Bitmap.createBitmap(bitmap, 0, 0, originWidth, originHeight, matrix, true)
        }
    }
}