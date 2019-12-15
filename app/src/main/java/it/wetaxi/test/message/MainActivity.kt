package it.wetaxi.test.message

import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.iid.FirebaseInstanceId

import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.*
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.wetaxi.test.message.models.Message
import it.wetaxi.test.message.network.RetrofitClient
import it.wetaxi.test.message.network.objects.MessageResponse
import it.wetaxi.test.message.network.services.Messages
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.message_item.view.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : WeFirebaseMessagingService.MessageNotificationHandler,  AppCompatActivity() {
    val TAG = "MainActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        sortMessages()

        recycler_messages.layoutManager = LinearLayoutManager(this)
        recycler_messages.adapter = MessageAdapter(messages, this)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Chiama API per avere un nuovo messaggio", Snackbar.LENGTH_LONG).show()
            lifecycleScope.launch(Dispatchers.IO) {
                getMessage()
            }
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result?.token
                WeFirebaseMessagingService.token = token
                Log.d(TAG, token)
            })
    }

    private suspend fun getMessage(){
        val messageService = RetrofitClient.buildRetrofitClient(this).create(Messages::class.java)
        messageService.getMessages().enqueue(object : Callback<MessageResponse>{
            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e(TAG, Log.getStackTraceString(t))
            }

            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {validResponse ->
                        validResponse.message?.let {validMessage ->
                            messages.add(Message(validMessage))
                            sortMessages()
                            lifecycleScope.launch(Dispatchers.Main) {
                                recycler_messages.adapter?.notifyDataSetChanged()
                            }

                        }
                    }
                }
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Snackbar.make(root_layout, "Segna tutti messaggi come letti e li riordina", Snackbar.LENGTH_LONG).show()
                for (message in messages) {
                    message.read = true
                }
                sortMessages()
                recycler_messages.adapter?.notifyDataSetChanged()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class MessageAdapter(val items : MutableList<Message>, val context: Context) : RecyclerView.Adapter<MessageViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            return MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.message_item,parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val thisMessage = items[position]
            holder.dateTime.text = context.getString(R.string.messages_message_date_time,  thisMessage.date, thisMessage.time)
            holder.imgEnvelope.setImageDrawable(if (thisMessage.read) context.getDrawable(R.drawable.ic_read_24px) else context.getDrawable(R.drawable.ic_unread_24px))
            holder.messageText.text = thisMessage.text
            holder.priority.text = context.getString(R.string.messages_message_priority, thisMessage.priority)

        }
    }

    class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val dateTime = view.txt_date_time
        val imgEnvelope = view.img_envelope
        val priority = view.txt_priority
        val messageText = view.txt_message
    }

    override fun onNewNotification(message: Message) {
        messages.add(message)
        sortMessages()
        lifecycleScope.launch(Dispatchers.Main) {
            recycler_messages.adapter?.notifyDataSetChanged()
        }
    }

    private fun sortMessages(){
        messages.sortBy { it.priority }
        messages.sortBy { it.read }
    }

    override fun onPause() {
        super.onPause()
        WeFirebaseMessagingService.messageHandler = null
    }

    override fun onResume() {
        super.onResume()
        WeFirebaseMessagingService.messageHandler = this
    }

    companion object {
        val messages : MutableList<Message> = ArrayList()
    }

}
