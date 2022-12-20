package com.draco.ssh.views


import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.draco.ssh.R
import com.google.android.material.textfield.TextInputEditText


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [shellfragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class shellfragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var to_control_fragment: Button
    private lateinit var add:Button
    private lateinit var string_of_commands: EditText
    private lateinit var delete:Button
    private lateinit var entering_commands:TextView

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

        return inflater.inflate(com.draco.ssh.R.layout.fragment_shell, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        to_control_fragment = requireView().findViewById(R.id.to_control_activity)
        to_control_fragment.setOnClickListener {
            val commands_list = (activity as ShellActivity).commands!!.split(";")
            for(command in commands_list){
                (activity as ShellActivity).command_btn.text = null
                (activity as ShellActivity).viewModel.shell.send(command)
            }
            findNavController().navigate(R.id.action_shellfragment_to_controlFragment)
        }
        entering_commands = requireView().findViewById(R.id.entering_commands)
        add = requireView().findViewById(R.id.add)
        string_of_commands = requireView().findViewById(R.id.string_of_commands)

        add.setOnClickListener {
            (activity as ShellActivity).commands = string_of_commands.text.toString()
            entering_commands.text = (activity as ShellActivity).commands
        }
        delete = requireView().findViewById(R.id.delete)
        delete.setOnClickListener {
            (activity as ShellActivity).commands = "help"
            entering_commands.text = (activity as ShellActivity).commands
        }
    }

    override fun onStart() {
        super.onStart()
        entering_commands.text = (activity as ShellActivity).commands
    }
}