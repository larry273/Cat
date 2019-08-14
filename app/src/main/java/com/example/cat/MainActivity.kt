package com.example.cat


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import android.os.AsyncTask
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

import kotlin.text.Regex




class MainActivity : AppCompatActivity() {

    internal lateinit var viewPager: ViewPager
    val adapter = ViewPageAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position > adapter.getCount() - 3){
                    addCatPhoto()
                }
            }

            override fun onPageSelected(position: Int) {
            }
        })

        //add cat 5 initial photos
        repeat(5){
            addCatPhoto()
            }
        }

    //get a random photo cat standing up photo from reddit
    private fun addCatPhoto(){
        ImageUrl(adapter).execute("https://old.reddit.com/r/CatsStandingUp/random")
    }

    //get the direct url of the reddit image and set the image
    inner class ImageUrl(internal val adpater:ViewPageAdapter) : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg urls: String): String? {
            //android.os.Debug.waitForDebugger()
            val reg = Regex("(http)?s?:?(\\/\\/[^\"']*\\.(?:png|jpg|jpeg|gif|png))")

            try {
                val sourceUrl = urls[0]
                while (true){
                    val doc = Jsoup.connect(sourceUrl).get()
                    val link = doc.select("div.media-preview-content > a")
                    var url = link.attr("href")

                    if (url != "" && url != null){
                        if (reg.matches(url)){
                            println("URL FOUND")
                            return url
                        }
                    }
                }
            }
            catch (e: IOException){
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            progressBar.visibility = View.GONE
            if (result != null){
                //Picasso.get().load(result).into(imageView)\
                println("url added ")
                Picasso.get().load(result).fetch()
                adapter.images.add(result)
                adpater.notifyDataSetChanged()
            }
            //else{
                //Toast.makeText(imageView.context, "Cat.", Toast.LENGTH_SHORT).show()
            //}
        }
    }
}



