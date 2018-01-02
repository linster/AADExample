package ca.stefanm.appwidgets

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import ca.stefanm.appwidgets.models.BasicWidgetViewModel

/**
 * Implementation of App Widget functionality.
 */
class BasicWidget : BroadcastReceiver() {

    //To see where this came from look in AppWidgetProvider
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action !in listOf(
                AppWidgetManager.ACTION_APPWIDGET_UPDATE,
                AppWidgetManager.ACTION_APPWIDGET_DELETED,
                AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED,
                AppWidgetManager.ACTION_APPWIDGET_ENABLED,
                AppWidgetManager.ACTION_APPWIDGET_DISABLED,
                AppWidgetManager.ACTION_APPWIDGET_RESTORED
        )){ return }

        //Useful for update.
        //AppWidgetIds on update contains which widget updated (such as a resize by the user, etc). If missing,
        //then we'll just use all the ids instantiated by the user that could possibly need an update.
        //Google designed this API so that you only have to update the widgets that changed, not all of them all the time.
        val appWidgetIds : IntArray = intent.extras?.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS)
                ?: AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, this::class.java))

        //Useful for Delete, OptionsChanged
        val appWidgetId : Int? = intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)

        when (intent.action) {

            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                for (id in appWidgetIds.asList()){
                    updateAppWidget(context, id, getViewModelFromIntent(intent))
                }
            }

            AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED -> {
                //Called on resize.
                if (appWidgetId != null &&
                        intent.extras != null &&
                        intent.extras.containsKey(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS)) {

                    val widgetExtras = intent.extras.getBundle(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS)
                }
            }
        }
    }

    fun getViewModelFromIntent(intent: Intent) : BasicWidgetViewModel {
        return BasicWidgetViewModel(
                magicNumber = intent.getIntExtra("viewModel_number", 0),
                timestamp = intent.getLongExtra("viewModel_ts", 0)
        )
    }

    internal fun updateAppWidget(context: Context, appWidgetId: Int, viewModel: BasicWidgetViewModel) {

        Log.d("BasicWidget", "Updating widget!")

        // RemoteViews is really neat. It's how processes can manipulate views in other processes.
        // For example, how an app can manipulate a widget in a launcher app (widget host).

        val views = RemoteViews(context.packageName, R.layout.basic_widget)
        views.setTextViewText(R.id.bw_number, viewModel.magicNumber.toString())
        views.setTextViewText(R.id.bw_ts, viewModel.timestamp.toString())

        // Instruct the widget manager to update the widget
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
    }

    companion object {

        fun getUpdateIntent(context: Context, viewModel: BasicWidgetViewModel) : Intent {
            val intent = Intent()

            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

            intent.putExtra("viewModel_number", viewModel.magicNumber)
            intent.putExtra("viewModel_ts", viewModel.timestamp)

            return intent
        }

    }
}

