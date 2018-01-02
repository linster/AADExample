package ca.stefanm.appwidgets

import android.app.Application
import ca.stefanm.appwidgets.backgroundupdates.BasicWidgetUpdateService


class WidgetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BasicWidgetUpdateService.initialize(this)
    }
}