package com.ls.custom_view_library

import android.content.Context
import android.text.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import com.ls.comm_util_library.*
import java.lang.StringBuilder
import kotlin.properties.Delegates

/**
 * @ClassName: CodeView
 * @Description:
 * @Author: ls
 * @Date: 2019/10/31 10:32
 */
class CodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mCodeBg by Delegates.notNull<Int>()
    private var mCodeSize by Delegates.notNull<Float>()
    private var mCodeNumber by Delegates.notNull<Int>()
    private val mEdits = ArrayList<EditText>()

    fun getLimit(): Int{
        return mCodeNumber
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CodeView)
        mCodeBg = a.getResourceId(R.styleable.CodeView_codeBackground, R.drawable.ls_library_code_style)
        mCodeSize = a.getDimension(R.styleable.CodeView_codeSize, Util.sp2px(24F).toFloat())
        mCodeNumber = a.getInt(R.styleable.CodeView_codeNumber,4)
        LogUtils.d("mCodeSize = ", "$mCodeSize")
        a.recycle()
        for (i in 0 until mCodeNumber) {
            val edit = generateEdit()
            addView(edit)
            if(i < mCodeNumber - 1){
                addView(generateGap())
            }
            else{
                edit.imeOptions = EditorInfo.IME_ACTION_SEND
            }
            mEdits.add(edit)
        }
    }


    private fun generateGap(): View {
        val view = View(context)
        view.layoutParams = LayoutParams(0,0,0.3F)
        view.visibility = View.INVISIBLE
        return view
    }

    private fun generateEdit(): EditText{
        val edit = EditText(context)
        edit.inputType = InputType.TYPE_CLASS_NUMBER
        edit.imeOptions = EditorInfo.IME_ACTION_NEXT
        edit.setTextSize(TypedValue.COMPLEX_UNIT_PX,mCodeSize)
        edit.setBackgroundResource(mCodeBg)
        edit.layoutParams = LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1F)
        edit.gravity = Gravity.CENTER
        edit.setPadding(0, Util.dp2px(3F).toInt(),0, Util.dp2px(3F).toInt())
        edit.addTextChangedListener(CodeTextWatcher(edit))
        edit.setOnKeyListener(CodeOnKeyListener(edit))
        return edit
    }

    override fun setEnabled(enabled: Boolean) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.isEnabled = enabled
        }
        alpha = if(enabled) 1F else 0.5F
    }

    inner class CodeTextWatcher(edit: EditText) : TextWatcher{
        private val mEdit = edit
        override fun afterTextChanged(s: Editable?) {
            if(s?.length?:0 > 1){
                s?.delete(1,s.length)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(s?.length?:0 >= 1){
                LogUtils.d("Editable = ", s?.toString()?:"")
                focus(mEdit,false)
                mEdit.isSelected = true
            }
            else mEdit.isSelected = false
        }
    }

    inner class CodeOnKeyListener(edit: EditText) : OnKeyListener{
        private val mEdit = edit
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(keyCode == KeyEvent.KEYCODE_DEL && mEdit.text.isEmpty()){
                focus(mEdit,true)
                return true
            }
            if(keyCode == KeyEvent.KEYCODE_ENTER){
                send()
                return true
            }
            return false
        }
    }

    private fun focus(currEdit: EditText?,upOrNext: Boolean){
        if(currEdit?.isFocused != true){
            return
        }
        mInputListener?.onValue(getText())
        var focusIndex = mEdits.indexOf(currEdit)
        if(!upOrNext) {
            focusIndex += 1
            if (focusIndex >= mEdits.size) {
                send()
                return
            }
        }
        else{
            focusIndex -= 1
            if (focusIndex < 0) {
                return
            }
        }
        ViewUtils.showKeyBoard(mEdits[focusIndex])
    }

    private fun send(){
        val code = StringBuilder()
        for (edit in mEdits){
            if(TextUtils.isEmpty(edit.text)){
                ViewUtils.showKeyBoard(edit)
                return
            }
            code.append(edit.text)
        }
        ViewUtils.hideKeyBoard(mEdits[mEdits.size-1])
        mFinishListener?.onValue(code.toString())
    }

    fun getText(): String{
        val code = StringBuilder()
        for (edit in mEdits){
            if(TextUtils.isEmpty(edit.text)){
                continue
            }
            code.append(edit.text)
        }
        return code.toString()
    }

    fun setText(code: String){
        if(code.length != mCodeNumber){
            return
        }
        for (i in 0 until mCodeNumber){
            mEdits[i].setText(code[i].toString())
        }
    }

    private var mInputListener: ISingleListener<String>? = null
    fun setInputListener(listener: ISingleListener<String>){
        mInputListener = listener
    }

    private var mFinishListener: ISingleListener<String>? = null
    fun setFinishListener(listener: ISingleListener<String>){
        mFinishListener = listener
    }
}