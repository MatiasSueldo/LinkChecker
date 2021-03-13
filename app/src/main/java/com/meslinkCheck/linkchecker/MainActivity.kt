package com.meslinkCheck.linkchecker

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.widget.TextView
import androidx.annotation.Keep
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine


interface MyInterface{
    fun onCallback(response:Boolean)
}
class MainActivity : AppCompatActivity()  {

    val myInterface = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notApprovedLinkText: TextView = findViewById(R.id.textView) as TextView
        val approvedLinkText: TextView = findViewById(R.id.textView2) as TextView

        approvedLinkText.isVisible = false
        notApprovedLinkText.isVisible = false

//        BottomSheetCall()

        var link: String = "dp8d.cn"
        link = "http://spotify-us.com/"



        if(isSecure(link)) {
            approvedLinkText.isVisible =true;

            continu.setOnClickListener {
                var i:Intent =  Intent(Intent.ACTION_WEB_SEARCH)
                i.putExtra(SearchManager.QUERY,link)
                startActivity(i)

            }


        }
        else {
            notApprovedLinkText.isVisible =true;
            continu.setOnClickListener {
                var i:Intent =  Intent(Intent.ACTION_WEB_SEARCH)
                i.putExtra(SearchManager.QUERY,link)
                startActivity(i)

            }
        }

    }


    fun BottomSheetCall(){
        val bottomSheetCallback = object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {}
            override fun onStateChanged(p0: View, p1: Int) {}
        }
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun isSecure(linkToCheck:String):Boolean {





        return checkLink(linkToCheck) && CheckReferences(linkToCheck)


    }

    fun checkLink(linkToCheck: String):Boolean {

        val regex = "[a-zA-Z]\\.com".toRegex(setOf(RegexOption.IGNORE_CASE))
        val regex2 = "[a-zA-Z]\\.com\\.ar".toRegex(setOf(RegexOption.IGNORE_CASE))

        return regex.containsMatchIn(linkToCheck) || regex2.containsMatchIn(linkToCheck)
    }

   suspend fun CheckReferences(linkToCheck: String):Boolean
    {


        val gson = Gson()
        val APIKey: String = "AIzaSyAbTjtsPniTPWLlFhGexWyZyo7dqRjHUT4"
        val cx:String = "653dbe73590d86847"
        var respuesta1:String =""
        val queue =  Volley.newRequestQueue(this)
        val url ="https://www.googleapis.com/customsearch/v1?num=2&q=https://www.dp8d.cn/&cx=${cx}&key=${APIKey}"

        var respuesta2:GoogleResponse

        var sarasa:Boolean= false
        var queueFinished:Boolean=false
        GlobalScope.async {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
              Response.Listener { response ->
                    "Response: %s".format(response.toString())
                    respuesta2= gson.fromJson(response.toString(),GoogleResponse::class.java)
                    //respuesta2.containsVirusFather()
                    //sarasa = respuesta2.containsViruses!!
                },
                Response.ErrorListener { error ->
                     //TODO: Handle error
                }
        )
            jsonObjectRequest.
        //GlobalScope.launch {

        //}
        //runBlocking { queue.add(jsonObjectRequest) }
        queue.add(jsonObjectRequest)
        }

        return sarasa


    }








    class GoogleResponse {
            var items: List<item>? = null
            var containsViruses:Boolean? = false

            fun containsVirusFather():Boolean?
            {
                for(it in this.items!!)
                {
                    if (it.containsVirus())
                    {
                        containsViruses=true
                    }
                }
                return false

            }
        }
    }


    class item{
        var htmlTitle:String=""
        var snippet:String=""
        var htmlSnippet:String=""
        fun containsVirus():Boolean
        {

            if (this.htmlSnippet.contains("virus"))
                return true
            if(this.snippet.contains("virus"))
                return true
            if(this.htmlTitle.contains("virus"))
            return true

            return false

        }
    }
    class ChildrenGoogleResponse{
        var errors:String = ""
      //  var Items:list<Strings>=""
    }





