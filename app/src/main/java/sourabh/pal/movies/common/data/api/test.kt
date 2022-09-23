package sourabh.pal.movies.common.data.api


import android.os.Bundle
import android.widget.TextView
import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import okhttp3.internal.toLongOrDefault
import sourabh.pal.movies.R

class MainActivity : Activity() {

    enum class TaskState {
        Todo,
        Done
    }

    private val taskList = mutableListOf<Pair<String, TaskState>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
    }

    private fun setupUI() {
        val adapter = createAdapter()
        attachAdapterToList(adapter)
        getData(adapter)
    }

    private fun attachAdapterToList(adapter: BaseAdapter) {
        val listView = findViewById<ListView>(R.id.list_view)
        listView.adapter = adapter
    }

    private fun createAdapter(): BaseAdapter {
        return MyAdapter()
    }


    private fun getData(adapter: BaseAdapter){
        taskList.clear()
        todoRepository.instance.fetch_all().forEachIndexed { index, task ->
            taskList.add(Pair(index.toString(), task))
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        taskList.clear()
    }

    inner class MyAdapter : BaseAdapter() {

        private lateinit var convertView: View

        override fun getCount(): Int {
            return taskList.size
        }

        override fun getItem(position: Int): Pair<String, TaskState> {
            val li = taskList.filter { it.second == TaskState.Todo } +
                     taskList.filter { it.second == TaskState.Done }
            return li[position]
        }

        override fun getItemId(position: Int): Long {
            return taskList[position].first.toLongOrDefault(0L)
        }

        override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
            this.convertView =
                convertView ?: layoutInflater.inflate(R.layout.list_item, container, false)

            val currentItem = getItem(position)
            this.convertView.findViewById<TextView>(R.id.item_label)
                .apply {
                    when (currentItem.second) {
                        TaskState.Todo -> {
                            text = "TODO"
                            setBackgroundColor(Color.YELLOW)
                        }
                        else -> {
                            text = "DONE"
                        }
                    }
                }
            this.convertView.findViewById<TextView>(R.id.item_text).text = currentItem.first
            return this.convertView
        }
    }
}