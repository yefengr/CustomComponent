package cn.com.hmsm.formtextview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.button.MaterialButton
import kotlin.properties.Delegates


/**
 * 表单下拉选
 */
@SuppressLint("AppCompatCustomView")
class FormTextView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {


    private fun Context.dp2px(dpValue: Float): Int {
        return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
    }

    fun Context.sp2px(spValue: Float): Int {
        return (spValue * resources.displayMetrics.scaledDensity + 0.5f).toInt()
    }


    private val TAG: String = "FormTextView"

    // 自定义参数
    // 长度单位为px
    lateinit var labelValue: String
    var labelSize by Delegates.notNull<Float>()
    var labelMarginStart by Delegates.notNull<Float>()
    var labelColor by Delegates.notNull<Int>()

    lateinit var textValue: String
    var textSize by Delegates.notNull<Float>()
    var textColor by Delegates.notNull<Int>()
    var textMarginEnd by Delegates.notNull<Float>()


    // 是否显示分割线
    var dividingLineShow by Delegates.notNull<Boolean>()
    var dividingLineSize by Delegates.notNull<Float>()
    var dividingLineColor by Delegates.notNull<Int>()


    var clickRipperColor by Delegates.notNull<Int>()
    var iconEndSize by Delegates.notNull<Float>()
    var iconEndColor by Delegates.notNull<Int>()
    var iconEndShow by Delegates.notNull<Boolean>()


    lateinit var root: ConstraintLayout
    lateinit var line: LinearLayout
    lateinit var textView: TextView
    lateinit var labelView: TextView
    lateinit var dividingLine: View
    lateinit var button: MaterialButton
    var spinnerWidth: Float = 0f


    init {
        initAttr(context, attrs)
        initView(context)
    }

    /**
     * 初始化参数
     */
    private fun initAttr(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }
        var a: TypedArray? = null

        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.FormTextView)
            labelValue = a.getString(R.styleable.FormTextView_labelValue) ?: "标签"
            labelSize = context.sp2px(a.getDimension(R.styleable.FormTextView_labelSize, 25f)).toFloat()
            labelMarginStart = context.dp2px(a.getDimension(R.styleable.FormTextView_labelMarginStart, 0f)).toFloat()
            labelColor = a.getColor(R.styleable.FormTextView_labelColor, Color.BLACK)

            textValue = a.getString(R.styleable.FormTextView_textValue) ?: "数据"
            textSize = context.sp2px(a.getDimension(R.styleable.FormTextView_textSize, 25f)).toFloat()
            textColor = a.getColor(R.styleable.FormTextView_textColor, Color.BLACK)
            textMarginEnd = context.dp2px(a.getDimension(R.styleable.FormTextView_textMarginEnd, 0f)).toFloat()


            dividingLineShow = a.getBoolean(R.styleable.FormTextView_dividingLineShow, true)
            dividingLineSize = context.sp2px(a.getDimension(R.styleable.FormTextView_dividingLineSize, 2f)).toFloat()
            dividingLineColor = a.getColor(R.styleable.FormTextView_dividingLineColor, Color.BLACK)

            clickRipperColor = a.getColor(R.styleable.FormTextView_clickRipperColor, Color.WHITE)
            iconEndColor = a.getColor(R.styleable.FormTextView_iconEndColor, Color.BLACK)
            iconEndSize = context.sp2px(a.getDimension(R.styleable.FormTextView_iconEndSize, 25f)).toFloat()
            iconEndShow = a.getBoolean(R.styleable.FormTextView_iconEndShow, true)


        } catch (e: Exception) {
            Log.e(TAG, "加载资源出错", e)
        } finally {
            a?.recycle()
        }
    }


    private fun initView(context: Context) {
        View.inflate(context, R.layout.layout_form_view, this)
        line = findViewById(R.id.line)
        labelView = findViewById(R.id.label)
        textView = findViewById(R.id.text)
        dividingLine = findViewById(R.id.dividingLine)
        button = findViewById(R.id.button)

        val rootPaddingStart = paddingStart
        val rootPaddingEnd = paddingEnd
        this.setPadding(paddingTop, 0, paddingBottom, 0)
        line.setPadding(rootPaddingStart, 0, rootPaddingEnd, 0)



        labelView.text = labelValue
        labelView.textSize = labelSize
        labelView.setTextColor(labelColor)
        (labelView.layoutParams as LinearLayout.LayoutParams).setMargins(labelMarginStart.toInt(), 0, 0, 0)


        textView.textSize = textSize
        textView.text = textValue
        textView.setTextColor(textColor)
        (textView.layoutParams as LinearLayout.LayoutParams).setMargins(
            0,
            0,
            textMarginEnd.toInt() + iconEndSize.toInt() * 3 / 2,
            0
        )


        if (dividingLineShow) {
            dividingLine.visibility = VISIBLE
            dividingLine.setBackgroundColor(dividingLineColor)
            dividingLine.layoutParams.height = dividingLineSize.toInt()
            (dividingLine.layoutParams as LayoutParams).marginStart = rootPaddingStart
            (dividingLine.layoutParams as LayoutParams).marginEnd = rootPaddingEnd
        } else {
            dividingLine.visibility = GONE
        }


        // 点击按钮相关样式
        // icon
        if (iconEndShow) {
            button.icon = ContextCompat.getDrawable(this.context, R.drawable.to_right)
            button.icon.setVisible(true, true)
            button.iconSize = iconEndSize.toInt()
            button.setPadding(rootPaddingEnd)
            // 二维数组：因为会有多个item，每个item又是由多个状态组合来的。
            val iconStates = arrayOfNulls<IntArray>(1)
            // 没有值表示默认状态
            iconStates[0] = intArrayOf()
            button.iconTint = ColorStateList(iconStates, intArrayOf(iconEndColor))
        } else {
            button.icon = null
        }


        val buttonStates = arrayOfNulls<IntArray>(1)
        // 没有值表示默认状态
        buttonStates[0] = intArrayOf()
        button.rippleColor = ColorStateList(buttonStates, intArrayOf(clickRipperColor))

    }


    fun setOnClick(click: (String) -> Unit) {
        button.setOnClickListener {
            click(textValue)
        }
    }


}
