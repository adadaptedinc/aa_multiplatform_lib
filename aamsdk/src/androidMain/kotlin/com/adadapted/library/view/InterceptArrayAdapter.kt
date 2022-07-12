package com.adadapted.library.view

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.LayoutRes
import com.adadapted.library.keyword.Suggestion

class InterceptArrayAdapter(
    context: Context,
    @LayoutRes resource: Int,
    items: List<String> = listOf()
) :
    ArrayAdapter<Any>(context, resource, items) {

    internal var storedItems: MutableSet<String> = mutableSetOf()
    internal var suggestions: MutableList<String> = mutableListOf()
    internal var intercepts: MutableSet<Suggestion> = mutableSetOf()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return if (constraint != null) {
                    suggestions.clear()
                    storedItems.forEach {
                        if (it.startsWith(constraint.toString(), ignoreCase = true)) {
                            suggestions.add(it)
                        }
                    }
                    intercepts.forEach {
                        if (it.term.searchTerm.startsWith(constraint.toString(), ignoreCase = true)) {
                            suggestions.add(it.name)
                            it.presented()
                        }
                    }

                    val filterResults = FilterResults()
                    filterResults.values = suggestions
                    filterResults.count = suggestions.size
                    filterResults
                } else {
                    FilterResults()
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                val filterList = (results.values as? List<*>)?.distinct()
                if (results.count > 0) {
                    clear()
                    filterList?.forEach {
                        add(it)
                    }.also {
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun add(`object`: Any?) {
        storedItems.add(`object`.toString())
        super.add(`object`)
    }

    override fun addAll(vararg items: Any?) {
        storedItems.addAll(items.map { i -> i.toString() })
        super.addAll(*items)
    }

    override fun addAll(collection: MutableCollection<out Any>) {
        storedItems.addAll(collection.map { i -> i.toString() })
        super.addAll(collection.toTypedArray())
    }

    override fun getItem(position: Int): String {
        return super.getItem(position).toString()
    }

    fun addInterceptMatches(matches: Set<Suggestion>) {
        if (!intercepts.any { it in matches }) {
            for (match in matches) {
                intercepts.add(match)
            }
            notifyDataSetChanged()
        }
    }

    fun getIntercepts(): MutableSet<Suggestion> {
        return this.intercepts
    }

    fun suggestionSelected(suggestion: String) {
        intercepts.firstOrNull { i -> i.name == suggestion }?.selected()
    }

    init {
        storedItems = items.toMutableSet()
        suggestions = ArrayList()
    }
}