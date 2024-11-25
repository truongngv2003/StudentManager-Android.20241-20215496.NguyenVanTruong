package com.example.studentmanagerwithmenu

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )
    private val studentAdapter = StudentAdapter(students)
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var launcher1: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),{
            if(it.resultCode == Activity.RESULT_OK) {
                val newName = it.data?.getStringExtra("newName").toString()
                val newId = it.data?.getStringExtra("newId").toString()
                students.add(0, StudentModel(newName, newId))
                studentAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Thêm sinh viên mới thành công", Toast.LENGTH_LONG).show()
            }
        })

        launcher1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),{
            if(it.resultCode == Activity.RESULT_OK) {
                val newName = it.data?.getStringExtra("newName").toString()
                val newId = it.data?.getStringExtra("newId").toString()
                val position = it.data?.getIntExtra("position", -1)
                if(position!! > -1){
                    students[position].studentName = newName
                    students[position].studentId = newId
                    studentAdapter.notifyDataSetChanged()
                    Toast.makeText(this, "Thay đổi thông tin sinh viên thành công", Toast.LENGTH_LONG).show()
                } else{
                    Toast.makeText(this, "Thay đổi thông tin sinh viên thất bại", Toast.LENGTH_LONG).show()
                }

            }
        })

        val studentList = findViewById<ListView>(R.id.student_list)
        studentList.adapter = studentAdapter

        registerForContextMenu(studentList)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.button_add_student -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                launcher.launch(intent)
//                startActivity(intent)
//                Toast.makeText(this, "Share action", Toast.LENGTH_LONG).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.context_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    @SuppressLint("ShowToast")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pos = (item.menuInfo as AdapterContextMenuInfo).position
        when(item.itemId) {
            R.id.button_edit_student -> {
                val studentName = students[pos].studentName
                val studentId = students[pos].studentId
                val intent = Intent(this, AddStudentActivity::class.java)
                intent.putExtra("studentName", studentName)
                intent.putExtra("studentId", studentId)
                intent.putExtra("position", pos)
                launcher1.launch(intent)
            }

            R.id.button_delete_student -> {
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.baseline_question_mark_24)
                    .setTitle("Xác nhận xóa sinh viên!")
                    .setMessage("Bạn chắc chắn muốn xóa sinh viên:\n${students[pos].studentName}-${students[pos].studentId}")
                    .setPositiveButton("Ok") { _, _ ->
                        val studentName = students[pos].studentName
                        val studentId = students[pos].studentId
                        students.removeAt(pos)
                        studentAdapter.notifyDataSetChanged()
                        Snackbar.make(findViewById(R.id.main), "Đã xóa 1 học sinh",  Snackbar.LENGTH_LONG)
                            .setAction("Hoàn tác") {
                                students.add(pos, StudentModel(studentName, studentId))
                                studentAdapter.notifyDataSetChanged()
                            }
                            .show()
                    }
                    .setNegativeButton("Cancel", null)
                    .setCancelable(false)
                    .show()

            }
        }
        return super.onContextItemSelected(item)
    }
}