package com.example.apitest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apitest.databinding.ActivityApiBinding
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

lateinit var binding3: ActivityApiBinding

data class MemesData(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: Memes
)

data class Memes(
    @SerializedName("memes")
    val memes: List<Meme>
)

data class Meme(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)

interface Api {
    @GET("get_memes")
    fun getMemes(): Call<MemesData>
}

object NetworkService{
    private const val BASE_URL = "https://api.imgflip.com/"
    private var retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApi(): Api {
        return  retrofit.create(Api::class.java)
    }
}



class MemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var nameTextView: TextView? = null
    var imageView: ImageView? = null

    init {
        nameTextView = itemView.findViewById(R.id.name)
        imageView = itemView.findViewById(R.id.image)
    }

}

class MemesAdapter : RecyclerView.Adapter<MemeViewHolder>() {

    private var memesList: List<Meme> = emptyList()

    fun changeList(list: List<Meme>) {
        memesList = list
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return memesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.meme_item,parent, false)
        return MemeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {

        val meme = memesList[position]
        holder.nameTextView?.text = meme.name
        Picasso.get().load(meme.url).into(holder.imageView)
    }
}

private var memesListRecyclerView: RecyclerView? = null
private var memesAdapter: MemesAdapter? = null

class ApiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding3 = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding3.root)

        memesListRecyclerView = binding3.memesList
        memesAdapter = MemesAdapter()
        memesListRecyclerView?.adapter = memesAdapter

        loadMemes()


    }

    fun loadMemes() {
        NetworkService
            .getApi()
            .getMemes()
            .enqueue(
                object : Callback<MemesData> {
                    override fun onFailure(call: Call<MemesData>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<MemesData>, response: Response<MemesData>) {
                        val list = response.body()?.data?.memes ?: emptyList()
                        memesAdapter?.changeList(list)
                    }
                }

            )
    }
}