package per.wsj.andratingbar.ui

import android.graphics.drawable.ClipDrawable
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

        ratingBar1.setOnRatingChangeListener { ratingBar, rating ->
            Log.e("ratingBar", "rating:$rating")
        }

        ratingBar2.setOnRatingChangeListener { ratingBar, rating ->
            Log.e("aaa", "rating:$rating")
        }

        ratingBar3.setOnRatingChangeListener { ratingBar, rating ->
            Log.e("aaa", "rating:$rating")
        }
    }
}