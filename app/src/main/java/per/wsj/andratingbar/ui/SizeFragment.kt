package per.wsj.andratingbar.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_size.*
import per.wsj.andratingbar.R

class SizeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_size, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        ratingBar0.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
//            Log.e("ratingBar0", "rating:$rating -- fromUser: $fromUser")
//        }

        ratingBar1.setOnRatingChangeListener { _, rating, fromUser ->
            Log.e("ratingBar1", "rating:$rating -- fromUser: $fromUser")
            tvRatingValue.text = "value:$rating"
        }

        ratingBar1.rating = 4.0f

        ratingBar2.setOnRatingChangeListener { _, rating, fromUser ->
            Log.e("ratingBar2", "rating:$rating -- fromUser: $fromUser")
        }

        ratingBar3.setOnRatingChangeListener { _, rating, fromUser ->
            Log.e("ratingBar3", "rating:$rating -- fromUser: $fromUser")
        }
    }
}