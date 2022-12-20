package com.draco.ssh.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.draco.ssh.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ControlFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var text: String? = null

    //Кнопки
    private lateinit var to_shell_fragment: Button

    private lateinit var stop_machine: Button
    private lateinit var buttonforward: ImageButton
    private lateinit var buttonleft: ImageButton
    private lateinit var buttonright: ImageButton
    private lateinit var buttonback: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        to_shell_fragment = requireView().findViewById(R.id.btn_to_shell_fragment)
        to_shell_fragment.setOnClickListener {
            text = "q"
            (activity as ShellActivity).command_btn.text = null
            (activity as ShellActivity).viewModel.shell.send(text.toString())
            findNavController().navigate(R.id.action_controlFragment_to_shellfragment)
        }
        stop_machine = requireView().findViewById(R.id.stop_machine)
        stop_machine.setOnClickListener{
            text = "z"
            (activity as ShellActivity).command_btn.text = null
            (activity as ShellActivity).viewModel.shell.send(text.toString())
        }

        buttonforward = requireView().findViewById(R.id.buttonforward)
        buttonforward.setOnClickListener{
            text = "w"
            (activity as ShellActivity).command_btn.text = null
            (activity as ShellActivity).viewModel.shell.send(text.toString())
        }
        buttonleft = requireView().findViewById(R.id.buttonleft)
        buttonleft.setOnClickListener{
            text = "a"
            (activity as ShellActivity).command_btn.text = null
            (activity as ShellActivity).viewModel.shell.send(text.toString())
        }
        buttonright = requireView().findViewById(R.id.buttonright)
        buttonright.setOnClickListener{
            text = "d"
            (activity as ShellActivity).command_btn.text = null
            (activity as ShellActivity).viewModel.shell.send(text.toString())
        }
        buttonback = requireView().findViewById(R.id.buttonback)
        buttonback.setOnClickListener{
            text = "s"
            (activity as ShellActivity).command_btn.text = null
            (activity as ShellActivity).viewModel.shell.send(text.toString())
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ControlFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ControlFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}