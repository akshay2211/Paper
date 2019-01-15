package com.pyrocodes.paper

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.pyrocodes.fragments.MainFragment
import io.realm.Realm

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        realm = Realm.getDefaultInstance()
        supportFragmentManager.beginTransaction().replace(R.id.container, MainFragment.newInstance("", "")).commitNow()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
