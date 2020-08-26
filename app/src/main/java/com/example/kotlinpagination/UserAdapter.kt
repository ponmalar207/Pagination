package com.example.kotlinpagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class UserAdapter : PagedListAdapter<User, RecyclerView.ViewHolder>(USER_COMPARATOR) {
    private var networkState: NetworkState? = null

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val totalRatings: TextView = itemView.findViewById(R.id.tv_total_rating)
        private val personName: TextView = itemView.findViewById(R.id.tv_name)
        private val specialist: TextView = itemView.findViewById(R.id.tv_specialist)
        private val languages: TextView = itemView.findViewById(R.id.tv_languages)
        private val experience: TextView = itemView.findViewById(R.id.tv_experience)
        private val charge: TextView = itemView.findViewById(R.id.tv_charge)
        private val waitTime: TextView = itemView.findViewById(R.id.tv_wait_time)
        private val profileImage: ImageView = itemView.findViewById(R.id.iv_profileImage)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.rb_ratings)
        private val verify: ImageView = itemView.findViewById(R.id.iv_verify)
        private val bell: ImageView = itemView.findViewById(R.id.iv_bell)
        private var chat: Button = itemView.findViewById(R.id.btn_chat)
        fun bind(user: User) {
            personName.text = user.name
            specialist.text = user.specialist
            languages.text = user.languages
            experience.text = user.experience
            charge.text = user.charge
            waitTime.text = user.waitingTime
            totalRatings.text = user.totalRatings.toString()
            Glide.with(itemView.context).load(user.profileImage).circleCrop().into(profileImage)
            ratingBar.rating = user.ratings.toFloat()
//            if (user.status == 1) {    //invisible
//                val gradientDrawable = GradientDrawable()
//                gradientDrawable.cornerRadius = 20F
//                gradientDrawable.setStroke(3, Color.parseColor("#D84BCA5C"))
//                chat.background = gradientDrawable
//            } else {         //visible
//                bell.visibility = View.VISIBLE
//                waitTime.visibility = View.VISIBLE
//                val gradientDrawable = GradientDrawable()
//                gradientDrawable.cornerRadius = 20F
//                gradientDrawable.setStroke(3, Color.RED)
//                chat.background = gradientDrawable
//                chat.setTextColor(Color.RED)
//            }
        }
    }

    class NetworkStateItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val errorMsg: TextView = itemView.findViewById(R.id.tv_message)

        fun bindView(networkState: NetworkState?) {
            if (networkState != null && networkState.status == NetworkState.Status.RUNNING) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }

            if (networkState != null && networkState.status == NetworkState.Status.FAILED) {
                errorMsg.visibility = View.VISIBLE
                errorMsg.text = networkState.msg
            } else {
                errorMsg.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.astrologer_list, parent, false)
            UserViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.network_state, parent, false)
            NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserViewHolder) {
            getItem(position)?.let { holder.bind(it) }
        } else {
            (holder as NetworkStateItemViewHolder).bindView(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            TYPE_PROGRESS
        } else {
            TYPE_ITEM
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState !== NetworkState.LOADED
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState: NetworkState? = networkState
        val previousExtraRow = hasExtraRow()
        networkState = newNetworkState
        val newExtraRow = hasExtraRow()
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(itemCount)
            } else {
                notifyItemInserted(itemCount)
            }
        } else if (newExtraRow && previousState !== newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private const val TYPE_PROGRESS = 0
        private const val TYPE_ITEM = 1
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                newItem == oldItem
        }
    }
}

