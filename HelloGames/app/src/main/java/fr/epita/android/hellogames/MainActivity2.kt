package fr.epita.android.hellogames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)



        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(jsonConverter)
            .build()


        val wsCallback: Callback<HelloGameObject2> = object : Callback<HelloGameObject2> {
            override fun onFailure(call: Call<HelloGameObject2>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")     }

            override fun onResponse(call: Call<HelloGameObject2>, response: Response<HelloGameObject2>) {
                if (response.code() == 200) {             // We got our data ! //
                    val responseData = response.body()
                    if (responseData != null) {
                        val data = responseData

                        Glide         .with(this@MainActivity2)
                            .load(data.picture)
                            .into(imageView)
                        textView.setText(data.name)
                        textView3.setText(data.type)
                        textView4.setText(data.players)
                        textView5.setText(data.year)
                        textView6.setText(data.description_en)
                    }
                }
            }
        }
        val origin = intent
        val id = origin.getIntExtra("id", 1)

        val service: WebSInterface = retrofit.create(WebSInterface::class.java)
        service.listToDos(id).enqueue(wsCallback)
    }
}

interface WebSInterface {
    @GET("game/details")
    fun listToDos(@Query("game_id") game_id: Int): Call<HelloGameObject2>
}
