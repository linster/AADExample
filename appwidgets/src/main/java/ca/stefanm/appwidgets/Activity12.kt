package ca.stefanm.appwidgets

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Activity12 : AppCompatActivity() {

    companion object {

        fun newIntent(context: Context, number : Int) : Intent {
            val intent = Intent(context, Activity12::class.java)
            intent.putExtra("number", number)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_12)

        findViewById<TextView>(R.id.tv_a12_num).setText(intent.getIntExtra("number", 0).toString())

    }
}
