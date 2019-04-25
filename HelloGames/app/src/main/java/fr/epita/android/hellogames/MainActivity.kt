package fr.epita.android.hellogames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn1 = findViewById<View>(R.id.imageButton5)
        val btn2 = findViewById<View>(R.id.imageButton6)
        val btn3 = findViewById<View>(R.id.imageButton7)
        val btn4 = findViewById<View>(R.id.imageButton8)

        val data = arrayListOf<HelloGameObject>()
        val baseURL = "https://androidlessonsapi.herokuapp.com/api/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
                                .baseUrl(baseURL)
                                .addConverterFactory(jsonConverter)
                                .build()


        val wsCallback: Callback<List<HelloGameObject>> = object : Callback<List<HelloGameObject>> {
            override fun onFailure(call: Call<List<HelloGameObject>>, t: Throwable) {
                // Code here what happens if calling the WebService fails
                Log.w("TAG", "WebService call failed")     }

            override fun onResponse(call: Call<List<HelloGameObject>>, response: Response<List<HelloGameObject>>) {
                if (response.code() == 200) {             // We got our data ! //
                    val responseData = response.body()
                    if (responseData != null) {
                        Log.d("TAG", "WebService success : " + responseData.size)
                        data.addAll(responseData)
                        data.shuffle()
                        Glide         .with(this@MainActivity)
                                      .load(responseData[0].picture)
                                      .into(imageButton5)
                        Glide         .with(this@MainActivity)
                            .load(responseData[1].picture)
                            .into(imageButton6)
                        Glide         .with(this@MainActivity)
                            .load(responseData[2].picture)
                            .into(imageButton7)
                        Glide         .with(this@MainActivity)
                            .load(responseData[3].picture)
                            .into(imageButton8)

                        btn1.setOnClickListener()
                        {
                            val explicitIntent = Intent(this@MainActivity, MainActivity2::class.java)
                            val id = responseData[0].id
                            val picture = responseData[0].picture
                            explicitIntent.putExtra("id", id)
                            explicitIntent.putExtra("picture", picture)
                            startActivity(explicitIntent)
                        }

                    }
                }
            }
        }

        val service: WebServiceInterface = retrofit.create(WebServiceInterface::class.java)
        service.listToDos().enqueue(wsCallback)
    }
}

interface WebServiceInterface {
    @GET("game/list")
    fun listToDos(): Call<List<HelloGameObject>>
}
