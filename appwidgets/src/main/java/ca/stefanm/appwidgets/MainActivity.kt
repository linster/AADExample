package ca.stefanm.appwidgets

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ca.stefanm.appwidgets.backgroundupdates.BasicWidgetUpdateService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
