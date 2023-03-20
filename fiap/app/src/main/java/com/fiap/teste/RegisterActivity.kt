package com.fiap.teste

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth= FirebaseAuth.getInstance()
        db= FirebaseFirestore.getInstance()
        Continue.setOnClickListener {
            if(checking())
            {
                var email=EmailRegister.text.toString()
                var password= PasswordRegister.text.toString()
                var name=Name.text.toString()
                var phone=Phone.text.toString()
                val user= hashMapOf(
                    "Name" to name,
                    "Phone" to phone,
                    "email" to email
                )
                val Users=db.collection("USERS")
                val query =Users.whereEqualTo("email",email).get()
                    .addOnSuccessListener {
                        tasks->
                        if(tasks.isEmpty)
                        {
                            auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(this){
                                    task->
                                    if(task.isSuccessful)
                                    {
                                            Users.document(email).set(user)
                                            val intent=Intent(this,LoggedIn::class.java)
                                            intent.putExtra("email",email)
                                            startActivity(intent)
                                            finish()
                                    }
                                    else
                                    {
                                        Toast.makeText(this,"Falha de autenticação", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else
                        {
                            Toast.makeText(this,"Usuário já registrado", Toast.LENGTH_LONG).show()
                            val intent= Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            else{
                Toast.makeText(this,"Insira suas informações", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun checking():Boolean{
        if(Name.text.toString().trim{it<=' '}.isNotEmpty()
            && Phone.text.toString().trim{it<=' '}.isNotEmpty()
            && EmailRegister.text.toString().trim{it<=' '}.isNotEmpty()
            && PasswordRegister.text.toString().trim{it<=' '}.isNotEmpty()
        )
        {
            return true
        }
        return false
    }
}