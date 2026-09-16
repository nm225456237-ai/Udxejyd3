package com.taskpostassistant

import android.app.Activity
import android.os.Bundle
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.*
import android.graphics.Color
import android.view.ViewGroup

class MainActivity : Activity() {
    private lateinit var clipboard: ClipboardManager
    private val fields = linkedMapOf<String, EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24,24,24,24)
        }

        root.addView(TextView(this).apply {
            text = "Task Post Assistant v2"
            textSize = 24f
        })
        root.addView(TextView(this).apply {
            text = "\nاز Telegram هر مورد را خودت Copy کن، سپس اینجا Paste کن."
            textSize = 16f
        })

        addField(root, "Password")
        addField(root, "Name")
        addField(root, "Username / Landing")

        root.addView(Button(this).apply {
            text = "باز کردن Instagram"
            setOnClickListener {
                packageManager.getLaunchIntentForPackage("com.instagram.android")?.let(::startActivity)
                    ?: Toast.makeText(this@MainActivity, "Instagram نصب نیست.", Toast.LENGTH_SHORT).show()
            }
        })

        root.addView(Button(this).apply {
            text = "پاک کردن همه اطلاعات"
            setOnClickListener {
                fields.values.forEach { it.text.clear() }
            }
        })

        root.addView(TextView(this).apply {
            text = "\nنکته امنیتی: این برنامه رمز را در فایل دائمی ذخیره نمی‌کند و انتشار/ارسال نهایی را خودکار انجام نمی‌دهد."
            textSize = 14f
        })

        setContentView(root)
    }

    private fun addField(parent: LinearLayout, title: String) {
        val label = TextView(this).apply {
            text = "\n$title"
            textSize = 17f
        }
        parent.addView(label)

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
        }

        val input = EditText(this).apply {
            hint = "Paste $title"
            if (title == "Password") inputType = 0x81
        }
        fields[title] = input
        row.addView(input, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))

        row.addView(Button(this).apply {
            text = "Paste"
            setOnClickListener {
                val clip = clipboard.primaryClip
                val value = if (clip != null && clip.itemCount > 0)
                    clip.getItemAt(0).coerceToText(this@MainActivity).toString()
                else ""
                input.setText(value)
            }
        })

        row.addView(Button(this).apply {
            text = "Copy"
            setOnClickListener {
                clipboard.setPrimaryClip(ClipData.newPlainText(title, input.text.toString()))
                Toast.makeText(this@MainActivity, "$title copied", Toast.LENGTH_SHORT).show()
            }
        })
        parent.addView(row)
    }
}
