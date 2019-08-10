package com.example.cat


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import android.os.AsyncTask
import android.widget.Button
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //fetch photo on start
        val button = this.button
        button.performClick()
        }

    //get a random photo cat standing up photo from reddit
    fun setCatPhoto(view: View){
        val image = findViewById<ImageView>(R.id.imageUrl)
        ImageUrl(image).execute("https://old.reddit.com/r/CatsStandingUp/random")
    }

    //get the direct url of the reddit image and set the image
    inner class ImageUrl(internal val imageView: ImageView) : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg urls: String): String? {
            //android.os.Debug.waitForDebugger()

            try {
                val sourceUrl = urls[0]
                while (true){
                    val doc = Jsoup.connect(sourceUrl).get()
                    val link = doc.select("div.media-preview-content > a")
                    var url = link.attr("href")

                    if (url != "" && url != null){
                        return url
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
                Picasso.get().load(result).into(imageView)
            }
            else{
                Toast.makeText(imageView.context, "Cat.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



