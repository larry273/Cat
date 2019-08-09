package com.example.cat


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import android.os.AsyncTask
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //get a random photo cat standing up photo from reddit
    fun setCatPhoto(view: View){
        val image = findViewById<ImageView>(R.id.imageUrl)
        ImageUrl(image).execute("https://old.reddit.com/r/CatsStandingUp/random")
    }
}

//get the url of the reddit image
private class ImageUrl(internal val imageView: ImageView) : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg urls: String): String? {
        android.os.Debug.waitForDebugger()
        try {
            while (true){
                val random_url = urls[0]
                val doc = Jsoup.connect(random_url).get()
                val link = doc.select("div.media-preview-content > a")
                var url = link.attr("href")

                if (url != "" && url != null){
                    return url
                }
            }
        }
        catch (e: Exception){
            return null
        }
    }

    override fun onPostExecute(result: String?) {
        if (result != null){
            Picasso.get().load(result).into(imageView)
        }
        else{
            Toast.makeText(imageView.context, "Cat.", Toast.LENGTH_SHORT).show()
        }
    }
}

