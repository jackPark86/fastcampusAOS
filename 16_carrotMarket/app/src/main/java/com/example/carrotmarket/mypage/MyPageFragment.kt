package com.example.carrotmarket.mypage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.carrotmarket.R
import com.example.carrotmarket.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.timerTask
import kotlin.math.sign

class MyPageFragment : Fragment(R.layout.fragment_mypage) {

    private var binding: FragmentMypageBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentMypageBinding = FragmentMypageBinding.bind(view)
        binding = fragmentMypageBinding


        fragmentMypageBinding.signInOutButton.setOnClickListener {
            binding?.let { binding ->
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                if (auth.currentUser == null) {
                    //로그인
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                successSignIn()
                            } else {
                                Toast.makeText(context, "로그인 실패!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    auth.signOut()
                    binding.emailEditText.text.clear()
                    binding.emailEditText.isEnabled = true
                    binding.passwordEditText.text.clear()
                    binding.passwordEditText.isEnabled = true

                    binding.signInOutButton.text = "로그인"
                    binding.signInOutButton.isEnabled = false
                    binding.signUpButton.isEnabled = false
                }
            }
        }

        fragmentMypageBinding.signUpButton.setOnClickListener {
            binding?.let { binding ->
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(context, "로그인 실패!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        fragmentMypageBinding.emailEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable =
                    binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signUpButton.isEnabled = enable
                binding.signInOutButton.isEnabled = enable
            }
        }

        fragmentMypageBinding.passwordEditText.addTextChangedListener {
            binding?.let { binding ->
                val enable =
                    binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signUpButton.isEnabled = enable
                binding.signInOutButton.isEnabled = enable
            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            binding?.let { binding ->
                binding.emailEditText.text.clear()
                binding.emailEditText.isEnabled = true
                binding.passwordEditText.text.clear()
                binding.passwordEditText.isEnabled = true

                binding.signInOutButton.text = "로그인"
                binding.signInOutButton.isEnabled = false
                binding.signUpButton.isEnabled = false
            }

        } else {
            binding?.let { binding ->
                binding.emailEditText.setText(auth.currentUser?.email)
                binding.emailEditText.isEnabled = false
                binding.passwordEditText.setText("**********")
                binding.passwordEditText.isEnabled = false

                binding.signInOutButton.text = "로그아웃"
                binding.signInOutButton.isEnabled = false
                binding.signUpButton.isEnabled = false
            }

        }
    }

    private fun successSignIn() {
        if (auth.currentUser == null) {
            Toast.makeText(context, "로그인 실패!, 다시 시도", Toast.LENGTH_SHORT).show()
            return
        }
        binding?.emailEditText?.isEnabled = false
        binding?.passwordEditText?.isEnabled = false
        binding?.signUpButton?.isEnabled = false
        binding?.signInOutButton?.text = "로그아웃"

    }

}