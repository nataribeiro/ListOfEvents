package br.com.natanaelribeiro.events.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.natanaelribeiro.basefeature.utils.SystemUtil
import br.com.natanaelribeiro.basefeature.utils.toFormattedDate
import br.com.natanaelribeiro.basefeature.utils.toFormattedPrice
import br.com.natanaelribeiro.events.R
import br.com.natanaelribeiro.events.customviews.TopRoundedTransformation
import br.com.natanaelribeiro.eventsdata.models.Event
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_item.view.*
import java.lang.Exception

class EventAdapter(
    private var events: List<Event>,
    val context: Context,
    val itemClickListener: (Event) -> Unit
): RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.event_item, parent, false))
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val event = events[position]

        val httpsImageUrl = event.imageUrl.replace("http", "https")
        val radius = SystemUtil.getPixelFromDp(context, 20)
        val transformation = TopRoundedTransformation(radius.toFloat(), 0f, true, false)

        Picasso.get()
            .load(httpsImageUrl)
            .transform(transformation)
            .error(R.drawable.ic_unavailable_image)
            .fit()
            .into(holder.eventImageView, object : Callback {
                override fun onSuccess() {
                    holder.loadingImage.visibility = View.GONE
                }
                override fun onError(e: Exception?) {
                    holder.loadingImage.visibility = View.GONE
                }
            })

        holder.eventTitleTextView.text = event.title
        holder.eventPriceTextView.text = event.price.toFormattedPrice()
        holder.eventDateTextView.text = event.date.toFormattedDate("dd/MM/yyyy")
        holder.eventCardView.setOnClickListener {
            itemClickListener.invoke(event)
        }
    }

    open class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val eventImageView: ImageView = view.eventImageView
        val eventTitleTextView: TextView = view.eventTitleTextView
        val eventPriceTextView: TextView = view.eventPriceTextView
        val eventDateTextView: TextView = view.eventDateTextView
        val eventCardView: View = view.eventCardView
        val loadingImage: ProgressBar = view.loadingImage
    }
}