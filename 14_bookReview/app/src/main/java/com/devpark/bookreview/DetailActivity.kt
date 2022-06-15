package com.devpark.bookreview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.devpark.bookreview.databinding.ActivityDetailBinding
import com.devpark.bookreview.model.Book
import com.devpark.bookreview.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book?>("bookModel")

        model?.title.orEmpty().also { binding.titleTextView.text = it }
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context).load(model?.coverSmallUrl.orEmpty()).into(binding.coverImageView)


        Thread {
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            //Log.e("DetailActivity", review?.review.orEmpty())
            val str = review?.review.orEmpty()
            runOnUiThread {
                binding.reviewEditText.setText(str)
            }

        }.start()

        binding.saveButton.setOnClickListener {
            Thread {

                db.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?: 0,
                        binding.reviewEditText.text.toString()
                    )
                )

            }.start()
        }
    }

}