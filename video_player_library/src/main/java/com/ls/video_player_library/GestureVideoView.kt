package com.ls.video_player_library

import android.app.Service
import android.content.Context
import android.graphics.*
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import android.widget.SeekBar
import kotlin.math.abs
import android.graphics.drawable.LayerDrawable
import android.media.MediaPlayer
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import com.ls.comm_util_library.*
import com.ls.glide_library.GlideUtils
import kotlinx.android.synthetic.main.ls_library_gesture_video_view.view.*
import kotlinx.android.synthetic.main.ls_library_options_bar.view.*
import java.lang.Exception


/**
 * @ClassName: GestureVideoView
 * @Description:
 * @Author: ls
 * @Date: 2019/9/3 14:38
 */
class GestureVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), GestureControlView.VideoGestureListener {
    private val TAG = this.javaClass.name

    private var mAudioManager: AudioManager? = null
    private var mBrightnessHelper: BrightnessHelper
    private var mWindow: Window? = null
    private var mLayoutParams: WindowManager.LayoutParams? = null

    //是否隐藏时间
    private var mIsHideTime = false

    fun setHideTime(hide: Boolean) {
        mIsHideTime = hide
        currentTime.visibility = if (mIsHideTime) View.GONE else View.VISIBLE
        totalTime.visibility = if (mIsHideTime) View.GONE else View.VISIBLE
    }

    //是否隐藏标题
    private var mIsHideTitle = false

    fun setHideTitle(hide: Boolean) {
        mIsHideTitle = hide
        titleText.visibility = if (mIsHideTitle) View.GONE else View.VISIBLE
    }

    private var mIsAllowGesture = true

    fun setAllowGesture(allow: Boolean) {
        mIsAllowGesture = allow
        if (mIsAllowGesture) {
            gestureControl.setVideoGestureListener(this)
        } else {
            gestureControl.setVideoGestureListener(null)
            gestureControl.setOnClickListener {
                changeOptionsBarState()
            }
        }
    }

    //是否隐藏更多按钮
    private var mIsHideMore = false

    fun setHideMore(hide: Boolean) {
        mIsHideMore = hide
        more.visibility = if (mIsHideMore) View.GONE else View.VISIBLE
    }

    //是否隐藏返回按钮
    private var mIsHideBack = false

    fun setHideBack(hide: Boolean) {
        mIsHideBack = hide
        back.visibility = if (mIsHideBack) View.GONE else View.VISIBLE
    }

    //是否隐藏全屏按钮
    private var mIsHideFullScreen = false

    fun setHideFullScreen(hide: Boolean) {
        mIsHideFullScreen = hide
        fullScreen.visibility = if (mIsHideFullScreen) View.GONE else View.VISIBLE
    }

    // 是否隐藏播放暂停键
    private var mIsHidePlayPause = false

    fun setHidePlayPause(hide: Boolean){
        mIsHidePlayPause = hide
        pause.visibility = if(mIsHidePlayPause) View.GONE else View.VISIBLE
    }

    //是否隐藏播放进度条
    private var mIsHideSeekBar = false

    fun setHideSeekBar(hide: Boolean) {
        mIsHideSeekBar = hide
        videoSeekBar.visibility = if (mIsHideSeekBar) View.GONE else View.VISIBLE
    }

    //是否隐藏快进快退提示
    private var mIsHideFF_RETip = false

    fun setHideFF_RETip(hide: Boolean) {
        mIsHideFF_RETip = hide
    }

    // 是否静音播放
    private var mMutePlayer = false

    fun setMutePlayer(mute: Boolean){
        mMutePlayer = mute
        if(mute){
            mMediaPlayer?.setVolume(0F,0F)
        }
        else{
            mMediaPlayer?.setVolume(1F,1F)
        }
    }

    // 自动释放
    private var mAutoRelease = true

    fun setAutoRelease(auto: Boolean){
        mAutoRelease = auto
    }

    // 是否使用内置进度条
    private var mUseInnerSeekBar = true

