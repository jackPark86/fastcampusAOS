package com.devpark.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.room.Room
import com.devpark.calculator.model.History
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private val expressionTV: TextView by lazy {
        findViewById<TextView>(R.id.tv_expression)
    }

    private val resultTv: TextView by lazy {
        findViewById<TextView>(R.id.tv_result)
    }

    private val historyLayout: View by lazy {
        findViewById<View>(R.id.layout_history)
    }

    private val historyLinearLayout: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.layout_linear_history)
    }

    private var isOperator = false
    private var hasOperator = false

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()

    }//end of onCreate


    fun onBtnClick(v: View) {
        when (v.id) {
            R.id.btn00 -> numberBtnClicked("0")
            R.id.btn01 -> numberBtnClicked("1")
            R.id.btn02 -> numberBtnClicked("2")
            R.id.btn03 -> numberBtnClicked("3")
            R.id.btn04 -> numberBtnClicked("4")
            R.id.btn05 -> numberBtnClicked("5")
            R.id.btn06 -> numberBtnClicked("6")
            R.id.btn07 -> numberBtnClicked("7")
            R.id.btn08 -> numberBtnClicked("8")
            R.id.btn09 -> numberBtnClicked("9")
            R.id.btn_add -> operatorBtnClicked("+")
            R.id.btn_minus -> operatorBtnClicked("-")
            R.id.btn_multiply -> operatorBtnClicked("*")
            R.id.btn_division -> operatorBtnClicked("/")
            R.id.btn_precent -> operatorBtnClicked("%")
        }


    }//end of onBtnClick

    private fun numberBtnClicked(number: String) {
        if (isOperator) {
            expressionTV.append(" ")
        }

        isOperator = false

        val expressionText = expressionTV.text.split(" ")

        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다!", Toast.LENGTH_SHORT).show()
            return
        } else if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        expressionTV.append(number)
        resultTv.text = calculateExpression()

    }//end of numberBtnClicked

    private fun operatorBtnClicked(operator: String) {
        if (expressionTV.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = expressionTV.text.toString()
                expressionTV.text = text.dropLast(1) + operator
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다", Toast.LENGTH_SHORT).show()
            }

            else -> {
                expressionTV.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(expressionTV.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            expressionTV.text.length - 1,
            expressionTV.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        expressionTV.text = ssb
        isOperator = true
        hasOperator = true

    }//end of operatorBtnClicked

    fun onClearClick(v: View) {
        expressionTV.text = ""
        resultTv.text = ""
        isOperator = false
        hasOperator = false
    }//end of onClearClick

    fun onBtnResult(v: View) {
        val expressionTexts = expressionTV.text.split(" ")
        if (expressionTV.text.isEmpty() || expressionTexts.size == 1) {
            return
        }

        if (expressionTexts.size != 3 && hasOperator) {
            Toast.makeText(this, "아직 완성되지 않은 수식입니다!", Toast.LENGTH_SHORT).show()
        }

        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }

        val expressionText = expressionTV.text.toString()
        val resultText = calculateExpression()

        //TODO 디비에 넣어주는 부분
        Thread(Runnable {
            db.historyDao().insertHistory(History(null, expressionText, resultText))
        }).start()

        resultTv.text = ""
        expressionTV.text = resultText

        isOperator = false
        hasOperator = false

    }//end of onBtnResult


    // 연산 후 결과 반환
    private fun calculateExpression(): String {
        val expressionTexts = expressionTV.text.split(" ")

        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }

        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()

        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }

    }

    fun onBtnHistory(v: View) {
        historyLayout.isVisible = true
        historyLinearLayout.removeAllViews()


        Thread(Runnable {
            db.historyDao().getAll().reversed().forEach {
                runOnUiThread {
                    val historyView = LayoutInflater.from(this).inflate(R.layout.history_row, null, false)
                    historyView.findViewById<TextView>(R.id.tv_expression).text = it.expression
                    historyView.findViewById<TextView>(R.id.tv_result).text = "= ${it.result}"

                    historyLinearLayout.addView(historyView)

                }
            }
        }).start()
        //TODO 디비에서 모든 기록 가져오기
        //TODO 뷰에서 모든 기록 할당하기


    }//end of onBtnHistory

    fun onBtnHistoryClose(v: View) {
        historyLayout.isVisible = false
    }//end of onBtnHistory

    fun onBtnHistoryClear(v: View) {
        //TODO 디비에서 모든 기록 삭제
        //TODO 뷰에서 모든 기록 삭
        historyLinearLayout.removeAllViews()
        Thread(Runnable {
            db.historyDao().deleteAll()
        }).start()
    }//end of onBtnHistory


}//end of class

//스트링 확장함수 만들기
//객체.확장함수명() 으로 기존의 객체에 새로운 함수를 만들 수 있다.
fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}