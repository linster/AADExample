package ca.stefanm.appwidgets.backgroundupdates

import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import ca.stefanm.appwidgets.BasicWidget
import ca.stefanm.appwidgets.models.BasicWidgetViewModel
import com.firebase.jobdispatcher.*


class BasicWidgetUpdateService : JobService() {


    companion object {

        lateinit private var jobDispatcher : FirebaseJobDispatcher

        fun initialize(context: Context) {
            //Setup the job dispatcher and schedule this job to some frequency.
            jobDispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))

            val job = jobDispatcher
                    .newJobBuilder()
                    .setService(BasicWidgetUpdateService::class.java)
                    .setTag("BasicWidgetUpdateService")
                    .setRecurring(true)
                    .setLifetime(Lifetime.FOREVER)
                    .setTrigger(Trigger.executionWindow(3, 5))
                    .build()

            jobDispatcher.mustSchedule(job)

        }

        //Singleton basic widget view model. IRL this would be persisted by something else.
        //None of this is clean architecture at all.
        var viewModel = BasicWidgetViewModel()

    }

    override fun onStartJob(job: JobParameters?): Boolean {
        updateBasicWidget(viewModel, System.currentTimeMillis() / 1000)
        return false //Is there still work going on?
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        return false //Should job be retried? == false
    }

    //IRL this should be in an Impl class, that implements an interface containing this, and ways
    //for this to get the state it needs to populate the widget.
    private fun updateBasicWidget(existingViewModel: BasicWidgetViewModel, time : Long) {

        val newViewModel = existingViewModel.copy(
                magicNumber = (existingViewModel.magicNumber + 1 ).rem(5),
                timestamp = time
        )

        viewModel = newViewModel

        Log.d("UpdateService", "Sending broadcast to update!")
        sendBroadcast(BasicWidget.getUpdateIntent(this, viewModel))
    }

}