    // 若为false，需要配合setSeekBar使用
    fun setUseInnerSeekBar(useInner: Boolean){
        mUseInnerSeekBar = useInner
        if(!useInner){
            setHideFullScreen(true)
            setHideSeekBar(true)
            setHideTime(true)
            setHidePlayPause(true)
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                // 设置进度条的颜色
                val layerDrawable = videoSeekBar.progressDrawable as LayerDrawable
                layerDrawable.getDrawable(0)
                        .setColorFilter(Color.parseColor("#747674"), PorterDuff.Mode.SRC)
                layerDrawable.getDrawable(2)
                        .setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC)
                videoSeekBar.thumb.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            }
            mVideoSeekBar = videoSeekBar
            setSeekBarListener()
        }
    }

    private var mVideoSeekBar: SeekBar? = null

    fun setSeekBar(seekBar: SeekBar){
        mVideoSeekBar = seekBar
        setSeekBarListener()
    }

    private var maxVolume = 0
    private var oldVolume = 0
    private var newProgress = 0
    private var oldProgress = 0
    private var brightness = 1f

    private var mLoadingAnimation: Animation

    init {
        if (null != attrs) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.GestureVideoView)
            mIsAllowGesture =
                array.getBoolean(R.styleable.GestureVideoView_allowGesture, true)
            mIsHideTime = array.getBoolean(R.styleable.GestureVideoView_hideTime, false)
            mIsHideBack = array.getBoolean(R.styleable.GestureVideoView_hideBack, false)
            mIsHideTitle = array.getBoolean(R.styleable.GestureVideoView_hideTitle, false)
            mIsHideMore = array.getBoolean(R.styleable.GestureVideoView_hideMore, false)
            mIsHideSeekBar = array.getBoolean(R.styleable.GestureVideoView_hideSeekBar, false)
            mIsHidePlayPause = array.getBoolean(R.styleable.GestureVideoView_hidePlayPause,false)
            mIsHideFullScreen = array.getBoolean(R.styleable.GestureVideoView_hideFullScreen, false)
            mIsHideFF_RETip = array.getBoolean(R.styleable.GestureVideoView_hideFF_RETip, false)
            mMutePlayer = array.getBoolean(R.styleable.GestureVideoView_mutePlayer,false)
            mAutoRelease = array.getBoolean(R.styleable.GestureVideoView_autoRelease,true)
            mUseInnerSeekBar = array.getBoolean(R.styleable.GestureVideoView_useInnerSeekBar,true)
            array.recycle()
        }
        //初始化获取音量属性
        mAudioManager = context.getSystemService(Service.AUDIO_SERVICE) as AudioManager
        maxVolume = mAudioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0

        //初始化亮度调节
        mBrightnessHelper = BrightnessHelper(context)

        LayoutInflater.from(context).inflate(R.layout.ls_library_gesture_video_view, this)

        setUseInnerSeekBar(mUseInnerSeekBar)

        initViewState()

        pause.setOnClickListener {
            if (pause.isSelected) {
                resume()
            } else {
                pause()
            }
        }
        videoPlayView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                if(mAutoRelease) {
                    mMediaPlayer?.stop()
                    release()
                    return true
                }
                return false
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                mSurfaceTexture = surface
            }
        }
        mLoadingAnimation = AnimationUtils.loadAnimation(context,R.anim.ls_library_video_loading)
    }

    private var mPauseListener: ((Boolean) -> Unit)? = null
    fun setPauseListener(listener: ((Boolean) -> Unit)?) {
        mPauseListener = listener
    }


    private fun initViewState() {
        currentTime.visibility = if (mIsHideTime) View.GONE else View.VISIBLE
        totalTime.visibility = if (mIsHideTime) View.GONE else View.VISIBLE
        titleText.visibility = if (mIsHideTitle) View.GONE else View.VISIBLE
        back.visibility = if (mIsHideBack) View.GONE else View.VISIBLE
        more.visibility = if (mIsHideMore) View.GONE else View.VISIBLE
        fullScreen.visibility = if (mIsHideFullScreen) View.GONE else View.VISIBLE
        pause.visibility = if(mIsHidePlayPause) View.GONE else View.VISIBLE
        mVideoSeekBar?.visibility = if (mIsHideSeekBar) View.GONE else View.VISIBLE
    }

    private fun setSeekBarListener(){
        mVideoSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                newProgress = progress / mVideoStep
                currentTime.text = TimeUtils.millis2Minute(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                removeCallbacks(mHideOptionsRun)
                if (optionsBar.visibility == View.GONE) {
                    optionsBar.visibility = View.VISIBLE
                }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (optionsBar.visibility == View.VISIBLE) {
                    postDelayed(mHideOptionsRun, mDelayHideTime)
                }
                updateVideoProgress(newProgress)
            }
        })
    }

    private var mDelayHideTime = 1500L
    private var mHideOptionsRun = Runnable {
        optionsBar.visibility = View.GONE
    }

    fun setWindow(window: Window) {
        mWindow = window
        //下面这是设置当前APP亮度的方法配置
        mLayoutParams = mWindow?.attributes
        brightness = mLayoutParams?.screenBrightness ?: 1F
    }


    override fun onBrightnessGesture(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) {
        if (mWindow == null) {
            //这是直接设置系统亮度的方法
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(context)) {
                if (abs(distanceY) > height / 255) {
                    if (distanceY > 0) {
                        setBrightness(4)
                    } else {
                        setBrightness(-4)
                    }
                }
            }
        } else {
            //下面这是设置当前APP亮度的方法
            Log.d(TAG, "onBrightnessGesture: old$brightness")
            var newBrightness = (e1.y - e2.y) / height
            newBrightness += brightness

            Log.d(TAG, "onBrightnessGesture: new$newBrightness")
            if (newBrightness < 0) {
                newBrightness = 0f
            } else if (newBrightness > 1) {
                newBrightness = 1f
            }
            mLayoutParams?.screenBrightness = newBrightness
            mWindow?.attributes = mLayoutParams
            showChangeLayout.setProgress((newBrightness * 100).toInt())
            showChangeLayout.setImageResource(R.mipmap.ls_library_brightness_w)
            showChangeLayout.show()
        }
    }

    fun setTitle(title: String) {
        titleText.text = title
    }

    fun setBackClickListener(listener: OnClickListener) {
        back.setOnClickListener(listener)
    }

    fun setMoreClickListener(listener: OnClickListener) {
        more.setOnClickListener(listener)
    }

    fun setFullScreenListener(listener: OnClickListener) {
        fullScreen.setOnClickListener(listener)
    }

    private var mReleaseListener: (() -> Unit)? = null
    fun setReleaseListener(listener: (() -> Unit)?) {
        mReleaseListener = listener
    }

    private var mVideoStep = 1
    private var mVideoThread: VideoThread? = null
    // 需要在MediaPlayer准备完成之后调用
    private fun setVideoInfo(mediaPlayer: MediaPlayer) {
        totalTime.text = TimeUtils.millis2Minute(mediaPlayer.duration.toLong())
        mVideoSeekBar?.max = mediaPlayer.duration
        mVideoStep = mediaPlayer.duration / 100 + 1
        startVideoThread(mediaPlayer)
        mediaPlayer.setOnCompletionListener {
            release()
        }
    }

    fun release() {
        LogUtils.d(TAG,"release...")
        ViewUtils.setSelectedView(pause, true)
        stopVideoThread()
        gestureControl.visibility = View.GONE
        mReleaseListener?.invoke()
        mMediaPlayer?.setOnCompletionListener(null)
        mMediaPlayer?.pause()
        mMediaPlayer = null
        frameView.visibility = View.VISIBLE
    }

    private fun startVideoThread(mediaPlayer: MediaPlayer) {
        if (null != mVideoThread && !mVideoThread!!.isInterrupted) {
            mVideoThread?.interrupt()
        }
        mVideoThread = VideoThread(mediaPlayer) {
            LogUtils.d(TAG, "progress = $it total = ${mediaPlayer.duration}")
            mVideoSeekBar?.progress = it
        }
        mVideoThread?.start()
    }

    private fun stopVideoThread() {
        mVideoThread?.stopListener()
    }

    private fun createSurface(): Surface {
       return Surface(mSurfaceTexture)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun resetSurface(){
        videoPlayView.surfaceTexture = mSurfaceTexture
    }

    private var mFullScreen = false
    fun setFullScreenMode(mode: Boolean){
        mFullScreen = mode
        fullScreen.setImageResource(if(mode) R.mipmap.ls_library_cancel_full_screen else R.mipmap.ls_library_full_screen)
    }

    fun isFullScreen(): Boolean{
        return mFullScreen
    }

    private var mVideoUrl: String? = null
    fun prepare(frame: String?, url: String) {
        mVideoUrl = url
        if (null != frame) {
            GlideUtils.getBitmap(context,frame){
                GlideUtils.load(it,frameView)
            }
        }else{
            GlideUtils.getVideoScreenshot(context,url,0){
                GlideUtils.load(it,frameView)
            }

        }
    }

    fun pause(){
        ViewUtils.setSelectedView(pause, true)
        mMediaPlayer?.pause()
        mPauseListener?.invoke(true)
    }

    fun resume(){
        ViewUtils.setSelectedView(pause, false)
        mMediaPlayer?.start()
        mPauseListener?.invoke(false)
    }

    fun isPlaying(): Boolean{
        return mMediaPlayer?.isPlaying?:false
    }

    private var mMediaPlayer: MediaPlayer? = null
    private var mSurfaceTexture: SurfaceTexture? = null
    fun start(mediaPlayer: MediaPlayer, listener: ((Boolean) -> Unit)) {
        val startListener: (Boolean) -> Unit = {
            postDelayed({
                loading.clearAnimation()
                loading.visibility = View.GONE
                listener.invoke(it)
            },200)
        }
        if (mVideoUrl == null) {
            startListener.invoke(false)
            return
        }
        if (null != mMediaPlayer) {
            resume()
            startListener.invoke(true)
            return
        }
        loading.visibility = View.VISIBLE
        loading.startAnimation(mLoadingAnimation)
        ViewUtils.setSelectedView(pause, false)
        mMediaPlayer = mediaPlayer
        mMediaPlayer?.setOnVideoSizeChangedListener { mp, width, height ->
            videoPlayView.setAspectRatio(width, height)
        }
        mMediaPlayer?.setOnSeekCompleteListener {
            LogUtils.d(TAG,"seek ${it.currentPosition}")
        }

        mMediaPlayer?.setSurface(createSurface())
        start(startListener)
    }

    private fun start(startListener: ((Boolean) -> Unit)) {
        if (null != mMediaPlayer && videoPlayView.isAvailable) {
            mMediaPlayer?.reset()
            mMediaPlayer?.setDataSource(mVideoUrl)
            try {
                mMediaPlayer?.setOnErrorListener { mp, what, extra ->
                    startListener.invoke(false)
                    true
                }
                mMediaPlayer?.prepareAsync()
                mMediaPlayer?.setOnPreparedListener {
                    if(mMutePlayer){
                        it.setVolume(0F,0F)
                    }
                    else{
                        it.setVolume(1F,1F)
                    }
                    if(null != mMediaPlayer) {
                        setVideoInfo(it)
                        startListener.invoke(true)
                        mMediaPlayer?.start()
                        frameView.visibility = View.GONE
                        gestureControl.visibility = View.VISIBLE
                        if (mIsAllowGesture) {
                            gestureControl.setVideoGestureListener(this)
                        } else {
                            gestureControl.setVideoGestureListener(null)
                            gestureControl.setOnClickListener {
                                changeOptionsBarState()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                startListener.invoke(false)
            }

        }
    }

    //这是直接设置系统亮度的方法
    private fun setBrightness(brightness: Int) {
        //要是有自动调节亮度，把它关掉
        mBrightnessHelper.offAutoBrightness()

        val oldBrightness = mBrightnessHelper.brightness
        Log.d(TAG, "onBrightnessGesture: oldBrightness: $oldBrightness")
        val newBrightness = oldBrightness + brightness
        Log.d(TAG, "onBrightnessGesture: newBrightness: $newBrightness")
        //设置亮度
        mBrightnessHelper.setSystemBrightness(newBrightness)
        //设置显示
        showChangeLayout.setProgress((java.lang.Float.valueOf(newBrightness.toFloat()) / mBrightnessHelper.maxBrightness * 100) as Int)
        showChangeLayout.setImageResource(R.mipmap.ls_library_brightness_w)
        showChangeLayout.show()

    }

    override fun onVolumeGesture(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) {
        Log.d(TAG, "onVolumeGesture: oldVolume $oldVolume")
        val value = height / maxVolume
        val newVolume = ((e1.y - e2.y) / value + oldVolume).toInt()

        mAudioManager?.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            newVolume,
            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
        )


//        int newVolume = oldVolume;

        Log.d(TAG, "onVolumeGesture: value$value")

        //另外一种调音量的方法，感觉体验不好，就没采用
//        if (distanceY > value){
//            newVolume = 1 + oldVolume;
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
//        }else if (distanceY < -value){
//            newVolume = oldVolume - 1;
//            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
//        }
        Log.d(TAG, "onVolumeGesture: newVolume $newVolume")

        //要强行转Float类型才能算出小数点，不然结果一直为0
        val volumeProgress =
            (newVolume / java.lang.Float.valueOf(maxVolume.toFloat()) * 100).toInt()
        if (volumeProgress >= 50) {
            showChangeLayout.setImageResource(R.mipmap.ls_library_volume_higher_w)
        } else if (volumeProgress > 0) {
            showChangeLayout.setImageResource(R.mipmap.ls_library_volume_lower_w)
        } else {
            showChangeLayout.setImageResource(R.mipmap.ls_library_volume_off_w)
        }
        showChangeLayout.setProgress(volumeProgress)
        showChangeLayout.show()
    }

    override fun onFF_REWGesture(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ) {
        mVideoThread?.updateProgress(false)
        if (optionsBar.visibility == View.GONE) {
            optionsBar.visibility = View.VISIBLE
        }
        if (mIsHideFF_RETip && showChangeLayout.visibility == View.VISIBLE) {
            showChangeLayout.hide()
        }
        val offset = e2.x - e1.x
        //根据移动的正负决定快进还是快退
        if (offset > 0) {
            showChangeLayout.setImageResource(R.mipmap.ls_library_ff)
            newProgress = (oldProgress + offset / width * 100).toInt()
            if (newProgress > 100) {
                newProgress = 100
            }
        } else {
            showChangeLayout.setImageResource(R.mipmap.ls_library_fr)
            newProgress = (oldProgress + offset / width * 100).toInt()
            if (newProgress < 0) {
                newProgress = 0
            }
        }
        if (null != mMediaPlayer) {
            mVideoSeekBar?.progress = newProgress * mVideoStep
        }

        showChangeLayout.setProgress(newProgress)
        if (!mIsHideFF_RETip) {
            showChangeLayout.show()
        }
    }


    override fun onSingleTapGesture(e: MotionEvent) {
        changeOptionsBarState()
    }

    private fun changeOptionsBarState() {
        if (optionsBar.visibility == View.GONE) {
            optionsBar.visibility = View.VISIBLE
            postDelayed(mHideOptionsRun, mDelayHideTime)
        } else {
            optionsBar.visibility = View.GONE
        }
    }

    private var mOnDoubleClickListener: (() -> Unit)? = null
    fun setOnLongClickListener(listener: (() -> Unit)) {
        mOnDoubleClickListener = listener
    }

    override fun onDoubleTapGesture(e: MotionEvent) {
        mOnDoubleClickListener?.invoke()
    }

    override fun onDown(e: MotionEvent) {
        removeCallbacks(mHideOptionsRun)
        //每次按下的时候更新当前亮度和音量，还有进度
        oldProgress = newProgress
        oldVolume = mAudioManager?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
        brightness = mLayoutParams?.screenBrightness ?: 1F
        if (brightness == -1f) {
            //一开始是默认亮度的时候，获取系统亮度，计算比例值
            brightness = mBrightnessHelper.brightness / 255f
        }
    }

    private var mProgressListener: ((Int) -> Unit)? = null
    fun setProgressListener(listener: ((Int) -> Unit)) {
        mProgressListener = listener
    }

    override fun onEndFF_REW(e: MotionEvent?) {
        postDelayed(mHideOptionsRun, mDelayHideTime)
        updateVideoProgress(newProgress)
        mVideoThread?.updateProgress(true)
    }

    private fun updateVideoProgress(progress: Int) {
        if (mMediaPlayer != null) {
            if (mProgressListener != null) {
                mProgressListener?.invoke(progress * mVideoStep)
            } else {
                mMediaPlayer!!.seekTo(progress * mVideoStep)
                if (!mMediaPlayer!!.isPlaying) {
                    resume()
                    startVideoThread(mMediaPlayer!!)
                }
            }
        }
    }


}